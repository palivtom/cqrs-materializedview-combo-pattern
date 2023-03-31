package cz.ctu.fee.palivtom.orderservice.config.db

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Disables default flyway configuration.
 */
@Configuration
class DefaultFlywayConfig {
    @Bean
    fun disableDefaultSpringCall(): FlywayMigrationStrategy {
        return FlywayMigrationStrategy {}
    }
}