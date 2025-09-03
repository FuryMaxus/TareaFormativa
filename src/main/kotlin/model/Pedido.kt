package org.example.model

class Pedido(val cliente: Cliente,
             val listaProductos: MutableList<Producto>,
             var estadoPedido: EstadoPedido
)
{
    fun calcSumTotal(): Double {
        val suma: Double = this.listaProductos.sumOf { it.precio }
        return suma
    }

    fun calcDescuentoPorTipoCliente(): Double {
        val descuento: Double = when (this.cliente.tipoCliente){
            TipoCliente.REGULAR -> 5/100.0
            TipoCliente.VIP -> 10/100.0
            TipoCliente.PREMIUM -> 15/100.0
        }
        return (calcSumTotal() * descuento)
    }

    fun calcIva(): Double {
        val iva: Double = (19/100.0)
        return (calcSumTotal() * iva)
    }

    fun calcPrecioTotal(): Double {
        return ( calcSumTotal() - calcDescuentoPorTipoCliente() + calcIva() )
    }
}