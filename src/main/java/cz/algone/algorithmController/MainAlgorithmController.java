package cz.algone.algorithmController;

import cz.algone.algorithm.AlgorithmCollection;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.fill.IFill;
import cz.algone.algorithm.fill.pattern.PatternCollection;
import cz.algone.algorithmController.clip.ClipPolygonController;
import cz.algone.algorithmController.controller3D.Controller3D;
import cz.algone.algorithmController.scene.SceneContext;
import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.common.enumAlias.AlgorithmControllerAlias;

public class MainAlgorithmController {
    private final SceneContext sceneContext;

    private final AlgorithmCollection algorithmCollection = new AlgorithmCollection();
    private final AlgorithmControllerCollection algorithmControllerCollection = new AlgorithmControllerCollection();
    private final PatternCollection patternCollection = new PatternCollection();

    public MainAlgorithmController(SceneContext sceneContext) {
        this.sceneContext = sceneContext;

        sceneContext.controllerAliasProperty().addListener((observable, oldValue, newValue) -> {
            updateAlgorithmController(newValue);
        });
        sceneContext.algorithmAliasProperty().addListener((observable, oldValue, newValue) -> {
            setupAlgorithm(newValue);
        });

        sceneContext.colorsProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && sceneContext.getCurrentAlgorithmController() != null)
                sceneContext.getCurrentAlgorithmController().setColors(newVal);
        });

        // === Property listenery druhé vrstvy (nastavení algoritmů) ===
        sceneContext.patternAliasProperty().addListener((obs, old, newVal) -> {
            if (sceneContext.getCurrentAlgorithm() instanceof IFill fill)
                fill.setPattern(newVal != null ? patternCollection.patternMap.get(newVal) : null);
        });
        sceneContext.polygonOrientationProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && sceneContext.getCurrentAlgorithmController() instanceof ClipPolygonController clip)
                clip.setOrientationMode(newVal);
        });
        sceneContext.clip3DEnabledProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && sceneContext.getCurrentAlgorithmController() instanceof Controller3D c3d)
                c3d.setEnabledClip(newVal);
        });
        sceneContext.animationEnabledProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && sceneContext.getCurrentAlgorithmController() instanceof Controller3D c3d)
                c3d.setAnimation(newVal);
        });
        sceneContext.projMatProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && sceneContext.getCurrentAlgorithmController() instanceof Controller3D c3d)
                c3d.setProjMat(newVal);
        });
        sceneContext.cubicAliasProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && sceneContext.getCurrentAlgorithmController() instanceof Controller3D c3d)
                c3d.setCubic(newVal);
        });
    }

    /** Nastaví {@link IAlgorithmController} a {@link IAlgorithm},
     *  předává {@link cz.algone.raster.ImageBuffer} a {@link SceneContext}
     *  + inicializuje listenery */
    private void setupAlgorithmController() {
        IAlgorithmController controller = sceneContext.getCurrentAlgorithmController();
        IAlgorithm algorithm = sceneContext.getCurrentAlgorithm();
        sceneContext.getImageBuffer().clearListeners();
        algorithm.setup(sceneContext.getImageBuffer());
        controller.setup(algorithm, sceneContext);
        controller.setColors(sceneContext.getColors());
        controller.initListeners();
    }

    /** Pomocí {@link AlgorithmAlias} získá konkrétní algoritmus, uloží do SceneContext a inicializuje */
    private void setupAlgorithm(AlgorithmAlias alias) {
        sceneContext.setCurrentAlgorithm(algorithmCollection.algorithmMap.get(alias));
        setupAlgorithmController();
    }

    /** Přijímá {@link AlgorithmControllerAlias} a následně získá daný controller z
     * {@link AlgorithmControllerCollection}, poté získá default algorithm pro daný
     * controller a oboje uloží do {@link SceneContext}.
     * Nastavení algorithmAlias triggeruje {@link #setupAlgorithm} přes listener. */
    private void updateAlgorithmController(AlgorithmControllerAlias alias) {
        IAlgorithmController controller = algorithmControllerCollection.algorithmControllerMap.get(alias);
        sceneContext.setCurrentAlgorithmController(controller);
        // Nastavení algorithmAlias vyvolá listener -> setupAlgorithm -> setupAlgorithmController
        sceneContext.setAlgorithmAlias(controller.getDefaultAlgorithm());
    }
}
