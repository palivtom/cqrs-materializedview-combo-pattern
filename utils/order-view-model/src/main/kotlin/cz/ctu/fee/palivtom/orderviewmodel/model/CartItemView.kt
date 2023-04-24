package cz.ctu.fee.palivtom.orderviewmodel.model

import javax.persistence.*

@Entity(name = "cart_items_view")
open class CartItemView(
    @Id
    @Column(name = "id")
    open val id: Long = 0,

    @Column(name = "product_no", nullable = false)
    open var productNo: String,

    @ManyToOne
    @JoinColumn(name = "cart_id")
    open var cart: CartView,

    @Column(name = "original_price", nullable = false)
    open var originalPrice: Int,

    @Column(name = "discount_price")
    open var discountPrice: Int? = null,

    @Column(name = "quantity", nullable = false)
    open var quantity: Int
)