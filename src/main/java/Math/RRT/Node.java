package Math.RRT;

import Math.Common.Vector2D;
import edu.wpi.first.math.geometry.Translation2d;

import java.util.ArrayList;
import java.util.List;

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

    // only means something when using nodes as vectors

    void setMagnitude(double magnitude){
        double angle = Math.atan2(y, x);
        this.x = magnitude * Math.cos(angle);
        this.y = magnitude * Math.sin(angle);
    }

    double getMagnitude(){
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Double.compare(node.x, x) == 0 && Double.compare(node.y, y) == 0;
    }

    public Vector2D toVector2D(){
        return new Vector2D(x, y);
    }

    public Translation2d toTranslation2d(){
        return new Translation2d(x, y);
    }
}