package cz.ctu.fee.palivtom.orderupdaterservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan(basePackages = ["cz.ctu.fee.palivtom"])
class OrderUpdaterServiceApplication

fun main(args: Array<String>) {
	runApplication<OrderUpdaterServiceApplication>(*args)
}