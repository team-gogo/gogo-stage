package gogo.gogostage.global.feign

import feign.FeignException
import feign.Response
import feign.codec.ErrorDecoder
import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus

class FeignClientErrorDecoder : ErrorDecoder {
    override fun decode(
        methodKey: String?,
        response: Response,
    ): Exception? {
        if (response.status() >= 400) {
            throw StageException("HTTP 통신 오류", HttpStatus.INTERNAL_SERVER_ERROR.value())
        }
        return FeignException.errorStatus(methodKey, response)
    }
}
