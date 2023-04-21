package cz.ctu.fee.palivtom.orderservice.model

import java.time.Instant
import javax.persistence.*

@Entity(name = "orders")
open class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open val id: Long = 0,

    @Column(name = "user_id", nullable = false, length = 36)
    open var userId: String = "",

    @OneToOne(optional = false)
    @JoinColumn(name="cart_id")
    open var cart: Cart? = null,

    @OneToOne(mappedBy = "order")
    open var shippingAddress: ShippingAddress? = null,

    @Column(name = "created_at", nullable = false)
    open val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
    open var updatedAt: Instant? = null,

    @Column(name = "deleted_at")
    open var deletedAt: Instant? = null
) {
    override fun toString(): String {
        return "Order(id=$id, userId='$userId', cart=$cart, shippingAddress=$shippingAddress, createdAt=$createdAt, updatedAt=$updatedAt, deletedAt=$deletedAt)"
    }
}