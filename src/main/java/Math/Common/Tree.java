package Math.Common;

import java.util.ArrayList;

public class Tree {
    public TreeNode root;
    public ArrayList<TreeNode> treeNodes = new ArrayList<>();
//    int prunedNum = 0;
    public Tree() {
    }

    public void prune(TreeNode n){
        treeNodes.remove(n);
        n.parent.children.remove(n);
//        prunedNum ++;
//        for (int i = 0; i < nodes.size(); i++) {
//            Node node = nodes.get(i);
//            if (node.parent == n) {
//                prune(node);
////                i++;
//            }
//        }
    }

}
