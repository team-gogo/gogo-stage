package gogo.gogostage.global.config

import gogo.gogostage.global.security.SecurityProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(
    basePackageClasses = [
        SecurityProperties::class
    ]
)
class PropertiesScanConfig
