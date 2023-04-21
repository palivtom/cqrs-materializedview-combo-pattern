package cz.ctu.fee.palivtom.orderviewservice.service

import cz.ctu.fee.palivtom.orderviewservice.exceptions.runtime.NotFoundApiRuntimeException
import cz.ctu.fee.palivtom.orderviewservice.repository.CartViewRepository
import cz.ctu.fee.palivtom.orderviewservice.service.interfaces.CartViewService
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.CartView
import cz.ctu.fee.palivtom.orderviewservice.service.interfaces.UserService
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