package cz.ctu.fee.palivtom.orderservice.repository.command

import cz.ctu.fee.palivtom.orderservice.model.command.Cart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartRepository : JpaRepository<Cart, Long> {
    fun findByUserIdAndExportedAtNull(userId: String): Cart?
}