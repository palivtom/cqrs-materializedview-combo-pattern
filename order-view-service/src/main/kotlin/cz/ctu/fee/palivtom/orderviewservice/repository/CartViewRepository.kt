package cz.ctu.fee.palivtom.orderviewservice.repository

import cz.ctu.fee.palivtom.orderviewmodel.model.entity.CartView
import org.springframework.data.jpa.repository.JpaRepository

interface CartViewRepository : JpaRepository<CartView, Long> {
    fun findByUserIdAndExportedAtNull(userId: String): CartView?
}