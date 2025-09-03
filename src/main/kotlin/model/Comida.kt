package org.example.model

class Comida(id: Int,
             nombre: String,
             precio: Double,
             categoria: String,
             tiempoPreparacion: Double,
             val isPremium: Boolean)
    :Producto(
        id,
        nombre,
        precio,
        categoria,
        tiempoPreparacion
    )
{
    override fun reCalcularPrecio(): Double {
        val aumento = if (this.isPremium) 1000 else 0
        this.precio += aumento
        return precio
    }

}