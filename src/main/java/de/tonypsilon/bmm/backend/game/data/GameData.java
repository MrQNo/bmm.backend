package de.tonypsilon.bmm.backend.game.data;

import de.tonypsilon.bmm.backend.game.service.Result;

import java.util.Optional;

public record GameData(Long id,
                       Long matchId,
                       Integer boardNumber,
                       Long homePlayerId,
                       Long awayPlayerId,
                       Optional<Result> playedResultHome,
                       Optional<Result> overruledResultHome,
                       Optional<Result> playedResultAway,
                       Optional<Result> overruledResultAway) {
}
