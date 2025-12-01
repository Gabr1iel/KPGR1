package cz.algone.controller;

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
        String style = button.getStyle();

        for (Button btn : buttons) {
            if (btn.getStyleClass().contains("active-picker"))
                btn.setStyle(style);
        }
        Color primary = Color.valueOf(mainColorPicker.getStyle().replaceAll(".*-fx-background-color:\\s*", "").replace(";",""));
        ColorPair colors = new ColorPair(primary,(secondaryColorPicker.getStyle().isEmpty()) ? null : Color.valueOf(secondaryColorPicker.getStyle().replaceAll(".*-fx-background-color:\\s*", "").replace(";","")));
        onColorChanged.accept(colors);
    }

    @FXML
    private void setActive(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        for (Button btn : buttons)
            btn.getStyleClass().remove("active-picker");

        if (source instanceof Button btn)
            btn.getStyleClass().add("active-picker");
    }

    public void setOnColorChanged(Consumer<ColorPair> listener) {
        this.onColorChanged = listener;
    }
}
