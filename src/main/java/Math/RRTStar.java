package Math;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RRTStar {
    private Tree tree;
    static int Iterations = 5000;
    static double Step_Size = .1;
    static double thresholdForCompletion = .2;
    static double neighborhood = 1;
    private Node goal;
    private double maxX;
    private double maxY;
    private Node goalButInList;
    boolean test = false;
    FileWriter writer;
    List<Node> oldPathPreMainRewire;

    public List<Node> rrtStar(Node start, Node goal) {
        if(test){
            try {
                writer = new FileWriter("/home/ibrahim/rrt.txt");
            } catch (Exception e){
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
                addNodeToTree(goalButInList, nearest);
                rewire(goalButInList);
                if(oldPathPreMainRewire == null){
                    System.out.println(goalButInList.cost);
                    oldPathPreMainRewire = findPathToGoalFromTree();
                } else {
                    System.out.println("hey");
                }
            } else {
                addNodeToTree(interpolated, nearest);
                rewire(interpolated);
            }
//            System.out.println("(" +(i/1000.) + "," + goalButInList.cost + ")");

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

    private void rewire(Node n){
        List<Node> nodesInNeighborhood = findAllNodesInNeighborhood(n);

        for (Node e: nodesInNeighborhood) {
            if(e.cost + findDistance(n,e) < n.cost){
//                System.out.println("reducing costs: new cost: " + (e.cost + findDistance(n,e)) + " old cost: " + n.cost);
                n.parent.neighbors.remove(n);
                addNodeToTree(n,e);
//                System.out.println("cost of n: " + n.cost);
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


    private void writeFullTree(){
        if(test){
            for (Node n: tree.nodes) {
                try {
                    writer.write(n.x + "," + n.y + "," + n.cost+  ",\n");
                    writer.flush();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {

        RRTStar rrt = new RRTStar();
        rrt.tree = new Tree(0, 0);
        rrt.maxX = 3;
        rrt.maxY = 3;
        List<Node> fullPath = rrt.rrtStar(new Node(0, 0), new Node(0, 3));
        System.out.println("path with rewire cost" + fullPath.get(0).cost);
        System.out.println("path without main rewire cost" + rrt.oldPathPreMainRewire.get(0).cost);
        System.out.println("delta: " +( fullPath.get(0).cost - rrt.oldPathPreMainRewire.get(0).cost));

        rrt.writeFullTree();



//        for (int j = 0; j < fullPath.size() - 1; j++) {
//            Node current = fullPath.get(j);
//            Node next = fullPath.get(j + 1);
//            System.out.println("\\operatorname{polygon}\\left(\\left(" + current.x + "," + current.y + "\\right),\\left(" + next.x + "," + next.y + "\\right)\\right)");
//        }


    }
}



