package cz.algone.model.models3D;

import cz.algone.common.enumAlias.SolidAlias;

/** Record pro získání aktuálního {@link SolidAlias}
 * a jestli se má ze scény odebrat nebo přidat */
public record SolidToggleEvent(SolidAlias alias, boolean enabled) {}
