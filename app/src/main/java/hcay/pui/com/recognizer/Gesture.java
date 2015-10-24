package hcay.pui.com.recognizer;

public enum Gesture {

    T("T", false), N("N", false), ARROW("-->", true), CLASSIFIER("CLASSIFIER", true), GENERALIZATION("--|>", true);

    public String name;
    public boolean isShape;

    Gesture(String name, boolean isShape) {
        this.name = name;
        this.isShape = isShape;
    }

}
