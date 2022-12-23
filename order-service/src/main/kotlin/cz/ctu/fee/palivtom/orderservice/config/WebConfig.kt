package cz.ctu.fee.palivtom.orderservice.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfig(
    private val hibernateRequestInterceptor: HibernateRequestInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addWebRequestInterceptor(hibernateRequestInterceptor)
    }

}