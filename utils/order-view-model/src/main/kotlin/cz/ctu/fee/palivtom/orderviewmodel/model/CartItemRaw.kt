package cz.ctu.fee.palivtom.orderviewmodel.model

import javax.persistence.*

@Entity(name = "cart_items_raw")
open class CartItemRaw(
    @Id
    @Column(name = "id")
    open val id: Long = 0,

    @Column(name = "product_no", nullable = false)
    open val productNo: String,

    @Column(name = "original_price", nullable = false)
    open var originalPrice: Int,

    @Column(name = "discount_price")
    open var discountPrice: Int? = null,

    @Column(name = "quantity", nullable = false)
    open var quantity: Int
)