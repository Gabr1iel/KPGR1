package cz.algone.ui.panel.common;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

/** Obousměrné svázání ovládacích prvků s observable stavem ve {@link cz.algone.model.SceneContext}u.
 *  Každé svázání si drží vlastní pojistku proti zacyklení zápisů. */
public final class ToggleBinding {
    private ToggleBinding() {}

    /** Sváže ToggleGroup s enum property; hodnotu tlačítka určuje jeho userData.
     *  Skupina musí mít vždy vybranou hodnotu — odznačení se vrátí zpět. */
    public static <E extends Enum<E>> void bindGroup(ToggleGroup group, Class<E> type, ObjectProperty<E> property) {
        bindGroup(group, type, property, false);
    }

    /** Jako {@link #bindGroup}, ale odznačení aktivního tlačítka nastaví property na null.
     *  Používá se tam, kde se tím zavírá panel. */
    public static <E extends Enum<E>> void bindCollapsibleGroup(ToggleGroup group, Class<E> type,
                                                                 ObjectProperty<E> property) {
        bindGroup(group, type, property, true);
    }

    private static <E extends Enum<E>> void bindGroup(ToggleGroup group, Class<E> type,
                                                      ObjectProperty<E> property, boolean allowNone) {
        if (group == null) return;
        boolean[] syncing = {false};

        group.selectedToggleProperty().addListener((obs, old, selected) -> {
            if (syncing[0]) return;

            if (selected == null) {
                // Bez povoleného prázdného stavu by UI zůstalo bez označeného tlačítka,
                // i když property dál drží hodnotu — proto výběr vrátíme zpět
                if (allowNone) property.set(null);
                else select(group, type, property.get(), syncing);
                return;
            }
            E value = valueOf(type, selected);
            if (value != null) property.set(value);
        });
        property.addListener((obs, old, value) -> select(group, type, value, syncing));
        select(group, type, property.get(), syncing);
    }

    /** Sváže přepínač s property, která je zapnutá hodnotou {@code onValue} a vypnutá {@code offValue}.
     *  Popisek nechává na volajícím — přepínač s kuličkou žádný nepotřebuje. */
    public static <T> void bindOptional(ToggleButton btn, ObjectProperty<T> property, T onValue, T offValue) {
        if (btn == null) return;
        boolean[] syncing = {false};

        btn.selectedProperty().addListener((obs, old, selected) -> {
            if (syncing[0]) return;
            property.set(selected ? onValue : offValue);
        });
        property.addListener((obs, old, value) -> apply(btn, value == onValue, syncing));
        apply(btn, property.get() == onValue, syncing);
    }

    private static void apply(ToggleButton btn, boolean selected, boolean[] syncing) {
        syncing[0] = true;
        try {
            btn.setSelected(selected);
        } finally {
            syncing[0] = false;
        }
    }

    /** Označí tlačítko odpovídající hodnotě, aniž by spustil zpětný zápis do property. */
    private static <E extends Enum<E>> void select(ToggleGroup group, Class<E> type, E value, boolean[] syncing) {
        syncing[0] = true;
        try {
            for (Toggle toggle : group.getToggles()) {
                if (valueOf(type, toggle) == value) {
                    group.selectToggle(toggle);
                    return;
                }
            }
            group.selectToggle(null);
        } finally {
            syncing[0] = false;
        }
    }

    /** Vrací hodnotu enumu podle userData tlačítka, nebo null. */
    private static <E extends Enum<E>> E valueOf(Class<E> type, Toggle toggle) {
        Object data = (toggle instanceof ToggleButton btn) ? btn.getUserData() : null;
        if (data == null) return null;
        try {
            return Enum.valueOf(type, data.toString());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
