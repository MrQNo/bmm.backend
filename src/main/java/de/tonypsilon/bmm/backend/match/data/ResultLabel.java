package de.tonypsilon.bmm.backend.match.data;

import de.tonypsilon.bmm.backend.game.service.Result;

public enum ResultLabel {
    UNKNOWN("?:?"),
    HOME_WIN("1:0"),
    AWAY_WIN("0:1"),
    DRAW("½:½"),
    HOME_WIN_FORFEIT("+:-"),
    AWAY_WIN_FORFEIT("-:+"),
    BOTH_LOSE_FORFEIT("-:-");

    private final String label;

    ResultLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static Result toHomeResult(ResultLabel resultLabel) {
        return switch(resultLabel) {
            case UNKNOWN -> null;
            case HOME_WIN -> Result.WIN;
            case AWAY_WIN -> Result.LOSS;
            case DRAW -> Result.DRAW;
            case HOME_WIN_FORFEIT -> Result.WIN_FORFEIT;
            case AWAY_WIN_FORFEIT, BOTH_LOSE_FORFEIT -> Result.LOSS_FORFEIT;
        };
    }

    public static Result toAwayResult(ResultLabel resultLabel) {
        return switch(resultLabel) {
            case UNKNOWN -> null;
            case HOME_WIN -> Result.LOSS;
            case AWAY_WIN -> Result.WIN;
            case DRAW -> Result.DRAW;
            case HOME_WIN_FORFEIT, BOTH_LOSE_FORFEIT -> Result.LOSS_FORFEIT;
            case AWAY_WIN_FORFEIT -> Result.WIN_FORFEIT;
        };
    }
}
