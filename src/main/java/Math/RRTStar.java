package Math;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RRTStar {
    private Tree tree;
    static int Iterations = 1500;
    static double Step_Size = .1;
    static double thresholdForCompletion = 1;
    static double neighborhood = .1;
    private Node goal;
    private double maxX;
    private double maxY;
    private Node goalButInList;

    public List<Node> rrtStar(Node start, Node goal) {
        this.goal = goal;
        this.tree.root = start;
        this.tree.root.cost = 0.0;
        this.tree.nodes.add(start);

        goalButInList = new Node(goal.x, goal.y);

        for (int i = 0; i < Iterations; i++) {
            Node random = findRandomNode();
            Node nearest = findNearestNode(random);
            Node delta = new Node(random.x - nearest.x, random.y - nearest.y);

            delta.setMagnitude(Math.min(Step_Size, delta.getMagnitude()));
            Node interpolated = new Node(nearest.x + delta.x, nearest.y + delta.y);


            if (findDistance(interpolated, goal) < thresholdForCompletion) {
                addNodeToTree(goalButInList, nearest);
//                System.out.println("\\operatorname{polygon}\\left(\\left(" + goal.x + "," + goal.y + "\\right),\\left(" + nearest.x + "," +nearest.y + "\\right)\\right)");
                break;
            } else {
                addNodeToTree(interpolated, nearest);
//                System.out.println("\\operatorname{polygon}\\left(\\left(" + interpolated.x + "," + interpolated.y + "\\right),\\left(" + nearest.x + "," +nearest.y + "\\right)\\right)");
            }

        }
        System.out.println(tree.root.parent);
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

    private boolean inNeighborhood(Node n1, Node n2) {
        return findDistance(n1, n2) < neighborhood;
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
        n.cost = nearestNode.cost + findDistance(n, nearestNode);
        tree.nodes.add(n);
    }


    double getGoalCost() {
        return goalButInList.cost;
    }

    private List<Node> findPathToGoalFromTree() {
        List<Node> output = new ArrayList<Node>();
        Node current = goalButInList;
        while (current != null) {
            output.add(current);
            current = current.parent;
        }
        return output;
    }

    private List<Node> findAllNodesInNeighborhood(Node n) {
        List<Node> output = new ArrayList<Node>();
        for (Node node : tree.nodes) {
            if (inNeighborhood(n, node)) {
                output.add(node);
            }
        }
        return output;
    }

    public static void main(String[] args) {

        RRTStar rrt = new RRTStar();
        rrt.tree = new Tree(0, 0);
        rrt.maxX = 10;
        rrt.maxY = 10;
        List<Node> fullPath = rrt.rrtStar(new Node(0, 0), new Node(0, 3));
        for (int j = 0; j < fullPath.size() - 1; j++) {
            Node current = fullPath.get(j);
            Node next = fullPath.get(j + 1);
            System.out.println("\\operatorname{polygon}\\left(\\left(" + current.x + "," + current.y + "\\right),\\left(" + next.x + "," + next.y + "\\right)\\right)");
        }


    }
}



