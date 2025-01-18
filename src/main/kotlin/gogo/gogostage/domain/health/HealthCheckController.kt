package gogo.gogostage.domain.health

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {

    @GetMapping("/stage/health")
    fun healthCheck() = "GOGO Stage Service OK"

}
