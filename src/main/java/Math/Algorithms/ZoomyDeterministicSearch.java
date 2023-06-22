// broken and you cant make me fix it





//package Math.Algorithms;
//
//import Math.Common.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static java.lang.Math.min;
//import static java.lang.Math.sqrt;
//
//public class ZoomyDeterministicSearch {
//
//
//    private Tree tree;
//    public static double thresholdForCompletion = 1;
//    private TreeNode goal;
//    private TreeNode goalButInList;
//
//    public List<Vector2D> allCorners;
//    public List<Obstacle> obstacles = new ArrayList<Obstacle>();
//
//
//    public List<TreeNode> graphSearchyThing(TreeNode start, TreeNode goal) {
//
//        this.goal = goal;
//        this.tree.root = start;
//        this.tree.root.cost = 0.0;
//        this.tree.treeNodes.add(start);
//        goalButInList = new TreeNode(goal.x, goal.y);
//        allCorners.add(new Vector2D(goal.x, goal.y));
//
////        for (int i = 0; i < Iterations; i++) {
//        for (int j = 0; j < allCorners.size(); j++) {
//            Vector2D v = allCorners.get(0);
//<<<<<<< HEAD:src/main/java/Math/Algorithms/ZoomyDeterministicSearch.java
//            TreeNode n = new TreeNode(v.x, v.y);
////            Node nearest = findNearestNode(n);
//            if (findDistance(n, goal) < thresholdForCompletion || findDistance(n,start) < .1) {
//=======
//            Node n = new Node(v.x, v.y);
//            Node nearest = findNearestNode(n);
//            if (findDistance(n, goal) < thresholdForCompletion) {
//                if (addNodeWithCollisionCheck(nearest, goalButInList)) {
//                    allCorners.remove(v);
////                    for (int i = 0; i < tree.nodes.size(); i++) {
////                        rewire(tree.nodes.get(i));
////                    }
//                    return (findPathToGoalFromTree());
//                }
////                    break;
//>>>>>>> 2bc17205019b8a547da2ac702e75d3141ff49786:src/main/java/Math/RRT/ZoomyDeterministicSearch.java
//            }
//            else if (fancyAddNodeWithCollisionCheck(n)) {
//                allCorners.remove(v);
//
//            }
//        }
////        Node nearest = findNearestNode(goalButInList);
//        fancyAddNodeWithCollisionCheck(goalButInList);
////        for (int i = 0; i < tree.nodes.size(); i++) {
////            rewire(tree.root);
////        for (Node n: tree.nodes) {
////            printPathSegment(n,n.parent);
////        }
////        System.out.println("--");
////        }
//
////            Node nearest = findNearestNode(random);
//
//        // must test adaptive step size, below is an optimization for no step size
////            Node delta = new Node(  .x - nearest.x, random.y - nearest.y);
////            delta.setMagnitude(Math.min(Step_Size, delta.getMagnitude()));
////            Node interpolated = new Node(nearest.x + delta.x, nearest.y + delta.y);
//
//
//        // otherwise do this:
////            if (findDistance(random, goal) < thresholdForCompletion) {
////                if (!addNodeWithCollisionCheck(nearest, goalButInList)) {
////                    continue;
////                }
////        System.out.println(hasGoalBeenReached());
//        return findPathToGoalFromTree();
////            } else {
////                // here
////                addNodeWithCollisionCheck(nearest, random);
////            }
//
////        }
////        if (hasGoalBeenReached()) {
//        // I have broken "safe exiting" maybe
////        }
////        return null;
//    }
//
//
//    private boolean hasCollision(TreeNode n1, TreeNode n2) {
//        for (Obstacle obstacle : obstacles) {
//            if (obstacle.hasCollided(n1, n2)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean addNodeWithCollisionCheck(TreeNode nearest, TreeNode interpolated) {
//
//        if (hasCollision(nearest, interpolated)) {
//            return false;
//        }
//        addNodeToTree(interpolated, nearest);
//<<<<<<< HEAD:src/main/java/Math/Algorithms/ZoomyDeterministicSearch.java
//        rewireSelf(interpolated);
//=======
////        rewire(interpolated);
//>>>>>>> 2bc17205019b8a547da2ac702e75d3141ff49786:src/main/java/Math/RRT/ZoomyDeterministicSearch.java
//        return true;
//    }
//
//    public boolean fancyAddNodeWithCollisionCheck(TreeNode needingParent){
//        if(needingParent == tree.root){
//            return true;
//        }
//        double minCost = 3000;
//        TreeNode newParent = null;
//        System.out.println(tree.treeNodes.size());
//        for (TreeNode n: tree.treeNodes) {
//            if(!hasCollision(n, needingParent)){
//                double newCost = findDistance(n,needingParent) + needingParent.cost;
//                if(newCost < minCost){
//                    newParent = n;
//                    minCost = newCost;
//                    System.out.println("here");
//                }
//            }
//        }
//        if(newParent != null){
//            addNodeToTree(needingParent,newParent);
//            return true;
//        }
//        return false;
//    }
//
//
//    private boolean hasGoalBeenReached() {
//        for (TreeNode treeNode : tree.treeNodes) {
//            if (findDistance(treeNode, goal) < thresholdForCompletion) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void recalculateCosts(TreeNode n1){
//        if(n1 != tree.root){
//            n1.cost = n1.parent.cost + findDistance(n1.parent,n1);
//        }
//        for (int i = n1.children.size() -1; i >= 0; i--) {
//            recalculateCosts(n1.children.get(i));
//        }
//    }
//    private void rewire(TreeNode n) {
//        rewireSelf(n);
//        for (int i = n.children.size() -1; i >= 0; i--) {
////            printPathSegment(n,n.children.get(i));
//            rewire(n.children.get(i));
//        }
//    }
//    private void rewireSelf(TreeNode n) {
//        for (int i = 0; i < tree.treeNodes.size(); i++) {
//            TreeNode e = tree.treeNodes.get(i);
//            if (e.cost + findDistance(n, e) < n.cost && !hasCollision(n, e)) {
//                tree.prune(n);
//                addNodeToTree(n, e);
//            }
//        }
//    }
//
//    private TreeNode findNearestNode(TreeNode n) {
//        double minDist = Double.POSITIVE_INFINITY;
//        TreeNode nearest = null;
//
//        for (TreeNode treeNode : tree.treeNodes) {
//            if (findDistance(treeNode, n) < minDist) {
//                nearest = treeNode;
//                minDist = findDistance(treeNode, n);
//            }
//        }
//        return nearest;
//    }
//
//    public static double findDistance(TreeNode n1, TreeNode n2) {
//        double x = n1.x - n2.x;
//        double y = n1.y - n2.y;
//        return sqrt(x * x + y * y);
//    }
//
//
//    private void addNodeToTree(TreeNode n, TreeNode nearestTreeNode) {
//        n.parent = nearestTreeNode;
//        n.parent.children.add(n);
//        n.cost = nearestTreeNode.cost + findDistance(n, nearestTreeNode);
//        tree.treeNodes.add(n);
//
//    }
//
//
//    private List<TreeNode> findPathToGoalFromTree() {
//        List<TreeNode> output = new ArrayList<TreeNode>();
//        TreeNode current = goalButInList;
//        while (current != null) {
//            output.add(current);
//            current = current.parent;
//        }
//        return output;
//    }
//
//
//    public static void printPath(List<TreeNode> fullPath) {
//        for (int j = 0; j < fullPath.size() - 1; j++) {
//            TreeNode current = fullPath.get(j);
//            TreeNode next = fullPath.get(j + 1);
//            System.out.println("\\operatorname{polygon}\\left(\\left(" + Math.floor(current.x * 1000) / 1000. + "," + Math.floor(current.y * 1000) / 1000 + "\\right),\\left(" + Math.floor(next.x * 1000) / 1000 + "," + Math.floor(next.y * 1000) / 1000 + "\\right)\\right)");
//        }
//    }
//    public static void printPathSegment(Node n1, Node n2) {
////        for (int j = 0; j < fullPath.size() - 1; j++) {
//        if(n1!= null && n2 !=null)
//        System.out.println("\\operatorname{polygon}\\left(\\left(" + Math.floor(n1.x * 1000) / 1000. + "," + Math.floor(n1.y * 1000) / 1000 + "\\right),\\left(" + Math.floor(n2.x * 1000) / 1000 + "," + Math.floor(n2.y * 1000) / 1000 + "\\right)\\right)");
//    }
//
//    public static void main(String[] args) {
//
//        Vector2D c1, c2, c3, c4;
//        c1 = new Vector2D(2.97, 1.55);
//        c2 = new Vector2D(4.88, 1.55);
//        c3 = new Vector2D(4.88, 3.95);
//        c4 = new Vector2D(2.97, 3.95);
//
//        ArrayList<Vector2D> obstacleArrayList = new ArrayList<Vector2D>();
//        obstacleArrayList.add(c1);
//        obstacleArrayList.add(c2);
//        obstacleArrayList.add(c3);
//        obstacleArrayList.add(c4);
//
//        ArrayList<Vector2D> obstacleArrayList2 = new ArrayList<Vector2D>();
//        c1 = new Vector2D(11.66, 3.95);
//        c2 = new Vector2D(13.5, 3.95);
//        c3 = new Vector2D(13.5, 1.55);
//        c4 = new Vector2D(11.66, 1.55);
//        obstacleArrayList2.add(c1);
//        obstacleArrayList2.add(c2);
//        obstacleArrayList2.add(c3);
//        obstacleArrayList2.add(c4);
//
//        ArrayList<Vector2D> obstacleArrayList3 = new ArrayList<Vector2D>();
//        c1 = new Vector2D(13.14, 5.445);
//        c2 = new Vector2D(16.5, 5.445);
//        obstacleArrayList3.add(c1);
//        obstacleArrayList3.add(c2);
//        ArrayList<Vector2D> obstacleArrayList4 = new ArrayList<Vector2D>();
//        c1 = new Vector2D(0, 5.445);
//        c2 = new Vector2D(3.33, 5.445);
//
//        obstacleArrayList4.add(c1);
//        obstacleArrayList4.add(c2);
//
//        List<TreeNode> fullPath = new ArrayList<TreeNode>();
//
//        ArrayList<Vector2D> allCorners = new ArrayList<>();
//
//
//        Obstacle[] obstacles = new Obstacle[4];
//        obstacles[0] = new Obstacle(obstacleArrayList);
//        obstacles[1] = new Obstacle(obstacleArrayList2);
//        obstacles[2] = new Obstacle(obstacleArrayList3);
//        obstacles[3] = new Obstacle(obstacleArrayList4);
//        for (Obstacle r : obstacles) {
//            for (int i = 0; i < r.corners.size(); i++) {
//                Vector2D delta = r.corners.get(i).subtract(r.center);
//                delta.setMagnitude(.02);
//                delta.add(r.corners.get(i));
//                allCorners.add(delta);
//            }
//        }
//
////        for (int i = 0; i < 1000000; i++) {
////        for (int i = 0; i < 1; i++) {
//            for (int i = 0; i < 100; i++) {
//
//
//                List<Vector2D> allCorners2 = new ArrayList<>(allCorners);
//            ZoomyDeterministicSearch rrt = new ZoomyDeterministicSearch();
//            rrt.tree = new Tree();
//            rrt.allCorners = allCorners2;
//            rrt.obstacles.add(obstacles[0]);
//            rrt.obstacles.add(obstacles[1]);
//            rrt.obstacles.add(obstacles[2]);
//            rrt.obstacles.add(obstacles[3]);
//<<<<<<< HEAD:src/main/java/Math/Algorithms/ZoomyDeterministicSearch.java
//            fullPath = rrt.graphSearchyThing(new TreeNode(Math.random() , 2 * Math.random() + 1.84), new TreeNode(14 + Math.random(), 1.84 + 2 * Math.random()));
////            System.out.println(rrt.tree.nodes.size());
//=======
//            fullPath = rrt.graphSearchyThing(new Node(Math.random() + 1, 2 * Math.random() + 1), new Node(14 + Math.random(), 1.84 + 2 * Math.random()));
//>>>>>>> 2bc17205019b8a547da2ac702e75d3141ff49786:src/main/java/Math/RRT/ZoomyDeterministicSearch.java
////            System.out.println(fullPath.size());
//        }
//        printPath(fullPath);
//    }
//
//
//}
