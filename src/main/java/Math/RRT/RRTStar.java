package Math.RRT;

import Math.Common.Vector2D;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.spline.CubicHermiteSpline;
import edu.wpi.first.math.spline.Spline;
import edu.wpi.first.math.spline.SplineHelper;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;


public class RRTStar {
    private Tree tree;
    public static int Iterations = 5000;
    public static double Step_Size = .5;
    public static double thresholdForCompletion = 1;
    public static double neighborhood = 9;
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
                boolean unableToAddGoal = !addNodeWithCollisionCheck(nearest, goalButInList);
                if (unableToAddGoal) {
                    continue;
                }

                if (!hasSolution) {
                    hasSolution = true;
//                    printPath(findPathToGoalFromTree());

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
        double x;
        double y;
        if (hasSolution) {
            double r = sqrt(random.nextDouble());
            double rho = random.nextDouble() * 2 * Math.PI;

            x = (r * cos(rho) + 1) * length / 2.0 - (length - cMin) / 2.0;
            y = r * sin(rho) * width / 2.0;
            double theta = Math.atan2(y, x);
            theta -= angleToSolution;
            double mag = sqrt(x * x + y * y);
            x = mag * cos(theta) + tree.root.x;
            y = mag * sin(theta) + tree.root.y;
//            System.out.println("x: " + x + " y: " + y + " theta: " + theta + " mag: " + mag + " angleToSolution: " + angleToSolution + " length: " + length + " width: " + width + " cMin: " + cMin + "goal but in list: " + goalButInList.cost);
        } else {
            x = random.nextDouble() * maxX;
            y = random.nextDouble() * maxY;
        }
        return new Node(x, y);
    }

    private void rewire(Node n) {
        List<Node> nodesInNeighborhood = findAllNodesInNeighborhood(n);

        for (Node e : nodesInNeighborhood) {
            if (e.cost + findDistance(n, e) < n.cost && !hasCollision(n, e)) {
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
        c2 = new Vector2D(-4.93, 1.413).add(shift.clone());
        obstacleArrayList3.add(c1);
        obstacleArrayList3.add(c2);

        ArrayList<Vector2D> obstacleArrayList4 = new ArrayList<Vector2D>();
        c1 = new Vector2D(12, 1.4).add(shift.clone());
        c2 = new Vector2D(4.93, 1.413).add(shift.clone());
        obstacleArrayList4.add(c1);
        obstacleArrayList4.add(c2);
        List<Node> fullPath = new ArrayList<Node>();

        CubicHermiteSpline[] arr = null;
        for (int i = 0; i < 1000; i++) {

            RRTStar rrt = new RRTStar();
            rrt.tree = new Tree();
            rrt.maxX = 16.5;
            rrt.maxY = 8;
            rrt.obstacles.add(new Obstacle(obstacleArrayList));
            rrt.obstacles.add(new Obstacle(obstacleArrayList2));
            rrt.obstacles.add(new Obstacle(obstacleArrayList3));
            rrt.obstacles.add(new Obstacle(obstacleArrayList4));


            fullPath = rrt.rrtStar(new Node(2.2, 2.8), new Node(15.59, 5.84));

//        Pose2d start = new Pose2d(fullPath.get(0).toTranslation2d(), Rotation2d.fromRadians(atan2(fullPath.get(1).y - fullPath.get(0).y, fullPath.get(1).x - fullPath.get(0).x)));
//        Pose2d end = new Pose2d(fullPath.get(fullPath.size() -1).toTranslation2d(), Rotation2d.fromRadians(atan2(fullPath.get(fullPath.size() - 1).y - fullPath.get(fullPath.size() - 2).y, fullPath.get(fullPath.size() - 1).x - fullPath.get(fullPath.size() - 2).x)));
            Translation2d[] interiorWaypoints = new Translation2d[fullPath.size() - 2];
            for (int j = 0; j < interiorWaypoints.length; j++) {
                interiorWaypoints[j] = fullPath.get(j + 1).toTranslation2d();
            }
            double[] array1 = new double[]{fullPath.get(0).x, fullPath.get(1).x - fullPath.get(0).x};
            double[] array2 = new double[]{fullPath.get(0).y, fullPath.get(1).y - fullPath.get(0).y};

            Spline.ControlVector start = new Spline.ControlVector(array1, array2);

            array1 = new double[]{fullPath.get(fullPath.size() - 1).x, fullPath.get(fullPath.size() - 1).x - fullPath.get(fullPath.size() - 2).x};
            array2 = new double[]{fullPath.get(fullPath.size() - 1).y, fullPath.get(fullPath.size() - 1).y - fullPath.get(fullPath.size() - 2).y};

            Spline.ControlVector end = new Spline.ControlVector(array1, array2);
            arr = SplineHelper.getCubicSplinesFromControlVectors(start, interiorWaypoints, end);
        }


        for (CubicHermiteSpline spline : arr) {
            for (int i = 0; i < 1000; i++) {
                System.out.println(new Vector2D( spline.getPoint(i / 1000.0).poseMeters));
            }
        }
//
//        printPath(fullPath);
    }
}



