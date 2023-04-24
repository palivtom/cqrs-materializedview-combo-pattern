package cz.ctu.fee.palivtom.orderupdaterservice.repository

import cz.ctu.fee.palivtom.orderviewmodel.model.CartView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartViewRepository : JpaRepository<CartView, Long>