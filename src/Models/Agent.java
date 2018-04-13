package Models;

import Util.Node;

import java.util.ArrayList;

public class Agent extends Part{

    private int x;
    private int y;
    private int intention;
    private int gender;
    private Agent fiance;
    private Agent spouse;
    private Agent interest;
    private ArrayList<Agent> preference;

    public Agent(String name, int x, int y, int gender) {
        super(name);
        this.x = x;
        this.y = y;
        this.gender = gender;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getIntention() {
        return intention;
    }

    public void setIntention(int intention) {
        this.intention = intention;
    }

    public Agent getFiance() {
        return fiance;
    }

    public void setFiance(Agent fiance) {
        this.fiance = fiance;
    }

    public Agent getSpouse() {
        return spouse;
    }

    public void setSpouse(Agent spouse) {
        this.spouse = spouse;
    }

    public Agent getInterest() {
        return interest;
    }

    public void setInterest(Agent interest) {
        this.interest = interest;
    }

    public ArrayList<Agent> getPreference() {
        return preference;
    }

    public void setPreference(ArrayList<Agent> preference) {
        this.preference = preference;
    }

    public Registry locateNearestRegistry(Matrix matrix){
        /**
         * TODO localizar cartório mais próximo
         */
        return null;
    }

    public void observe(Matrix matrix){
        //TODO observar 2 casas ao seu redor por um par

        ArrayList<Agent> agents = new ArrayList<Agent>();
    }

    public void walk(Matrix matrix){
        /**
         * TODO andar verticalmente uma casa, desviar de objetos
         */
    }

    public void engage(Agent agent){
        this.setFiance(agent);
        this.setInterest(null);

        if(agent.getFiance() != null && !agent.getFiance().getName().equals(this.getName())){
            agent.engage(this);
            agent.setInterest(null);
        }
    }

    public void marry(Agent agent){
        this.setSpouse(agent);
        this.setInterest(null);

        if(agent.getSpouse() != null && !agent.getSpouse().getName().equals(this.getName())){
            agent.marry(this);
            agent.setInterest(null);
        }
    }

    public void divorce(){
        if(this.getSpouse() != null){
            this.getSpouse().divorce();
        }
        this.setSpouse(null);
    }


}
