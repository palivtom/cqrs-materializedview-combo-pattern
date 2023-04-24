package cz.ctu.fee.palivtom.orderviewservice.service

import cz.ctu.fee.palivtom.orderviewmodel.model.CartView

interface CartViewService {
    fun getCartView(): CartView
}