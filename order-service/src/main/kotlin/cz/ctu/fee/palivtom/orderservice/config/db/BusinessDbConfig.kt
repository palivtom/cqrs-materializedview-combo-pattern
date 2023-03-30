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
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["cz.ctu.fee.palivtom.orderservice.repository.command"],
    entityManagerFactoryRef = "businessEntityManagerFactory",
    transactionManagerRef = "businessTransactionManager"
)
class BusinessDbConfig(
    private val hibernateTransactionInterceptor: HibernateTransactionInterceptor,
    @Value("\${spring.jpa.hibernate.ddl-auto:none}") private val ddlAuto: String
) {



    @Bean
    @ConfigurationProperties("spring.business-datasource")
    fun businessDataSourceProps(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Primary
    @Bean
    fun businessDataSource(): HikariDataSource {
        return businessDataSourceProps().initializeDataSourceBuilder().type(HikariDataSource::class.java).build()
    }

    @Bean
    @ConditionalOnProperty(name = ["spring.flyway.enabled"], havingValue = "true")
    fun businessFlywayMigration() {
        Flyway.configure()
            .dataSource(businessDataSource())
            .locations("classpath:db/migration/business")
            .load()
            .migrate()
    }

    @Primary
    @Bean
    fun businessEntityManagerFactory(builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(businessDataSource())
            .packages("cz.ctu.fee.palivtom.orderservice.model.command")
            .persistenceUnit("business")
            .properties(
                mapOf(
                    "hibernate.hbm2ddl.auto" to ddlAuto,
                    "hibernate.ejb.interceptor" to hibernateTransactionInterceptor
                )
            )
            .build()
    }

    @Primary
    @Bean
    fun businessTransactionManager(@Qualifier("businessEntityManagerFactory") entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}