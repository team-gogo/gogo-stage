package gogo.gogostage.domain.community.root.persistence

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import gogo.gogostage.domain.community.board.persistence.BoardRepository
import gogo.gogostage.domain.community.board.persistence.QBoard
import gogo.gogostage.domain.community.board.persistence.QBoard.board
import gogo.gogostage.domain.community.boardlike.persistence.BoardLikeRepository
import gogo.gogostage.domain.community.comment.persistence.QComment.comment
import gogo.gogostage.domain.community.commentlike.persistence.QCommentLike.commentLike
import gogo.gogostage.domain.community.root.application.dto.*
import gogo.gogostage.domain.community.root.persistence.QCommunity.community
import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.internal.student.api.StudentApi
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus

import org.springframework.stereotype.Repository

@Repository
class CommunityCustomRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
    private val studentApi: StudentApi,
    private val boardRepository: BoardRepository,
    private val boardLikeRepository: BoardLikeRepository
): CommunityCustomRepository {

    override fun searchCommunityBoardPage(stageId: Long, size: Int, category: GameCategory?, sort: SortType, pageable: Pageable): GetCommunityBoardResDto {
        val predicate = BooleanBuilder()

        predicate.and(community.stage.id.eq(stageId))

        category?.let {
            predicate.and(community.category.eq(it))
        }

        val boards = queryFactory
            .selectFrom(board)
            .where(predicate)
            .orderBy(
                when (sort) {
                    SortType.LAST -> board.createdAt.asc()
                    SortType.LATEST -> board.createdAt.desc()
                }
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()



        val condition = listOfNotNull(
            stageId?.let { community.stage.id.eq(it) },
            category?.let { community.category.eq(it) }
        ).reduceOrNull(BooleanExpression::and) ?: Expressions.TRUE

        val studentIds = queryFactory
            .select(board.studentId)
            .from(board)
            .where(condition)
            .fetch()



        val communityStudents = studentApi.queryByStudentsIds(studentIds.toSet().toList())

        val boardDtoList = boards.map { board ->
            val author = communityStudents.students.find { it.studentId == board.studentId }?.let {
                AuthorDto(it.studentId, it.name, it.classNumber, it.studentNumber)
            }

            BoardDto(
                boardId = board.id,
                studentId = board.studentId,
                gameCategory = board.community.category,
                title = board.title,
                likeCount = board.likeCount,
                createdAt = board.createdAt,
                stageType = board.community.stage.type,
                author = author!!
            )
        }.toList()

        val totalElement = boardDtoList.size

        val totalPage = boardRepository.findAll(pageable).totalPages

        val infoDto = InfoDto(totalPage, totalElement)

        return GetCommunityBoardResDto(infoDto, boardDtoList)
    }

    override fun getCommunityBoardInfo(boardId: Long, student: StudentByIdStub): GetCommunityBoardInfoResDto {
        val board = boardRepository.findByIdOrNull(boardId)
            ?: throw StageException("Board Not Found, boardId = $boardId", HttpStatus.NOT_FOUND.value())

        val stageDto = StageDto(board.community.stage.name, board.community.category)

        val boardAuthorList = listOf(board.studentId)

        val boardAuthor = studentApi.queryByStudentsIds(boardAuthorList).students[0]

        val boardAuthorDto = AuthorDto(boardAuthor.studentId, boardAuthor.name, boardAuthor.classNumber, boardAuthor.studentNumber)

        val isAuthorBoardLike = boardLikeRepository.existsByStudentIdAndBoardId(boardAuthorDto.studentId, board.id)

        val predicate = BooleanBuilder()

        predicate.and(QBoard.board.id.eq(comment.board.id))

        val comments = queryFactory.selectFrom(comment)
            .where(predicate)
            .fetch()

        val commentStudentIds = comments.map { it.studentId }.toSet().toList()

        val commentLikeCommentIds = queryFactory.select(commentLike.comment.id)
            .from(commentLike)
            .where(commentLike.comment.board.id.eq(boardId).and(
                commentLike.studentId.eq(student.studentId)
            ))
            .fetch()

        val commentStudents = studentApi.queryByStudentsIds(commentStudentIds)

        val commentDto = comments.map { comment ->
            val author = commentStudents.students.find { it.studentId == comment.studentId }?.let {
                AuthorDto(it.studentId, it.name, it.classNumber, it.studentNumber)
            }

            val isLiked = commentLikeCommentIds.contains(comment.id)

            CommentDto(
                commentId = comment.id,
                content = comment.content,
                createdAt = comment.createdAt,
                likeCount = comment.likeCount,
                isLiked = isLiked,
                author = author!!
            )

        }

        val response = GetCommunityBoardInfoResDto(
            boardId = board.id,
            title = board.title,
            content = board.content,
            likeCount = board.likeCount,
            isLiked = isAuthorBoardLike,
            createdAt = board.createdAt,
            stage = stageDto,
            author = boardAuthorDto,
            commentCount = board.commentCount,
            comment = commentDto
        )

        return response

    }
}
