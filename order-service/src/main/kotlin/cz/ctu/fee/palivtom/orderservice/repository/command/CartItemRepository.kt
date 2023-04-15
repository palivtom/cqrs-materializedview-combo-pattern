package cz.ctu.fee.palivtom.orderservice.repository.command

import cz.ctu.fee.palivtom.orderservice.model.command.CartItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartItemRepository : JpaRepository<CartItem, Long>