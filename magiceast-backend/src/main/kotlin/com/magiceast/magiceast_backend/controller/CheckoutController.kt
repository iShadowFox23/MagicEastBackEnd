package com.magiceast.magiceast_backend.controller

import com.magiceast.magiceast_backend.service.ProductoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class ItemCompraDTO(
    val productoId: Long,
    val cantidad: Int
)

data class CompraDTO(
    val items: List<ItemCompraDTO>
)

@RestController
@RequestMapping("/api/checkout")
class CheckoutController(
    private val productoService: ProductoService
) {

    @PostMapping
    fun procesarCompra(@RequestBody compra: CompraDTO): ResponseEntity<String> {

        compra.items.forEach { item ->
            productoService.reducirStock(item.productoId, item.cantidad)
        }

        return ResponseEntity.ok("Compra procesada correctamente")
    }
}
