package cz.ctu.fee.palivtom.transactiongrouperservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan(basePackages = ["cz.ctu.fee.palivtom"])
class TransactionGrouperServiceApplication

fun main(args: Array<String>) {
	runApplication<TransactionGrouperServiceApplication>(*args)
}