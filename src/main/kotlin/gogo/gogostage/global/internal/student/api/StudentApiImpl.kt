package gogo.gogostage.global.internal.student.api

import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import org.springframework.stereotype.Component

@Component
class StudentApiImpl : StudentApi {
    override fun queryByUserId(userId: Long): StudentByIdStub {
        TODO("Not yet implemented")
    }
}
