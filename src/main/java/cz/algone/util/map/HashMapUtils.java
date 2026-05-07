package cz.algone.util.map;

import java.util.Map;
import java.util.Objects;

public class HashMapUtils {
    /** Vrací první klíč v mapě, jehož hodnota se rovná zadané, nebo null. */
    public static  <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
