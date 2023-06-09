package cz.ctu.fee.palivtom.orderviewservice.repository

import cz.ctu.fee.palivtom.orderviewmodel.model.OrderView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderViewRepository : JpaRepository<OrderView, Long> {
    fun findAllByUserId(userId: String): List<OrderView>
    fun findByIdAndUserId(id: Long, userId: String): OrderView?
}