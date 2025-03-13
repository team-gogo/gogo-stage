package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.comment.persistence.Comment
import gogo.gogostage.domain.community.root.application.dto.AuthorDto
import gogo.gogostage.domain.community.root.application.dto.WriteBoardCommentReqDto
import gogo.gogostage.domain.community.root.application.dto.WriteBoardCommentResDto
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import org.springframework.stereotype.Component

@Component
class CommunityMapper {

    fun mapWriteBoardCommentResDto(
        comment: Comment,
        writeBoardCommentDto: WriteBoardCommentReqDto,
        student: StudentByIdStub
    ) =
        WriteBoardCommentResDto(
            commentId = comment.id,
            content = writeBoardCommentDto.content,
            createdAt = comment.createdAt,
            likeCount = comment.likeCount,
            author = AuthorDto(
                studentId = student.studentId,
                name = student.name,
                classNumber = student.classNumber,
                studentNumber = student.studentNumber
            )
        )

}