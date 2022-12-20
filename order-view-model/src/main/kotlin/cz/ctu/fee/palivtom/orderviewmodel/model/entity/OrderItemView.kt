package cz.ctu.fee.palivtom.orderviewmodel.model.entity

import javax.persistence.*

@Entity(name = "order_item_view")
open class OrderItemView(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null,

    @Column(name = "product_id", nullable = false)
    open var productId: Long,

    @Column(name = "quantity", nullable = false)
    open var quantity: Int,

    @Column(name = "shipping_price", nullable = false)
    open var shippingPrice: Double,
)