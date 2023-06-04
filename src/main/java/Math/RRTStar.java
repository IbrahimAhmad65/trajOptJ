package Math;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RRTStar {
    private Tree tree;
    static int Iterations = 5000;
    static double Step_Size = .1;
    static double thresholdForCompletion = .5;
    static double neighborhood = 1;
    private Node goal;
    private double maxX;
    private double maxY;
    private Node goalButInList;
    boolean test = false;
    private double cMin;
    private double width;
    private double length;
    FileWriter writer;
    private double angleToSolution;
    private boolean hasSolution = false;

    public List<Node> rrtStar(Node start, Node goal) {

        cMin = findDistance(start,goal);
        angleToSolution = Math.atan2(start.y - goal.y, start.x - goal.x);
        if (test) {
            try {
                writer = new FileWriter("/home/ibrahim/rrt.txt");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
                if(!nearest.equals(goalButInList)){
                    addNodeToTree(goalButInList, nearest);
                    rewire(goalButInList);
                    width = Math.sqrt(goalButInList.cost * goalButInList.cost - cMin*cMin);
                    length = goalButInList.cost;
                }
                if (!hasSolution) {
                    hasSolution = true;
                    printPath(findPathToGoalFromTree());

                }
            } else {
                addNodeToTree(interpolated, nearest);
                rewire(interpolated);
            }
//            System.out.println("(" + (i / 1000.) + "," + goalButInList.cost + ")");

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
        double x;
        double y;
        if (hasSolution) {
//            length = 1;
//            width = .5;
            x = random.nextDouble() * length;
            y = random.nextDouble() * width - width/2.0;
            double theta = Math.atan2(y,x);
            theta -= angleToSolution;
            double mag = Math.sqrt(x*x + y*y);
            x = mag * Math.cos(theta) + tree.root.x;
            y = mag * Math.sin(theta) + tree.root.y;

//            System.out.println(width + " " + length);
//            x = random.nextDouble() * maxX;
//            y = random.nextDouble() * maxY;
        } else {
            x = random.nextDouble() * maxX;
            y = random.nextDouble() * maxY;
        }
        return new Node(x, y);
    }

    private void rewire(Node n) {
        List<Node> nodesInNeighborhood = findAllNodesInNeighborhood(n);

        for (Node e : nodesInNeighborhood) {
            if (e.cost + findDistance(n, e) < n.cost) {
                n.parent.neighbors.remove(n);
                addNodeToTree(n, e);
            }
        }
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


//        System.out.println("\\operatorname{polygon}\\left(\\left(" + n.x + "," + n.y + "\\right),\\left(" + nearestNode.x + "," + nearestNode.y + "\\right)\\right)");
//        if(nearestNode.equals(goalButInList)){
//            return;
//        }

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
            System.out.println(current);
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


    private void writeFullTree() {
        if (test) {
            for (Node n : tree.nodes) {
                try {
                    writer.write(n.x + "," + n.y + "," + n.cost + ",\n");
                    writer.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {

        RRTStar rrt = new RRTStar();
        rrt.tree = new Tree(0, 0);
        rrt.maxX = 10;
        rrt.maxY = 10;
        List<Node> fullPath = rrt.rrtStar(new Node(0, 0), new Node(0, 3));


        rrt.writeFullTree();


        printPath(fullPath);


    }

    public static void printPath(List<Node> fullPath) {
        for (int j = 0; j < fullPath.size() - 1; j++) {
            Node current = fullPath.get(j);
            Node next = fullPath.get(j + 1);
            System.out.println("\\operatorname{polygon}\\left(\\left(" + current.x + "," + current.y + "\\right),\\left(" + next.x + "," + next.y + "\\right)\\right)");
        }
    }
}



