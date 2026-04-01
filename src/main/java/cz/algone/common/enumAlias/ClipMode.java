package cz.algone.common.enumAlias;

public enum ClipMode {
    /** Žádné ořezání (rasterizér omezí pixel bounds). */
    NONE,
    /** Rychlé ořezání – celé těleso zahodí pokud má alespoň jeden vrchol mimo frustum. */
    FAST,
    /** Analytické ořezání – ořezání každé hrany (Liang-Barsky) / trojúhelníku (Sutherland-Hodgman) v clip space. */
    ANALYTICAL
}
