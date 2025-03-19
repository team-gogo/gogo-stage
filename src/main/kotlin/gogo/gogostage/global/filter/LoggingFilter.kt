package gogo.gogostage.global.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

@Component
class LoggingFilter : OncePerRequestFilter() {

    private val log: Logger = LoggerFactory.getLogger(LoggingFilter::class.java)

    private val NOT_LOGGING_URL: Array<String> = arrayOf(
        "/actuator/**"
    )

    private val matcher = AntPathMatcher()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        if (isNotLoggingURL(request.requestURI)) {
            try {
                filterChain.doFilter(request, response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val requestWrapper = ContentCachingRequestWrapper(request)
        val responseWrapper = ContentCachingResponseWrapper(response)
        val logId = UUID.randomUUID()
        val startTime = System.currentTimeMillis()

        try {
            requestLogging(requestWrapper, logId)
            filterChain.doFilter(requestWrapper, responseWrapper)
        } catch (e: Exception) {
            log.error("LoggingFilter의 FilterChain에서 예외가 발생했습니다.", e)
        } finally {
            responseLogging(responseWrapper, startTime, logId)
            try {
                responseWrapper.copyBodyToResponse()
            } catch (e: IOException) {
                log.error("LoggingFilter에서 response body를 출력하는 도중 예외가 발생했습니다.", e)
            }
        }
    }

    private fun isNotLoggingURL(requestURI: String): Boolean {
        return Arrays.stream(NOT_LOGGING_URL)
            .anyMatch { pattern: String -> matcher.match(pattern, requestURI) }
    }

    private fun requestLogging(request: ContentCachingRequestWrapper, logId: UUID) {
        log.info(
            "Log-ID: {}, IP: {}, URI: {}, Http-Method: {}, Params: {}, Content-Type: {}, User-Agent: {}, Request-Body: {}",
            logId,
            request.remoteAddr,
            request.requestURI,
            request.method,
            request.queryString,
            request.contentType,
            request.getHeader("User-Agent"),
            getRequestBody(request.contentAsByteArray)
        )
    }

    private fun responseLogging(response: ContentCachingResponseWrapper, startTime: Long, logId: UUID) {
        val responseTime = System.currentTimeMillis() - startTime
        log.info(
            "Log-ID: {}, Status-Code: {}, Content-Type: {}, Response Time: {}ms, Response-Body: {}",
            logId,
            response.status,
            response.contentType,
            responseTime,
            String(response.contentAsByteArray, StandardCharsets.UTF_8)
        )
    }

    private fun getRequestBody(byteArrayContent: ByteArray): String {
        val oneLineContent = String(byteArrayContent, StandardCharsets.UTF_8).replace(Regex("\\s"), "")
        return if (StringUtils.hasText(oneLineContent)) oneLineContent else "[empty]"
    }

}
