package de.tonypsilon.bmm.backend.game.data;

import de.tonypsilon.bmm.backend.game.service.Result;
import jakarta.persistence.*;

import java.util.Optional;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_id", unique = false, nullable = false)
    private Long matchId;

    @Column(name = "board_number", unique = false, nullable = false)
    private Integer boardNumber;

    @Column(name = "home_player_id", unique = false, nullable = false)
    private Long homePlayerId;

    @Column(name = "away_player_id", unique = false, nullable = false)
    private Long awayPlayerId;

    @Column(name = "played_result_home", unique = false, nullable = true)
    private String playedResultHome;

    @Column(name = "overruled_result_home", unique = false, nullable = true)
    private String overruledResultHome;

    @Column(name = "played_result_away", unique = false, nullable = true)
    private String playedResultAway;

    @Column(name = "overruled_result_away", unique = false, nullable = true)
    private String overruledResultAway;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Integer getBoardNumber() {
        return boardNumber;
    }

    public void setBoardNumber(Integer boardNumber) {
        this.boardNumber = boardNumber;
    }

    public Long getHomePlayerId() {
        return homePlayerId;
    }

    public void setHomePlayerId(Long homePlayerId) {
        this.homePlayerId = homePlayerId;
    }

    public Long getAwayPlayerId() {
        return awayPlayerId;
    }

    public void setAwayPlayerId(Long awayPlayerId) {
        this.awayPlayerId = awayPlayerId;
    }

    public Optional<Result> getPlayedResultHome() {
        if (playedResultHome == null) {
            return Optional.empty();
        }
        return Optional.of(Result.valueOf(playedResultHome));
    }

    public void setPlayedResultHome(Optional<Result> playedResultHome) {
        if (playedResultHome.isEmpty()) {
            this.playedResultHome = null;
        }
        this.playedResultHome = playedResultHome.get().toString();
    }

    public Optional<Result> getOverruledResultHome() {
        if (overruledResultHome == null) {
            return Optional.empty();
        }
        return Optional.of(Result.valueOf(overruledResultHome));
    }

    public void setOverruledResultHome(Optional<Result> judgedResultHome) {
        if(judgedResultHome.isEmpty()) {
            this.overruledResultHome = null;
        }
        this.overruledResultHome = judgedResultHome.get().toString();
    }

    public Optional<Result> getPlayedResultAway() {
        if (playedResultAway == null) {
            return Optional.empty();
        }
        return Optional.of(Result.valueOf(playedResultAway));
    }

    public void setPlayedResultAway(Optional<Result> playedResultAway) {
        if (playedResultAway.isEmpty()) {
            this.playedResultAway = null;
        }
        this.playedResultAway = playedResultAway.get().toString();
    }

    public Optional<Result> getOverruledResultAway() {
        if (overruledResultAway == null) {
            return Optional.empty();
        }
        return Optional.of(Result.valueOf(overruledResultAway));
    }

    public void setOverruledResultAway(Optional<Result> judgedResultAway) {
        if(judgedResultAway.isEmpty()) {
            this.overruledResultAway = null;
        }
        this.overruledResultAway = judgedResultAway.get().toString();
    }
}
