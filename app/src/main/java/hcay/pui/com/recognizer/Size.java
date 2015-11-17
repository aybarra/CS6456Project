package hcay.pui.com.recognizer;

/**
 * Size class containing the width and height information.
 *
 * @author Hyun Seo Chung
 * @author Andy Ybarra
 */
public class Size {
    private int width;
    private int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }
}
