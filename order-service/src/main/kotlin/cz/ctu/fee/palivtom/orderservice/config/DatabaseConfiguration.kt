package cz.ctu.fee.palivtom.orderservice.config

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseConfiguration(
    private val hibernateTransactionInterceptor: HibernateTransactionInterceptor,
) {
    @Bean
    fun hibernateCustomization(): HibernatePropertiesCustomizer = HibernatePropertiesCustomizer {
        it["hibernate.session_factory.interceptor"] = hibernateTransactionInterceptor
    }
}