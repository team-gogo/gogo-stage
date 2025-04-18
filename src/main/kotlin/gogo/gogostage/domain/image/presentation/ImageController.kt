package gogo.gogostage.domain.image.presentation

import gogo.gogostage.domain.image.application.ImageService
import gogo.gogostage.domain.image.application.dto.ImageUploadResDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/stage/image")
class ImageController(
    private val imageService: ImageService
) {

    @PostMapping
    fun uploadImage(
        @RequestPart("image") file: MultipartFile
    ): ResponseEntity<ImageUploadResDto> {
        val response = imageService.upload(file)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

}