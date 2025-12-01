package cz.algone.controller;

import cz.algone.rasterizer.RasterizerAlias;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.List;
import java.util.function.Consumer;

public class SidebarController {
    @FXML private Button btnPolygon;
    @FXML private Button btnDDA;
    @FXML private Button btnBresenham;
    @FXML private Button btnTrivial;

    private Consumer<RasterizerAlias> onRasterizerChanged;

    @FXML
    private void initialize() {
        btnPolygon.getStyleClass().add("active");
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

    private void setActive(Button active) {
        List<Button> buttons = List.of(btnPolygon, btnDDA, btnBresenham, btnTrivial);

        for (Button btn : buttons) {
            btn.getStyleClass().remove("active");
        }

        if (!active.getStyleClass().contains("active")) {
            active.getStyleClass().add("active");
        }
    }

    //Veřejná metoda pro MainViewController, slouží k poskytnutí Consumeru
    public void setOnRasterizerChange(Consumer<RasterizerAlias> listener) {
        this.onRasterizerChanged = listener;
    }
}
