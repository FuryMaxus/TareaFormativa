package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.example.model.*


fun pedirDato(mensaje: String, validar: (String) -> Boolean): String {
    while(true){
        println(mensaje)
        val input = readLine()?.trim().orEmpty()
        if (validar(input)) return input
        println("Error: entrada invalida, intentelo de nuevo")
    }
}

fun pedirDatosCliente(): Cliente {
    val nombre = pedirDato(
        "Ingrese su nombre") { it.isNotBlank() }

    val tipoCliente = when(pedirDato(
        "Seleccione el tipo de usuario:\n1. Regular\n2. VIP\n3. Deluxe")
    {it in listOf("1", "2", "3")})
    {
        "1" -> TipoCliente.REGULAR
        "2" -> TipoCliente.VIP
        "3" -> TipoCliente.PREMIUM
        else -> TipoCliente.REGULAR
    }
    return Cliente(nombre,tipoCliente)
}

fun obtenerCategorias(catalogo: List<Producto>): MutableMap<Int,String> {
    val mapCategorias: MutableMap<Int,String> = mutableMapOf()
    catalogo.forEachIndexed { i,value ->
        if (value.categoria !in mapCategorias.values){
            mapCategorias.put(i+1, value.categoria)
        }
    }
    return mapCategorias
}

fun escogerCategoria(categorias: MutableMap<Int,String>): Int {
    val builderOpcionesCategorias: StringBuilder =
        StringBuilder("Elige que categoria de producto desea ver\n0. Cancelar\n")

    for((key,value) in categorias){
        builderOpcionesCategorias.append("${key}. $value\n")
    }

    val textoOpcionesCategorias: String = builderOpcionesCategorias.toString()

    val opcionCategoriaEscogida: Int = pedirDato(textoOpcionesCategorias) {
        val num = it.toIntOrNull(); it.isNotBlank() && (num in categorias.keys || num == 0)
    }.toInt()

    return opcionCategoriaEscogida
}

fun mostrarCatalogo(catalogo: List<Producto>) {

    val categorias: MutableMap<Int,String> = obtenerCategorias(catalogo)
    val opcionCategoriaEscogida: Int = escogerCategoria(categorias)

    if (opcionCategoriaEscogida == 0) return

    val categoriaSeleccionada: String = categorias.getValue(opcionCategoriaEscogida)
    val catalogofiltrado: List<Producto> = catalogo.filter { it.categoria == categoriaSeleccionada }

    println("Catalogo filtrado por $categoriaSeleccionada")
    println("ID\tNOMBRE\tPRECIO")

    catalogofiltrado.forEach { println("${it.id}\t${it.nombre}\t${it.precio}") }
}

fun agregarProducto(pedido: Pedido,catalogo: List<Producto>) {

    println("Ingrese el id del producto que desee agregar a su orden: ")

    val idProducto: String = readlnOrNull()?.trim().orEmpty()
    val existe: Boolean = catalogo.any {it.id.toString() == idProducto}

    if(idProducto.isEmpty() || (!existe)){
        println("No se encontro el producto que ingreso, para ver los productos revise el catalogo")
        return
    }
    pedido.listaProductos.add(catalogo[idProducto.toInt()-1])
}


/*fun errorPedido(pedido: Pedido){
    pedido.estadoPedido = EstadoPedido.Error("Error en el proceso del pedido")
}*/

suspend fun procesarPedido(pedido: Pedido){
    println("Pedido en preparacion...")
    pedido.estadoPedido = EstadoPedido.EnPreparacion
    val tiempoPreparacion: Long = pedido.listaProductos.sumOf { it.tiempoPreparacion }
    delay(tiempoPreparacion)
    println("Pedido listo para entregar")
    pedido.estadoPedido = EstadoPedido.ListoParaEntrega
}

fun crearTextoPreciosFinales(pedido: Pedido): String {
    val builderPreciosFinales: StringBuilder =
        StringBuilder("Id\t\tNombre Producto\t\tCategoria\t\tValor Base\t\t'Valor añadido'\t\tValor Final\n")

    pedido.listaProductos.forEach { producto ->
        builderPreciosFinales.append("${producto.id}\t\t${producto.nombre}\t\t${producto.categoria}\t\t${producto.precio}")
        val precioFinal = producto.reCalcularPrecio()
        builderPreciosFinales.append("\t\t${precioFinal - producto.precio}\t\t${precioFinal}\n")
    }

    val textoPreciosFinales: String = builderPreciosFinales.toString()
    return textoPreciosFinales
}

fun mostrarDesgloseFinal(pedido: Pedido){
    val builderDesglose: StringBuilder = StringBuilder("Desglose de pedido:\n")

    builderDesglose.append(crearTextoPreciosFinales(pedido))
    builderDesglose.append("Total en bruto\t\tDescuento\t\ttipo cliente\t\tIVA\t\tPrecio a pagar\n")
    builderDesglose.append("${pedido.calcSumTotal()}\t\t${pedido.calcDescuentoPorTipoCliente()}\t\t${pedido.calcPrecioTotal()}")

    val textoDesglose: String = builderDesglose.toString()
    println(textoDesglose)
}

fun pedidoNoVacio(pedido: Pedido): Boolean{
    return pedido.listaProductos.isEmpty()
}

suspend fun menuPrincipal(pedido: Pedido, catalogo: List<Producto>): Boolean {
    var salir = false
    when (pedirDato(
        "Escoja una de las opciones\n" +
                "0.Salir\n1.Ver catalogo\n2.Agregar Producto\n3.Calcular precios\n4.Procesar pedido\n5.Resumen de operacion"
    )
    { it in listOf("0", "1", "2", "3", "4", "5") }) {

        "0" -> salir = true
        "1" -> mostrarCatalogo(catalogo)
        "2" -> agregarProducto(pedido, catalogo)
        "3" -> println(crearTextoPreciosFinales(pedido))
        "4" -> if ( pedidoNoVacio(pedido) ) procesarPedido(pedido) else println("Pedido vacio")
        "5" -> if ( pedidoNoVacio(pedido) ) { mostrarDesgloseFinal(pedido); salir = true } else println("Pedido vacio")
    }
    return salir
}

fun main(): Unit = runBlocking {
    val cliente: Cliente = pedirDatosCliente()
    val pedido = Pedido(cliente, mutableListOf(),EstadoPedido.Pendiente)
    val producto1 = Bebida(1,"coca-cola",3000.56,"Gaseosa",1000L,TamañoBebida.MEDIANO)
    val producto2 = Comida(2,"burger",5000.75,"Comida rapida",5000L,false)



    val listaProductos = listOf(producto1,producto2)

    do {
        if (menuPrincipal(pedido,listaProductos)) break
    }while (true)

}