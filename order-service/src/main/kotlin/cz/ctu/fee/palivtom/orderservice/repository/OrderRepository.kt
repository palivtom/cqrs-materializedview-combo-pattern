package cz.ctu.fee.palivtom.orderservice.repository

import cz.ctu.fee.palivtom.orderservice.model.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long>