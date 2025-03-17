package gogo.gogostage.domain.stage.participant.root.application

import gogo.gogostage.domain.stage.participant.root.application.dto.MyPointDto
import gogo.gogostage.domain.stage.participant.root.application.dto.MyTempPointDto
import gogo.gogostage.domain.stage.participant.root.application.dto.PointDto
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipantRepository
import gogo.gogostage.domain.stage.root.application.StageReader
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ParticipantServiceImpl(
    private val participantReader: ParticipantReader,
    private val participantMapper: ParticipantMapper,
    private val stageReader: StageReader,
    private val userUtil: UserContextUtil,
) : ParticipantService {

    override fun queryPoint(stageId: Long, studentId: Long): PointDto {
        val participant = participantReader.read(stageId, studentId)
        return participantMapper.mapPoint(participant)
    }

    @Transactional(readOnly = true)
    override fun getMyTempPoint(stageId: Long): MyTempPointDto {
        val student = userUtil.getCurrentStudent()
        val stage = stageReader.read(stageId)
        val stageParticipant = participantReader.readStageParticipantByStageIdAndStudentId(stage.id, student.studentId)
        val tempPointList = participantReader.readTempPointList(stageParticipant.id)
        return participantMapper.mapMyTempPointDto(tempPointList)
    }

    @Transactional(readOnly = true)
    override fun getMyPoint(stageId: Long): MyPointDto {
        val student = userUtil.getCurrentStudent()
        val stage = stageReader.read(stageId)
        val stageParticipant = participantReader.readStageParticipantByStageIdAndStudentId(stage.id, student.studentId)
        return participantMapper.mapMyPointDto(stageParticipant.point)
    }

}
