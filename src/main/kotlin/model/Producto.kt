package org.example.model

abstract class Producto(open val id: Int,
                        open val nombre: String,
                        open var precio: Double,
                        open val categoria: String,
                        open val tiempoPreparacion: Long)
{
    abstract fun reCalcularPrecio() : Double;
}