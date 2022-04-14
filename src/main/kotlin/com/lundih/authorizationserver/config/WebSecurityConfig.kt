package com.lundih.authorizationserver.config

import com.lundih.authorizationserver.domain.UserDetailsServiceImpl
import com.lundih.authorizationserver.utils.auth.AuthEntryPointJwt
import com.lundih.authorizationserver.utils.auth.AuthTokenFilter
import com.lundih.authorizationserver.utils.auth.JwtUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * Web security configurations
 *
 * @author lundih
 * @since 0.0.1
 *
 * @param userDetailsService The [UserDetailsServiceImpl] object provides the 'loadByUserName' function implementation
 * that is used by the authentication manager and the custom authentication token filter, [AuthTokenFilter]
 * @param unauthorizedHandler [AuthEntryPointJwt] that takes care of authentication exception handling
 * @param jwtUtils [JWT utility class object][JwtUtils] used by the [AuthTokenFilter] to validate and retrieve the
 * subject from JWTs
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(private val userDetailsService: UserDetailsServiceImpl,
                        private val unauthorizedHandler: AuthEntryPointJwt,
                        private val jwtUtils: JwtUtils) : WebSecurityConfigurerAdapter() {

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationJwtTokenFilter(): AuthTokenFilter {
        return AuthTokenFilter(jwtUtils , userDetailsService)
    }

    public override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests().antMatchers("/api/v*/auth/**").permitAll()
            .and().authorizeRequests().antMatchers("/swagger-ui**/**").permitAll()
            .and().authorizeRequests().antMatchers("/v3/api-docs").permitAll()
            .antMatchers("/api/test/**").permitAll()
            .anyRequest().authenticated()
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**")
        super.configure(web)
    }
}