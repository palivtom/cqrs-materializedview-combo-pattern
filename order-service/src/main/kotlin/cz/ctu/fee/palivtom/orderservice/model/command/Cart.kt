package cz.ctu.fee.palivtom.orderservice.model.command

import java.time.Instant
import javax.persistence.*

@Entity(name = "carts")
open class Cart(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open val id: Long = 0,

    @Column(name = "user_id", nullable = false, length = 36)
    open var userId: String = "",

    @OneToOne(mappedBy = "cart")
    open var order: Order? = null,

    @OneToMany(mappedBy = "cart")
    open val cartItems: MutableList<CartItem> = ArrayList(),

    @Column(name = "exported_at")
    open var exportedAt: Instant? = null
) {
    override fun toString(): String {
        return "Cart(id=$id, userId='$userId', order=$order, cartItems=$cartItems, exportedAt=$exportedAt)"
    }
}