package Models;

public class Wall extends Part{

    private int x;
    private int y;
    private int orientation;
    private int length;

    /**
     *
     * @param x
     * @param y
     * @param orientation, 0 = horizonral 1,  = vertical
     * @param length
     */
    public Wall(int x, int y, int orientation, int length) {
        super(Matrix.WALL_CHAR);
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.length = length;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
