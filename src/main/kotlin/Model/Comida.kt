package org.example.Model

class Comida(nombre: String, precio: Double, categoria: String, tiempoPreparacion: Double, val isPremium: Boolean) : Producto(nombre, precio, categoria,
    tiempoPreparacion
)
{
    override fun recalcularPrecio(): Double {
        if(this.isPremium) {
            this.precio += 1000.0
            return precio
        }
    }

}