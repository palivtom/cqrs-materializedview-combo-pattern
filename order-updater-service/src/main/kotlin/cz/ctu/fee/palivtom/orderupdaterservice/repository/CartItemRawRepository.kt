package cz.ctu.fee.palivtom.orderupdaterservice.repository

import cz.ctu.fee.palivtom.orderviewmodel.model.CartItemRaw
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartItemRawRepository : JpaRepository<CartItemRaw, Long>