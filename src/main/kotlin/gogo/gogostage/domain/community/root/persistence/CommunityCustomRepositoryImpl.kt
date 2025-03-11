package gogo.gogostage.domain.community.root.persistence

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import gogo.gogostage.domain.community.board.persistence.QBoard.board
import gogo.gogostage.domain.community.root.application.dto.*
import gogo.gogostage.domain.community.root.persistence.QCommunity.community
import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.global.internal.student.api.StudentApi
import org.springframework.data.domain.Pageable

import org.springframework.stereotype.Repository

@Repository
class CommunityCustomRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
    private val studentApi: StudentApi
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

        val totalElement = queryFactory
            .select(board.count())
            .from(board)
            .where(board.community.id.eq(community.id))
            .fetchOne()
            ?: 0L

        val totalPage = if (totalElement.toInt() % size == 0) {
            totalElement.toInt() / size
        } else {
            totalElement.toInt() / size + 1
        }

        val infoDto = InfoDto(totalPage, totalElement.toInt())

        return GetCommunityBoardResDto(infoDto, boardDtoList)
    }
}
