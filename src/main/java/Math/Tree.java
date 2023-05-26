package Math;

import java.util.ArrayList;

public class Tree {
    Node root;
    ArrayList<Node> nodes = new ArrayList<>();
    public Tree(double x, double y) {
        this.root = new Node(x, y);
        this.root.cost = 0.0;
    }
}
