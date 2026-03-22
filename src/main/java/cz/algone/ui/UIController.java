package cz.algone.ui;

import cz.algone.algorithmController.scene.SceneContext;

/** Base třída pro UI controllery, poskytuje přístup ke SceneContext */
public abstract class UIController {
    protected SceneContext sceneContext;

    public void initSceneContext(SceneContext sceneContext) {
        this.sceneContext = sceneContext;
        onSceneContextReady();
    }

    /** Volá se po nastavení SceneContext - child controllery zde registrují listenery */
    protected void onSceneContextReady() {}
}
