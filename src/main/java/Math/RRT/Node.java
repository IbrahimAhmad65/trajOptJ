package Math.RRT;

// Could be completely replace with Unsafe mwahaha
public class Node {
    double x;
    double y;
    double cost;
    Node parent;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
        this.cost = Double.POSITIVE_INFINITY;
        this.parent = null;
    }
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}