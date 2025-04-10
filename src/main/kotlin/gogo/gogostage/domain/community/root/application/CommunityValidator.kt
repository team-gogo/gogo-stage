package gogo.gogostage.domain.community.root.application

import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class CommunityValidator {

    fun validPageAndSize(page: Int, size: Int) {
        if (page < 0 || size < 0) {
            throw StageException("page 혹은 size는 음수일 수 없습니다.", HttpStatus.BAD_REQUEST.value())
        }
    }

}