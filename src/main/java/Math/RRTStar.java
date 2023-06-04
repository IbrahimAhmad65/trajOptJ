package Math;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;


public class RRTStar {
    private Tree tree;
    public static int Iterations = 5000;
    public static double Step_Size = .1;
    public static double thresholdForCompletion = .5;
    public static double neighborhood = 1;
    private Node goal;
    private double maxX;
    private double maxY;
    private Node goalButInList;
    public boolean test = false;
    private double cMin;
    protected int itr;
    private double width;
    private double length;
    public static double minWidthCutoffPoint = .001;
    FileWriter writer;
    private double angleToSolution;
    private boolean hasSolution = false;
    public List<Obstacle> obstacles = new ArrayList<Obstacle>();

    public List<Node> rrtStar(Node start, Node goal) {

        cMin = findDistance(start, goal);
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
                if (nearest.equals(goalButInList)) {
                    continue;
                }
                addNodeWithCollisionCheck(nearest, goalButInList);


                if (!hasSolution) {
                    hasSolution = true;
                    printPath(findPathToGoalFromTree());

                }
                double arg = goalButInList.cost * goalButInList.cost - cMin * cMin;
                if (arg >= 0.0) {
                    width = sqrt(arg);
                } else {
                    width = 0.0;
                    break;
                }

                length = goalButInList.cost;
            } else {
                addNodeWithCollisionCheck(nearest, interpolated);
            }

        }
        if (hasGoalBeenReached()) {
            return findPathToGoalFromTree();
        }
        return null;
    }

    public void addNodeWithCollisionCheck(Node nearest, Node interpolated) {
        boolean mustBreak = false;
        for (Obstacle obstacle : obstacles) {
            if (obstacle.hasCollided(interpolated, nearest)) {
                mustBreak = true;
                break;
            }
        }

        if (mustBreak) {
            System.out.println("node that has collided");
            System.out.println("nearest: " + nearest);
            System.out.println("interpolated: " + interpolated);
            return;
        }
        System.out.println("node that hasnt collided");
        addNodeToTree(interpolated, nearest);
        rewire(interpolated);
    }


    private boolean hasGoalBeenReached() {
        for (Node node : tree.nodes) {
            if (findDistance(node, goal) < thresholdForCompletion) {
                return true;
            }
        }
        return false;
    }

//    private boolean hasObstacle(Node n1, Node n2){
//
//    }

    private Node findRandomNode() {
        Random random = new Random();
        double x;
        double y;
        if (hasSolution) {
            double r = sqrt(random.nextDouble());
            double rho = random.nextDouble() * 2 * Math.PI;

            x = (r * cos(rho) + 1) * length / 2.0 - (length - cMin) / 2.0;
            y = r * sin(rho) * width / 2.0;

//            x = random.nextDouble() * length - (length - cMin) / 2.0;
//            y = random.nextDouble() * width - width / 2.0;
//
//            double elipseCheck = (x-length/2)*(x-length/2)/ (length*length/4) + (y)*(y)/ (width*width/4);
//            if(elipseCheck > 1){
//                return findRandomNode();
//            }

            double theta = Math.atan2(y, x);
            theta -= angleToSolution;
            double mag = sqrt(x * x + y * y);
            x = mag * cos(theta) + tree.root.x;
            y = mag * sin(theta) + tree.root.y;


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

    public static double findDistance(Node n1, Node n2) {
        return sqrt(Math.pow(n1.x - n2.x, 2) + Math.pow(n1.y - n2.y, 2));
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
        Vector2D c1,c2,c3,c4;
        c1 = new Vector2D(-1,1);
        c2 = new Vector2D(1,1);
        c3 = new Vector2D(1,2);
        c4 = new Vector2D(-1,2);

        ArrayList<Vector2D> obstacleArrayList = new ArrayList<Vector2D>();
        obstacleArrayList.add(c1);
        obstacleArrayList.add(c2);
        obstacleArrayList.add(c3);
        obstacleArrayList.add(c4);

        for (int i = 0; i < 1; i++) {
            RRTStar rrt = new RRTStar();
            rrt.tree = new Tree();
            rrt.maxX = 10;
            rrt.maxY = 10;
            rrt.obstacles.add(new Obstacle(obstacleArrayList));
            List<Node> fullPath = rrt.rrtStar(new Node(0, 0), new Node(0, 3));
        printPath(fullPath);
        }
//        System.out.println(total/1000.);

//        rrt.writeFullTree();


//        }

    }


    // 6:09 with elipse recursion
    // 6:02 without

    public static void printPath(List<Node> fullPath) {
        for (int j = 0; j < fullPath.size() - 1; j++) {
            Node current = fullPath.get(j);
            Node next = fullPath.get(j + 1);
            System.out.println("\\operatorname{polygon}\\left(\\left(" + Math.floor(current.x*1000)/1000. + "," + Math.floor(current.y*1000)/1000 + "\\right),\\left(" + Math.floor(next.x*1000)/1000 + "," + Math.floor(next.y*1000)/1000+ "\\right)\\right)");
        }
    }
}



