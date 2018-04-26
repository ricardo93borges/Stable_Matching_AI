package Models;

import Util.Node;

import java.util.ArrayList;

public class Matrix {

    private int lines;
    private int columns;
    private ArrayList<Agent> agents;
    private ArrayList<Couple> couples;
    private ArrayList<Registry> registries;
    private ArrayList<Wall> walls;
    private ArrayList<ArrayList<Part>> matrix;

    public static final String EMPTY_CHAR = "__";
    public static final String WALL_CHAR = "WW";

    public Matrix(int lines, int columns, ArrayList<Registry> registries, ArrayList<Wall> walls, ArrayList<Agent> agents) {
        this.lines = lines;
        this.columns = columns;
        this.registries = registries;
        this.walls = walls;
        this.agents = agents;
        this.couples = new ArrayList<Couple>();
        this.update();
    }

    public Matrix(int lines, int columns) {
        this.lines = lines;
        this.columns = columns;
        this.couples = new ArrayList<Couple>();
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public void setAgents(ArrayList<Agent> agents) {
        this.agents = agents;
    }

    public ArrayList<Couple> getCouples() {
        return couples;
    }

    public void setCouples(ArrayList<Couple> couples) {
        this.couples = couples;
    }

    public ArrayList<Registry> getRegistries() {
        return registries;
    }

    public void setRegistries(ArrayList<Registry> registries) {
        this.registries = registries;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public void setWalls(ArrayList<Wall> walls) {
        this.walls = walls;
    }

    public ArrayList<ArrayList<Part>> getMatrix() {
        return matrix;
    }

    public void setMatrix(ArrayList<ArrayList<Part>> matrix) {
        this.matrix = matrix;
    }

    public boolean isCloser(int[] location1, int[] location2){
        return true;
    }

    public boolean isCoordValid(int x, int y){
        if(!this.isCoordNullPointer(x,y) && this.isCoordEmpty(x,y))
            return true;
        return false;
    }

    public boolean isCoordEmpty(int x, int y){
        Part part = this.getMatrix().get(y).get(x);
        if(!part.getName().equals(Matrix.EMPTY_CHAR)) {
            return false;
        }
        return true;
    }

    public boolean isCoordNullPointer(int x, int y){
        if(y < 0 || y >= this.getColumns() || x < 0 || x >= this.getLines()) {
            return true;
        }
        return false;
    }

    public void update(){
        this.matrix = new ArrayList<ArrayList<Part>>(this.lines);

        for(int i = 0; i<this.lines; i++){
            ArrayList<Part> line = new ArrayList<Part>(this.columns);
            for(int j = 0; j<this.columns; j++){
                line.add(new Part(EMPTY_CHAR));
            }
            this.matrix.add(line);
        }

        if(this.walls != null) {
            for (int i = 0; i < this.walls.size(); i++) {
                Wall w = this.walls.get(i);
                for (int j = 0; j < w.getLength(); j++) {
                    this.matrix.get(w.getY() + j).set(w.getX(), w);
                }
            }
        }

        if(this.registries != null) {
            for (int i = 0; i < this.registries.size(); i++) {
                Registry r = this.registries.get(i);
                this.matrix.get(r.getY()).set(r.getX(), r);
            }
        }

        if(this.agents != null) {
            if (this.agents != null) {
                for (int i = 0; i < this.agents.size(); i++) {
                    Agent a = this.agents.get(i);
                    this.matrix.get(a.getY()).set(a.getX(), a);
                }
            }
        }

        if(this.couples != null) {
            for (int i = 0; i < this.couples.size(); i++) {
                Couple c = this.couples.get(i);
                this.matrix.get(c.getY()).set(c.getX(), c);
            }
        }
    }

    public void drawShortestPath(ArrayList<Node> shortestPath){
        for(Node node : shortestPath){
            matrix.get(node.getX()).set(node.getY(), new Part("*"));
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("   ");
        for(int j = 0; j<this.columns; j++) {

            if(j == 0)
                str.append("|"+j+" |");
            else if(j<10)
                str.append(""+j+" |");
            else
                str.append(j+"|");
        }
        str.append("\n");

        str.append("   ");
        for(int j = 0; j<this.columns; j++) {
            str.append(" __");
        }

        str.append("\n");

        for(int i = 0; i<this.lines; i++){
            if(i<10)
                str.append(i+"  ");
            else
                str.append(i+" ");


            for(int j = 0; j<this.columns; j++){
                str.append("|"+this.matrix.get(i).get(j).getName());
                if(j == this.columns-1){
                    str.append("|");
                }
            }
            str.append("\n\n");
        }
        return str.toString();
    }
}
