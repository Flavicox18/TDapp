package com.flavicox.tdapp.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.util.Date

data class HistorialEncomienda(
    val idHistorialEncomienda: Int? = null,

    val fechaEvento: Date? = null,

    val tipoEvento: String? = null,
    val lugarActual: String? = null,
    val estadoActual: String? = null,
    val descripcionEvento: String? = null,

    @ManyToOne
    @JoinColumn(name = "id_encomienda", nullable = false)
    @JsonBackReference
    val encomienda: Encomienda? = null
)