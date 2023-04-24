package cz.ctu.fee.palivtom.orderviewmodel.model

import java.time.Instant
import javax.persistence.*

@Entity(name = "carts_view")
open class CartView(
    @Id
    @Column(name = "id")
    open val id: Long = 0,

    @Column(name = "user_id", nullable = false, length = 36)
    open var userId: String = "",

    @Column(name = "price_sum", nullable = false)
    open var priceSum: Int = 0,

    @Column(name = "item_count", nullable = false)
    open var itemCount: Int = 0,

    @OneToOne(mappedBy = "cart")
    open var order: OrderView? = null,

    @OneToMany(mappedBy = "cart")
    open val cartItems: MutableList<CartItemView> = ArrayList(),

    @Column(name = "exported_at")
    open var exportedAt: Instant? = null
)