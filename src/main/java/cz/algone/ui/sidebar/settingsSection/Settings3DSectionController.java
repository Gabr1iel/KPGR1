package cz.algone.ui.sidebar.settingsSection;

import cz.algone.common.enumAlias.CubicAlias;
import cz.algone.common.enumAlias.EnabledAlias;
import cz.algone.common.enumAlias.ProjMatAlias;
import cz.algone.ui.sidebar.ISidebarSectionController;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.util.function.Consumer;

public class Settings3DSectionController implements ISidebarSectionController {
    @FXML private ToggleButton btnToggleClip;
    @FXML private ToggleButton btnToggleAnimation;
    @FXML private ToggleGroup projectionToggle;

    private Consumer<EnabledAlias> onClip3DChanged;
    private Consumer<EnabledAlias> onAnimationChanged;
    private Consumer<ProjMatAlias> onProjMatChanged;

    @FXML
    public void initialize() {
        projectionToggle.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof ToggleButton btn) {
                Object data = btn.getUserData();
                if (data != null) {
                    try {
                        ProjMatAlias alias = ProjMatAlias.valueOf(data.toString());
                        onProjMatChanged.accept(alias);
                    } catch (IllegalArgumentException e) {

                    }
                }
            }
        });
    }

    @FXML
    public void toggleClip3D() {
        boolean selected = btnToggleClip.isSelected();
        if (selected)
            btnToggleClip.setText("ON");
        else
            btnToggleClip.setText("OFF");
        onClip3DChanged.accept(selected ? EnabledAlias.ENABLED : EnabledAlias.DISABLED);
    }

    @FXML
    public void toggleAnimation() {
        boolean selected = btnToggleAnimation.isSelected();
        if (selected)
            btnToggleAnimation.setText("ON");
        else
            btnToggleAnimation.setText("OFF");
        onAnimationChanged.accept(selected ? EnabledAlias.ENABLED : EnabledAlias.DISABLED);
    }

    public void setOnClip3DChanged(Consumer<EnabledAlias> listener) {this.onClip3DChanged = listener;}
    public void setOnAnimationChanged(Consumer<EnabledAlias> listener) {this.onAnimationChanged = listener;}
    public void setOnProjMatChanged(Consumer<ProjMatAlias> listener) {this.onProjMatChanged = listener;}

    @Override
    public ToggleGroup getToggleGroup() {return null;}
}
