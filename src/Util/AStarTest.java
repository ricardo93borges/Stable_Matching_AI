package Util;

import Models.Agent;
import Models.Matrix;
import Models.Registry;
import Models.Wall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AStarTest {

    public AStarTest() {
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

        AStar aStar = new AStar();
        aStar.setMatrix(matrix);
        aStar.setOrigin(new Node(agents.get(0).getX(), agents.get(0).getY()));
        aStar.setDestination(new Node(registries.get(0).getX(), registries.get(0).getY()));
        ArrayList<Node> path = aStar.findPath();
        System.out.println(path);

        for(Node node : path){
            matrix.getMatrix().get(node.getY()).get(node.getX()).setName("_x");
        }
        printMatrix(matrix);
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
        registries.add(new Registry("R1", 14, 7));

        return registries;
    }

    public static ArrayList<Agent> instantiateAgents(Matrix matrix, int couplesQuantity,  ArrayList<String[]> data){
        //Instatiate agents
        ArrayList<Agent> agents = new ArrayList<Agent>();
        agents.add(new Agent("H1",6,3, 1));

        return agents;
    }

    public static void printMatrix(Matrix matrix){
        //matrix.update();
        clear();
        System.out.print(matrix.toString());
    }

    public static void clear(){
        for (int i = 0; i < 10; ++i)
            System.out.println();
    }
}
