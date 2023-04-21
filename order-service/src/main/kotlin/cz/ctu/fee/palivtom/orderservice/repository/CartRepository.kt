package cz.ctu.fee.palivtom.orderservice.repository

import cz.ctu.fee.palivtom.orderservice.model.Cart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartRepository : JpaRepository<Cart, Long> {
    fun findByUserIdAndExportedAtNull(userId: String): Cart?
}