package de.tonypsilon.bmm.backend.security.rnr.data;

import javax.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@IdClass(ClubAdminKey.class)
public class ClubAdmin {

    @Id
    private String username;

    @Id
    @Column(name = "club_id")
    private Long clubId;

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public Long getClubId() {
        return clubId;
    }

    public void setClubId(@NonNull Long clubId) {
        this.clubId = clubId;
    }
}
