package cz.ctu.fee.palivtom.orderservice.repository

import cz.ctu.fee.palivtom.orderservice.model.ShippingAddress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShippingAddressRepository : JpaRepository<ShippingAddress, Long> {

    fun findByOrderId(orderId: Long): ShippingAddress?

}