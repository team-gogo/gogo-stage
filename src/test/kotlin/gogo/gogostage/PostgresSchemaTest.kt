package gogo.gogostage

import gogo.gogostage.domain.stage.root.persistence.Stage
import gogo.gogostage.domain.stage.root.persistence.StageRepository
import gogo.gogostage.domain.stage.root.persistence.StageStatus
import gogo.gogostage.domain.stage.root.persistence.StageType
import gogo.gogostage.domain.stage.rule.persistence.StageRule
import gogo.gogostage.domain.stage.rule.persistence.StageRuleRepository
import gogo.gogostage.global.config.QueryDslConfig
import gogo.gogostage.global.internal.student.api.StudentApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.bean.override.mockito.MockitoBean
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig::class)
@EnabledIfEnvironmentVariable(named = "DATABASE_URL", matches = "jdbc:postgresql:.*")
class PostgresSchemaTest @Autowired constructor(
    private val stages: StageRepository,
    private val rules: StageRuleRepository,
    private val jdbc: JdbcTemplate,
) {
    @MockitoBean
    private lateinit var studentApi: StudentApi

    @Test
    fun `flyway schema validates stage relationships and unique rules`() {
        val stage = stages.saveAndFlush(
            Stage(
                schoolId = 901L,
                type = StageType.FAST,
                name = "Schema test stage",
                studentId = 902L,
                passCode = null,
                initialPoint = 100L,
                status = StageStatus.RECRUITING,
                participantCount = 0,
                isActiveMiniGame = false,
                isActiveShop = false,
            )
        )
        rules.saveAndFlush(StageRule(stage = stage, maxBettingPoint = 50L, minBettingPoint = 1L))

        assertTrue(stages.findAllBySchoolId(stage.schoolId).any { it.id == stage.id })
        assertEquals(50L, rules.findByStageId(stage.id).maxBettingPoint)

        assertFailsWith<DataIntegrityViolationException> {
            jdbc.update(
                "INSERT INTO tbl_stage_rule(stage_id, max_betting_point, min_betting_point) VALUES (?, ?, ?)",
                stage.id,
                60L,
                2L,
            )
        }
    }
}
