package Models;

import Util.AStar;
import Util.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Agent extends Part {

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

    public void log(String msg){
        System.out.println(msg);
    }

    public void act(Matrix matrix) {
        if (this.getIntention() == null) {
            if (this.getStatus() == Status.HAPPY_MARRIAGE) {
                this.walk(matrix);
                log(this.getName()+" move-se [1]");
            } else if (this.getStatus() == Status.HAPPY_ENGAGEMENT) {
                this.goToResgistry(matrix);
                log(this.getName()+" está indo ao cartório");
            } else {
                if (this.getGender() == 1) {
                    Agent interestingFemaleAgent = this.lookForAgent(matrix);
                    if (interestingFemaleAgent != null) {
                        log(this.getName()+" está interessado em "+interestingFemaleAgent.getName());
                        if (this.isAgentCloser(matrix, interestingFemaleAgent)) {
                            if (interestingFemaleAgent.isInterestedIn(this)) {
                                if (this.getStatus() == Status.SINGLE || this.getStatus() == Status.UNHAPPY_ENGAGEMENT || this.getStatus() == Status.HAPPY_ENGAGEMENT) {
                                    this.engage(interestingFemaleAgent);
                                    log(this.getName() + "," + interestingFemaleAgent.getName() + " estão noivos ");
                                } else {
                                    log(interestingFemaleAgent.getName()+" está casada, estão indo para o cartório para divorciar e casar");
                                    this.locateNearestRegistry(matrix);
                                    this.setInterest(interestingFemaleAgent);
                                    this.setIntention(Intention.GO_REGISTRY_SEPARATE);

                                    interestingFemaleAgent.setNearestRegistryPath(this.getNearestRegistryPath());
                                    interestingFemaleAgent.setInterest(this);
                                    interestingFemaleAgent.setIntention(Intention.GO_REGISTRY_SEPARATE);

                                    if(interestingFemaleAgent.getSpouse() != null) {
                                        interestingFemaleAgent.getSpouse().setNearestRegistryPath(this.getNearestRegistryPath());
                                        interestingFemaleAgent.getSpouse().setIntention(Intention.GO_REGISTRY_SEPARATE);
                                    }
                                }
                            }else{
                                log(interestingFemaleAgent.getName()+" não está interessada em "+this.getName());
                            }
                        } else {
                            this.walk(matrix, new Node(interestingFemaleAgent.getX(), interestingFemaleAgent.getY()));
                            log(this.getName()+" move-se em direção a "+interestingFemaleAgent.getName());
                        }
                    } else {
                        this.walk(matrix);
                        log(this.getName()+" move-se [2]");
                    }
                } else {
                    this.walk(matrix);
                    log(this.getName()+" move-se [3]");
                }
            }
        } else {

            if (this.getGender() == 0) {
                this.walk(matrix);
                log(this.getName()+" move-se [4]");
            } else {
                if (this.getIntention() == Intention.GO_REGISTRY_MARRY) {
                    log(this.getName()+" verifica se está perto de um cartório para casar");
                    Registry registry = this.lookForRegistry(matrix);
                    if (registry == null) {
                        this.goToResgistry(matrix);
                        log(this.getName()+" move-se em direção ao cartório ");
                    } else {
                        this.setNearestRegistryPath(null);
                        this.marry();
                        log(this.getName() + "," + this.getSpouse().getName() + " estão casados ");
                    }
                } else if (this.getIntention() == Intention.GO_REGISTRY_SEPARATE) {
                    log(this.getName()+" verifica se está perto de um cartório para divorciar");
                    Registry registry = this.lookForRegistry(matrix);
                    if (registry == null) {
                        this.goToResgistry(matrix);
                        log(this.getName()+" move-se em direção ao cartório ");
                    } else {
                        this.setNearestRegistryPath(null);
                        log(this.getName() + "," + this.getSpouse().getName() + " estão divorciados ");
                        this.divorce();
                        this.marry();
                        log(this.getName() + "," + this.getSpouse().getName() + " estão casados ");
                    }
                }
            }
        }
    }

    public void locateNearestRegistry(Matrix matrix) {
        ArrayList<ArrayList<Node>> paths = new ArrayList<ArrayList<Node>>();
        AStar aStar = new AStar();
        aStar.setMatrix(matrix);

        for (Registry registry : matrix.getRegistries()) {
            aStar.setOrigin(new Node(this.getX(), this.getY()));
            aStar.setDestination(new Node(registry.getX(), registry.getY()));
            ArrayList<Node> path = aStar.findPath();
            paths.add(path);
        }

        ArrayList<Node> shortestPath = paths.get(0);
        for (int i = 1; i < paths.size(); i++) {
            if (paths.get(i).size() < shortestPath.size()) {
                shortestPath = paths.get(i);
            }
        }

        Stack<Node> nearestRegistryPath = new Stack<Node>();

        for (int i = shortestPath.size() - 1; i >= 0; i--) {
            nearestRegistryPath.push(shortestPath.get(i));
        }

        this.setNearestRegistryPath(nearestRegistryPath);
    }

    public void goToResgistry(Matrix matrix) {
        if (this.getNearestRegistryPath() == null) {
            this.locateNearestRegistry(matrix);
        }

        if(this.getNearestRegistryPath().size() == 0){
            this.setNearestRegistryPath(null);
        }else {
            walk(matrix, this.getNearestRegistryPath().pop());
        }
    }

    public Agent lookForAgent(Matrix matrix) {
        ArrayList<Agent> agents = new ArrayList<Agent>();
        ArrayList<ArrayList<Part>> m = matrix.getMatrix();
        int x, y;

        int[][] spots = {
                {this.getX(), this.getY() - 1},//top
                {this.getX(), this.getY() + 1},//bottom
                {this.getX() + 1, this.getY()},//right
                {this.getX() - 1, this.getY()},//left
                {this.getX() + 1, this.getY() + 1},//upper right diagonal
                {this.getX() - 1, this.getY() - 1},//upper left diagonal
                {this.getX() + 1, this.getY() - 1},//bottom right diagonal
                {this.getX() - 1, this.getY() - 1},//bottom left diagonal
                {this.getX(), this.getY() - 2},//top
                {this.getX(), this.getY() + 2},//bottom
                {this.getX() + 2, this.getY()},//right
                {this.getX() - 2, this.getY()},//left
                {this.getX() + 2, this.getY() + 2},//upper right diagonal
                {this.getX() - 2, this.getY() - 2},//upper left diagonal
                {this.getX() + 2, this.getY() - 2},//bottom right diagonal
                {this.getX() - 2, this.getY() - 2},//bottom left diagonal
                {this.getX() - 2, this.getY() - 1},
                {this.getX() + 2, this.getY() - 1},
                {this.getX() - 2, this.getY() + 1},
                {this.getX() + 2, this.getY() + 1}
        };

        for (int[] spot : spots) {
            x = spot[0];
            y = spot[1];

            if (y >= 0 && y < matrix.getColumns() && x >= 0 && x < matrix.getLines()) {
                Part part = m.get(y).get(x);
                if (part.getClass().equals(this.getClass())) {
                    Agent agent = (Agent) part;
                    if (agent.getGender() == 0) {
                        agents.add(agent);
                    }
                }
            }
        }

        Agent favoriteAgent = null;
        if (agents.size() > 0) {
            favoriteAgent = agents.get(0);
            int index = Integer.MAX_VALUE;

            for (int i = 1; i < agents.size(); i++) {
                for (Integer key : this.getPreference().keySet()) {
                    if (agents.get(i).getName().equals(this.getPreference().get(key).getName()) && key < index) {
                        favoriteAgent = agents.get(i);
                        index = key;
                    }
                }
            }
        }

        return favoriteAgent;
    }

    public boolean isAgentCloser(Matrix matrix, Agent agent) {
        ArrayList<ArrayList<Part>> m = matrix.getMatrix();
        int x, y;

        int[][] spots = {
                {this.getX(), this.getY() - 1},//top
                {this.getX(), this.getY() + 1},//bottom
                {this.getX() + 1, this.getY()},//right
                {this.getX() - 1, this.getY()},//left
                {this.getX() + 1, this.getY() + 1},//upper right diagonal
                {this.getX() - 1, this.getY() - 1},//upper left diagonal
                {this.getX() + 1, this.getY() - 1},//bottom right diagonal
                {this.getX() - 1, this.getY() - 1},//bottom left diagonal
        };

        for (int[] spot : spots) {
            x = spot[0];
            y = spot[1];

            if (y >= 0 && y < matrix.getColumns() && x >= 0 && x < matrix.getLines()) {
                Part part = m.get(y).get(x);
                if (part.getClass().equals(this.getClass())) {
                    Agent a = (Agent) part;
                    if (a.getName().equals(agent.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Registry lookForRegistry(Matrix matrix) {
        ArrayList<Registry> registries = new ArrayList<Registry>();
        ArrayList<ArrayList<Part>> m = matrix.getMatrix();
        int x, y;

        int[][] spots = {
                {this.getX(), this.getY() - 1},//top
                {this.getX(), this.getY() + 1},//bottom
                {this.getX() + 1, this.getY()},//right
                {this.getX() - 1, this.getY()},//left
                {this.getX() + 1, this.getY() + 1},//upper right diagonal
                {this.getX() - 1, this.getY() - 1},//upper left diagonal
                {this.getX() + 1, this.getY() - 1},//bottom right diagonal
                {this.getX() - 1, this.getY() - 1},//bottom left diagonal
                {this.getX(), this.getY() - 2},//top
                {this.getX(), this.getY() + 2},//bottom
                {this.getX() + 2, this.getY()},//right
                {this.getX() - 2, this.getY()},//left
                {this.getX() + 2, this.getY() + 2},//upper right diagonal
                {this.getX() - 2, this.getY() - 2},//upper left diagonal
                {this.getX() + 2, this.getY() - 2},//bottom right diagonal
                {this.getX() - 2, this.getY() - 2},//bottom left diagonal
                {this.getX() - 2, this.getY() - 1},
                {this.getX() + 2, this.getY() - 1},
                {this.getX() - 2, this.getY() + 1},
                {this.getX() + 2, this.getY() + 1}
        };

        for (int[] spot : spots) {
            x = spot[0];
            y = spot[1];

            if (y >= 0 && y < matrix.getColumns() && x >= 0 && x < matrix.getLines()) {
                Part part = m.get(y).get(x);
                if (part.getClass().equals(Registry.class)) {
                    return (Registry) part;
                }
            }
        }

        return null;
    }

    public void walk(Matrix matrix, Node location) {
        AStar aStar = new AStar();
        aStar.setMatrix(matrix);

        aStar.setOrigin(new Node(this.getX(), this.getY()));
        aStar.setDestination(location);
        ArrayList<Node> path = aStar.findPath();

        Stack<Node> nearestPath = new Stack<Node>();

        for (int i = path.size() - 1; i >= 0; i--) {
            nearestPath.push(path.get(i));
        }

        Node n = nearestPath.pop();
        this.setX(n.getX());
        this.setY(n.getY());
    }

    public int walkDefineX(Matrix matrix) {
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

    public void walk(Matrix matrix) {
        int x = this.getX();
        int y = this.getY();

        //Define y
        if (this.getGoingY() == 1) {
            y = this.getY() - 1;//Walk up
            if (!matrix.isCoordValid(x, y)) {

                if (matrix.isCoordNullPointer(x, y))
                    this.setGoingY(0);

                y = this.getY();
                x = this.walkDefineX(matrix);
            }
        } else {
            y = this.getY() + 1;//Walk down
            if (!matrix.isCoordValid(x, y)) {

                if (matrix.isCoordNullPointer(x, y))
                    this.setGoingY(1);

                y = this.getY();
                x = this.walkDefineX(matrix);
            }
        }

        if (!matrix.isCoordValid(x, y)) {
            y = this.getY();
            x = this.getX();
        }

        this.setX(x);
        this.setY(y);
    }

    public void engage(Agent agent) {
        if (agent.getFiance() == null) {

            //Update female agent
            agent.setIntention(Intention.GO_REGISTRY_MARRY);
            agent.setInterest(null);
            agent.setFiance(this);

            if (agent.isHighPriority(this))
                agent.setStatus(Status.HAPPY_ENGAGEMENT);
            else
                agent.setStatus(Status.UNHAPPY_ENGAGEMENT);

            //Update male agent
            this.setIntention(Intention.GO_REGISTRY_MARRY);
            this.setInterest(null);
            this.setFiance(agent);

            if (this.isHighPriority(agent))
                this.setStatus(Status.HAPPY_ENGAGEMENT);
            else
                this.setStatus(Status.UNHAPPY_ENGAGEMENT);

        } else {

            if (agent.isInterestedIn(this)) {
                //Update current fiance
                agent.getFiance().setIntention(null);
                agent.getFiance().setInterest(null);
                agent.getFiance().setFiance(null);
                agent.getFiance().setStatus(Status.SINGLE);

                //Update female agent
                agent.setIntention(Intention.GO_REGISTRY_MARRY);
                agent.setInterest(null);
                agent.setFiance(this);

                if (agent.isHighPriority(this))
                    agent.setStatus(Status.HAPPY_ENGAGEMENT);
                else
                    agent.setStatus(Status.UNHAPPY_ENGAGEMENT);

                //Update male agent
                this.setIntention(Intention.GO_REGISTRY_MARRY);
                this.setInterest(null);
                this.setFiance(agent);

                if (this.isHighPriority(agent))
                    this.setStatus(Status.HAPPY_ENGAGEMENT);
                else
                    this.setStatus(Status.UNHAPPY_ENGAGEMENT);
            }
        }
    }

    public boolean isInterestedIn(Agent agent) {
        int currentFiancePriority = Integer.MAX_VALUE;
        int newFiancePriority = Integer.MAX_VALUE;

        for (Integer key : this.getPreference().keySet()) {
            if (this.getFiance() != null && this.getPreference().get(key).getName().equals(this.getFiance().getName())) {
                currentFiancePriority = key;
            } else if (this.getPreference().get(key).getName().equals(agent.getName())) {
                newFiancePriority = key;
            }
        }

        if (newFiancePriority < currentFiancePriority)
            return true;

        return false;
    }

    public boolean isHighPriority(Agent agent) {
        for (Integer key : this.getPreference().keySet()) {
            if (key == 1) {
                Agent preference = this.getPreference().get(key);
                String preferenceName = preference.getName();
                return preferenceName.equals(agent.getName());
                //return preference.getName().equals(agent.getName());
            }
        }
        return false;
    }

    /**
     * TODO make them walk toghter
     */
    public void marry() {

        if (this.isHighPriority(this.getFiance()))
            this.setStatus(Status.HAPPY_MARRIAGE);
        else
            this.setStatus(Status.UNHAPPY_MARRIAGE);

        this.getFiance().setFiance(null);
        this.getFiance().setSpouse(this);
        this.getFiance().setInterest(null);
        this.getFiance().setIntention(null);
        this.getFiance().setStatus(Status.HAPPY_MARRIAGE);

        if (this.getFiance().isHighPriority(this))
            this.getFiance().setStatus(Status.HAPPY_MARRIAGE);
        else
            this.getFiance().setStatus(Status.UNHAPPY_MARRIAGE);

        this.setSpouse(this.getFiance());
        this.setFiance(null);
        this.setInterest(null);
        this.setIntention(null);
    }

    public void divorce() {
        this.getInterest().getSpouse().setFiance(this);
        this.getInterest().getSpouse().setSpouse(null);
        this.getInterest().getSpouse().setIntention(null);
        this.getInterest().getSpouse().setStatus(Status.SINGLE);

        this.getInterest().setFiance(this);
        this.getInterest().setIntention(null);
        this.getInterest().setStatus(Status.SINGLE);

        this.getInterest().getSpouse().setInterest(this);
        this.getInterest().setSpouse(null);
        this.getInterest().setInterest(this);
    }

    public static Agent findAgentByName(ArrayList<Agent> agents, String name) {
        for (Agent agent : agents) {
            if (agent.getName().equals(name)) {
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
