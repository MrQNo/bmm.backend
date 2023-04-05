package de.tonypsilon.bmm.backend.match.service;

import de.tonypsilon.bmm.backend.match.data.MatchData;
import de.tonypsilon.bmm.backend.match.data.MatchState;
import de.tonypsilon.bmm.backend.matchday.service.MatchdayService;
import de.tonypsilon.bmm.backend.security.rnr.service.AuthorizationService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MatchStateService {

    private final MatchService matchService;
    private final MatchdayService matchdayService;
    private final AuthorizationService authorizationService;

    public MatchStateService(final MatchService matchService,
                             final MatchdayService matchdayService,
                             final AuthorizationService authorizationService) {
        this.matchService = matchService;
        this.matchdayService = matchdayService;
        this.authorizationService = authorizationService;
    }

    public MatchData changeMatchState(MatchData matchData, MatchState state, String username) {
        if(matchData.matchState() == MatchState.OPEN) {
            verifyUserIsClubAdminOrTeamAdmin(matchData, username);
        } else {
            verifyUserIsSeasonAdmin(matchData, username);
        }
        return matchService.changeMatchState(matchData.id(), state);
    }

    private void verifyUserIsClubAdminOrTeamAdmin(MatchData matchData, String username) {
        authorizationService.verifyUserIsClubAdminOrTeamAdminOfAnyTeam(username,
                Set.of(matchData.homeTeamId(), matchData.awayTeamId()));
    }

    private void verifyUserIsSeasonAdmin(MatchData matchData, String username) {
        authorizationService.verifyUserIsSeasonAdminOfSeason(username,
                matchdayService.getSeasonIdOfMatchday(matchData.matchdayId()));
    }

}
