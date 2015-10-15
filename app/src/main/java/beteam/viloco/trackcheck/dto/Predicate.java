package beteam.viloco.trackcheck.dto;

public class Predicate {
    public String Column;
    public String Value;
    public String Comparison;

    public Predicate(String column, String value, String comparison) {
        Column = column;
        Value = value;
        Comparison = comparison;
    }

    public static class ComparisonPredicate {
        public static String AND = "AND";
        public static String OR = "OR";
    }
}
