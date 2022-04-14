package com.lundih.authorizationserver.utils.auth

import com.lundih.authorizationserver.exceptions.jwt.AttemptToRefreshValidTokenException
import com.lundih.authorizationserver.exceptions.jwt.EmptyJwtClaimsException
import com.lundih.authorizationserver.exceptions.jwt.InvalidJwtException
import com.lundih.authorizationserver.exceptions.jwt.InvalidJwtSignatureException
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

/**
 * Json Web Token utility class<br><br>
 *
 * Note about Access and Refresh token implementation:<br><br>
 * Access tokens are not stored as they already have the user's identifying property encoded into the token which can be
 * checked upon being received by the service. Refresh tokens contain both the user's identifier and the access token
 * that they are expected to refresh in their subject. This approach was chosen because if the access token and its
 * corresponding refresh token were to be stored in a database, it is expected they would have a one-to-one mapping.
 * This can still be achieved by having each refresh token contain its (encrypted) access token. This approach allows to
 * remove the need to constantly access a database for tokens during authentication and the need for token storage space
 * <br><br>
 * A concern to note is that without a database, it becomes a challenge to invalidate specific user tokens in order to
 * force a user to re-authenticate via login. This can however be implemented by using a property in the
 * [User][com.lundih.authorizationserver.entities.User] entity that can be used to accommodate the aforementioned
 * situation. The property is checked during authentication to determine if the user should be forced to log in
 *
 * @author lundih
 * @since 0.0.1
 *
 * @param jwtSecret Key used to sign the JWT
 * @param jwtExpiration Duration in seconds until expiry of the access JWT after it has been issued
 * @param jwtRefreshExpiration Duration in seconds until the expiry of the refresh JWT after it has been issued
 * @property tokenDelimiter Allows to clearly define where n access token starts and ends in the refresh token subject
 * @property ownerDelimiter Allows to clearly define where the property that identifies the owner of an access token in
 * the refresh token subject starts and ends
 */
@Component
class JwtUtils(@Value("\${auth.jwt-signing-key}") private val jwtSecret: String = "",
               @Value("\${auth.jwt-access-expiration}") private val jwtExpiration: Int = 0,
               @Value("\${auth.jwt-refresh-expiration}") private val jwtRefreshExpiration: Int = 0 ) {

    private val tokenDelimiter = "<AccessToken>"
    private val ownerDelimiter = "<Owner>"

    /**
     *  Generates a JWT (access token)
     *
     *  @param subject Identifying parameter of a user that allows to know who the token belongs to (employee number
     *  in this case)
     *  @return JWT String
     */
    fun generateAccessJwt(subject: String): String {
        return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + (jwtExpiration * 1000)))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    /**
     * Generates a JWT (refresh token) with the access JWT and user identifier as the subject<br>
     *
     * It may appear redundant to encode the user's identifier that is already in the access token into the refresh token
     * as well, however, if the access token is to be considered viable to be refreshed, it has to have expired.
     * Attempting to retrieve the subject from an expired access token throws an Exception. So it follows that its
     * subject is also stored in the refresh token's subject.
     *
     * @param accessJwT JWT that will act as the access token that the generated refresh token will renew
     * @return JWT String
     */
    fun generateRefreshJwt(accessJwT: String): String {
        return Jwts.builder()
            .setSubject("$tokenDelimiter$accessJwT$tokenDelimiter$ownerDelimiter${getSubjectFromJwt(accessJwT)}$ownerDelimiter")
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + (jwtRefreshExpiration * 1000)))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    /**
     * Gets subject from JWT
     *
     * @param token JWT string
     * @return subject that was encoded into the JWT
     */
    fun getSubjectFromJwt(token: String?): String {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
    }

    /**
     * Checks if a JWT is valid
     *
     * @param token Jwt string
     * @return true if JWT is valid else throws an Exception describing the issue
     */
    fun validateJwt(token: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
        } catch (e: SignatureException) {
            logger.error("Invalid JWT signature: ${e.message}")
            throw InvalidJwtSignatureException("Invalid JWT signature")
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT: ${e.message}")
            throw InvalidJwtException("Malformed JWT")
        } catch (e: ExpiredJwtException) {
            throw com.lundih.authorizationserver.exceptions.jwt.ExpiredJwtException("JWT is expired")
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT is unsupported: ${e.message}")
            throw com.lundih.authorizationserver.exceptions.jwt.UnsupportedJwtException("JWT is unsupported")
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: ${e.message}")
            throw EmptyJwtClaimsException("JWT claims string is empty")
        }

        return true
    }

    /**
     * Specifically validates refresh tokens, not access tokens
     *
     * The method validates the refresh token and retrieves the access token from it if the refresh token itself is
     * still valid. It then checks that the access token is expired before generating a new access and refresh token.
     * If the access token is still valid or has any other issue apart from being expired, an Exception is thrown
     *
     * @param refreshToken A JWT string (of a refresh token)
     * @return true if the refresh JWT is valid else throws an Exception describing the issue
     */
    fun validateRefreshJwt(refreshToken: String): Boolean {
        return if (validateJwt(refreshToken)) {
            val accessToken = getAccessTokenFromRefreshToken(refreshToken)
            try {
                if (validateJwt(accessToken)) {
                    logger.error("Attempted to refresh a valid access token <$accessToken> with refresh token <$refreshToken>")
                    throw AttemptToRefreshValidTokenException()
                } else false
            } catch (expiredJwtException: com.lundih.authorizationserver.exceptions.jwt.ExpiredJwtException) { true }
        } else false
    }

    /**
     * Returns the access token that was encoded into the refresh token and the access token
     *
     * @param refreshToken JWT String.
     * @return Access token JWT string
     */
    fun getAccessTokenFromRefreshToken(refreshToken: String): String {
        return getSubjectFromJwt(refreshToken).split(tokenDelimiter, ignoreCase = false, limit = 3)[1]
    }

    /**
     * Returns the user identifier that was encoded into the refresh token and the access token corresponding to the
     * refresh token
     *
     * @param refreshToken JWT String.
     * @return A parameter that can be used to uniquely identify the user the token belongs to (Employee number in this
     * case)
     */
    fun getAccessTokenOwnerFromRefreshToken(refreshToken: String): String {
        return getSubjectFromJwt(refreshToken).split(ownerDelimiter, ignoreCase = false, limit = 3)[1]
    }

    companion object {
        private val logger = LoggerFactory.getLogger(JwtUtils::class.java)
    }
}