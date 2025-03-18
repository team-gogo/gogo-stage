package gogo.gogostage.domain.match.root.persistence


interface MatchCustomRepository {
    fun search(stageId: Long, studentId: Long, y: Int, m: Int, d: Int): List<Match>
}
