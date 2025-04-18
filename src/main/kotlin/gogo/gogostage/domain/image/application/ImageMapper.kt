package gogo.gogostage.domain.image.application

import gogo.gogostage.domain.image.application.dto.ImageUploadResDto
import org.springframework.stereotype.Component

@Component
class ImageMapper {

    fun mapUpload(url: String): ImageUploadResDto =
        ImageUploadResDto(
            imageUrl = url
        )

}