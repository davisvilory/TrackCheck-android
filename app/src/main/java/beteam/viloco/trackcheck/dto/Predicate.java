package beteam.viloco.trackcheck.dto;

public class Predicate {
    public String Column;
    public String Value;
    public ComparisonPredicate Comparison;

    public Predicate(String column, String value, ComparisonPredicate comparison) {
        Column = column;
        Value = value;
        Comparison = comparison;
    }

    public enum ComparisonPredicate {
        AND(1), OR(2);

        public int value;

        ComparisonPredicate(int value) {
            this.value = value;
        }
    }
}
