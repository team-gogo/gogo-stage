package gogo.gogostage.domain.team.participant.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface TeamParticipantRepository : JpaRepository<TeamParticipant, Long>
