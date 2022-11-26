package cz.ctu.fee.palivtom.orderservice.repository.query

import cz.ctu.fee.palivtom.orderservice.model.query.OrderView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderViewRepository : JpaRepository<OrderView, Long> {
}