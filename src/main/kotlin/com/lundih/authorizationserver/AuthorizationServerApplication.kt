package com.lundih.authorizationserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 *  Application entry point
 *
 *  @author lundih
 *  @since 0.0.1
 */
@SpringBootApplication
class AuthorizationServerApplication

fun main(args: Array<String>) {
	runApplication<AuthorizationServerApplication>(*args)
}
