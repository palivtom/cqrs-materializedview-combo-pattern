package cz.ctu.fee.palivtom.orderservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val accessTokenInterceptor: AccessTokenInterceptor
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        return http
            .csrf().disable()
            .addFilterBefore(accessTokenInterceptor, UsernamePasswordAuthenticationFilter::class.java)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .build()
    }
}