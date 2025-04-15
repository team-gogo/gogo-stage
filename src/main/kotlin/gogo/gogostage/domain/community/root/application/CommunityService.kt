package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.root.application.dto.*
import gogo.gogostage.domain.community.root.persistence.SortType
import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.global.internal.student.stub.StudentByIdStub

interface CommunityService {
    fun writeCommunityBoard(stageId: Long, writeCommunityBoardDto: WriteCommunityBoardDto)
    fun getStageBoard(stageId: Long, page: Int, size: Int, category: GameCategory?, sort: SortType, isFiltered: Boolean, student: StudentByIdStub): GetCommunityBoardResDto
    fun getStageBoardInfo(boardId: Long, isFiltered: Boolean, student: StudentByIdStub): GetCommunityBoardInfoResDto
    fun likeStageBoard(boardId: Long): LikeResDto
    fun writeBoardComment(boardId: Long, writeBoardCommentDto: WriteBoardCommentReqDto): WriteBoardCommentResDto
    fun likeBoardComment(commentId: Long): LikeResDto
}