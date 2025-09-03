package org.example.model

class Bebida(id: Int,
             nombre: String,
             precio: Double,
             categoria: String,
             tiempoPreparacion: Double,
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
        precio += aumento
        return precio
    }
}