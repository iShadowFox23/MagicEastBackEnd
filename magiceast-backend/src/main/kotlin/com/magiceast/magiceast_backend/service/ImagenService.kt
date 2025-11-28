package com.magiceast.magiceast_backend.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
class ImagenService {

    private val uploadDir = "uploads/"

    init {
        val dir = File(uploadDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    fun guardarImagen(file: MultipartFile): String {
        val fileName = System.currentTimeMillis().toString() + "_" + (file.originalFilename ?: "imagen")

        val destino = File(uploadDir + fileName)

        file.transferTo(destino)

        return fileName
    }
}
