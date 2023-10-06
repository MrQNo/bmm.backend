package de.tonypsilon.bmm.backend.match.data;

import java.util.List;

public record MatchResultClientData(String date,
                                    String venueLabel,
                                    String refereeLabel,
                                    String state,
                                    List<GameDataForClient> games) {
}
