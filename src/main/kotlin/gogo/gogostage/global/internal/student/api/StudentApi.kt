package gogo.gogostage.global.internal.student.api

import gogo.gogostage.global.internal.student.stub.StudentByIdStub

interface StudentApi {
    fun queryByUserId(userId: Long): StudentByIdStub
}