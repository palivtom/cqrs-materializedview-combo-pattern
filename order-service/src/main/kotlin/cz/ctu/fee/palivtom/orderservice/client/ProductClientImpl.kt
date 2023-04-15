package cz.ctu.fee.palivtom.orderservice.client

import org.springframework.stereotype.Service

@Service
class ProductClientImpl : ProductClient {
    override fun getProductPrice(id: String): Int {
        var price = id.hashCode()

        while (price > 10000) {
            price /= 10
        }

        return price
    }
}