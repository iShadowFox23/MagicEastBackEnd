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

        return try {


            compra.items.forEach { item ->
                productoService.reducirStock(item.productoId, item.cantidad)
            }


            ResponseEntity.ok("Compra procesada correctamente")

        } catch (e: IllegalStateException) {

            ResponseEntity.badRequest().body(e.message)
        } catch (e: Exception) {

            ResponseEntity.internalServerError()
                .body("Error interno al procesar la compra: ${e.message}")
        }
    }
}
