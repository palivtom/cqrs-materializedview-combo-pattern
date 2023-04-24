package cz.ctu.fee.palivtom.orderupdaterservice.repository

import cz.ctu.fee.palivtom.orderviewmodel.model.OrderView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderViewRepository : JpaRepository<OrderView, Long> {
    fun findByShippingAddressId(shippingAddressId: Long): Optional<OrderView>
}