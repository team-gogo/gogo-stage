package gogo.gogostage.domain.image.application

import gogo.gogostage.global.error.StageException
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class ImageValidator {

    fun validImage(image: MultipartFile) {
        val list = listOf("jpg", "png", "gif")
        val splitFile = image.originalFilename.toString().split(".")

        if(splitFile.size != 2)
            throw StageException("Image Extension Invalid", 400)

        val extension = splitFile[1].lowercase()

        if(list.none { it == extension })
            throw StageException("Image Extension Invalid", 400)
    }

}