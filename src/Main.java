import Models.*;
import Util.AStar;
import Util.Node;
import Util.Reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {

    public static void main(String[] args){
        Reader reader = new Reader();

        //Read
        ArrayList<String[]> data = reader.readCsv("data.csv", " ");

        //Get couples and registries
        String[] line = data.get(0);
        int couplesQuantity = Integer.parseInt(line[0]);
        int registriesQuantity = Integer.parseInt(line[1]);

        Matrix matrix= new Matrix(20,20);

        //Instantiate walls
        ArrayList<Wall> walls = instantiateWalls();
        matrix.setWalls(walls);

        //Instantiate registries
        ArrayList<Registry> registries = instantiateRegistries(registriesQuantity, walls);
        matrix.setRegistries(registries);

        //Instantiate Agents
        matrix.update();
        ArrayList<Agent> agents = instantiateAgents(matrix, couplesQuantity, data);
        matrix.setAgents(agents);

        printMatrix(matrix);

        agents.get(0).locateNearestRegistry(matrix);
/*
        AStar aStar = new AStar(new Node(19,7), new Node(5,5), matrix);
        ArrayList<Node> shortestPath = aStar.findPath();
        System.out.println("end");
*/

        /*matrix.drawShortestPath(shortestPath);
        printMatrix(matrix);*/

        /*while(true) {
            for (Agent agent : agents) {
                agent.walk(matrix);
            }
            printMatrix(matrix);
        }*/

        //run(matrix,agents,registries);
    }

    public static void run(Matrix matrix, ArrayList<Agent> agents, ArrayList<Registry> registries){
        for(Agent agent : agents){
            if(agent.getIntention() == null) {
                if (agent.getStatus() == Status.HAPPY_MARRIAGE) {
                    agent.walk(matrix);
                } else if (agent.getStatus() == Status.HAPPY_ENGAGEMENT) {
                    agent.goToResgistry(matrix);
                } else {
                    if (agent.getGender() == 1) {
                        Agent interestingFemaleAgent = agent.observe(matrix);
                        int[] coord1 = {agent.getX(), agent.getY()};
                        int[] coord2 = {interestingFemaleAgent.getX(), interestingFemaleAgent.getY()};
                        if (matrix.isCloser(coord1, coord2)) {
                            /*TODO female agent has to decide*/
                            if (agent.getStatus() == Status.SINGLE || agent.getStatus() == Status.UNHAPPY_ENGAGEMENT) {
                                //engage
                            } else {
                                //locate nearest registry
                                //set intention to all involved agents
                            }
                        } else {
                            //get closer
                        }

                    } else {
                        agent.walk(matrix);
                    }
                }
            }else{
                //deal with the agents intentions
            }
        }

        printMatrix(matrix);
    }

    public static void clear(){
        for (int i = 0; i < 10; ++i)
            System.out.println();
    }

    public static ArrayList<Agent> instantiateAgents(Matrix matrix, int couplesQuantity,  ArrayList<String[]> data){
        //Instatiate agents
        ArrayList<Agent> agents = new ArrayList<Agent>();
        Random random = new Random();
        String prefix = "H";
        int c = 1;
        int n = 1;
        int gender = 0;

        int condition = couplesQuantity == 1 ? 2 : couplesQuantity*2;
        int x,y;
        while (c <= condition){

            x = random.nextInt(matrix.getLines());
            y = random.nextInt(matrix.getColumns());
            /*String coord = matrix.getMatrix().get(y).get(x).getName();
            while (!coord.equals(matrix.EMPTY_CHAR)){
                x = random.nextInt(matrix.getLines());
                y = random.nextInt(matrix.getColumns());
            }*/

            agents.add(new Agent(prefix+n,x,y, gender));
            c++;
            n++;
            if(c > couplesQuantity && prefix.equals("H")) {
                gender = 1;
                n = 1;
                prefix = "M";
            }
        }

        //Set preferences
        String prefixAgent = "H";
        String prefixPreference = "M";
        for(int i = 1; i<data.size(); i++){
            if(i > couplesQuantity){
                prefixAgent = "M";
                prefixPreference = "H";
            }

            String[] l = data.get(i);
            Agent agent = Agent.findAgentByName(agents, prefixAgent+l[0]);
            HashMap<Integer, Agent> preference = new HashMap<Integer, Agent>();

            for(int j=1; j < l.length; j++){
                Agent a = Agent.findAgentByName(agents, prefixPreference+l[j]);
                preference.put(j, a);
            }
            agent.setPreference(preference);


        }

        return agents;
    }

    public static ArrayList<Wall> instantiateWalls(){
        ArrayList<Wall> walls = new ArrayList<Wall>();

        walls.add(new Wall(4,4,1,11));
        walls.add(new Wall(8,9,1,10));
        walls.add(new Wall(13,6,1,9));
        walls.add(new Wall(17,3,1,11));

        return walls;
    }

    public static ArrayList<Registry> instantiateRegistries(int registriesQuantity, ArrayList<Wall> walls){
        ArrayList<Registry> registries = new ArrayList<Registry>();
        Random random = new Random();

        int c = 1;
        int w = 0;
        int i = 1;
        while (c <= registriesQuantity){
            Wall wall = walls.get(w);
            registries.add(new Registry("R"+c, wall.getX()+i, wall.getY()+1));

            c++;
            w++;
            if(w > walls.size()-1){
                w = 0;
                i++;
            }
        }
        return registries;
    }

    public static void printMatrix(Matrix matrix){
        matrix.update();
        clear();
        System.out.print(matrix.toString());
    }
}
