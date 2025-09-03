package org.example.model

abstract class Producto(open val nombre: String,
                        open var precio: Double,
                        open val categoria: String,
                        open val tiempoPreparacion: Double) 
{
    abstract fun reCalcularPrecio() : Double;
}