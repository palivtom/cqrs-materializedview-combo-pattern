package cz.ctu.fee.palivtom.orderservice.model.command

import javax.persistence.*

@Entity(name = "orders")
open class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    open var userId: Long,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "shipping_address_id")
    open var shippingAddress: ShippingAddress? = null,

    @OneToMany
    @JoinColumn(name = "order_id")
    open var items: MutableList<OrderItem> = mutableListOf()


) {
    override fun toString(): String {
        return "Order(id=$id, userId=$userId, shippingAddress=$shippingAddress, items=$items)"
    }
}