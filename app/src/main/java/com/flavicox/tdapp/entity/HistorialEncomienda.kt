package com.flavicox.tdapp.entity

import com.google.gson.annotations.SerializedName
import java.util.Date

data class HistorialEncomienda(
    @SerializedName("id_historial_encomienda")
    val id_historial_encomienda: Int? = null,

    @SerializedName("fecha_evento")
    val fecha_evento: String? = null,

    @SerializedName("tipo_evento")
    val tipo_evento: String? = null,
    @SerializedName("lugar_actual")
    val lugar_actual: String? = null,
    @SerializedName("estado_actual")
    val estado_actual: String? = null,
    @SerializedName("descripcion_evento")
    val descripcion_evento: String? = null,

    val encomienda: Encomienda? = null
)