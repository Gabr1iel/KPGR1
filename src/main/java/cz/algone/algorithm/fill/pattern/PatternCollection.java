package cz.algone.algorithm.fill.pattern;

import java.util.HashMap;
import java.util.Map;

public class PatternCollection {
    public final Map<PatternAlias, IPattern> patternMap = new HashMap<>();
    public final CheckerPattern checkerPattern;

    public PatternCollection() {
        checkerPattern = new CheckerPattern(8);
        setupPatternCollection();
    }

    private void setupPatternCollection() {
        patternMap.put(PatternAlias.CHECKER, checkerPattern);
    }
}
