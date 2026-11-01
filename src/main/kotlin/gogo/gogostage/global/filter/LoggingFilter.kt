package gogo.gogostage.global.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
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
            filterChain.doFilter(request, response)
            return
        }

        val logId = UUID.randomUUID()
        val startTime = System.nanoTime()
        var completed = false

        try {
            filterChain.doFilter(request, response)
            completed = true
        } finally {
            val status = if (completed) response.status.toString() else "incomplete"
            log.info(
                "Log-ID: {}, Http-Method: {}, URI: {}, Status-Code: {}, Response Time: {}ms",
                logId,
                request.method,
                request.requestURI,
                status,
                (System.nanoTime() - startTime) / 1_000_000
            )
        }
    }

    private fun isNotLoggingURL(requestURI: String): Boolean {
        return Arrays.stream(NOT_LOGGING_URL)
            .anyMatch { pattern: String -> matcher.match(pattern, requestURI) }
    }
}
