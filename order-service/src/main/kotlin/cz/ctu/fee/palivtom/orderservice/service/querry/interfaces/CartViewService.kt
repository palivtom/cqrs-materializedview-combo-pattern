package cz.ctu.fee.palivtom.orderservice.service.querry.interfaces

import cz.ctu.fee.palivtom.orderviewmodel.model.entity.CartView

interface CartViewService {
    fun getCartView(): CartView
}