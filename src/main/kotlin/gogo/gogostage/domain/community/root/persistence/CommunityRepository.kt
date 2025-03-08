package gogo.gogostage.domain.community.root.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface CommunityRepository: JpaRepository<Community, Long>, CommunityCustomRepository {
    fun findByStageId(stageId: Long): Community?
}