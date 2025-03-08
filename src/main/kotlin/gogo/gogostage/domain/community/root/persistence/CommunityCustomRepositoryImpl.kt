package gogo.gogostage.domain.community.root.persistence

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import gogo.gogostage.domain.community.board.persistence.QBoard.board
import gogo.gogostage.domain.community.root.application.dto.*
import gogo.gogostage.domain.game.persistence.QGame.game
import gogo.gogostage.domain.stage.root.persistence.QStage.stage
import gogo.gogostage.global.internal.student.api.StudentApi
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

import org.springframework.stereotype.Repository

@Repository
class CommunityCustomRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
    private val studentApi: StudentApi
): CommunityCustomRepository {

    override fun searchCommunityBoardPage(community: Community, getCommunityBoardReqDto: GetCommunityBoardReqDto): Page<GetCommunityBoardResDto> {
        val boards = queryFactory.select(
            Projections.constructor(
                BoardDto::class.java,
                board.id,
                board.studentId,
                game.system,
                board.title,
                board.likeCount,
                board.createdAt,
                board.isFiltered,
                stage.type
            ))
            .from(board)
            .join(stage).on(QCommunity.community.stage.id.eq(community.stage.id))
            .join(game).on(stage.id.eq(game.stage.id))
            .orderBy(
                when(getCommunityBoardReqDto.sort) {
                    SortType.LAST -> board.createdAt.asc()
                    SortType.LATEST -> board.createdAt.desc()
                }
            )

            .offset((getCommunityBoardReqDto.page.toLong() - 1) * getCommunityBoardReqDto.size.toLong())
            .limit(getCommunityBoardReqDto.size.toLong())
            .fetch()

        val studentIds = queryFactory
            .select(board.studentId)
            .from(board)
            .fetch()

        val communityStudents = studentApi.queryByStudentsIds(studentIds)

        val boardDtoList = boards.map { board ->
            val author = communityStudents.find { it.studentId == board.studentId }?.let {
                AuthorDto(it.studentId, it.name, it.classNumber, it.studentNumber)
            }

            board.copy(author = author!!)
        }

        val totalElement = queryFactory
            .select(board.count())
            .from(board)
            .where(board.community.id.eq(community.id))
            .fetchOne()
            ?: 0L

        val totalPage = if (totalElement.toInt() % getCommunityBoardReqDto.size == 0) {
            totalElement.toInt() / getCommunityBoardReqDto.size
        } else {
            totalElement.toInt() / getCommunityBoardReqDto.size + 1
        }

        val infoDto = InfoDto(totalPage, totalElement.toInt())

        val getCommunityBoardResDtoList = listOf(GetCommunityBoardResDto(infoDto, boardDtoList))

        val pageable = PageRequest.of(getCommunityBoardReqDto.page, getCommunityBoardReqDto.size)

        return PageImpl(getCommunityBoardResDtoList, pageable, totalElement)
    }
}
