public enum Gesture {

    T("T", false), N("N", false), NAVIGABLE("-->", true), BI_NAVIGABLE("<->", true), CLASSIFIER("[]", true), GENERALIZATION("--|>", true),
    UNSPECIFIED("---", true), NON_NAVIGABLE("x", true), REALIZATION("o", true), AGGREGATION("<>-", true), COMPOSITION("</>-", true),
    DEPENDENCY("- ->", true), REALIZATION_DEPENDENCY("- -|>", true);

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
