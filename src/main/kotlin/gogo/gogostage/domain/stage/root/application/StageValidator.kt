package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class StageValidator {

    fun valid(maintainer: List<Long>) {
        if (maintainer.size > 5) {
            throw StageException("스테이지 관리자는 최대 5명까지 가능합니다.", HttpStatus.BAD_REQUEST.value())
        }
    }

}
