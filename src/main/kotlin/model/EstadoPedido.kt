package org.example.model

sealed class EstadoPedido {
    object Pendiente: EstadoPedido()
    object EnPreparacion: EstadoPedido()
    object ListoParaEntrega: EstadoPedido()
    data class Error(val mensajeError: String): EstadoPedido()
}