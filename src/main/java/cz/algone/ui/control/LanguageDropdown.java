package cz.algone.ui.control;

import cz.algone.common.enumAlias.LanguageAlias;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Popup;

/** Volba jazyka. V nabídce jsou všechny jazyky s vlastním názvem, vybraný je zaškrtnutý,
 *  a pod oddělovačem je vstup do nastavení jazyka. */
public class LanguageDropdown extends Button {
    private static final String GLOBE_OUTLINE = "M12 3 A9 9 0 1 1 11.99 3";
    private static final String GLOBE_LINES = "M3 12 H21 M12 3 C7.5 7 7.5 17 12 21 C16.5 17 16.5 7 12 3";
    private static final String CHEVRON = "M6 10 L12 16 L18 10";
    private static final String CHECK = "M5 12.5 L9.5 17 L19 7";
    // Ozubené kolo musí mít velký prsten s dírkou a jen krátké zuby.
    // S malým prstenem a dlouhými zuby ikona vypadá jako slunce.
    private static final String GEAR_RING = "M12 6 A6 6 0 1 1 11.99 6";
    private static final String GEAR_HOLE = "M12 9.8 A2.2 2.2 0 1 1 11.99 9.8";
    private static final String GEAR_TEETH =
            "M12 3.5 V6 M12 18 V20.5 M3.5 12 H6 M18 12 H20.5 "
            + "M6.2 6.2 L7.7 7.7 M16.3 16.3 L17.8 17.8 M17.8 6.2 L16.3 7.7 M7.7 16.3 L6.2 17.8";

    /** Otevřená nabídka — CSS ji čte jako .dropdown-btn:showing */
    private static final PseudoClass SHOWING = PseudoClass.getPseudoClass("showing");

    private final ObjectProperty<LanguageAlias> language = new SimpleObjectProperty<>(LanguageAlias.CS);
    private final Label codeLabel = new Label();
    private final Popup popup = new Popup();
    private final VBox menu = new VBox();

    public LanguageDropdown() {
        getStyleClass().setAll("dropdown-btn");
        setFocusTraversable(false);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(buildButtonContent());

        menu.getStyleClass().add("dropdown-menu");
        popup.getContent().add(menu);
        popup.setAutoHide(true);
        popup.showingProperty().addListener((obs, old, showing) -> pseudoClassStateChanged(SHOWING, showing));

        language.addListener((obs, old, value) -> showLanguage(value));
        showLanguage(language.get());

        setOnAction(e -> toggleMenu());
    }

    public ObjectProperty<LanguageAlias> languageProperty() {
        return language;
    }

    private HBox buildButtonContent() {
        codeLabel.getStyleClass().add("dropdown-code");

        HBox content = new HBox(globe(), codeLabel, icon(CHEVRON, 1.6));
        content.getStyleClass().add("dropdown-btn-content");
        return content;
    }

    private void showLanguage(LanguageAlias value) {
        codeLabel.setText(value == null ? "" : value.name());
    }

    private void toggleMenu() {
        if (popup.isShowing()) {
            popup.hide();
            return;
        }
        rebuildMenu();

        Bounds bounds = localToScreen(getBoundsInLocal());
        popup.show(this, bounds.getMinX(), bounds.getMaxY() + 6);
    }

    private void rebuildMenu() {
        menu.getChildren().clear();

        for (LanguageAlias value : LanguageAlias.values())
            menu.getChildren().add(buildLanguageRow(value));

        Region separator = new Region();
        separator.getStyleClass().add("panel-separator");
        separator.setMaxWidth(Double.MAX_VALUE);
        menu.getChildren().add(separator);

        menu.getChildren().add(buildSettingsRow());
    }

    /** Řádek jazyka — zkratka, název a zaškrtnutí u právě vybraného. */
    private Node buildLanguageRow(LanguageAlias value) {
        boolean selected = value == language.get();

        Label code = new Label(value.name());
        code.getStyleClass().add("dropdown-item-code");

        Label name = new Label(value.getNativeName());
        name.getStyleClass().add("dropdown-item-name");

        VBox texts = new VBox(code, name);
        texts.getStyleClass().add("dropdown-item-texts");

        Node check = icon(CHECK, 2);
        check.getStyleClass().add("check");
        check.setVisible(selected);

        HBox row = new HBox(globe(), texts, grow(), check);
        row.getStyleClass().add("dropdown-item");
        if (selected) row.getStyleClass().add("selected");
        row.setMaxWidth(Double.MAX_VALUE);
        // Pozadí řádku je průhledné a takové plochy JavaFX pro myš nesnímá —
        // bez pickOnBounds by reagovaly jen přesné zásahy do textu a ikon
        row.setPickOnBounds(true);
        row.setOnMouseClicked(e -> {
            language.set(value);
            popup.hide();
        });
        return row;
    }

    private Node buildSettingsRow() {
        Label label = new Label("Nastavení jazyka");
        label.getStyleClass().add("dropdown-item-action");

        HBox row = new HBox(gear(), label);
        row.getStyleClass().add("dropdown-item");
        row.setMaxWidth(Double.MAX_VALUE);
        row.setPickOnBounds(true);
        // Obrazovka nastavení zatím není, proto jen naznačený vstup
        row.setDisable(true);
        return row;
    }

    private Region grow() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        return spacer;
    }

    private Node globe() {
        return new Group(icon(GLOBE_OUTLINE, 1.6), icon(GLOBE_LINES, 1.3));
    }

    private Node gear() {
        return new Group(icon(GEAR_RING, 1.5), icon(GEAR_HOLE, 1.4), icon(GEAR_TEETH, 1.4));
    }

    private SVGPath icon(String content, double strokeWidth) {
        SVGPath path = new SVGPath();
        path.setContent(content);
        path.getStyleClass().add("icon");
        path.setStrokeWidth(strokeWidth);
        path.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        path.setStrokeLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);
        return path;
    }
}
