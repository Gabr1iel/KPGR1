package cz.algone.common.enumAlias;

import java.util.ArrayList;
import java.util.List;

/** Kategorie v levém railu — rozcestník, který přepíná obsah levého panelu.
 *  Každá kategorie patří do jednoho {@link SceneAlias} a volí výchozí {@link AlgorithmControllerAlias}. */
public enum CategoryAlias implements IAlias {
    /* === Obecné === */
    COLORS(SceneAlias.GENERAL, "Barvy", null, true),
    THICKNESS(SceneAlias.GENERAL, "Tloušťka", null, false),
    ANTIALIASING(SceneAlias.GENERAL, "Antialiasing", null, false),

    /* === 2D === */
    RASTERIZATION(SceneAlias.SCENE_2D, "Rasterizace", AlgorithmControllerAlias.LINE, true),
    FILL(SceneAlias.SCENE_2D, "Vyplnit", AlgorithmControllerAlias.SEED_FILL, true),
    CLIP_2D(SceneAlias.SCENE_2D, "Ořezání", AlgorithmControllerAlias.CLIP, true),
    ERASER(SceneAlias.SCENE_2D, "Guma", null, false),
    TEXT(SceneAlias.SCENE_2D, "Text", null, false),

    /* === 3D === */
    SOLIDS(SceneAlias.SCENE_3D, "Tělesa", AlgorithmControllerAlias.CONTROLLER_3D, true),
    CUBICS(SceneAlias.SCENE_3D, "Kubiky", AlgorithmControllerAlias.CONTROLLER_3D, true),
    LIGHT(SceneAlias.SCENE_3D, "Světlo", AlgorithmControllerAlias.CONTROLLER_3D, true),
    PROJECTION(SceneAlias.SCENE_3D, "Projekce", AlgorithmControllerAlias.CONTROLLER_3D, true),
    CLIP_3D(SceneAlias.SCENE_3D, "Ořezání", AlgorithmControllerAlias.CONTROLLER_3D, true);

    private final SceneAlias scene;
    private final String label;
    private final AlgorithmControllerAlias defaultController;
    private final boolean implemented;

    CategoryAlias(SceneAlias scene, String label, AlgorithmControllerAlias defaultController, boolean implemented) {
        this.scene = scene;
        this.label = label;
        this.defaultController = defaultController;
        this.implemented = implemented;
    }

    public SceneAlias getScene() { return scene; }
    public String getLabel() { return label; }
    public AlgorithmControllerAlias getDefaultController() { return defaultController; }
    /** False u kategorií, které jsou zatím jen placeholder (tlačítko je disabled). */
    public boolean isImplemented() { return implemented; }

    /** Vrací kategorie daného prostoru v pořadí deklarace. */
    public static List<CategoryAlias> forScene(SceneAlias scene) {
        List<CategoryAlias> result = new ArrayList<>();
        for (CategoryAlias category : values()) {
            if (category.scene == scene) result.add(category);
        }
        return result;
    }

    /** Vrací první implementovanou kategorii daného prostoru, nebo null. */
    public static CategoryAlias firstImplemented(SceneAlias scene) {
        for (CategoryAlias category : values()) {
            if (category.scene == scene && category.implemented) return category;
        }
        return null;
    }

    @Override
    public IAlias getAlias(String alias) {
        return CategoryAlias.valueOf(alias.toUpperCase());
    }
}
