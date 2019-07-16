package Stronghold;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Baran on 6/1/2017.
 */

public class GameEvent {
    public static final int JOIN_TO_GAME = 1;
    public static final int USER_JOINED_TO_NETWORK = 2;
    public static final int DUPLICATE_USERNAME = 3;
    public static final int USER_SUCCESSFULLY_CREATED = 4;


    public int type;
    public String message;

    GameEvent(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public String readStringFromJSON (String pathToJSON) throws Exception {
        FileReader reader = new FileReader(pathToJSON);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
        return jsonObject.toJSONString();
    }

    public void stringWriteInJSON (String command, Path pathToJSON) {
        try {
            Files.write(pathToJSON, command.getBytes());
        } catch (Exception e) {

        }
    }

    public static GameEvent parseFromString(String string) {
        JSONParser jsonParser = new JSONParser();
        GameEvent gameEvent = null;
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(string);
            int type = ((Long) jsonObject.get("type")).intValue();
            String message = (String) jsonObject.get("message");
            gameEvent = new GameEvent(type, message);
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return gameEvent;
    }

    public void show() {
        System.out.println("type: " + type + " , message: " + message);
    }

    public String getJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", this.type);
        jsonObject.put("message", this.message);
        return jsonObject.toJSONString();
    }
}
