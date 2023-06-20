package Math.RRT;

import Math.Common.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;


public class InformedRRTStar {
    private Tree tree;
    public static int Iterations = 5000;
    public static double Step_Size = 1000;
    public static double thresholdForCompletion = 1;
    private Node goal;
    private double maxX;
    private double maxY;
    private Node goalButInList;

    private boolean hasSolution = false;
    public List<Vector2D> allCorners;
    public List<Obstacle> obstacles = new ArrayList<Obstacle>();


    public List<Node> rrtStar(Node start, Node goal) {

        this.goal = goal;
        this.tree.root = start;
        this.tree.root.cost = 0.0;
        this.tree.nodes.add(start);
        goalButInList = new Node(goal.x, goal.y);

        for (int i = 0; i < Iterations; i++) {
            int maxOffset = 0;
            for (int j = 0; j < allCorners.size() - maxOffset; j++) {
                Vector2D v = allCorners.get(j);
                Node n = new Node(v.x, v.y);
                Node nearest = findNearestNode(n);
                if (addNodeWithCollisionCheck(nearest, n)) {
                    allCorners.remove(v);
                }
                j--;
                maxOffset++;
            }
            Node random = findRandomNode();
            Node nearest = findNearestNode(random);
            Node delta = new Node(random.x - nearest.x, random.y - nearest.y);

            delta.setMagnitude(Math.min(Step_Size, delta.getMagnitude()));
            Node interpolated = new Node(nearest.x + delta.x, nearest.y + delta.y);


            if (findDistance(interpolated, goal) < thresholdForCompletion) {
                if (nearest.equals(goalButInList)) {
                    continue;
                }
                boolean unableToAddGoal = !addNodeWithCollisionCheck(nearest, goalButInList);
                if (unableToAddGoal) {
                    continue;
                }
                break;
            } else {
                addNodeWithCollisionCheck(nearest, interpolated);
            }

        }
        if (hasGoalBeenReached()) {
            return findPathToGoalFromTree();
        }
        return null;
    }


    private boolean hasCollision(Node n1, Node n2) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.hasCollided(n1, n2)) {
                return true;
            }
        }
        return false;
    }

    public boolean addNodeWithCollisionCheck(Node nearest, Node interpolated) {
        if (hasSolution && findDistance(interpolated, goalButInList) + nearest.cost + findDistance(interpolated, nearest) > goalButInList.cost) {
            return false;
        }
        if (hasCollision(nearest, interpolated)) {
            return false;
        }


        addNodeToTree(interpolated, nearest);
        rewire(interpolated);
        return true;
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
        double x, y;
        x = random.nextDouble() * maxX;
        y = random.nextDouble() * maxY;
        return new Node(x, y);
    }

    static double pythag(double x, double y) {
        return sqrt(x * x + y * y);
    }

    private void rewire(Node n) {
        for (int i = 0; i < tree.nodes.size(); i++) {
            Node e = tree.nodes.get(i);
            if (e.cost + findDistance(n, e) < n.cost && !hasCollision(n, e)) {
                tree.prune(n);
                addNodeToTree(n, e);
            }
        }


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
        n.parent = nearestNode;
        n.cost = nearestNode.cost + findDistance(n, nearestNode);
        tree.nodes.add(n);

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


    public static void printPath(List<Node> fullPath) {
        for (int j = 0; j < fullPath.size() - 1; j++) {
            Node current = fullPath.get(j);
            Node next = fullPath.get(j + 1);
            System.out.println("\\operatorname{polygon}\\left(\\left(" + Math.floor(current.x * 1000) / 1000. + "," + Math.floor(current.y * 1000) / 1000 + "\\right),\\left(" + Math.floor(next.x * 1000) / 1000 + "," + Math.floor(next.y * 1000) / 1000 + "\\right)\\right)");
        }
    }

    public static void main(String[] args) {
        Vector2D shift = new Vector2D(8.276, 3.914);

        Vector2D c1, c2, c3, c4;
        c1 = new Vector2D(-5.33, -0.0).add(shift.clone());
        c2 = new Vector2D(-3.29, -0.0).add(shift.clone());
        c3 = new Vector2D(-3.28, -2.48).add(shift.clone());
        c4 = new Vector2D(-5.34, -2.48).add(shift.clone());

        ArrayList<Vector2D> obstacleArrayList = new ArrayList<Vector2D>();
        obstacleArrayList.add(c1);
        obstacleArrayList.add(c2);
        obstacleArrayList.add(c3);
        obstacleArrayList.add(c4);

        ArrayList<Vector2D> obstacleArrayList2 = new ArrayList<Vector2D>();
        c1 = new Vector2D(5.33, -0.0).add(shift.clone());
        c2 = new Vector2D(3.29, -0.0).add(shift.clone());
        c3 = new Vector2D(3.28, -2.48).add(shift.clone());
        c4 = new Vector2D(5.34, -2.48).add(shift.clone());
        obstacleArrayList2.add(c1);
        obstacleArrayList2.add(c2);
        obstacleArrayList2.add(c3);
        obstacleArrayList2.add(c4);

        ArrayList<Vector2D> obstacleArrayList3 = new ArrayList<Vector2D>();
        c1 = new Vector2D(-12, 1.4).add(shift.clone());
        c2 = new Vector2D(-4.93, 1.4).add(shift.clone());
        c3 = new Vector2D(-12, 1.42).add(shift.clone());
        c4 = new Vector2D(-4.93, 1.42).add(shift.clone());
        obstacleArrayList3.add(c1);
        obstacleArrayList3.add(c2);
        obstacleArrayList3.add(c4);
        obstacleArrayList3.add(c3);

        ArrayList<Vector2D> obstacleArrayList4 = new ArrayList<Vector2D>();
        c1 = new Vector2D(12, 1.4).add(shift.clone());
        c2 = new Vector2D(4.93, 1.4).add(shift.clone());
        c3 = new Vector2D(4.93, 1.42).add(shift.clone());
        c4 = new Vector2D(12, 1.42).add(shift.clone());
        obstacleArrayList4.add(c1);
        obstacleArrayList4.add(c2);
        obstacleArrayList4.add(c3);
        obstacleArrayList4.add(c4);
        List<Node> fullPath = new ArrayList<Node>();

        ArrayList<Vector2D> allCorners = new ArrayList<>();


        Obstacle[] obstacles = new Obstacle[4];
        obstacles[0] = new Obstacle(obstacleArrayList);
        obstacles[1] = new Obstacle(obstacleArrayList2);
        obstacles[2] = new Obstacle(obstacleArrayList3);
        obstacles[3] = new Obstacle(obstacleArrayList4);
        for (Obstacle r : obstacles) {
            for (int i = 0; i < 4; i++) {
                Vector2D delta = r.corners.get(i).subtract(r.center);
                delta.setMagnitude(.02);
                delta.add(r.corners.get(i));
                allCorners.add(delta);
            }
        }

        for (int i = 0; i < 100000; i++) {
//        for (int i = 0; i < 1; i++) {

            List<Vector2D> allCorners2 = new ArrayList<>(allCorners);
            InformedRRTStar rrt = new InformedRRTStar();
            rrt.tree = new Tree();
            rrt.maxX = 16.5;
            rrt.maxY = 8;
            rrt.allCorners = allCorners2;
            rrt.obstacles.add(obstacles[0]);
            rrt.obstacles.add(obstacles[1]);
            rrt.obstacles.add(obstacles[2]);
            rrt.obstacles.add(obstacles[3]);
            fullPath = rrt.rrtStar(new Node(Math.random() + 1, Math.random() + 1), new Node(14 + Math.random(), 1.84 + Math.random() / 2.0));
//            System.out.println(rrt.tree.nodes.size());
        }
        printPath(fullPath);
    }
}



