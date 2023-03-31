package cz.ctu.fee.palivtom.orderservice.config.db

import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory

/**
 * Additional (second) database configuration.
 *
 * Set-ups the view database targeting context of view model.
 * To specify database use `spring.view-datasource` prefix.
 *
 * Flyway migration is integrated at path `classpath:db/migration/view`.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["cz.ctu.fee.palivtom.orderservice.repository.query"],
    entityManagerFactoryRef = "viewEntityManagerFactory",
    transactionManagerRef = "viewTransactionManager"
)
class ViewDbConfig(
    @Value("\${spring.jpa.hibernate.ddl-auto:none}") private val ddlAuto: String
) {
    @Bean
    @ConfigurationProperties("spring.view-datasource")
    fun viewDataSourceProps(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    fun viewDataSource(): HikariDataSource {
        return viewDataSourceProps().initializeDataSourceBuilder().type(HikariDataSource::class.java).build()
    }

    @Bean
    @ConditionalOnProperty(name = ["spring.flyway.enabled"], havingValue = "true")
    fun viewFlywayMigration() {
        Flyway.configure()
            .dataSource(viewDataSource())
            .locations("classpath:db/migration/view")
            .load()
            .migrate()
    }

    @Bean
    fun viewEntityManagerFactory(builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(viewDataSource())
            .packages("cz.ctu.fee.palivtom.orderviewmodel.model.entity")
            .persistenceUnit("view")
            .properties(
                mapOf(
                    "hibernate.hbm2ddl.auto" to ddlAuto
                )
            )
            .build()
    }

    @Bean
    fun viewTransactionManager(@Qualifier("viewEntityManagerFactory") entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}