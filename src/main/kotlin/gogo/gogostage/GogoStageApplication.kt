package gogo.gogostage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@EnableAsync
@EnableScheduling
@SpringBootApplication
class GogoStageApplication

fun main(args: Array<String>) {
    runApplication<GogoStageApplication>(*args)
}
