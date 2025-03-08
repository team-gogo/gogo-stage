package gogo.gogostage.global.internal.student.api

import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import gogo.gogostage.global.internal.student.stub.StudentByIdsStub

interface StudentApi {
    fun queryByUserId(userId: Long): StudentByIdStub
    fun queryByStudentsIds(studentIds: List<Long>): List<StudentByIdsStub>
}