public enum Gesture {

    NAVIGABLE("-->", true), BI_NAVIGABLE("<->", true), CLASSIFIER("[]", true), GENERALIZATION("--|>", true),
    UNSPECIFIED("---", true), NON_NAVIGABLE("x", true), REALIZATION_CIRCLE("o", true), REALIZATION("o--", true), AGGREGATION("<>-", true),
    COMPOSITION("</>-", true), DEPENDENCY("- ->", true), REALIZATION_DEPENDENCY("- -|>", true), REQUIRED("--C", true);

    public String name;
    public boolean isShape;

    Gesture(String name, boolean isShape) {
        this.name = name;
        this.isShape = isShape;
    }

    @Override
    public String toString() {
    	return name;
    }

}
