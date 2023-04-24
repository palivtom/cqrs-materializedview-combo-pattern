package cz.ctu.fee.palivtom.orderviewservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
@EntityScan(basePackages = ["cz.ctu.fee.palivtom"])
class OrderViewServiceApplication

fun main(args: Array<String>) {
	runApplication<OrderViewServiceApplication>(*args)
}