package Util;

public class Node {

    private int x;
    private int y;
    private String id;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = Integer.toString(x+y);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
