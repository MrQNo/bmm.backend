package de.tonypsilon.bmm.backend.security.rnr.facade;

import de.tonypsilon.bmm.backend.exception.BadDataException;
import de.tonypsilon.bmm.backend.security.rnr.Roles;
import de.tonypsilon.bmm.backend.security.rnr.data.ChangePasswordData;
import de.tonypsilon.bmm.backend.security.rnr.data.UserData;
import de.tonypsilon.bmm.backend.security.rnr.service.UserService;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.Objects;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @RolesAllowed(Roles.ADMIN)
    @PostMapping(value = "/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserData> createUser(RequestEntity<UserData> userDataRequestEntity) {
        UserData createUserData = Objects.requireNonNull(userDataRequestEntity).getBody();
        Objects.requireNonNull(createUserData);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(createUserData));
    }

    /*
     * The only supported patch is a change of the password.
     */
    @PatchMapping(value = "/users/{username}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserData> changeUserPassword(
            RequestEntity<ChangePasswordData> changePasswordDataRequestEntity,
            @NonNull @PathVariable String username,
            Principal principal) {
        ChangePasswordData changePasswordData = changePasswordDataRequestEntity.getBody();
        if (changePasswordData == null || changePasswordData.username() == null) {
            throw new BadDataException("Unvollständige Daten gegeben!");
        }
        if(!(principal.getName().equals(username)
                && principal.getName().equals(changePasswordData.username()))) {
            throw new SecurityException("Passwortänderung nicht erlaubt!");
        }
        return ResponseEntity
                .ok(userService.changePassword(changePasswordData));
    }
}
