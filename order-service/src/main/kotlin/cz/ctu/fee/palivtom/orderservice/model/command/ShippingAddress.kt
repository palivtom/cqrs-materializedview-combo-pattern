package cz.ctu.fee.palivtom.orderservice.model.command

import javax.persistence.*

@Entity(name = "shipping_addresses")
open class ShippingAddress(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null,

    @Column(name = "street", nullable = false)
    open var street: String,

    @Column(name = "city", nullable = false)
    open var city: String,

    @Column(name = "zip_code", nullable = false)
    open var zipCode: String,

    @Column(name = "country", nullable = false)
    open var country: String


) {
    override fun toString(): String {
        return "ShippingAddress(id=$id, street='$street', city='$city', zipCode='$zipCode', country='$country')"
    }
}