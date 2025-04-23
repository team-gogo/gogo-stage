package gogo.gogostage.domain.image.application

import gogo.gogostage.global.error.StageException
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class ImageValidator {

    fun validImage(image: MultipartFile) {
        val allowedExtensions = listOf("jpg", "png", "gif")
        val maxFileSize = 10 * 1024 * 1024

        val fileName = image.originalFilename ?: throw StageException("No file name", 400)

        val extension = fileName.substringAfterLast('.', missingDelimiterValue = "").lowercase()

        if (extension.isBlank() || extension !in allowedExtensions) {
            throw StageException("Image Extension Invalid", 400)
        }

        if (image.size >= maxFileSize) {
            throw StageException("Image Size Exceeds 10MB", 400)
        }
    }


}