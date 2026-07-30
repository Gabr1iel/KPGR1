package cz.algone.common.enumAlias;

/** Jazyk aplikace. Zatím jen přepínač v UI — texty se podle něj ještě nepřekládají.
 *  Kódy odpovídají ISO 639-1, proto CS a ne CZ (to je kód země). */
public enum LanguageAlias {
    CS("Čeština"),
    EN("English");

    private final String nativeName;

    LanguageAlias(String nativeName) {
        this.nativeName = nativeName;
    }

    /** Název jazyka v jazyce samotném. */
    public String getNativeName() {
        return nativeName;
    }
}
