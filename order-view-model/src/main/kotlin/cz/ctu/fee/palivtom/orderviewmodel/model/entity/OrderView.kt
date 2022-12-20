package cz.ctu.fee.palivtom.orderviewmodel.model.entity

import java.time.Instant
import javax.persistence.*

@Entity(name = "orders_view")
open class OrderView(
    @Id
    @Column(name = "id", nullable = false)
    open val id: Long = 0,

    @Column(name = "user_id")
    open var userId: Long? = null,

    @Column(name = "shipping_address_id")
    open var shippingAddressId: Long? = null,

    @Column(name = "street")
    open var street: String? = null,

    @Column(name = "city")
    open var city: String? = null,

    @Column(name = "zip_code")
    open var zipCode: String? = null,

    @Column(name = "country")
    open var country: String? = null,

    @OneToMany
    @JoinColumn(name = "order_id")
    open var items: MutableList<OrderItemView> = mutableListOf(),

    @Column(name = "created_at")
    open var createdAt: Instant? = null,

    @Column(name = "updated_at")
    open var updatedAt: Instant? = null,

    @Column(name = "deleted_at")
    open var deletedAt: Instant? = null
)