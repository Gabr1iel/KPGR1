package cz.algone.ui.panel.left;

import cz.algone.transforms.Col;
import cz.algone.ui.MainUIController;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.util.List;

/** Panel kategorie Barvy — primární a sekundární barva vybíraná z palety.
 *  Stav drží {@link cz.algone.model.SceneContext}, panel se s ním synchronizuje při zobrazení. */
public class ColorsPanelController extends MainUIController {
    @FXML private Button mainColorPicker;
    @FXML private Button secondaryColorPicker;

    private List<Button> pickers;

    @FXML
    private void initialize() {
        pickers = List.of(mainColorPicker, secondaryColorPicker);
        mainColorPicker.getStyleClass().add("active-picker");
    }

    @Override
    protected void onSceneContextReady() {
        sceneContext.colorsProperty().addListener((obs, old, colors) -> showColors(colors));
        showColors(sceneContext.getColors());
    }

    /** Nastaví barvu aktivního color pickeru podle stisknutého tlačítka palety. */
    @FXML
    private void changeColor(ActionEvent event) {
        Button button = (Button) event.getSource();
        String color = (String) button.getUserData();

        for (Button picker : pickers) {
            if (picker.getStyleClass().contains("active-picker")) {
                setPickerColor(picker, color);
            }
        }
        sceneContext.setColors(getSelectedColors());
    }

    /** Označí stisknuté tlačítko jako aktivní color picker. */
    @FXML
    private void setActiveBtn(ActionEvent event) {
        for (Button picker : pickers)
            picker.getStyleClass().remove("active-picker");

        if (event.getSource() instanceof Button btn)
            btn.getStyleClass().add("active-picker");
    }

    /** Zruší sekundární barvu. */
    @FXML
    private void clearSecondary() {
        setPickerColor(secondaryColorPicker, null);
        sceneContext.setColors(getSelectedColors());
    }

    /** Promítne barvy ze {@link cz.algone.model.SceneContext}u do color pickerů. */
    private void showColors(ColorPair colors) {
        if (colors == null) return;
        setPickerColor(mainColorPicker, toHex(colors.primary()));
        setPickerColor(secondaryColorPicker, toHex(colors.secondary()));
    }

    private void setPickerColor(Button picker, String color) {
        if (color == null) {
            picker.setStyle("");
            picker.setUserData(null);
        } else {
            picker.setStyle("-fx-background-color: " + color + ";");
            picker.setUserData(color);
        }
    }

    /** Vrací aktuálně vybranou primární a sekundární barvu jako {@link ColorPair}. */
    private ColorPair getSelectedColors() {
        Col primary = toCol(mainColorPicker.getUserData());
        if (primary == null) primary = ColorUtils.DEFAULT_COLORPICKER_COLOR.primary();
        return new ColorPair(primary, toCol(secondaryColorPicker.getUserData()));
    }

    private Col toCol(Object userData) {
        if (userData == null || userData.toString().isBlank()) return null;
        return ColorUtils.toCol(Color.valueOf(userData.toString()));
    }

    private String toHex(Col color) {
        if (color == null) return null;
        return String.format("#%06X", color.getARGB() & 0xFFFFFF);
    }
}
