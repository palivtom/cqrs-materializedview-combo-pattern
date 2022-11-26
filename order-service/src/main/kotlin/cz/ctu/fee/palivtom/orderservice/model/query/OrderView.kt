package cz.ctu.fee.palivtom.orderservice.model.query

import javax.persistence.*

@Entity(name = "orders_view")
open class OrderView(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null,

    @Column(nullable = false)
    open var userId: Long,

    @Column(name = "street", nullable = false)
    open var street: String,

    @Column(name = "city", nullable = false)
    open var city: String,

    @Column(name = "zip_code", nullable = false)
    open var zipCode: String,

    @Column(name = "country", nullable = false)
    open var country: String,

    @OneToMany
    open var items: MutableList<OrderItemView> = mutableListOf()
)