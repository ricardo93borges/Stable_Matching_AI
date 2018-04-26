package Util;

public class Node implements Comparable<Node>{

    private int x;
    private int y;
    private int value;
    private String id;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = "n"+x+""+y;
        this.value = 1;
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

    public int compareTo(Node node){
        if (this.value < node.value) {
            return -1;
        }
        if (this.value > node.value) {
            return 1;
        }
        return 0;
    }

    public boolean isSameNode(Node node){
        if(this.getId().equals(node.getId())){
            return true;
        }
        return false;
    }
}
