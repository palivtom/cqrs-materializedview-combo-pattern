package cz.ctu.fee.palivtom.orderviewservice.service.interfaces

import cz.ctu.fee.palivtom.orderviewmodel.model.entity.CartView

interface CartViewService {
    fun getCartView(): CartView
}