package com.flavicox.tdapp.service

import com.flavicox.tdapp.entity.Encomienda
import com.flavicox.tdapp.entity.HistorialEncomienda
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EncomiendaService {

    @GET("api/encomienda/listarEncomienda/{id_encomienda}")
    fun obtenerEncomienda(@Path("id_encomienda") idEncomienda: Int): Call<Encomienda>

    @GET("api/historialEncomienda/listarHistorialEncomienda/{id_encomienda}")
    fun listarHistorial(@Path("id_encomienda") idEncomienda: Int): Call<List<HistorialEncomienda>>
}
