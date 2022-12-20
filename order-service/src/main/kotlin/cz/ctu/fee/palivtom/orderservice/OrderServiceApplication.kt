package cz.ctu.fee.palivtom.orderservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrderServiceApplication

fun main(args: Array<String>) {
	val app = runApplication<OrderServiceApplication>(*args)

//	val orderRepo = app.getBean(OrderRepository::class.java)
//	val shippAddrRepo = app.getBean(ShippingAddressRepository::class.java)
//
//	val order = orderRepo.save(
//		Order(
//			userId = 1L
//		)
//	)
//
//	val shippAddr = shippAddrRepo.save(
//		ShippingAddress(
//			street = "street",
//			city = "city",
//			zipCode = "zipCode",
//			country = "country",
//			order = order
//		)
//	)
//
//	println(shippAddr)

}