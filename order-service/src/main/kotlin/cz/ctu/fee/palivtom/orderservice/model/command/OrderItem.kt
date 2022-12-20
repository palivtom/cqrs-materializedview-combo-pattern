package cz.ctu.fee.palivtom.orderservice.model.command

import javax.persistence.*

@Entity(name = "order_items")
open class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long = 0,

    @Column(name = "product_id", nullable = false)
    open var productId: Long,

    @Column(name = "quantity", nullable = false)
    open var quantity: Int,
)