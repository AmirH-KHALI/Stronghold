package Stronghold;

public class GameEvent {

    /*
    simple set

    public static final int JOIN_TO_GAME = 1;

     */

    public int type;
    public String message;

    GameEvent(int type, String message) {
        this.type = type;
        this.message = message;
    }


}
