package Models;

import java.util.ArrayList;

public class Matrix {

    private int lines;
    private int columns;
    private ArrayList<Agent> agents;
    private ArrayList<Couple> couples;
    private ArrayList<Registry> registries;
    private ArrayList<Wall> walls;
    private ArrayList<ArrayList<String>> matrix;

    public Matrix(int lines, int columns, ArrayList<Registry> registries, ArrayList<Wall> walls, ArrayList<Agent> agents) {
        this.lines = lines;
        this.columns = columns;
        this.registries = registries;
        this.walls = walls;
        this.agents = agents;
        this.couples = new ArrayList<>();
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

    public void update(){
        this.matrix = new ArrayList<>(this.lines);

        for(int i = 0; i<this.lines; i++){
            ArrayList<String> n = new ArrayList<>(this.columns);
            for(int j = 0; j<this.columns; j++){
                n.add("__");
            }
            this.matrix.add(n);
        }

        for(int i=0; i<this.walls.size(); i++){
            Wall w = this.walls.get(i);
            for(int j=0; j<w.getLength(); j++) {
                this.matrix.get(w.getX()+j).set(w.getY(), "WW");
            }
        }

        for(int i=0; i<this.registries.size(); i++){
            Registry r = this.registries.get(i);
            this.matrix.get(r.getX()).set(r.getY(),r.getName());
        }

        for(int i=0; i<this.agents.size(); i++){
            Agent a = this.agents.get(i);
            this.matrix.get(a.getX()).set(a.getY(),a.getName());
        }

        for(int i=0; i<this.couples.size(); i++){
            Couple c = this.couples.get(i);
            this.matrix.get(c.getX()).set(c.getY(),c.getName());
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
                str.append("|"+this.matrix.get(i).get(j));
                if(j == this.columns-1){
                    str.append("|");
                }
            }
            str.append("\n\n");
        }
        return str.toString();
    }
}
