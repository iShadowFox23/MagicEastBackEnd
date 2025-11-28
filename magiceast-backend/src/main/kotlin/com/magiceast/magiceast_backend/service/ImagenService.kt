package com.magiceast.magiceast_backend.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
class ImagenService(

    @Value("\${app.upload-dir}")
    private val uploadDirPath: String

) {

    fun guardarImagen(imagen: MultipartFile): String {

        val absolutePath = File(uploadDirPath).absoluteFile


        if (!absolutePath.exists()) {
            absolutePath.mkdirs()
        }

        val nombreArchivo = "${System.currentTimeMillis()}_${imagen.originalFilename}"

        // Destino de las imagenes
        val destino = File(absolutePath, nombreArchivo)

        imagen.inputStream.use { input ->
            destino.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return nombreArchivo
    }
}
