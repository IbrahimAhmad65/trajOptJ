package Math;

import java.util.ArrayList;
import java.util.List;

public class Node {
    double x;
    double y;
    List<Node> neighbors;
    double cost;
    Node parent;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
        this.neighbors = new ArrayList<>();
        this.cost = Double.POSITIVE_INFINITY;
        this.parent = null;
    }
    public String toString(){
        return "(" + x + ", " + y + ")";
    }

    // only means something when using nodes as vectors

    void setMagnitude(double magnitude){
        double angle = Math.atan2(y, x);
        this.x = magnitude * Math.cos(angle);
        this.y = magnitude * Math.sin(angle);
    }
}