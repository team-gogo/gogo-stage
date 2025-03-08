package gogo.gogostage.global.internal.student.api

import gogo.gogostage.global.feign.client.StudentClient
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import gogo.gogostage.global.internal.student.stub.StudentByIdsStub
import org.springframework.stereotype.Component

@Component
class StudentApiImpl(
    private val studentClient: StudentClient
) : StudentApi {
    override fun queryByUserId(userId: Long): StudentByIdStub {
        return studentClient.queryStudentByUserId(userId)
    }

    override fun queryByStudentsIds(studentIds: List<Long>): List<StudentByIdsStub> {
        return studentClient.queryCommunityStudentsByStudentId(studentIds)
    }
}
