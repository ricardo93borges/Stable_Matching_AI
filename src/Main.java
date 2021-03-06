import Models.*;
import Util.AStar;
import Util.Node;
import Util.Reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {

    public static int cycles = 0;

    public static void main(String[] args){
        Reader reader = new Reader();

        //Read
        ArrayList<String[]> data = reader.readCsv("files/10Casais.txt", " ");

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

        try {
            run(matrix,agents);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void printAgentsStatus(ArrayList<Agent> agents){
        for(Agent agent : agents){
            if(agent.getGender() == 1) {
                if (agent.getStatus() == Status.SINGLE) {
                    System.out.println(agent.getName() + " Está solteiro ");
                } else if (agent.getFiance() != null && (agent.getStatus() == Status.HAPPY_ENGAGEMENT || agent.getStatus() == Status.UNHAPPY_ENGAGEMENT)) {
                    System.out.println(agent.getName() + " Está noivo de " + agent.getFiance().getName());
                } else if (agent.getSpouse() != null && (agent.getStatus() == Status.HAPPY_MARRIAGE || agent.getStatus() == Status.UNHAPPY_MARRIAGE)) {
                    System.out.println(agent.getName() + " Está casado com " + agent.getSpouse().getName());
                }
            }
        }
    }

    public static boolean existSingleAgent(ArrayList<Agent> agents){
        System.out.println("Ciclo: "+cycles);
        printAgentsStatus(agents);
        cycles++;
        for (Agent agent : agents) {
            if(agent.getStatus() == Status.SINGLE || agent.getStatus() == Status.HAPPY_ENGAGEMENT || agent.getStatus() == Status.UNHAPPY_ENGAGEMENT)
                return true;
        }
        System.out.println("Todos os agentes estão casados. \n"+cycles+" ciclos");
        for (Agent agent : agents)
            System.out.println(agent.getName()+":"+agent.getStatus());
        return false;
    }

    public static void run(Matrix matrix, ArrayList<Agent> agents) throws InterruptedException {
        while (existSingleAgent(agents)) {
            for (Agent agent : agents) {
                agent.act(matrix);
                Thread.sleep(30);
            }
            printMatrix(matrix);
            //Thread.sleep(500);
        }
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
        int c = 0;
        int n = 0;
        int gender = 1;

        int condition = couplesQuantity == 1 ? 2 : couplesQuantity*2;
        int x,y;
        while (c < condition){

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
            if(c >= couplesQuantity && prefix.equals("H")) {
                gender = 0;
                n = 0;
                prefix = "M";
            }
        }

        //Set preferences
        String prefixAgent = "H";
        String prefixPreference = "M";
        for(int i = 1; i<data.size(); i++){

            String[] l = data.get(i);

            if(l[0].equals(""))
                continue;

            if(i > couplesQuantity){
                prefixAgent = "M";
                prefixPreference = "H";
            }


            Agent agent = Agent.findAgentByName(agents, prefixAgent+l[0]);
            HashMap<Integer, Agent> preference = new HashMap<Integer, Agent>();

            for(int j=l.length-1; j >= 1; j--){
                if(l[j].equals(""))
                    continue;
                Agent a = Agent.findAgentByName(agents, prefixPreference+l[j]);
                preference.put(j, a);
            }
            agent.setPreference(preference);


        }

        return agents;
    }

    public static ArrayList<Wall> instantiateWalls(){
        ArrayList<Wall> walls = new ArrayList<Wall>();

        walls.add(new Wall(2,4,1,11));
        walls.add(new Wall(6,9,1,10));
        walls.add(new Wall(11,6,1,9));
        walls.add(new Wall(15,4,1,11));

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
            registries.add(new Registry("R"+c, wall.getX()+1, wall.getY()+i*2));

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
