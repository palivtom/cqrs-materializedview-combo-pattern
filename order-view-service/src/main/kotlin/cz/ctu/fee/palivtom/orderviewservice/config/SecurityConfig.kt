package cz.ctu.fee.palivtom.orderviewservice.config

import cz.ctu.fee.palivtom.security.AccessTokenInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
class SecurityConfig(
    private val handlerExceptionResolver: HandlerExceptionResolver
) {

    private val permitAccessWithoutFiltering = AntPathRequestMatcher("/actuator/**", HttpMethod.GET.name)

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf().disable()
            .addFilterBefore(AccessTokenInterceptor(handlerExceptionResolver, permitAccessWithoutFiltering), RequestHeaderAuthenticationFilter::class.java)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .requestMatchers(permitAccessWithoutFiltering).permitAll()
            .anyRequest().authenticated()
            .and()
            .build()
    }
}