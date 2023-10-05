package de.tonypsilon.bmm.backend.match.service;

import com.google.common.collect.Comparators;
import de.tonypsilon.bmm.backend.exception.BadDataException;
import de.tonypsilon.bmm.backend.game.data.GameCreationData;
import de.tonypsilon.bmm.backend.game.data.GameData;
import de.tonypsilon.bmm.backend.game.service.GameService;
import de.tonypsilon.bmm.backend.match.data.*;
import de.tonypsilon.bmm.backend.matchday.service.MatchdayService;
import de.tonypsilon.bmm.backend.participant.service.ParticipantService;
import de.tonypsilon.bmm.backend.referee.data.Referee;
import de.tonypsilon.bmm.backend.referee.data.RefereeData;
import de.tonypsilon.bmm.backend.referee.service.RefereeService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchResultService {

    private final MatchService matchService;
    private final MatchdayService matchdayService;
    private final RichMatchInformationAssemblyService richMatchInformationAssemblyService;
    private final ParticipantService participantService;
    private final GameService gameService;
    private final RefereeService refereeService;

    public MatchResultService(final MatchService matchService,
                              final MatchdayService matchdayService,
                              final RichMatchInformationAssemblyService richMatchInformationAssemblyService,
                              final ParticipantService participantService,
                              final GameService gameService,
                              final RefereeService refereeService) {
        this.matchService = matchService;
        this.matchdayService = matchdayService;
        this.richMatchInformationAssemblyService = richMatchInformationAssemblyService;
        this.participantService = participantService;
        this.gameService = gameService;
        this.refereeService = refereeService;
    }

    @Transactional
    @NonNull
    public RichMatchData putPlayedResultsForMatch(@NonNull PutResultsData putResultsData,
                                                  @NonNull Long matchId) {
        if(!matchService.getAllOpenMatchesOfRunningSeasons().stream()
                .map(MatchData::id).collect(Collectors.toSet())
                .contains(matchId)) {
            throw new BadDataException("Dieser Wettkampf kann derzeit nicht bearbeitet werden!");
        }
        MatchData matchData = matchService.getMatchDataById(matchId);
        Integer numberOfBoardsForMatchday = matchdayService.getNumberOfBoardsForMatchday(matchData.matchdayId());
        if(!numberOfBoardsForMatchday
                .equals(putResultsData.games().size())) {
            throw new BadDataException(
                    "Die Anzahl der gegebenen Teilnehmenden stimmt nicht mit der geforderten Brettanzahl überein!");
        }
        RefereeData refereeData = refereeService.getById(putResultsData.refereeId());
        if(!matchdayService.getSeasonIdForMatchday(matchData.matchdayId()).equals(refereeData.seasonId())) {
            throw new BadDataException("Der Schiedsrichter gehört nicht zur passenden Saison!");
        }
        var homeParticipantIds = putResultsData.games().stream()
                .map(GameDataForClient::homeParticipant)
                .map(ParticipantDataForClient::id)
                .toList();
        var awayParticipantIds = putResultsData.games().stream()
                .map(GameDataForClient::awayParticipant)
                .map(ParticipantDataForClient::id)
                .toList();
        verifyParticipantOrder(homeParticipantIds);
        verifyParticipantOrder(awayParticipantIds);

        gameService.deleteAllGamesFromMatch(matchId);
        for (int i = 0; i < numberOfBoardsForMatchday; i++) {
            gameService.createGame(new GameCreationData(
                    matchId,
                    i+1,
                    homeParticipantIds.get(i),
                    awayParticipantIds.get(i),
                    ResultLabel.toHomeResult(ResultLabel.ofLabel(putResultsData.games().get(i).result().label())),
                    null,
                    ResultLabel.toAwayResult(ResultLabel.ofLabel(putResultsData.games().get(i).result().label())),
                    null
            ));
        }
        matchService.assignReferee(matchId, refereeData);
        if(Boolean.TRUE.equals(putResultsData.closeMatch())) {
            if (putResultsData.games().stream()
                    .map(GameDataForClient::result)
                    .map(ResultDataForClient::label)
                    .anyMatch("?:?"::equals)) {
                throw new BadDataException(
                        "Es müssen alle Ergebnisse gegeben sein, damit der Wettkampf geschlossen werden kann!");
            }
            matchService.changeMatchState(matchId, MatchState.CLOSED);
        }
        return richMatchInformationAssemblyService.assembleRichMatchData(matchId);
    }

    private void verifyParticipantOrder(List<Long> participantIds) {
        if(!Comparators.isInOrder(
                participantIds.stream().map(participantService::getCodeOfParticipant).toList(),
                Comparator.naturalOrder())) {
            throw new BadDataException("Die Teilnehmer sind nicht in der richtigen Reihenfolge!");
        }
    }
}