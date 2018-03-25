package Models;

public class Couple {

    private Agent maleAgent;
    private Agent femaleAgent;
    private int x;
    private int y;

    public Couple(Agent maleAgent, Agent femaleAgent, int x, int y) {
        this.maleAgent = maleAgent;
        this.femaleAgent = femaleAgent;
        this.x = x;
        this.y = y;
    }

    public Agent getMaleAgent() {
        return maleAgent;
    }

    public void setMaleAgent(Agent maleAgent) {
        this.maleAgent = maleAgent;
    }

    public Agent getFemaleAgent() {
        return femaleAgent;
    }

    public void setFemaleAgent(Agent femaleAgent) {
        this.femaleAgent = femaleAgent;
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
}
