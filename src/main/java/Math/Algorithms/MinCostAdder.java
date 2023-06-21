package Math.Algorithms;

import Math.Common.TreeNode;
import Math.Common.Obstacle;
import Math.Common.Tree;
import Math.Common.Vector2D;

import java.util.ArrayList;
import java.util.List;

import static Math.Algorithms.ZoomyDeterministicSearch.findDistance;
import static Math.Algorithms.ZoomyDeterministicSearch.printPath;

public class MinCostAdder {
    Tree tree;
    Obstacle[] obstacles;
    TreeNode goal;
    List<TreeNode> extrema;
    List<TreeNode> queue = new ArrayList<>();

    public MinCostAdder(Obstacle[] obstacles) {
        this.obstacles = obstacles;
        this.tree = new Tree();
    }

    public List<TreeNode> go(TreeNode start, TreeNode goal, List<TreeNode> extrema) {
        tree.root = new TreeNode(start.x, start.y);
        tree.root.parent = null;
        tree.root.cost = 0;
        tree.treeNodes.add(tree.root);
        this.extrema = extrema;
        this.goal = goal;
        for (int i = extrema.size() - 1; i >= 0; i--) {
            if (!addNode(extrema.get(i))) {
                queue.add(extrema.get(i));
            }
            extrema.remove(i);
        }
        for (TreeNode n: tree.treeNodes) {
            rewireSelf(n);
        }
        while (queue.size() > 0) {
            for (int i = queue.size() - 1; i >= 0; i--) {
                if (addNode(queue.get(i))) {
                    queue.remove(queue.get(i));
                }
            }
            for (TreeNode n: tree.treeNodes) {
                rewireSelf(n);
            }
        }
        addNode(goal);


        return findPathToGoalFromTree();
    }

    private void rewire(TreeNode n) {
        rewireSelf(n);
        if (n.children.size() > 0) {
            for (int i = n.children.size() - 1; i >= 0; i--) {
//            printPathSegment(n,n.children.get(i));
                rewire(n.children.get(i));
            }
        }
    }

    private void rewireSelf(TreeNode n) {
        for (int i = 0; i < tree.treeNodes.size(); i++) {
            TreeNode e = tree.treeNodes.get(i);
            if (e.cost + findDistance(n, e) < n.cost && !hasCollision(n, e)) {
                n.parent = e;
                n.cost = e.cost + findDistance(n, e);
            }
        }
    }

    private List<TreeNode> findPathToGoalFromTree() {
        List<TreeNode> output = new ArrayList<TreeNode>();
        TreeNode current = goal;
        while (current != null) {
            output.add(current);
            current = current.parent;
        }
        return output;
    }

    public boolean addNode(TreeNode n1) {
        double minCost = 3000;
        TreeNode parent = null;
        for (TreeNode n : tree.treeNodes) {

            if (!hasCollision(n, n1)) {
                double cost = n.cost + findDistance(n, n1);
                if (cost < minCost) {
                    parent = n;
                    minCost = cost;
                }
            }
        }
        if (parent != null) {
            tree.treeNodes.add(n1);
            n1.cost = minCost;
            n1.parent = parent;
            n1.parent.children.add(n1);
            return true;
        }
        return false;
    }

    private boolean hasCollision(TreeNode n1, TreeNode n2) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.hasCollided(n1, n2)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Vector2D c1, c2, c3, c4;
        c1 = new Vector2D(2.97, 1.55);
        c2 = new Vector2D(4.88, 1.55);
        c3 = new Vector2D(4.88, 3.95);
        c4 = new Vector2D(2.97, 3.95);

        ArrayList<Vector2D> obstacleArrayList = new ArrayList<Vector2D>();
        obstacleArrayList.add(c1);
        obstacleArrayList.add(c2);
        obstacleArrayList.add(c3);
        obstacleArrayList.add(c4);

        ArrayList<Vector2D> obstacleArrayList2 = new ArrayList<Vector2D>();
        c1 = new Vector2D(11.66, 3.95);
        c2 = new Vector2D(13.5, 3.95);
        c3 = new Vector2D(13.5, 1.55);
        c4 = new Vector2D(11.66, 1.55);
        obstacleArrayList2.add(c1);
        obstacleArrayList2.add(c2);
        obstacleArrayList2.add(c3);
        obstacleArrayList2.add(c4);

        ArrayList<Vector2D> obstacleArrayList3 = new ArrayList<Vector2D>();
        c1 = new Vector2D(13.14, 5.445);
        c2 = new Vector2D(16.5, 5.445);
        obstacleArrayList3.add(c1);
        obstacleArrayList3.add(c2);
        ArrayList<Vector2D> obstacleArrayList4 = new ArrayList<Vector2D>();
        c1 = new Vector2D(0, 5.445);
        c2 = new Vector2D(3.33, 5.445);

        obstacleArrayList4.add(c1);
        obstacleArrayList4.add(c2);

        List<TreeNode> fullPath = new ArrayList<TreeNode>();

        ArrayList<TreeNode> allCorners = new ArrayList<>();


        Obstacle[] obstacles = new Obstacle[4];
        obstacles[0] = new Obstacle(obstacleArrayList);
        obstacles[1] = new Obstacle(obstacleArrayList2);
        obstacles[2] = new Obstacle(obstacleArrayList3);
        obstacles[3] = new Obstacle(obstacleArrayList4);
        for (Obstacle r : obstacles) {
            for (int i = 0; i < r.corners.size(); i++) {
                Vector2D delta = r.corners.get(i).subtract(r.center);
                delta.setMagnitude(.02);
                delta.add(r.corners.get(i));
                allCorners.add(new TreeNode(delta.x, delta.y));
            }
        }

        for (int i = 0; i < 1000000; i++) {
//        for (int i = 0; i < 0001; i++) {
//        for (int i = 0; i < 100; i++) {


            List<TreeNode> allCorners2 = new ArrayList<>(allCorners);
            MinCostAdder rrt = new MinCostAdder(new Obstacle[]{new Obstacle(obstacleArrayList), new Obstacle(obstacleArrayList2), new Obstacle(obstacleArrayList3), new Obstacle(obstacleArrayList4)});
//            rrt.obstacles.add(obstacles[0]);
//            rrt.obstacles.add(obstacles[1]);
//            rrt.obstacles.add(obstacles[2]);
//            rrt.obstacles.add(obstacles[3]);
            fullPath = rrt.go(new TreeNode(Math.random(), 2 * Math.random() + 1.84), new TreeNode(14 + Math.random(), 1.84 + 2 * Math.random()), allCorners2);
//            System.out.println(rrt.tree.nodes.size());
//            System.out.println(fullPath.size());
        }
        printPath(fullPath);
    }
}
