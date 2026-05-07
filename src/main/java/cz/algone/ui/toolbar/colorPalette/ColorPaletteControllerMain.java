package cz.algone.ui.toolbar.colorPalette;

import cz.algone.transforms.Col;
import cz.algone.ui.MainUIController;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;
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
    /** Nastaví barvu aktivního color pickeru podle stisknutého tlačítka palety. */
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
    /** Označí stisknuté tlačítko jako aktivní color picker. */
    @FXML
    private void setActiveBtn(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        for (Button btn : buttons)
            btn.getStyleClass().remove("active-picker");

        if (source instanceof Button btn)
            btn.getStyleClass().add("active-picker");
    }
    /** Vrátí color pickery do výchozího stavu. */
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
    /** Vrací aktuálně vybranou primární a sekundární barvu jako {@link ColorPair}. */
    public ColorPair getSelectedColors() {
        Col primary = ColorUtils.toCol(Color.valueOf((String) mainColorPicker.getUserData()));
        Col secondary = (secondaryColorPicker.getUserData() == null || secondaryColorPicker.getUserData().toString().isBlank()) ? null : ColorUtils.toCol(Color.valueOf((String) secondaryColorPicker.getUserData()));
        return new ColorPair(primary, secondary);
    }
}
