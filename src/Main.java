import Models.Agent;
import Models.Matrix;
import Models.Registry;
import Models.Wall;
import Util.Reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args){
        Reader reader = new Reader();

        //Read
        ArrayList data = reader.readCsv("data.csv", " ");

        //Get couples and registries
        String[] line = (String[]) data.get(0);
        int couplesQuantity = Integer.parseInt(line[0]);
        int registriesQuantity = Integer.parseInt(line[1]);

        //Instantiate Agents
        ArrayList<Agent> agents = instantiateAgents(couplesQuantity);

        //Instantiate walls
        ArrayList<Wall> walls = instantiateWalls();

        //Instantiate registries
        ArrayList<Registry> registries = instantiateRegistries(registriesQuantity, walls);

        //Instatiate matrix
        Matrix matrix= new Matrix(20,20, registries, walls, agents);

        printMatrix(matrix);
    }

    public static void clear(){
        for (int i = 0; i < 10; ++i)
            System.out.println();
    }

    public static ArrayList<Agent> instantiateAgents(int couplesQuantity){
        ArrayList<Agent> agents = new ArrayList<Agent>();
        Random random = new Random();
        int c = 1;
        int gender = 0;

        while (c <= couplesQuantity*2){
            agents.add(new Agent("A"+c,random.nextInt(20),random.nextInt(20), gender));
            c++;
            if(c == 3)
                gender = 1;
        }

        return agents;
    }

    public static ArrayList<Wall> instantiateWalls(){
        ArrayList<Wall> walls = new ArrayList<Wall>();
        walls.add(new Wall(4,4,1,11));
        walls.add(new Wall(9,8,1,10));
        walls.add(new Wall(6,13,1,9));
        walls.add(new Wall(3,17,1,11));
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
            if(w > wall.getLength()-1){
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
