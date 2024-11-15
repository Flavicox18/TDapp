package com.flavicox.tdapp.entity

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Encomienda(

    @SerializedName("id_encomienda")
    val id_encomienda: Int? = null,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("ciudad_origen")
    val ciudad_origen: String? = null,
    @SerializedName("ciudad_destino")
    val ciudad_destino: String? = null,
    @SerializedName("direccion_destino")
    val direccion_destino: String? = null,
    @SerializedName("tipo_entrega")
    val tipo_entrega: String? = null,
    @SerializedName("cant_paquetes")
    val cant_paquetes: Int? = null,
    @SerializedName("estado")
    val estado: String? = null,

    @SerializedName("fecha_envio")
    val fecha_envio: String? = null,
    @SerializedName("fecha_entrega")
    val fecha_entrega: String? = null,

    @SerializedName("nombre_emisor")
    val nombre_emisor: String? = null,
    @SerializedName("apellido_emisor")
    val apellido_emisor: String? = null,
    @SerializedName("dni_emisor")
    val dni_emisor: Int? = null,
    @SerializedName("nombre_receptor")
    val nombre_receptor: String? = null,
    @SerializedName("apellido_receptor")
    val apellido_receptor: String? = null,
    @SerializedName("dni_receptor")
    val dni_receptor: Int? = null,
    @SerializedName("razon_social_emisor")
    val razon_social_emisor: String? = null,
    @SerializedName("ruc_emisor")
    val ruc_emisor: Long? = null,
    @SerializedName("razon_social_receptor")
    val razon_social_receptor: String? = null,
    @SerializedName("ruc_receptor")
    val ruc_receptor: Long? = null,

    val historiales: List<HistorialEncomienda>? = null
)
