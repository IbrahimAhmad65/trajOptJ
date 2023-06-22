package Math.Common;

import java.util.ArrayList;

// Could be completely replace with Unsafe mwahaha
public class TreeNode extends Node{
    public double cost;
    public TreeNode parent;
    public ArrayList<TreeNode> children;

    public TreeNode(double x, double y) {
        this.x = x;
        this.y = y;
        this.cost = Double.POSITIVE_INFINITY;
        this.parent = null;
        children = new ArrayList<>();
    }

    public void setMagnitude(double magnitude){
        double angle = Math.atan2(y, x);
        this.x = magnitude * Math.cos(angle);
        this.y = magnitude * Math.sin(angle);
    }

    public double getMagnitude(){
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}