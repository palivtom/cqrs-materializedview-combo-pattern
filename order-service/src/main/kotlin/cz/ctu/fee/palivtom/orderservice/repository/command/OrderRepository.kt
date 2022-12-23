package cz.ctu.fee.palivtom.orderservice.repository.command

import cz.ctu.fee.palivtom.orderservice.model.command.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long>