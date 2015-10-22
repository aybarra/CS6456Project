package hcay.pui.com.recognizer;

public enum Gesture {

    T("T", false), N("N", false), D("D", false);

    public String name;
    public boolean isShape;

    Gesture(String name, boolean isShape) {
        this.name = name;
        this.isShape = isShape;
    }

}
