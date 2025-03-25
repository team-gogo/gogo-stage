package gogo.gogostage.domain.community.root.persistence

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import gogo.gogostage.domain.community.board.persistence.BoardRepository
import gogo.gogostage.domain.community.board.persistence.QBoard.board
import gogo.gogostage.domain.community.boardlike.persistence.BoardLikeRepository
import gogo.gogostage.domain.community.comment.persistence.QComment.comment
import gogo.gogostage.domain.community.commentlike.persistence.QCommentLike.commentLike
import gogo.gogostage.domain.community.root.application.dto.*
import gogo.gogostage.domain.community.root.persistence.QCommunity.community
import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.internal.student.api.StudentApi
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
        ).reduceOrNull(BooleanExpression::and) ?: Expressions.TRUE // 조건이 없으면 항상 참

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

        val totalPage = if (totalElement % size == 0) {
            totalElement / size
        } else {
            totalElement / size + 1
        }

        val infoDto = InfoDto(totalPage, totalElement)

        return GetCommunityBoardResDto(infoDto, boardDtoList)
    }

    override fun getCommunityBoardInfo(boardId: Long): GetCommunityBoardInfoResDto {
        val queryBoard = boardRepository.findByIdOrNull(boardId)
            ?: throw StageException("Board Not Found, boardId = $boardId", HttpStatus.NOT_FOUND.value())

        val stageDto = StageDto(queryBoard.community.stage.name, queryBoard.community.category)

        val boardAuthorStudentIdList = listOf(queryBoard.studentId)

        val boardAuthor = studentApi.queryByStudentsIds(boardAuthorStudentIdList).students

        val boardAuthorDto = boardAuthor.map { student ->
            AuthorDto(student.studentId, student.name, student.classNumber, student.studentNumber)
        }

        val boardAuthorStudentId = boardAuthorDto.get(0).studentId

        val isAuthorBoardLike = boardLikeRepository.existsByStudentIdAndBoardId(boardAuthorStudentId, queryBoard.id)

        val predicate = BooleanBuilder()

        predicate.and(board.id.eq(comment.board.id))

        val comments = queryFactory.selectFrom(comment)
            .where(predicate)
            .fetch()

        val studentIds = comments.map { it.studentId }

        val commentLikeIds = queryFactory.select(commentLike.id)
            .from(commentLike)
            .where(commentLike.comment.board.id.eq(boardId).and(
                commentLike.studentId.eq(boardAuthorStudentId)
            ))
            .fetch()

        val commentStudents = studentApi.queryByStudentsIds(studentIds)

        val commentDto = comments.map { comment ->
            val author = commentStudents.students.find { it.studentId == comment.studentId }?.let {
                AuthorDto(it.studentId, it.name, it.classNumber, it.studentNumber)
            }

            val isLiked = commentLikeIds.contains(comment.id)

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
            boardId = queryBoard.id,
            title = queryBoard.title,
            content = queryBoard.content,
            likeCount = queryBoard.likeCount,
            isLiked = isAuthorBoardLike,
            createdAt = queryBoard.createdAt,
            stage = stageDto,
            author = boardAuthorDto.get(0),
            commentCount = queryBoard.commentCount,
            comment = commentDto
        )

        return response

    }
}
