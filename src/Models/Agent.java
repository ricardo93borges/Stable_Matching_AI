package Models;

import Util.AStar;
import Util.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Agent extends Part{

    private int x;
    private int y;
    private Intention intention;
    private int gender;
    private Agent fiance;
    private Agent spouse;
    private Agent interest;
    private HashMap<Integer, Agent> preference;
    private Status status;
    private int goingX;//1 = right, 0 = left
    private int goingY;//1 = up, 0 = down
    private Stack<Node> nearestRegistryPath;

    public Agent(String name, int x, int y, int gender) {
        super(name);
        this.x = x;
        this.y = y;
        this.gender = gender;
        this.status = Status.SINGLE;
        this.goingX = 1;
        this.goingY = 1;
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

    public Intention getIntention() {
        return intention;
    }

    public void setIntention(Intention intention) {
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public int getGoingX() {
        return goingX;
    }

    public void setGoingX(int goingX) {
        this.goingX = goingX;
    }

    public int getGoingY() {
        return goingY;
    }

    public void setGoingY(int goingY) {
        this.goingY = goingY;
    }

    public Stack<Node> getNearestRegistryPath() {
        return nearestRegistryPath;
    }

    public void setNearestRegistryPath(Stack<Node> nearestRegistryPath) {
        this.nearestRegistryPath = nearestRegistryPath;
    }

    public void locateNearestRegistry(Matrix matrix){
        ArrayList<ArrayList<Node>> paths = new ArrayList<ArrayList<Node>>();
        AStar aStar = new AStar();
        aStar.setMatrix(matrix);

        for(Registry registry : matrix.getRegistries()){
            aStar.setOrigin(new Node(this.getX(), this.getY()));
            aStar.setDestination(new Node(registry.getX(), registry.getY()));
            ArrayList<Node> path = aStar.findPath();
            paths.add(path);
        }

        ArrayList<Node> shortestPath = paths.get(0);
        for(int i=1; i<paths.size(); i++){
            if(paths.get(i).size() < shortestPath.size()){
                shortestPath = paths.get(i);
            }
        }

        Stack<Node> nearestRegistryPath = new Stack<Node>();
        /*for(Node node : shortestPath){
            nearestRegistryPath.push(node);
        }*/

        for(int i = shortestPath.size()-1; i >= 0; i--){
            nearestRegistryPath.push(shortestPath.get(i));
        }

        this.setNearestRegistryPath(nearestRegistryPath);
    }

    public void goToResgistry(Matrix matrix){
        if(this.getNearestRegistryPath() == null){
            this.locateNearestRegistry(matrix);
        }

        walk(matrix, this.getNearestRegistryPath().pop());
    }

    /**
     * Look around for agents that is in preference list
     * @param matrix
     */
    public Agent observe(Matrix matrix){
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

        Agent favoriteAgent = null;
        if(agents.size() > 0) {
            favoriteAgent = agents.get(0);
            int index = Integer.MAX_VALUE;

            for (int i = 1; i < agents.size(); i++) {
                for (Integer key : this.getPreference().keySet()) {
                    if (agents.get(i).equals(this.getPreference().get(key)) && key < index) {
                        favoriteAgent = agents.get(i);
                        index = key;
                    }
                }
            }
        }

        return favoriteAgent;
    }

    public void walk(Matrix matrix, Node location){
        /**
         * TODO andar verticalmente uma casa, desviar de objetos
         */
    }

    public int walkDefineX(Matrix matrix){
        int x = this.getX();
        int y = this.getY();
        if (this.getGoingX() == 0) {
            x = this.getX() - 1;//Walk left
            if (!matrix.isCoordValid(x, y)) {
                x = this.getX() + 1;//Walk right
                this.setGoingX(1);
            }
        } else {
            x = this.getX() + 1;//Walk right
            if (!matrix.isCoordValid(x, y)) {
                x = this.getX() - 1;//Walk left
                this.setGoingX(0);
            }
        }

        return x;
    }

    public void walk(Matrix matrix){
        int x = this.getX();
        int y = this.getY();

        //Define y
        if(this.getGoingY() == 1){
            y = this.getY()-1;//Walk up
            if(!matrix.isCoordValid(x,y)){

                if(matrix.isCoordNullPointer(x,y))
                    this.setGoingY(0);

                y = this.getY();
                x = this.walkDefineX(matrix);
            }
        }else{
            y = this.getY()+1;//Walk down
            if(!matrix.isCoordValid(x,y)){

                if(matrix.isCoordNullPointer(x,y))
                    this.setGoingY(1);

                y = this.getY();
                x = this.walkDefineX(matrix);
            }
        }

        if(!matrix.isCoordValid(x,y)){
            y = this.getY();
            x = this.getX();
        }

        this.setX(x);
        this.setY(y);
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
