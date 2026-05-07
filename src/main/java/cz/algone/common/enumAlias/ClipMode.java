package cz.algone.common.enumAlias;

public enum ClipMode {
    /** Bez ořezání ve frustum testu. */
    NONE,
    /** Zahodí celé těleso pokud má alespoň jeden vrchol mimo frustum. */
    FAST,
    /** Analyticky ořeže každou hranu i trojúhelník v clip space. */
    ANALYTICAL
}
