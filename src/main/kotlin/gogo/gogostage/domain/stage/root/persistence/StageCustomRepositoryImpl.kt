package gogo.gogostage.domain.stage.root.persistence

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import gogo.gogostage.domain.stage.participant.root.persistence.QStageParticipant.stageParticipant
import gogo.gogostage.domain.stage.root.application.dto.PageDto
import gogo.gogostage.domain.stage.root.application.dto.PointRankDto
import gogo.gogostage.domain.stage.root.application.dto.StageParticipantPointOnly
import gogo.gogostage.domain.stage.root.application.dto.StageParticipantPointRankDto
import gogo.gogostage.global.internal.student.api.StudentApi
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class StageCustomRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
    private val studentApi: StudentApi
): StageCustomRepository {

    override fun queryPointRank(stage: Stage, pageable: Pageable, size: Int): StageParticipantPointRankDto {
        val predicate = BooleanBuilder()

        predicate.and(stageParticipant.stage.id.eq(stage.id))

        val studentParticipants = queryFactory
            .select(
                Projections.constructor(
                    StageParticipantPointOnly::class.java,
                    stageParticipant.id,
                    stageParticipant.studentId,
                    stageParticipant.point
                )
            )
            .from(stageParticipant)
            .where(predicate)
            .orderBy(stageParticipant.point.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val studentIds = studentParticipants.map { it.studentId }.toSet().toList()

        val students = studentApi.queryByStudentsIds(studentIds).students

        val pointRankDto = studentParticipants.mapIndexed { index, stageParticipant ->
            val student = students.find { it.studentId == stageParticipant.studentId }!!

            PointRankDto(
                rank = pageable.offset.toInt() + index + 1,
                studentId = stageParticipant.studentId,
                point = stageParticipant.point,
                name = student.name,
                classNumber = student.classNumber,
                studentNumber = student.studentNumber,
            )
        }

        val totalElement = studentParticipants.size

        val totalPage = if (totalElement % size == 0) {
            totalElement / size
        } else {
            totalElement / size + 1
        }

        val pageDto = PageDto(totalPage, totalElement)

        return StageParticipantPointRankDto(pageDto, pointRankDto)

    }

}
