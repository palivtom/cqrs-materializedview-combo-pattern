package cz.ctu.fee.palivtom.orderservice.repository.command

import cz.ctu.fee.palivtom.orderservice.model.command.ShippingAddress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ShippingAddressRepository : JpaRepository<ShippingAddress, Long> {

    fun findByOrderId(orderId: Long): Optional<ShippingAddress>

}