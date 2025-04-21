package gogo.gogostage.domain.image.application

import gogo.gogostage.domain.image.application.dto.ImageUploadResDto
import org.springframework.web.multipart.MultipartFile

interface ImageService {
    fun upload(image: MultipartFile): ImageUploadResDto
}