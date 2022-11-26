package cz.ctu.fee.palivtom.orderservice.model.query

import javax.persistence.*

@Entity(name = "order_item_view")
open class OrderItemView(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null,

    open var productId: Long,

    open var quantity: Int,

    open var shippingPrice: Double
)