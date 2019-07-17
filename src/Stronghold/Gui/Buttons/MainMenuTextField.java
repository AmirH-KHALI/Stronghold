package Stronghold.Gui.Buttons;

import Stronghold.GameGui;
import Stronghold.ResourceManager;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class MainMenuTextField extends TextField {

    public MainMenuTextField(String example) {

        super("");

        setMaxHeight(53);
        setMinHeight(53);
        setMaxWidth(361);
        setMinWidth(361);
        setPromptText(example);

    }
}
