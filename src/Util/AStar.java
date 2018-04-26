package Util;

import Models.Matrix;
import Models.Part;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class AStar {

    private Node origin;
    private Node destination;
    private Matrix matrix;

    public AStar(Node origin, Node destination, Matrix matrix) {
        this.origin = origin;
        this.destination = destination;
        this.matrix = matrix;
    }

    public AStar() {

    }

    public Node getOrigin() {
        return origin;
    }

    public void setOrigin(Node origin) {
        this.origin = origin;
    }

    public Node getDestination() {
        return destination;
    }

    public void setDestination(Node destination) {
        this.destination = destination;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public HashMap<String, Node> astar(){
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<Node> frontier = new ArrayList<Node>();
        frontier.add(this.origin);

        HashMap<String, Node> came_from = new HashMap<String, Node>();
        HashMap<String, Double> cost_so_far = new HashMap<String, Double>();

        came_from.put(origin.getId(), null);
        cost_so_far.put(origin.getId(), 0.0);

        while(frontier.size() > 0){
            Node current = (Node) frontier.get(0);
            frontier.remove(0);

            if(current.getX() == this.destination.getX() && current.getY() == this.destination.getY())
                break;

            Double d = Double.MAX_VALUE;
            ArrayList<Node> neighbors = getNeighbors(current);

            for (Node next: neighbors) {
                double new_cost = 0.0;
                double priority = this.heuristic(new_cost, next);

                if(!cost_so_far.containsKey(next.getId()) || priority < d){
                    d = priority;
                    cost_so_far.put(next.getId(), new_cost);

                    if(!ids.contains(next.getId())) {
                        frontier.add(next);
                        ids.add(next.getId());
                    }

                    if(!came_from.containsKey(next.getId())) {
                        came_from.put(next.getId(), current);
                    }
                }
            }
        }

        return came_from;
    }

    public ArrayList<Node> findPath(){
        HashMap<String, Node> path = this.astar();
        ArrayList<Node> shortestPath = new ArrayList<Node>();

        shortestPath.add(this.destination);
        Node came_from = path.get(this.destination.getId());

        while (true && came_from != null){
            came_from = path.get(came_from.getId());
            if(came_from == null){
                break;
            }
            shortestPath.add(came_from);
        }

        return shortestPath;
    }

    public double heuristic(double new_cost, Node next){
        return new_cost + (Math.sqrt(Math.pow((this.destination.getX()-next.getX()),2) + Math.pow((this.destination.getY()-next.getY()),2))
        );
    }

    public ArrayList<Node> getNeighbors(Node node){
        ArrayList<Node> neighbors = new ArrayList<Node>();
        ArrayList<ArrayList<Part>> m = this.matrix.getMatrix();
        int x,y;

        int[][] spots = {
            {node.getX(),node.getY()-1},//top
            {node.getX(),node.getY()+1},//bottom
            {node.getX()+1,node.getY()},//right
            {node.getX()-1,node.getY()},//left
            {node.getX()+1,node.getY()+1},//upper right diagonal
            {node.getX()-1,node.getY()-1},//upper left diagonal
            {node.getX()+1,node.getY()-1},//bottom right diagonal
            {node.getX()-1,node.getY()-1},//bottom left diagonal
        };

        for(int[] spot : spots){
            x = spot[0];
            y = spot[1];

            if(y >= 0 && y < this.matrix.getColumns() && x >= 0 && x < this.matrix.getLines())
                if(m.get(y) != null)
                    if(m.get(y).get(x) != null)
                        if(!m.get(y).get(x).getName().equals(Matrix.WALL_CHAR))
                            neighbors.add(new Node(x,y));
        }

        return  neighbors;
    }
}
