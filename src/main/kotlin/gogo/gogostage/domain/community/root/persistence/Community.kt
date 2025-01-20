package gogo.gogostage.domain.community.root.persistence

import gogo.gogostage.domain.game.persistence.Game
import jakarta.persistence.*

@Entity
@Table(name = "tbl_community")
class Community(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    val game: Game
)
