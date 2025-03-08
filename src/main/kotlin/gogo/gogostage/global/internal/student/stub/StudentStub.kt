package gogo.gogostage.global.internal.student.stub

import gogo.gogostage.global.internal.user.stub.Sex
import java.time.LocalDateTime

data class StudentByIdStub(
    val userId: Long,
    val studentId: Long,
    val schoolId: Long,
    val email: String,
    val name: String,
    val deviceToken: String,
    val sex: Sex,
    val classNumber: Int,
    val studentNumber: Int,
    val isActiveProfanityFilter: Boolean,
    val createdAt: LocalDateTime
)

data class StudentByIdsStub(
    val studentId: Long,
    val schoolId: Long,
    val name: String,
    val deviceToken: String?,
    val sex: Sex,
    val classNumber: Int,
    val studentNumber: Int
)