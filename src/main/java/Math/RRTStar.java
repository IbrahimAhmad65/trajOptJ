package Math;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RRTStar {
    private Tree tree;
    static int Iterations = 100;
    static double Step_Size = .1;
    static double thresholdForCompletion = .4;
    private Node goal;
    private double maxX;
    private double maxY;

    public List<Node> rrtStar(Node start, Node goal) {
        this.goal = goal;
        this.tree.root = start;
        this.tree.root.cost = 0.0;
        this.tree.nodes.add(start);
        addNodeToTree(findNearestNode(tree.root), tree.root);
        System.out.println(goal);

        for (int i = 0; i < Iterations; i++) {
//            System.out.println("looping");
            Node random = findRandomNode();
            Node nearest = findNearestNode(random);
            Node delta = new Node(random.x - nearest.x, random.y - nearest.y);
            delta.setMagnitude(Step_Size);
            Node interpolated = new Node(nearest.x + delta.x, nearest.y + delta.y);

            if(findDistance(interpolated, goal) < thresholdForCompletion){
                addNodeToTree(goal, nearest);
//                System.out.println("\\operatorname{polygon}\\left(\\left(" + goal.x + "," + goal.y + "\\right),\\left(" + nearest.x + "," +nearest.y + "\\right)\\right)");
            } else {
                addNodeToTree(interpolated, nearest);
//                System.out.println("\\operatorname{polygon}\\left(\\left(" + interpolated.x + "," + interpolated.y + "\\right),\\left(" + nearest.x + "," +nearest.y + "\\right)\\right)");
            }

            //\operatorname{polygon}\left(\left(1,1\right),\left(2,2\right)\right)
        }
        if (hasGoalBeenReached()) {
            return findPathToGoalFromTree();
        }
        return null;
    }


    private boolean hasGoalBeenReached() {
        for (Node node : tree.nodes) {
            if (findDistance(node, goal) < thresholdForCompletion) {
                return true;
            }
        }
        return false;
    }

    private Node findRandomNode() {
        Random random = new Random();
        double x = random.nextDouble() * maxX;
        double y = random.nextDouble() * maxY;
        return new Node(x, y);
    }

    private Node findNearestNode(Node n) {
        double minDist = Double.POSITIVE_INFINITY;
        Node nearest = null;
        for (Node node : tree.nodes) {
            if (findDistance(node, n) < minDist) {
                nearest = node;
                minDist = findDistance(node, n);
            }
        }
        return nearest;
    }

    private double findDistance(Node n1, Node n2) {
        return Math.sqrt(Math.pow(n1.x - n2.x, 2) + Math.pow(n1.y - n2.y, 2));
    }

    private void addNodeToTree(Node n, Node nearestNode) {
        nearestNode.neighbors.add(n);
        n.parent = nearestNode;
        tree.nodes.add(n);
    }

    private List<Node> findPathToGoalFromTree(){
        List<Node> output = new ArrayList<Node>();
        Node current = goal;
        while(current != null){
            output.add(current);
            current = current.parent;
        }
        return output;
    }

    public static void main(String[] args) {
        RRTStar rrt = new RRTStar();
        rrt.tree = new Tree(0, 0);
        rrt.maxX = 10;
        rrt.maxY = 10;
        List<Node> fullPath = rrt.rrtStar(new Node(0, 0), new Node(3, 3));
        System.out.println(new Node(-1,-1));
        for (int i = 0; i < fullPath.size() -1; i++) {
            Node current = fullPath.get(i);
            Node next = fullPath.get(i + 1);
            System.out.println("\\operatorname{polygon}\\left(\\left(" + current.x + "," + current.y + "\\right),\\left(" + next.x + "," +next.y + "\\right)\\right)");

        }
    }
}
