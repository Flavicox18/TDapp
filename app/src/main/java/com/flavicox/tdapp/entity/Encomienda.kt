package com.flavicox.tdapp.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.CascadeType
import jakarta.persistence.OneToMany
import java.util.Date

data class Encomienda(

    val idEncomienda: Int? = null,

    val descripcion: String? = null,
    val ciudadOrigen: String? = null,
    val ciudadDestino: String? = null,
    val direccionDestino: String? = null,
    val tipoEntrega: String? = null,
    val cantPaquetes: Int? = null,
    val estado: String? = null,

    val fechaEnvio: Date? = null,
    val fechaEntrega: Date? = null,

    val nombreEmisor: String? = null,
    val apellidoEmisor: String? = null,
    val dniEmisor: Int? = null,
    val nombreReceptor: String? = null,
    val apellidoReceptor: String? = null,
    val dniReceptor: Int? = null,
    val razonSocialEmisor: String? = null,
    val rucEmisor: Long? = null,
    val razonSocialReceptor: String? = null,
    val rucReceptor: Long? = null,

    @OneToMany(mappedBy = "encomienda", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference
    val historiales: List<HistorialEncomienda>? = null
)
