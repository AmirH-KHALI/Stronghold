package Stronghold.GameObjects.Human;

import Stronghold.Game;

public class Swordsman extends Human {

    int decreaseHealthRate;
    int maxDistanceForAttack;

    public Swordsman(String side, int[] location) {

        super("swordsman", location, side);
        decreaseHealthRate = Integer.parseInt(humanInfo.get("health_rate").toString());
        maxDistanceForAttack = Integer.parseInt(humanInfo.get("human_initial_health").toString());
        buildHuman(side);

    }

    @Override
    public void buildHuman(String humanName) {

        Game.createRect3D(xform,30,0,60,location[0],15,location[1],null,"HUMAN-SWORDSMAN-" + side,true);


    }


}
