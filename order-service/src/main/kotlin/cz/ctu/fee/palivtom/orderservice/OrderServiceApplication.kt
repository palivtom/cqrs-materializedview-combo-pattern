package cz.ctu.fee.palivtom.orderservice

import cz.ctu.fee.palivtom.orderservice.model.command.Order
import cz.ctu.fee.palivtom.orderservice.repository.command.OrderRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrderServiceApplication

fun main(args: Array<String>) {
	val repo = runApplication<OrderServiceApplication>(*args).getBean(OrderRepository::class.java)

	val a = repo.save(
		Order(
			shippingAddress = cz.ctu.fee.palivtom.orderservice.model.command.ShippingAddress(
				street = "Street",
				city = "City",
				zipCode = "ZipCode",
				country = "Country"
			),
			userId = 1L
		)
	)

	val b = repo.save(
		Order(
			userId = 1L
		)
	)

	println(a.toString())
	println(b.toString())
}