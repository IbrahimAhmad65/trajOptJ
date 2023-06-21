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
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}