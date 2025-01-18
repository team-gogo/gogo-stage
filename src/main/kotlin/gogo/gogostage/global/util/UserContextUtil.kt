package gogo.gogostage.global.util

import gogo.gogostage.global.internal.student.api.StudentApi
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserContextUtil(
    private val studentApi: StudentApi
) {

    fun getCurrentUserId(): Long {
        return SecurityContextHolder.getContext().authentication.name.toLong()
    }

    fun getCurrentStudent(): StudentByIdStub {
        val student = studentApi.queryByUserId(getCurrentUserId())
        return student
    }
}
