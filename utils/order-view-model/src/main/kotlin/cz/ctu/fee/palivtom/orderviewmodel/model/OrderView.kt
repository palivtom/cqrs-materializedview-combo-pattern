package cz.ctu.fee.palivtom.orderviewmodel.model

import java.time.Instant
import javax.persistence.*

@Entity(name = "orders_view")
open class OrderView(
    @Id
    @Column(name = "id", nullable = false)
    open val id: Long = 0,

    @Column(name = "user_id")
    open var userId: String,

    @OneToOne(optional = false)
    @JoinColumn(name = "cart_id")
    open var cart: CartView,

    @Column(name = "shipping_address_id", unique = true)
    open var shippingAddressId: Long? = null,

    @Column(name = "street")
    open var street: String? = null,

    @Column(name = "city")
    open var city: String? = null,

    @Column(name = "zip_code")
    open var zipCode: String? = null,

    @Column(name = "country")
    open var country: String? = null,

    @Column(name = "created_at")
    open var createdAt: Instant,

    @Column(name = "updated_at")
    open var updatedAt: Instant? = null,

    @Column(name = "deleted_at")
    open var deletedAt: Instant? = null
)