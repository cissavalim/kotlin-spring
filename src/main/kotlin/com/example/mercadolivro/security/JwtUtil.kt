package com.example.mercadolivro.security

import com.example.mercadolivro.exception.AuthenticationException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtil {

    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    @Value("\${jwt.secret}")
    private val secret: String? = null

    fun generateToken(id: String): String {
        return Jwts.builder()
            .setSubject(id)
            .setExpiration(Date(System.currentTimeMillis() + expiration!!))
            .signWith(getKey())
            .compact()
    }

    private fun getKey(): Key {
        return Keys.hmacShaKeyFor(secret!!.toByteArray())
    }

    fun isTokenValid(token: String): Boolean {
        val claims = getClaims(token)
        return !(claims.subject == null || claims.expiration == null || Date().after(claims.expiration))
    }

    private fun getClaims(token: String): Claims {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(secret!!.toByteArray())
                .build()
                .parseClaimsJws(token)
                .body
        } catch (ex: Exception) {
            throw AuthenticationException("Token invalido", "999")
        }
    }

    fun getSubject(token: String) :String {
        return getClaims(token).subject
    }
}