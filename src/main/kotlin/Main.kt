package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.example.model.Cliente
import org.example.model.Comida
import org.example.model.EstadoPedido
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

fun obtenerCategorias(catalogo: List<Producto>): MutableMap<Int,String>
{
    val mapCategorias: MutableMap<Int,String> = mutableMapOf()
    catalogo.forEachIndexed { i,value ->
        if (value.categoria !in mapCategorias.values){
            mapCategorias.put(i+1, value.categoria)
        }
    }
    return mapCategorias
}

fun escogerCategoria(catalogo: List<Producto>,categorias: MutableMap<Int,String>): Int
{
    val builderOpcionesCategorias: StringBuilder = StringBuilder("Elige que categoria de producto desea ver\n0. Cancelar")

    for((key,value) in categorias){
        builderOpcionesCategorias.append("${key}. $value\n")
    }

    val textoOpcionesCategorias: String = builderOpcionesCategorias.toString()

    val opcionCategoriaEscogida: Int = pedirDato(textoOpcionesCategorias) {
        val num = it.toIntOrNull(); it.isNotBlank() && (num in categorias.keys ||num == 0)
    }.toInt()

    return opcionCategoriaEscogida
}

fun mostrarCatalogo(catalogo: List<Producto>)
{
    val categorias: MutableMap<Int,String> = obtenerCategorias(catalogo)

    val opcionCategoriaEscogida: Int = escogerCategoria(catalogo,categorias)

    if (opcionCategoriaEscogida == 0) return

    val categoriaSeleccionada: String = categorias.getValue(opcionCategoriaEscogida)

    val catalogofiltrado: List<Producto> = catalogo.filter { it.categoria == categoriaSeleccionada }

    println("Catalogo filtrado por $categoriaSeleccionada")
    println("ID\tNOMBRE\tPRECIO")
    catalogofiltrado.forEach { println("${it.id}\t${it.nombre}\t${it.precio}") }

}

fun añadirProducto(pedido: Pedido,catalogo: List<Producto>) {
    print("Ingrese el id del producto que desee agregar a su orden: ")
    val idProducto: String = readLine()?.trim().orEmpty()
    if(idProducto.isEmpty()){
        println("No se encontro el producto que ingreso, para ver los productos revise el catalogo")
        return
    }
    pedido.listaProductos.add(catalogo[idProducto.toInt()])
}

fun aplicarDescuentosYpromociones(pedido: Pedido): Double{
    pedido.listaProductos.forEach { it.precio += it.reCalcularPrecio() }
    val precioFinal = pedido.calcPrecioTotal()
    return precioFinal
}

fun errorPedido(pedido: Pedido){
    pedido.estadoPedido = EstadoPedido.Error("Error en el proceso del pedido")
}

suspend fun procesarPedido(pedido: Pedido){

    println("Pedido en preparacion...")
    pedido.estadoPedido = EstadoPedido.EnPreparacion
    val tiempoPreparacion: Long = pedido.listaProductos.sumOf { it.tiempoPreparacion }
    delay(tiempoPreparacion)
    println("Pedido listo para entregar")
    pedido.estadoPedido = EstadoPedido.ListoParaEntrega
    TODO("Siento que faltan hacer cositas aqui")
}

fun mostrarDesgloseFinal(pedido: Pedido){
    val builder: StringBuilder = StringBuilder("Resultado de pedido:\nN°  Id  Nombre  Producto  Precio_Base + Valor_añadido  = Valor_Final")
    pedido.listaProductos.forEachIndexed { i,producto ->
        builder.append("${i+1}. ${producto.id} ${producto.nombre} ${producto.categoria}  ${producto.precio}")
        val valorAñadido = producto.reCalcularPrecio()
        builder.append("\t${valorAñadido}\t${producto.precio + valorAñadido}")
    }
    builder.append("Total en bruto\t Descuento tipo cliente\t IVA\t Precio a pagar\n")
    TODO("AGREGAR LOS VALORES")
}

fun menuPrincipal() {
    TODO("menu con las 5 opciones de la guia")
}
fun main() = runBlocking {
    menuPrincipal()
}