package org.example.Model

class Bebida(nombre: String,
             precio: Double,
             categoria: String,
             tiempoPreparacion: Double,
             val tamaño: TamañoBebida )
    :Producto(nombre,precio, categoria, tiempoPreparacion)
{
    override fun recalcularPrecio() :Double {
        when(this.tamaño){
            TamañoBebida.PEQUEÑO -> {}
            TamañoBebida.MEDIANO -> {
                this.precio += 200
            }
            TamañoBebida.GRANDE -> {
                this.precio += 400
            }
        }
        return precio
    }
}