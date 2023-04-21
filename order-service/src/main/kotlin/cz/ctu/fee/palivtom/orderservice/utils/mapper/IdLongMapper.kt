package cz.ctu.fee.palivtom.orderservice.utils.mapper

import cz.ctu.fee.palivtom.orderservice.model.IdLongDto

object IdLongMapper {
    fun Long.toIdDto() = IdLongDto(
        id = this
    )
}