package gogo.gogostage.domain.image.application

import gogo.gogostage.domain.image.application.dto.ImageUploadResDto
import gogo.gogostage.global.aws.s3.S3Uploader
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImageServiceImpl(
    private val s3Uploader: S3Uploader,
    private val imageMapper: ImageMapper,
    private val imageValidator: ImageValidator
): ImageService {

    override fun upload(image: MultipartFile): ImageUploadResDto {
        imageValidator.validImage(image)
        val url = s3Uploader.upload(image)
        return imageMapper.mapUpload(url)
    }

}