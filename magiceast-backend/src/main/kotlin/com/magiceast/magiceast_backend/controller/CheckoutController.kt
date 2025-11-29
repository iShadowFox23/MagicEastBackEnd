package com.magiceast.magiceast_backend.controller

import com.magiceast.magiceast_backend.dto.CompraDTO
import com.magiceast.magiceast_backend.service.ProductoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Checkout",
    description = "Procesamiento de compras y actualización de stock."
)
@RestController
@RequestMapping("/api/checkout")
class CheckoutController(
    private val productoService: ProductoService
) {

    @Operation(
        summary = "Procesar compra",
        description = "Recibe una compra con uno o más ítems y descuenta el stock de cada producto. " +
                "Si alguna validación falla (por ejemplo, stock insuficiente), devuelve un error."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Compra procesada correctamente",
                content = [
                    Content(
                        schema = Schema(
                            implementation = String::class,
                            example = "Compra procesada correctamente"
                        )
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Error de negocio (por ejemplo, stock insuficiente).",
                content = [
                    Content(
                        schema = Schema(
                            implementation = String::class,
                            example = "No hay stock suficiente para el producto 1"
                        )
                    )
                ]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Error interno al procesar la compra."
            )
        ]
    )
    @PostMapping
    fun procesarCompra(
        @RequestBody
        compra: CompraDTO
    ): ResponseEntity<String> {

        return try {
            // Recorremos los ítems y reducimos stock
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
