package cz.ctu.fee.palivtom.orderservice.model.command

import java.time.Instant
import javax.persistence.*

@Entity(name = "orders")
open class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    open var userId: Long,

    @OneToOne(mappedBy = "order")
    open var shippingAddress: ShippingAddress? = null,

    @OneToMany
    @JoinColumn(name = "order_id")
    open var items: MutableList<OrderItem> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    open val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
    open var updatedAt: Instant? = null,

    @Column(name = "deleted_at")
    open var deletedAt: Instant? = null

) {
    override fun toString(): String {
        return "Order(id=$id, userId=$userId, shippingAddress=$shippingAddress, createdAt=$createdAt, updatedAt=$updatedAt, deletedAt=$deletedAt)"
    }
}