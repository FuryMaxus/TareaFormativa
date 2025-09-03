package org.example.model

class Bebida(nombre: String,
             precio: Double,
             categoria: String,
             tiempoPreparacion: Double,
             val tamaño: TamañoBebida )
    :Producto(nombre,precio, categoria, tiempoPreparacion)
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