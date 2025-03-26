package gogo.gogostage.global.config


import gogo.gogostage.global.filter.AuthenticationFilter
import gogo.gogostage.global.filter.LoggingFilter
import gogo.gogostage.global.handler.CustomAccessDeniedHandler
import gogo.gogostage.global.handler.CustomAuthenticationEntryPointHandler
import gogo.gogostage.global.internal.user.stub.Authority
import gogo.gogostage.global.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.IpAddressMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    private val customAccessDeniedHandler: CustomAccessDeniedHandler,
    private val customAuthenticationEntryPointHandler: CustomAuthenticationEntryPointHandler,
    private val authenticationFilter: AuthenticationFilter,
    private val loggingFilter: LoggingFilter,
    private val securityProperties: SecurityProperties,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.formLogin { it.disable() }
            .httpBasic { it.disable() }

        http.csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }

        http.exceptionHandling { handling ->
            handling.accessDeniedHandler(customAccessDeniedHandler)
            handling.authenticationEntryPoint(customAuthenticationEntryPointHandler)
        }

        http.sessionManagement { sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        http.addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(authenticationFilter, LoggingFilter::class.java)

        http.authorizeHttpRequests { httpRequests ->
            // health check
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/health").permitAll()
            httpRequests.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()

            // stage
            httpRequests.requestMatchers(HttpMethod.POST, "/stage/fast").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.POST, "/stage/official").hasAnyRole(Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.POST, "/stage/join/{stage_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.POST, "/stage/team/{game_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.POST, "/stage/confirm/{stage_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/team/{game_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/team/temp/{game_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.GET, "/stage").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/game/{stage_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/me").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/team/info/{team_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/rank/{stage_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.PATCH, "/stage/match/notice/{match_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/maintainer/me/{stage_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/temp-point/me/{stage_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)

            // match
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/match/search/{stage_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/match/me/{stage_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/match/info/{match_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/team/temp/{game_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)

            // community
            httpRequests.requestMatchers(HttpMethod.POST, "/stage/community/{game_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/community/{stage_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/community/board/{board_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.POST, "/stage/community/board/like/{board_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.POST, "/stage/community/comment/{board_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.POST, "/stage/community/comment/like/{board_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)

            // server to server
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/api/point/{stage_id}").access { _, context -> hasIpAddress(context) }
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/api/match/info").access { _, context -> hasIpAddress(context) }
            httpRequests.requestMatchers(HttpMethod.GET, "/stage/api/participant/{stage_id}").access { _, context -> hasIpAddress(context) }

            httpRequests.anyRequest().denyAll()
        }

        return http.build()
    }

    private fun hasIpAddress(context: RequestAuthorizationContext): AuthorizationDecision {
        val ALLOWED_IP_ADDRESS_MATCHER = IpAddressMatcher("${securityProperties.serverToServerIp}${securityProperties.serverToServerSubnet}")
        return AuthorizationDecision(ALLOWED_IP_ADDRESS_MATCHER.matches(context.request))
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        // plz custom allowed client origins
        configuration.allowedOrigins = listOf("*")

        configuration.allowedMethods = listOf(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.DELETE.name(),
            HttpMethod.OPTIONS.name()
        )

        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}
