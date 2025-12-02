package cz.algone.ui.colorPalette;

import cz.algone.util.color.ColorPair;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.function.Consumer;

public class ColorPaletteController {
    @FXML private Button mainColorPicker;
    @FXML private Button secondaryColorPicker;
    List<Button> buttons;

    private Consumer<ColorPair> onColorChanged;

    @FXML void initialize() {
        buttons = List.of(mainColorPicker, secondaryColorPicker);
        mainColorPicker.getStyleClass().add("active-picker");
    }

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

        onColorChanged.accept(getSelectedColors());
    }

    @FXML
    private void setActiveBtn(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        for (Button btn : buttons)
            btn.getStyleClass().remove("active-picker");

        if (source instanceof Button btn)
            btn.getStyleClass().add("active-picker");
    }

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
        onColorChanged.accept(getSelectedColors());
    }

    public ColorPair getSelectedColors() {
        Color primary = Color.valueOf((String) mainColorPicker.getUserData());
        Color secondary = (secondaryColorPicker.getUserData() == null || secondaryColorPicker.getUserData().toString().isBlank()) ? null : Color.valueOf((String) secondaryColorPicker.getUserData());
        return new ColorPair(primary, secondary);
    }

    public void setOnColorChanged(Consumer<ColorPair> listener) {
        this.onColorChanged = listener;
    }
}
