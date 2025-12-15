package com.magiceast.magiceast_backend.service

import com.magiceast.magiceast_backend.dto.*
import com.magiceast.magiceast_backend.modelo.Order
import com.magiceast.magiceast_backend.modelo.OrderItem
import com.magiceast.magiceast_backend.repositorio.OrderRepository
import com.magiceast.magiceast_backend.repositorio.ProductoRepository
import com.magiceast.magiceast_backend.repositorio.UsuarioRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val usuarioRepository: UsuarioRepository,
    private val productoRepository: ProductoRepository,
    private val productoService: ProductoService
) {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    @Transactional
    fun crearOrden(request: CreateOrderRequest): OrderResponse {
        // Validar que el usuario existe
        val usuario = usuarioRepository.findById(request.usuarioId).orElseThrow {
            IllegalArgumentException("Usuario con ID ${request.usuarioId} no encontrado")
        }

        // Validar que hay items
        if (request.items.isEmpty()) {
            throw IllegalArgumentException("La orden debe contener al menos un item")
        }

        // Crear la orden (sin items aún)
        val orden = Order(
            usuario = usuario,
            direccionEnvio = request.direccionEnvio
        )

        // Crear los items y calcular el total
        var total = 0
        val orderItems = mutableListOf<OrderItem>()

        for (itemRequest in request.items) {
            // Validar que el producto existe
            val producto = productoRepository.findById(itemRequest.productoId).orElseThrow {
                IllegalArgumentException("Producto con ID ${itemRequest.productoId} no encontrado")
            }

            // Validar cantidad
            if (itemRequest.cantidad <= 0) {
                throw IllegalArgumentException("La cantidad debe ser mayor a 0")
            }

            // Validar stock disponible
            if (producto.stock < itemRequest.cantidad) {
                throw IllegalStateException(
                    "Stock insuficiente para '${producto.nombre}': " +
                            "disponible = ${producto.stock}, solicitado = ${itemRequest.cantidad}"
                )
            }

            // Crear el item
            val orderItem = OrderItem(
                order = orden,
                producto = producto,
                cantidad = itemRequest.cantidad,
                precioUnitario = producto.precio
            )

            orderItems.add(orderItem)
            total += orderItem.subtotal

            // Reducir el stock del producto
            productoService.reducirStock(producto.id, itemRequest.cantidad)
        }

        // Asignar items y total a la orden
        orden.items.addAll(orderItems)
        orden.total = total

        // Guardar la orden (cascade guardará los items)
        val ordenGuardada = orderRepository.save(orden)

        return toOrderResponse(ordenGuardada)
    }

    fun obtenerOrden(id: Long): OrderResponse? {
        val orden = orderRepository.findById(id).orElse(null) ?: return null
        return toOrderResponse(orden)
    }

    fun listarTodasLasOrdenes(): List<OrderResponse> {
        return orderRepository.findAll().map { toOrderResponse(it) }
    }

    fun listarOrdenesPorUsuario(usuarioId: Long): List<OrderResponse> {
        return orderRepository.findByUsuarioId(usuarioId).map { toOrderResponse(it) }
    }

    fun listarOrdenesPorEstado(estado: String): List<OrderResponse> {
        return orderRepository.findByEstado(estado).map { toOrderResponse(it) }
    }

    @Transactional
    fun actualizarEstado(id: Long, nuevoEstado: String): OrderResponse? {
        val orden = orderRepository.findById(id).orElse(null) ?: return null
        
        // Validar que el estado es válido
        val estadosValidos = listOf("PENDIENTE", "CONFIRMADA", "ENVIADA", "ENTREGADA", "CANCELADA")
        if (nuevoEstado !in estadosValidos) {
            throw IllegalArgumentException("Estado inválido: $nuevoEstado. Estados válidos: $estadosValidos")
        }

        orden.estado = nuevoEstado
        val ordenActualizada = orderRepository.save(orden)
        return toOrderResponse(ordenActualizada)
    }

    @Transactional
    fun cancelarOrden(id: Long): OrderResponse? {
        return actualizarEstado(id, "CANCELADA")
    }

    // Función auxiliar para convertir Order a OrderResponse
    private fun toOrderResponse(orden: Order): OrderResponse {
        return OrderResponse(
            id = orden.id,
            usuarioId = orden.usuario.id,
            usuarioNombre = orden.usuario.nombre,
            items = orden.items.map { item ->
                OrderItemResponse(
                    id = item.id,
                    productoId = item.producto.id,
                    productoNombre = item.producto.nombre,
                    cantidad = item.cantidad,
                    precioUnitario = item.precioUnitario,
                    subtotal = item.subtotal
                )
            },
            fechaCreacion = orden.fechaCreacion.format(dateFormatter),
            total = orden.total,
            estado = orden.estado,
            direccionEnvio = orden.direccionEnvio
        )
    }
}
