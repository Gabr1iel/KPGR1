package cz.algone.model.models3D;

/** Record pro získání aktuálního {@link SolidAlias}
 * a jestli se má ze scény odebrat nebo přidat */
public record SolidToggleEvent(SolidAlias alias, boolean enabled) {}
