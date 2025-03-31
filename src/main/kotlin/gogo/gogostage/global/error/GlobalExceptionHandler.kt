package gogo.gogostage.global.error

import feign.FeignException
import jakarta.validation.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(StageException::class)
    fun stageExceptionHandler(e: StageException): ResponseEntity<ErrorResponse> =
        ResponseEntity(ErrorResponse.of(e), HttpStatus.valueOf(e.status))

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): ResponseEntity<ErrorResponse> =
        ResponseEntity(ErrorResponse.of(e.message, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(MethodArgumentNotValidException::class, HttpMessageNotReadableException::class)
    fun handleValidationException(e: Exception): ResponseEntity<ErrorResponse> {
        val errorMessage = when (e) {
            is MethodArgumentNotValidException -> methodArgumentNotValidExceptionToJson(e.bindingResult)
            is HttpMessageNotReadableException -> "요청 본문을 읽을 수 없습니다."
            else -> "잘못된 요청입니다."
        }
        return ResponseEntity(ErrorResponse.of(errorMessage, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST)
    }

    private fun methodArgumentNotValidExceptionToJson(bindingResult: BindingResult): String {
        val fieldErrors = bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
        return fieldErrors.toString().replace("\"", "'")
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(e: DataIntegrityViolationException): ResponseEntity<DataErrorResponse> =
        ResponseEntity(ErrorResponse.of(e), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(e: NoHandlerFoundException): ResponseEntity<NoHandlerErrorResponse> =
        ResponseEntity(ErrorResponse.of(e), HttpStatus.FORBIDDEN)

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<ErrorResponse> =
        ResponseEntity(ErrorResponse.of("필드 유효성 검사에 실패했습니다: ${e.message}", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(FeignException::class)
    fun handleFeignException(e: FeignException): ResponseEntity<ErrorResponse> =
        ResponseEntity(ErrorResponse.of("서버 내부 통신 중 오류가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR)

    @ExceptionHandler(RuntimeException::class)
    fun handleException(e: RuntimeException): ResponseEntity<ErrorResponse> =
        ResponseEntity(ErrorResponse.of(e.message, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR)
}
