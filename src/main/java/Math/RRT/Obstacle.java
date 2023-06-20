package Math.RRT;

import Math.Common.Matrix;
import Math.Common.Vector2D;

import java.util.ArrayList;
import java.util.List;

import static Math.RRT.InformedRRTStar.findDistance;
import static Math.RRT.InformedRRTStar.pythag;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Obstacle {

    List<Vector2D> corners;
    double THRESHOLD = .01;
    private final Node cachedNode;
    public Vector2D center;
    Vector2D cache1 = new Vector2D();
    Vector2D cache2 = new Vector2D();
    Vector2D cache3 = new Vector2D();
    public double R;


    public Obstacle(List<Vector2D> corners) {
        this.corners = corners;
        cachedNode = new Node(0, 0);
        center = new Vector2D();
        for (Vector2D v : corners) {
            center.add(v);
        }
        center.scale(.25);
        for (Vector2D v: corners) {
            R = max(R,v.getDistance(center));
//            System.out.println("R: " + R);
        }
//        System.out.println("Final R: " + R);
    }

    private boolean circleCheck(Node n1, Node n2){
        double x1, x2, y1, y2;
        x1 = n1.x - center.x;
        x2 = n2.x - center.x;
        y1 = n1.y - center.y;
        y2 = n2.y - center.y;
        double D = (x1*y2-y1*x2);
        double dx = x2 - x1;
        double dy = y2 -y1;
        double dr = dx*dx + dy * dy;
        if( R*R* dr -(D*D) <= 0){
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
        if(!circleCheck(n1,n2)){
            return false;
        }
//        System.out.println("eyo");
        double a1;
        double b1;
        double c1;
        {
            cache1.setXY(n2.x - n1.x, n2.y - n1.y);
            cache2.setXY( n1.x, n1.y);
            cache3.setXY(-cache1.y, cache1.x);
            a1 = cache3.x;
            c1 = cache3.x * cache2.x + cache3.y * cache2.y;
            b1 = cache3.y;
        }


        for (int i = 0; i < corners.size(); i++) {
            Vector2D corner1 = corners.get(i);
            Vector2D corner2 = corners.get((i + 1) % corners.size());


            cache1 = corner1.subtract(corner2);
            cache2 = corner1.clone();
            cache3.setXY(-cache1.y, cache1.x);


            double a2 = cache3.x;
            double b2 = cache3.y;
            double c2 = cache3.x * cache2.x + cache3.y * cache2.y;

            double x;
            double y;

            Matrix matrix = new Matrix(new double[][]{{a1, b1}, {a2, b2}});
            double[] answers = Matrix.solve(matrix, new double[]{c1, c2});

            x = answers[0];
            y = answers[1];
            cache1.setXY(x, y);
            cachedNode.x = x;
            cachedNode.y = y;
            Node intersceptNode = cachedNode;


            if (Double.isNaN(x) || Double.isNaN(y)) {
                continue;
            }
            boolean checkCorners = Math.abs(cache1.getDistance(corner1) + cache1.getDistance(corner2) - corner1.getDistance(corner2)) < THRESHOLD;
            boolean checkNodes = Math.abs(findDistance(intersceptNode, n1) + findDistance(intersceptNode, n2) - findDistance(n1, n2)) < THRESHOLD;
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
        Node n1 = new Node(0, 0);
        Node n2 = new Node(1, 1);
        System.out.println(o.hasCollided(n1, n2));
    }

}
