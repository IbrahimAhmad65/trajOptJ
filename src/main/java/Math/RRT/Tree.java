package Math.RRT;

import java.util.ArrayList;

public class Tree {
    Node root;
    ArrayList<Node> nodes = new ArrayList<>();
    int prunedNum = 0;
    public Tree() {
    }

    public void prune(Node n){
        nodes.remove(n);
        prunedNum ++;
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (node.parent == n) {
                prune(node);
//                i++;
            }
        }
    }

}
