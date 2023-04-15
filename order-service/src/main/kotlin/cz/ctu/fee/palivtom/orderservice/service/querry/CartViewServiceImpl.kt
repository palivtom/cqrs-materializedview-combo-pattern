package cz.ctu.fee.palivtom.orderservice.service.querry

import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.NotFoundApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.repository.query.CartViewRepository
import cz.ctu.fee.palivtom.orderservice.service.UserService
import cz.ctu.fee.palivtom.orderservice.service.querry.interfaces.CartViewService
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.CartView
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