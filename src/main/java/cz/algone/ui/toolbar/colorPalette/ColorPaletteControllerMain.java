package cz.algone.ui.toolbar.colorPalette;

import cz.algone.ui.MainUIController;
import cz.algone.util.color.ColorPair;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import java.util.List;

public class ColorPaletteControllerMain extends MainUIController {
    @FXML private Button mainColorPicker;
    @FXML private Button secondaryColorPicker;
    List<Button> buttons;

    @FXML void initialize() {
        buttons = List.of(mainColorPicker, secondaryColorPicker);
        mainColorPicker.getStyleClass().add("active-picker");
    }
    /** změní color style a userData {@link javafx.scene.control.ToggleButton},
     * který má třídu active-picker, podle tlačítka barvy na které se klikne */
    @FXML
    private void changeColor(ActionEvent event) {
        Button button = (Button) event.getSource();
        String color = (String) button.getUserData();

        for (Button btn : buttons) {
            if (btn.getStyleClass().contains("active-picker")) {
                btn.setStyle("-fx-background-color: " + color + ";");
                btn.setUserData(color);
            }
        }

        sceneContext.setColors(getSelectedColors());
    }
    /** Přepíná který {@link javafx.scene.control.ToggleButton} je aktivní ->
     * dostane třídu active-picker*/
    @FXML
    private void setActiveBtn(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        for (Button btn : buttons)
            btn.getStyleClass().remove("active-picker");

        if (source instanceof Button btn)
            btn.getStyleClass().add("active-picker");
    }
    /** Vymaže userData a barvu obou hlavních {@link javafx.scene.control.ToggleButton},
     * main dostane zpátky třídu active-picker a defaultní barvu */
    public void clearColorPicker() {
        for (Button btn : buttons) {
            btn.getStyleClass().remove("active-picker");
            if (btn == mainColorPicker) {
                btn.getStyleClass().add("active-picker");
                btn.setStyle("-fx-background-color: #000000;");
                btn.setUserData("#000000");
            } else {
                btn.setStyle("");
                btn.setUserData(null);
            }
        }
        sceneContext.setColors(getSelectedColors());
    }
    /** vezme userData color pickerů a převede je na {@link Color},
     * následně je uloží do {@link ColorPair} */
    public ColorPair getSelectedColors() {
        Color primary = Color.valueOf((String) mainColorPicker.getUserData());
        Color secondary = (secondaryColorPicker.getUserData() == null || secondaryColorPicker.getUserData().toString().isBlank()) ? null : Color.valueOf((String) secondaryColorPicker.getUserData());
        return new ColorPair(primary, secondary);
    }
}
