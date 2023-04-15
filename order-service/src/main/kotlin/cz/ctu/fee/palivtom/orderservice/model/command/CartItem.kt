package cz.ctu.fee.palivtom.orderservice.model.command

import javax.persistence.*

@Entity(name = "cart_items")
open class CartItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open val id: Long = 0,

    @Column(name = "product_no", nullable = false)
    open val productNo: String,

    @ManyToOne
    @JoinTable(
        name = "cart_items_carts",
        joinColumns = [JoinColumn(name = "cart_item_id")],
        inverseJoinColumns = [JoinColumn(name = "cart_id")]
    )
    open var cart: Cart? = null,

    @Column(name = "original_price", nullable = false)
    open var originalPrice: Int? = null,

    @Column(name = "discount_price")
    open var discountPrice: Int? = null,

    @Column(name = "quantity", nullable = false)
    open var quantity: Int
) {
    override fun toString(): String {
        return "CartItem(id=$id, productNo='$productNo', cart=$cart, originalPrice=$originalPrice, discountPrice=$discountPrice, quantity=$quantity)"
    }
}