package hcay.pui.com.recognizer;

public enum Gesture {

    NAVIGABLE("navigable", "-->", true), BI_NAVIGABLE("bi_navigable", "<->", true), CLASSIFIER("classifier", "[]", true), GENERALIZATION("generalization", "--|>", true),
    UNSPECIFIED("unspecified", "---", true), NON_NAVIGABLE("non_navigable", "x", true), REALIZATION_CIRCLE("realization_circle", "o", true), REALIZATION("realization", "--o", true), AGGREGATION("aggregation", "-<>", true),
    COMPOSITION("composition", "-</>", true), DEPENDENCY("dependency", "- ->", true), REALIZATION_DEPENDENCY("realization_dependency", "- -|>", true), REQUIRED("required", "--C", true);

    public String imageName, name;
    public boolean isShape;

    Gesture(String imageName, String name, boolean isShape) {
        this.imageName = imageName;
        this.name = name;
        this.isShape = isShape;
    }

    @Override
    public String toString() {
    	return name;
    }

}
