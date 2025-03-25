package gogo.gogostage.global.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security.internal")
class SecurityProperties(
    val serverToServerIp: String,
    val serverToServerSubnet: String,
)
