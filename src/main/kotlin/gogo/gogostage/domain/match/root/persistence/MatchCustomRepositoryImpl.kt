package gogo.gogostage.domain.match.root.persistence

import com.querydsl.jpa.JPQLQueryFactory
import gogo.gogostage.domain.game.persistence.QGame.*
import gogo.gogostage.domain.match.result.persistence.QMatchResult.*
import gogo.gogostage.domain.match.root.persistence.QMatch.*
import gogo.gogostage.domain.stage.root.persistence.QStage.*
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class MatchCustomRepositoryImpl(
    private val queryFactory: JPQLQueryFactory,
) : MatchCustomRepository {

    override fun search(stageId: Long, studentId: Long, y: Int, m: Int, d: Int): List<Match> =
        queryFactory
            .selectFrom(match)
            .leftJoin(match.matchResult, matchResult).fetchJoin()
            .join(match.game, game).fetchJoin()
            .join(game.stage, stage).fetchJoin()
            .leftJoin(match.aTeam).fetchJoin()
            .leftJoin(match.bTeam).fetchJoin()
            .where(
                stage.id.eq(stageId)
                    .and(match.startDate.between(
                        LocalDate.of(y, m, d).atStartOfDay(),
                        LocalDate.of(y, m, d).atTime(23, 59, 59)
                    ))
            )
            .fetch()

}
