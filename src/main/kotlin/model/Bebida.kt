package org.example.model

class Bebida(id: Int,
             nombre: String,
             precio: Double,
             categoria: String,
             tiempoPreparacion: Long,
             val tamaño: TamañoBebida )
    :Producto(
        id,
        nombre,
        precio,
        categoria,
        tiempoPreparacion)
{
    override fun reCalcularPrecio() :Double {
        val aumento: Int = when(this.tamaño){
            TamañoBebida.PEQUEÑO -> 0
            TamañoBebida.MEDIANO -> 200
            TamañoBebida.GRANDE -> 400
        }
        val precioAumentado = this.precio + aumento
        return precioAumentado
    }
}