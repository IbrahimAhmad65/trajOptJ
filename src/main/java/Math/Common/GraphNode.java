package Math.Common;

import java.util.ArrayList;
import java.util.List;

public class GraphNode extends Node{

    public List<GraphNode> neighbors = new ArrayList<>();
    public double cost = 1e300;

    public static double findDistance( Node g1, Node g2){
        double x = (g1.x - g2.x);
        double y = (g1.y - g2.y);
        return Math.sqrt(x*x+y*y);
    }
    public GraphNode(double x, double y){
        this.x = x;
        this.y = y;
    }

    public GraphNode(double x, double y,double cost, List<GraphNode> neighbors){
        this.x = x;
        this.cost = cost;
        this.y = y;
//        for (GraphNode g : neighbors) {
//            this.neighbors.add(g.clone());
//        }
    }


    public void flush(GraphNode n1, GraphNode n2){
        cost = 1e300;
        neighbors.remove(n1);
        neighbors.remove(n2);
//        neighbors.clear();
    }
    public String toString(){
        return "(" + x + ", " + y + ")";
    }

//    public GraphNode clone(){
//        return new GraphNode(x,y,cost,neighbors);
//    }

}
