package cz.ctu.fee.palivtom.orderviewservice.service.impl

import cz.ctu.fee.palivtom.orderviewservice.exceptions.runtime.NotFoundApiRuntimeException
import cz.ctu.fee.palivtom.orderviewservice.repository.CartViewRepository
import cz.ctu.fee.palivtom.orderviewservice.service.CartViewService
import cz.ctu.fee.palivtom.orderviewmodel.model.CartView
import cz.ctu.fee.palivtom.orderviewservice.service.UserService
import org.springframework.stereotype.Service

@Service
class CartViewServiceImpl(
    private val userService: UserService,
    private val cartViewRepository: CartViewRepository
) : CartViewService {
    override fun getCartView(): CartView {
        val userId = userService.getUserId()
        return cartViewRepository.findByUserIdAndExportedAtNull(userId)
            ?: throw NotFoundApiRuntimeException("Current user with id '$userId' does not have a cart.")
    }
}