package Math.Common;

import java.util.ArrayList;
import java.util.List;

import static Math.Algorithms.JankRRTStar.findDistance;
import static java.lang.Math.*;

public class Obstacle {

   public List<Vector2D> corners;
    double THRESHOLD = .01;
    private final TreeNode cachedTreeNode;
    public Vector2D center;
    //    Vector2D cache1 = new Vector2D();
//    Vector2D cache2 = new Vector2D();
//    Vector2D cache3 = new Vector2D();
//    double d1X = 0;
//    double d1Y = 0;
//    double d2X = 0;
//    double d2Y = 0;
//    double d3X = 0;
//    double d3Y = 0;
    public double R;


    public Obstacle(List<Vector2D> corners) {
        this.corners = corners;
        cachedTreeNode = new TreeNode(0, 0);
        center = new Vector2D();
        for (Vector2D v : corners) {
            center.add(v);
        }
        center.scale(.25);
        for (Vector2D v : corners) {
            R = max(R, v.getDistance(center));
//            System.out.println("R: " + R);
        }
//        System.out.println("Final R: " + R);
    }

    private boolean circleCheck(Node n1, Node n2) {
        double x1, x2, y1, y2;
        x1 = n1.x - center.x;
        x2 = n2.x - center.x;
        y1 = n1.y - center.y;
        y2 = n2.y - center.y;
        double D = (x1 * y2 - y1 * x2);
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dr = dx * dx + dy * dy;
        if (R * R * dr - (D * D) <= 0) {
//            System.out.println("Center: " + center + " R: " + R + " n1 " + n1 + " n2 " + n2 + " value: " + (R*R* pythag(x2-x1,y2-y1) -(D*D)));
            return false;
        }
        return true;
    }

    public boolean hasCollided(Node n1, Node n2) {


        //d_x = x2-x1
        //d_y = y2-y1
        //dr=pythag(d_x,d_y)
        //D = x1y2-x2y1
        if (!circleCheck(n1, n2)) {
            return false;
        }
//        System.out.println("eyo");
//        double[][] mainArr = new double[2][2];
//        double[] secondArr = new double[2];
        double a1;
        double b1;
        double c1;
        {

            // instead of caching vectors, I directly do math so less overhad. I know it sucks,but future me remember ax + by = c
            b1 = n2.x - n1.x;
//            mainArr[0][0] =  n1.y - n2.y;
//            mainArr[0][1] =  n2.x - n1.x;

            a1 = -(n2.y - n1.y);
            c1 = a1 * n1.x + b1 * n1.y;
//            secondArr[0] = mainArr[0][0] * n1.x + mainArr[0][1] * n1.y;
        }


        for (int i = 0; i < corners.size(); i++) {
            Vector2D corner1 = corners.get(i);
            Vector2D corner2 = corners.get((i + 1) % corners.size());


//            cache1 = corner1.subtract(corner2);
//            cache2 = corner1.clone();
//            cache3.setXY(, );


            double a2 = corner2.y - corner1.y;
            double b2 = corner1.x - corner2.x;
            double c2 = a2 * corner1.x + b2 * corner1.y;
//            mainArr[1][0] = corner2.y - corner1.y;
//            mainArr[1][1] = corner1.x - corner2.x;
//            secondArr[1] = mainArr[1][0] * corner1.x + mainArr[1][1]* corner1.y;

            double x;
            double y;

//            Matrix matrix = new Matrix(mainArr);
//            double[] answers = Matrix.solve(matrix,secondArr);
//            Matrix matrix =new Matrix(new double[][]{{a1,b1},{a2,b2}});
//            double[] answers = Matrix.solve(matrix,new double[]{c1,c2});

            // 2x2 inversion
            // 1 / (ad -bc ) * {
            // {d -b}
            // {-c a}
            // }

            double scale = 1 /(a1 * b2 - b1 * a2);
            // [c1 * d -b * c2]
            // [c1 * -c c2 *a]

            x = scale * (c1 * b2 - c2 * b1);
            y = scale * (c2 * a1 -  c1 * a2);
//            cache1.setXY(x, y);
            cachedTreeNode.x = x;
            cachedTreeNode.y = y;
            TreeNode intersceptTreeNode = cachedTreeNode;


            if (!Double.isFinite(x) || !Double.isFinite(y)) {
                continue;
            }
//            boolean checkCorners = Math.abs(cache1.getDistance(corner1) + cache1.getDistance(corner2) - corner1.getDistance(corner2)) < THRESHOLD;
            double xDiff = x-corner1.x;
            double yDiff = y - corner1.y;
            double rDiff = sqrt(xDiff * xDiff + yDiff * yDiff);
            xDiff = x-corner2.x;
            yDiff = y - corner2.y;
            boolean checkCorners = Math.abs( rDiff + sqrt(xDiff * xDiff + yDiff * yDiff) - corner1.getDistance(corner2)) < THRESHOLD;
            boolean checkNodes = Math.abs(findDistance(intersceptTreeNode, n1) + findDistance(intersceptTreeNode, n2) - findDistance(n1, n2)) < THRESHOLD;
            if ((checkCorners && checkNodes)) {

                return true;
            }

        }
        return false;
    }


    public static void main(String[] args) {
        Vector2D c1, c2, c3, c4;
        c1 = new Vector2D(-1, 1);
        c2 = new Vector2D(1, 1);
        c3 = new Vector2D(1, 2);
        c4 = new Vector2D(-1, 2);

        ArrayList<Vector2D> obstacleArrayList = new ArrayList<Vector2D>();
        obstacleArrayList.add(c1);
        obstacleArrayList.add(c2);
        obstacleArrayList.add(c3);
        obstacleArrayList.add(c4);
        Obstacle o = new Obstacle(obstacleArrayList);
        TreeNode n1 = new TreeNode(0, 0);
        TreeNode n2 = new TreeNode(1, 1);
        System.out.println(o.hasCollided(n1, n2));
    }

}
