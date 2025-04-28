package gogo.gogostage.domain.community.root.persistence

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import gogo.gogostage.domain.community.board.persistence.Board
import gogo.gogostage.domain.community.board.persistence.QBoard.board
import gogo.gogostage.domain.community.boardlike.persistence.BoardLikeRepository
import gogo.gogostage.domain.community.comment.persistence.QComment.comment
import gogo.gogostage.domain.community.commentlike.persistence.QCommentLike.commentLike
import gogo.gogostage.domain.community.root.application.dto.*
import gogo.gogostage.domain.community.root.persistence.QCommunity.community
import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import org.springframework.data.domain.Pageable

import org.springframework.stereotype.Repository

@Repository
class CommunityCustomRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
    private val boardLikeRepository: BoardLikeRepository
): CommunityCustomRepository {

    override fun searchCommunityBoardPage(isActiveProfanityFilter: Boolean, stageId: Long, size: Int, category: GameCategory?, sort: SortType, pageable: Pageable): GetCommunityBoardResDto {
        val predicate = BooleanBuilder()

        predicate.and(community.stage.id.eq(stageId))

        category?.let {
            predicate.and(community.category.eq(it))
        }

        if (isActiveProfanityFilter.not()) {
            predicate.and(board.isFiltered.eq(false))
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

        val boardDtoList = boards.map { board ->

            BoardDto(
                boardId = board.id,
                studentId = board.studentId,
                gameCategory = board.community.category,
                title = board.title,
                likeCount = board.likeCount,
                createdAt = board.createdAt,
                stageType = board.community.stage.type,
                commentCount = board.commentCount,
            )
        }.toList()

        val totalElement = queryFactory
            .select(board.count())
            .from(board)
            .where(predicate)
            .fetchOne() ?: 0L

        val totalPage = if (totalElement % pageable.pageSize == 0L) {
            (totalElement / pageable.pageSize).toInt()
        } else {
            (totalElement / pageable.pageSize + 1).toInt()
        }

        val infoDto = InfoDto(totalPage, boardDtoList.size)

        return GetCommunityBoardResDto(infoDto, boardDtoList)
    }

    override fun getCommunityBoardInfo(isActiveProfanityFilter: Boolean, board: Board, student: StudentByIdStub): GetCommunityBoardInfoResDto {
        val stageDto = StageDto(board.community.stage.name, board.community.category)

        val isAuthorBoardLike = boardLikeRepository.existsByStudentIdAndBoardId(student.studentId, board.id)

        val predicate = BooleanBuilder()
        predicate.and(comment.board.id.eq(board.id))

        if (isActiveProfanityFilter.not()) {
            predicate.and(comment.isFiltered.eq(false))
        }

        val comments = queryFactory.selectFrom(comment)
            .where(predicate)
            .fetch()

        val commentLikeCommentIds = queryFactory.select(commentLike.comment.id)
            .from(commentLike)
            .where(commentLike.comment.board.id.eq(board.id).and(
                commentLike.studentId.eq(student.studentId)
            ))
            .fetch()

        val commentDto = comments.map { comment ->
            val isLiked = commentLikeCommentIds.contains(comment.id)

            CommentDto(
                commentId = comment.id,
                content = comment.content,
                createdAt = comment.createdAt,
                likeCount = comment.likeCount,
                isLiked = isLiked,
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
            commentCount = board.commentCount,
            comment = commentDto,
            imageUrl = board.imageUrl,
            viewCount = board.viewCount,
        )

        return response

    }
}
