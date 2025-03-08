package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.board.application.BoardProcessor
import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardReqDto
import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardResDto
import gogo.gogostage.domain.community.root.application.dto.WriteCommunityBoardDto
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommunityServiceImpl(
    private val communityReader: CommunityReader,
    private val boardProcessor: BoardProcessor,
    private val userUtil: UserContextUtil
): CommunityService {

    @Transactional
    override fun writeCommunityBoard(stageId: Long, writeCommunityBoardDto: WriteCommunityBoardDto) {
        val student = userUtil.getCurrentStudent()
        val community = communityReader.readByStageId(stageId)
        boardProcessor.saveCommunityBoard(community, student.studentId, writeCommunityBoardDto)

        // 추후 욕설 필터링 요청 처리 필요
    }

    @Transactional(readOnly = true)
    override fun getStageBoard(stageId: Long, getCommunityBoardDto: GetCommunityBoardReqDto): Page<GetCommunityBoardResDto> {
        val community = communityReader.readByStageId(stageId)
        return communityReader.readBoards(community, getCommunityBoardDto)
    }


}