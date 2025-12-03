package cz.algone.ui.sidebar;

import cz.algone.app.ShapeAlias;
import cz.algone.rasterizer.RasterizerAlias;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SidebarController {
    @FXML private Button btnDDA;
    @FXML private Button btnBresenham;
    @FXML private Button btnTrivial;
    @FXML private ToggleButton btnAlgorithms;
    @FXML private HBox algorithmBox;
    @FXML private VBox lineAlgorithms;
    @FXML private Polygon arrowIcon;

    private List<VBox> options;
    private Consumer<RasterizerAlias> onRasterizerChanged;

    @FXML
    private void initialize() {
        btnBresenham.getStyleClass().add("active");
        options = List.of(lineAlgorithms);
        algorithmBox.managedProperty().bind(algorithmBox.visibleProperty());
    }

    @FXML
    private void setRasterizerOnClick(ActionEvent ev) {
        Object source = ev.getSource();
        if (source instanceof Button btn) {
            setActive(btn);
            Object data = btn.getUserData();
            if (data instanceof String string) {
                RasterizerAlias alias = RasterizerAlias.valueOf(string);
                onRasterizerChanged.accept(alias);
            }
        }
    }

    @FXML
    private void toggleAlgorithms() {
        boolean visible = btnAlgorithms.isSelected();
        algorithmBox.setVisible(visible);
        arrowIcon.setRotate(visible ? 180 : 0);
    }

    public void toggleOptionSections(ShapeAlias alias) {
        for (VBox option : options) {
            String type = option.getUserData().toString();
            option.setVisible(type.equals(alias.name()));
        }
    }

    private void setActive(Button active) {
        List<Button> buttons = List.of(btnDDA, btnBresenham, btnTrivial);

        for (Button btn : buttons) {
            btn.getStyleClass().remove("active");
        }

        if (!active.getStyleClass().contains("active")) {
            active.getStyleClass().add("active");
        }
    }

    //metoda pro MainViewController, slouží k poskytnutí Consumeru
    public void setOnRasterizerChange(Consumer<RasterizerAlias> listener) {
        this.onRasterizerChanged = listener;
    }
}
