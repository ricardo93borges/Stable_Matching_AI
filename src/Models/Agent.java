package Models;

import Util.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class Agent extends Part{

    private int x;
    private int y;
    private int intention;
    private int gender;
    private Agent fiance;
    private Agent spouse;
    private Agent interest;
    private HashMap<Integer, Agent> preference;

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

    public HashMap<Integer, Agent> getPreference() {
        return preference;
    }

    public void setPreference(HashMap<Integer, Agent> preference) {
        this.preference = preference;
    }

    public Registry locateNearestRegistry(Matrix matrix){
        /**
         * TODO localizar cartório mais próximo
         */
        return null;
    }

    /**
     * Look around for agents that is in preference list
     * @param matrix
     */
    public void observe(Matrix matrix){
        //TODO observar 2 casas ao seu redor por um par
        ArrayList<Agent> agents = new ArrayList<Agent>();
        ArrayList<ArrayList<Part>> m = matrix.getMatrix();
        int x,y;

        int[][] spots = {
                {this.getX(),this.getY()-1},//top
                {this.getX(),this.getY()-2},//top
                {this.getX(),this.getY()+1},//bottom
                {this.getX(),this.getY()+2},//bottom
                {this.getX()+1,this.getY()},//right
                {this.getX()+2,this.getY()},//right
                {this.getX()-1,this.getY()},//left
                {this.getX()-2,this.getY()},//left
                {this.getX()+1,this.getY()+1},//upper right diagonal
                {this.getX()+2,this.getY()+2},//upper right diagonal
                {this.getX()-1,this.getY()-1},//upper left diagonal
                {this.getX()-2,this.getY()-2},//upper left diagonal
                {this.getX()+1,this.getY()-1},//bottom right diagonal
                {this.getX()+2,this.getY()-2},//bottom right diagonal
                {this.getX()-1,this.getY()-1},//bottom left diagonal
                {this.getX()-1,this.getY()-1},//bottom left diagonal
        };

        for(int[] spot : spots){
            x = spot[0];
            y = spot[1];

            if(y >= 0 && y <= matrix.getColumns()){
                Part part = m.get(x).get(y);
                if(part.getClass().equals(this.getClass())){
                    agents.add((Agent) part);
                }
            }
        }

        /**
         * TODO verificar se os agents encontrados estão na lista de preferencias e escolher o com prioridade mais alta
         */

        Agent favoriteAgent = agents.get(0);

        for(Agent agent : agents){

        }

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

    public static Agent findAgentByName(ArrayList<Agent> agents, String name){
        for (Agent agent : agents){
            if(agent.getName().equals(name)){
                return agent;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getName().equals(obj.getClass().getName());
    }
}
