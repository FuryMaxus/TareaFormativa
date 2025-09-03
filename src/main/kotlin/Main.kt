package org.example

import kotlinx.coroutines.runBlocking
import org.example.model.Cliente
import org.example.model.Pedido
import org.example.model.Producto
import org.example.model.TipoCliente


fun pedirDato(mensaje: String,
              validar: (String) -> Boolean): String
{
    while(true){
        println(mensaje)
        val input = readLine()?.trim().orEmpty()
        if (validar(input)) return input
        println("Error: entrada invalida, intentelo de nuevo")
    }
}

fun pedirDatosCliente(): Cliente
{
    val nombre = pedirDato(
        "Ingrese su nombre",
        {it.isNotBlank()}
    )

    val tipoCliente = when( pedirDato(
        "Seleccione el tipo de usuario:\n1. Regular\n2. VIP\n3. Deluxe",
        { it in listOf("1","2","3") } )
    ){
        "1" -> TipoCliente.REGULAR
        "2" -> TipoCliente.VIP
        "3" -> TipoCliente.PREMIUM
        else -> TipoCliente.REGULAR
    }
    return Cliente(nombre,tipoCliente)
}

fun

fun mostrarCatalogoFiltradoPorCategoria(categoria: String,
                                        catalogo: List<Producto>): List<Producto>
{
    val catalogoFiltrado: List<Producto> = catalogo.filter { it.categoria == categoria }
    println("Catalogo de $categoria")
    for( (index,producto) in catalogoFiltrado.withIndex()) {
        println("$index. $producto")
    }
    return catalogoFiltrado
    //RECORDATORIO: no tengo idea si hay que solo mostrarlo o esto cambia como se elijen los productos
}

fun agregarProducto(catalogo: List<Producto>,
                    pedido: Pedido){
    val opcionProducto = when (pedirDato(
        "Quieres agregar por id o por nombre?\n1.Id\n2.Nombre",
        {it in listOf("1","2") } )
    ){
        "1" -> {
            do {

            }

        }
        "2" -> {

        }
         else -> {}
    }
}

fun main() = runBlocking {

}