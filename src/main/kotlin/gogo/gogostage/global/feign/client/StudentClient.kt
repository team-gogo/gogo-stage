package gogo.gogostage.global.feign.client

import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import gogo.gogostage.global.internal.student.stub.StudentByIdsStub
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "gogo-user")
interface StudentClient {
    @GetMapping("/user/student")
    fun queryStudentByUserId(
        @RequestParam("userId") userId: Long
    ): StudentByIdStub

    @GetMapping("/user/student/bundle")
    fun queryCommunityStudentsByStudentId(
        @RequestParam("studentIds") ids: List<Long>
    ): StudentByIdsStub
}
