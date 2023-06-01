package de.tonypsilon.bmm.backend.security;

import de.tonypsilon.bmm.backend.matchadministration.service.MatchAdministrationService;
import de.tonypsilon.bmm.backend.security.rnr.Roles;
import de.tonypsilon.bmm.backend.security.rnr.service.ClubAdminService;
import de.tonypsilon.bmm.backend.security.rnr.service.SeasonAdminService;
import de.tonypsilon.bmm.backend.security.rnr.service.TeamAdminService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LoginController {

    private final SeasonAdminService seasonAdminService;
    private final ClubAdminService clubAdminService;
    private final TeamAdminService teamAdminService;
    private final MatchAdministrationService matchAdministrationService;

    public LoginController(final SeasonAdminService seasonAdminService,
                           final ClubAdminService clubAdminService,
                           final TeamAdminService teamAdminService,
                           final MatchAdministrationService matchAdministrationService) {
        this.seasonAdminService = seasonAdminService;
        this.clubAdminService = clubAdminService;
        this.teamAdminService = teamAdminService;
        this.matchAdministrationService = matchAdministrationService;
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> user(Principal user) {
        return ResponseEntity.ok(new AuthenticationResponse(user.getName(),
                SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).toList()
                        .contains(Roles.ADMIN),
                clubAdminService.getClubsOfClubAdmin(user.getName()),
                teamAdminService.getTeamsOfTeamAdmin(user.getName()),
                seasonAdminService.getSeasonsOfSeasonAdmin(user.getName()),
                matchAdministrationService.getMatchAdministrationDataForUser(user.getName())));
    }

    @GetMapping(value = "/administration/logout")
    public void logout() {
        // Actual logout handled by Spring framework.
    }
}
