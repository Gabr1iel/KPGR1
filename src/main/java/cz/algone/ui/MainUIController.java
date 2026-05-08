package cz.algone.ui;

import cz.algone.model.SceneContext;

/** Předek UI controllerů s odkazem na sdílený {@link SceneContext}. */
public abstract class MainUIController {
    protected SceneContext sceneContext;

    public void initSceneContext(SceneContext sceneContext) {
        this.sceneContext = sceneContext;
        onSceneContextReady();
    }

    /** Hook volaný po nastavení {@link SceneContext}u, určený pro registraci listenerů. */
    protected void onSceneContextReady() {}
}
