package Models;

import java.util.ArrayList;

public class Matrix {

    private int lines;
    private int columns;
    private ArrayList<Agent> agents;
    private ArrayList<Couple> couples;
    private ArrayList<Registry> registries;
    private ArrayList<Wall> walls;

    public Matrix(int lines, int columns, ArrayList<Registry> registries, ArrayList<Wall> walls) {
        this.lines = lines;
        this.columns = columns;
        this.registries = registries;
        this.walls = walls;
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
        /**
         * TODO atualizar matrix com as localizações dos elementos que estão nela
         */
    }

    @Override
    public String toString() {
        /**
         * TODO exibir matriz
         */
        return "Matrix{" +
                "lines=" + lines +
                ", columns=" + columns +
                ", agents=" + agents +
                ", couples=" + couples +
                ", registries=" + registries +
                ", walls=" + walls +
                '}';
    }
}
