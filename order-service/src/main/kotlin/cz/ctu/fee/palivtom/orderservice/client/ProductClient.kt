package cz.ctu.fee.palivtom.orderservice.client

interface ProductClient {
    fun getProductPrice(id: String): Int
}