package Math.RRT;

import Math.Common.Matrix;
import Math.Common.Vector2D;

import java.util.ArrayList;
import java.util.List;

import static Math.RRT.InformedRRTStar.findDistance;

public class Obstacle {

    List<Vector2D> corners;
    double THRESHOLD = .01;
    private Node cachedNode;
    public Obstacle(List<Vector2D> corners){
        this.corners = corners;
        cachedNode = new Node(0,0);
    }

    public boolean hasCollided(Node n1, Node n2){
        double a1;
        double b1;
        double c1;
        {
            Vector2D direction = new Vector2D(n2.x - n1.x, n2.y - n1.y);
            Vector2D base = n1.toVector2D();
            Vector2D orth = new Vector2D(-direction.y, direction.x);
            a1 = orth.x;
            c1 = orth.x * base.x + orth.y * base.y;
            b1 = orth.y;
        }







        for (int i = 0; i < corners.size(); i++) {
            Vector2D corner1 = corners.get(i);
            Vector2D corner2 = corners.get((i + 1) % corners.size());


            Vector2D direction = corner1.clone().subtract(corner2);
            Vector2D base = corner1.clone();
            Vector2D orth = new Vector2D(-direction.y,direction.x);



            double a2 = orth.x;
            double b2 = orth.y;
            double c2 = orth.x * base.x + orth.y * base.y;


//            double x = (c2 - (b2* c1) / b1)/ (a2 - (b2*a1) / b1);
//
//            double y = (c1-a1*x) / b1;
            double x;
            double y;

            Matrix matrix = new Matrix(new double[][]{{a1,b1},{a2,b2}});
            double[] answers = Matrix.solve(matrix,new double[]{c1,c2});

            x = answers[0];
            y = answers[1];
            Vector2D interscept = new Vector2D(x,y);
            cachedNode.x = x;
            cachedNode.y = y;
            Node intersceptNode = cachedNode;

//
//            System.out.println("a1 : " + a1);
//            System.out.println("a2 : " + a2);
//            System.out.println("b1 : " + b1);
//            System.out.println("b2 : " + b2);
//            System.out.println("c1 : " + c1);
//            System.out.println("c2 : " + c2);
//            System.out.println("x : " + x);
//            System.out.println("y : " + y);
//            System.out.println("corner1 : " + corner1);
//            System.out.println("corner2 : " + corner2);
//            System.out.println("n1 : " + n1);
//            System.out.println("n2 : " + n2);
//            System.out.println("interscept to corner1: " + interscept.getDistance(corner1));
//            System.out.println("interscept to corner2: " + interscept.getDistance(corner2));
//            System.out.println("corner1 to corner2: " + corner1.getDistance(corner2));
//            System.out.println("interscept to n1: " + findDistance(intersceptNode,n1));
//            System.out.println("interscept to n2: " + findDistance(intersceptNode,n2));
//            System.out.println("n1 to n2: " + findDistance(n1, n2));
//            System.out.println("total value1: " + (Math.abs(interscept.getDistance(corner1) + interscept.getDistance(corner2)  - corner1.getDistance(corner2) )));
//            System.out.println("total value2: " + (Math.abs(findDistance(intersceptNode,n1) + findDistance(intersceptNode,n2)  - findDistance(n1, n2) )));
//
//            System.out.println("total value1: " + (Math.abs(interscept.getDistance(corner1) + interscept.getDistance(corner2)  - corner1.getDistance(corner2) ) < THRESHOLD));
//            System.out.println("total value2: " + (Math.abs(findDistance(intersceptNode,n1) + findDistance(intersceptNode,n2)  - findDistance(n1, n2) ) < THRESHOLD));


            if(Double.isNaN(x) ||Double.isNaN(y)){
                // TODO add edge case check of if the lines are the same line
//                System.out.println("NaN skip");
                continue;
            }

            // if check corners is true, then the interscept is not on the line segment
            // if check nodes is true, then the interscept is not on the line segment
            boolean checkCorners = Math.abs(interscept.getDistance(corner1) + interscept.getDistance(corner2)  - corner1.getDistance(corner2) ) < THRESHOLD;
            boolean checkNodes = Math.abs(findDistance(intersceptNode,n1) + findDistance(intersceptNode,n2)  - findDistance(n1, n2) ) < THRESHOLD;


            if((checkCorners  && checkNodes)){

                return true;
            }

        }
        return false;
    }

    public static void main(String[] args) {
        FixedNodePool fixedNodePool = new FixedNodePool(100);
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
        Obstacle o = new Obstacle(obstacleArrayList);
        Node n1 = new Node(0,0);
        Node n2 = new Node(0,.5);
        System.out.println(o.hasCollided(n1,n2));
    }

}
