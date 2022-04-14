package com.lundih.authorizationserver.utils.auth

import com.lundih.authorizationserver.exceptions.jwt.*
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JwtUtilsTest {
    private val jwtSecret = "reallySecretKey"
    private val jwtExpiration = 3600
    private val jwtRefreshExpiration = 7200
    private val jwtSubject = "testJwtSubject"

    private val jwtUtils = JwtUtils(jwtSecret, jwtExpiration, jwtRefreshExpiration)

    @Test
    fun generateAccessJwt_should_generate_jwt_with_provided_subject() {
        assertEquals(jwtSubject, jwtUtils.getSubjectFromJwt(jwtUtils.generateAccessJwt(jwtSubject)))
    }

    @Test
    fun generateRefreshJwt_should_generate_jwt_with_access_jwt_and_owner_in_subject() {
        val jwt = jwtUtils.generateAccessJwt(jwtSubject)
        val refreshJwt = jwtUtils.generateRefreshJwt(jwt)

        assertEquals(jwt, jwtUtils.getAccessTokenFromRefreshToken(refreshJwt))
        assertEquals(jwtSubject, jwtUtils.getAccessTokenOwnerFromRefreshToken(refreshJwt))
    }

    @Test
    fun validateJwt_should_throw_Exception_for_malformed_jwt() {
        assertThrows<InvalidJwtException> {
            jwtUtils.validateJwt("yJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjM0NTYiLCJpYXQiOjE2NDk0OTU5NjIsImV4cCI6MTY0OTUzOTE2Mn0.Snt-UnXauUVd5ugV7d4B1cdZK3mE1rZ4zROxTfGA9GRuICp22aOGweDyXJFlQpoDr3GeoW1a8o6HA1Lvcnxg-w")
        }
    }

    @Test
    fun validateJwt_should_throw_Exception_for_expired_jwt() {
        assertThrows<ExpiredJwtException> {
            jwtUtils.validateJwt(Jwts.builder()
                    .setSubject(jwtSubject)
                    .setIssuedAt(Date())
                    .setExpiration(Date(Date().time - 1))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact())
        }
    }

    @Test
    fun validateJwt_should_throw_Exception_for_unsupported_jwt() {
        assertThrows<UnsupportedJwtException> {
            jwtUtils.validateJwt(Jwts.builder()
                    .setSubject(jwtSubject)
                    .setIssuedAt(Date())
                    .setExpiration(Date(Date().time + (jwtExpiration * 1000)))
                    .compact())
        }
    }

    @Test
    fun validateJwt_should_throw_Exception_for_jwt_with_empty_claims() {
        assertThrows<Exception> {
            jwtUtils.validateJwt(Jwts.builder()
                    .setSubject(jwtSubject)
                    .setIssuedAt(Date())
                    .setExpiration(Date(Date().time + (jwtExpiration * 1000)))
                    .signWith(SignatureAlgorithm.HS512, "")
                    .compact())
        }
    }

    @Test
    fun validateJwt_should_throw_Exception_for_jwt_with_incorrect_signature() {
        assertThrows<Exception> {
            jwtUtils.validateJwt(Jwts.builder()
                    .setSubject(jwtSubject)
                    .setIssuedAt(Date())
                    .setExpiration(Date(Date().time + (jwtExpiration * 1000)))
                    .signWith(SignatureAlgorithm.ES512, jwtSecret)
                    .compact())
        }
    }

    @Test
    fun validateJwtShould_return_true_for_valid_jwt() {
        assertTrue {
            jwtUtils.validateJwt(Jwts.builder()
                    .setSubject(jwtSubject)
                    .setIssuedAt(Date())
                    .setExpiration(Date(Date().time + (jwtExpiration * 1000)))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact())
        }
    }

    @Test
    fun validateRefreshJwt_should_not_return_true_for_invalid_jwt() {
        assertFalse {
            try {
                jwtUtils.validateRefreshJwt("dummy.refresh.jwt")
            } catch (e: Exception) { false }
        }
    }

    @Test
    fun validateRefreshJwt_should_throw_Exception_when_refreshing_a_still_valid_access_jwt() {
        val accessToken = jwtUtils.generateAccessJwt(jwtSubject)
        val refreshToken = jwtUtils.generateRefreshJwt(accessToken)

        assertThrows<AttemptToRefreshValidTokenException> {
            jwtUtils.validateRefreshJwt(refreshToken)
        }
    }

    @Test
    fun validateRefreshJwt_should_not_return_true_when_refreshing_an_access_jwt_with_a_problem() {
        val accessToken = jwtUtils.generateAccessJwt(jwtSubject)
        val refreshToken = Jwts.builder()
                .setSubject("malformed$accessToken")
                .setIssuedAt(Date())
                .setExpiration(Date(Date().time + (jwtRefreshExpiration * 1000)))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact()

        assertFalse {
            try {
                jwtUtils.validateRefreshJwt(refreshToken)
            } catch (e: Exception) { false }
        }
    }

    @Test
    fun validateRefreshJwt_should_return_true_when_refreshing_an_expired_access_token() {
        val jwtUtils = JwtUtils(jwtSecret, 1, 7200)
        val refreshToken = jwtUtils.generateRefreshJwt(jwtUtils.generateAccessJwt(jwtSubject))

        // Wait so that the access jwt expires
        Thread.sleep(1000)

        assertTrue(jwtUtils.validateRefreshJwt(refreshToken))
    }
}