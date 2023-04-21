package cz.ctu.fee.palivtom.orderviewservice.config

import cz.ctu.fee.palivtom.orderviewservice.exceptions.AccessTokenException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val HEADER_KEY = "accessToken"
private val defaultAuthorities = listOf<GrantedAuthority>(
    SimpleGrantedAuthority("ROLE_USER")
)

@Component
class AccessTokenInterceptor(
    private val handlerExceptionResolver: HandlerExceptionResolver
) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain,
    ) {
        request.getHeader(HEADER_KEY).run {
            try {
                checkFormat(this)
                authenticate(this)
            } catch (e: AccessTokenException) {
                handlerExceptionResolver.resolveException(request, response, null, e)
                return
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun checkFormat(token: String?) {
        if (token.isNullOrEmpty() || token.length > 36) {
            throw AccessTokenException("The '$HEADER_KEY' has invalid format.")
        }
    }

    private fun authenticate(token: String) {
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            token,
            null,
            defaultAuthorities
        )
    }
}