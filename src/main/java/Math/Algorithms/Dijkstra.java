package Math.Algorithms;

import Math.Common.*;

import java.util.ArrayList;
import java.util.List;

import static Math.Common.GraphNode.findDistance;

public class Dijkstra {
    public List<GraphNode> unvisited;

    public List<GraphNode> path = new ArrayList<>();


    public List<GraphNode> aStarIThinkNotReallySureMaybe(Graph graph, GraphNode start, GraphNode end) {
        unvisited = new ArrayList<>(graph.listOfNodes);
        unvisited.remove(start);
        start.cost = 0;
//        path.add(start);
        GraphNode current = start;
        while (current != end && current != null && unvisited.size() > 0) {
            for (int i = 0; i < current.neighbors.size(); i++) {
                if (!unvisited.contains(current.neighbors.get(i))) {
                    i++;
                    continue;
                }
                GraphNode check = current.neighbors.get(i);
                double newCost = findDistance(current, check) + current.cost + findDistance(check,end);
                if (newCost < check.cost) {
                    check.cost = newCost;
                }
            }
            double minCost = Double.POSITIVE_INFINITY;
            GraphNode min = null;
            for (GraphNode g : unvisited) {
                if (Double.isFinite(g.cost)) {
                    if (g.cost < minCost) {
                        min = g;
                        minCost = g.cost;
                    }
                }
            }

            current = min;
            unvisited.remove(current);
        }
        GraphNode currentCheck = end;
        while (currentCheck != start){
            path.add(currentCheck);
            double minCost = Double.POSITIVE_INFINITY;
            GraphNode min = null;
            for (GraphNode g : currentCheck.neighbors) {
                if (Double.isFinite(g.cost)) {
                    if (g.cost < minCost) {
                        min = g;
                        minCost = g.cost;
                    }
                }
            }
            currentCheck = min;
        }
        path.add(start);
        return path;
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

//        ArrayList<Vector2D> obstacleArrayList3 = new ArrayList<Vector2D>();
//        c1 = new Vector2D(13.14, 5.445);
//        c2 = new Vector2D(16.5, 5.445);
//        obstacleArrayList3.add(c1);
//        obstacleArrayList3.add(c2);
//        ArrayList<Vector2D> obstacleArrayList4 = new ArrayList<Vector2D>();
//        c1 = new Vector2D(0, 5.445);
//        c2 = new Vector2D(3.33, 5.445);
//
//        obstacleArrayList4.add(c1);
//        obstacleArrayList4.add(c2);

        ArrayList<Vector2D> allCorners = new ArrayList<>();
        Obstacle[] obstacles = new Obstacle[2];
        obstacles[0] = new Obstacle(obstacleArrayList);
        obstacles[1] = new Obstacle(obstacleArrayList2);
//        obstacles[2] = new Obstacle(obstacleArrayList3);
//        obstacles[3] = new Obstacle(obstacleArrayList4);
        for (Obstacle r : obstacles) {
            for (int i = 0; i < r.corners.size(); i++) {
                Vector2D delta = r.corners.get(i).subtract(r.center);
                delta.setMagnitude(.02);
                delta.add(r.corners.get(i));
                allCorners.add(delta);
            }
        }
//        System.out.println(allCorners.size());

//        ArrayList<GraphNode> graphNodes = new ArrayList<>();
//        for (Vector2D v : allCorners) {
//            graphNodes.add(new GraphNode(v.x, v.y));
//        }
//        for (int i = 0; i < graphNodes.size() - 2; i++) {
//            for (int j = i + 1; j < graphNodes.size() - 1; j++) {
//                boolean pass = true;
//                for (Obstacle o: obstacles) {
//                    if(o.hasCollided(graphNodes.get(i), graphNodes.get(j))){
//                        pass = false;
//                    }
//                }
//                if(pass){
//                    graphNodes.get(i).neighbors.add(graphNodes.get(j));
//                    graphNodes.get(j).neighbors.add(graphNodes.get(i));
//                }
//            }
//        }
        ArrayList<GraphNode> graphNodes = new ArrayList<>();
        for (Vector2D v: allCorners) {
            graphNodes.add(new GraphNode(v.x, v.y));
        }
        GraphNode iNode;
        GraphNode jNode;
        for (int i = 0; i < graphNodes.size() - 1; i++) {
            bake(obstacles, graphNodes, i);
        }
        GraphNode n1 = null;
        GraphNode n2 = null;
        for (int z = 0; z < 10000000; z++) {
            if(n1 != null){
                graphNodes.remove(n1);
                graphNodes.remove(n2);
                for (GraphNode g: graphNodes) {
                    g.flush(n1,n2);
                }
            }
            GraphNode start = new GraphNode(Math.random() , 2 * Math.random() + 1.84);
            GraphNode end = new GraphNode(14 + Math.random(), 1.84 + 2 * Math.random());
            graphNodes.add(0,start);
            graphNodes.add(0,end);
            for (int i = 0; i < 2 ; i++) {
                bake(obstacles, graphNodes, i);
            }






            Graph g = new Graph();
            g.listOfNodes = graphNodes;
            Dijkstra dijkstra = new Dijkstra();
//            System.out.println("fhsjklhdjghfdghjlhsfld");
            dijkstra.aStarIThinkNotReallySureMaybe(g,start,end);
            n1 = start;
            n2 = end;
//            printPath( dijkstra.dijkstra(g,graphNodes.get(0),graphNodes.get(graphNodes.size()-1)));
//            dijkstra.dijkstra(g, graphNodes.get(0), graphNodes.get(graphNodes.size() - 1));

        }


    }

    private static void bake(Obstacle[] obstacles, ArrayList<GraphNode> graphNodes, int i) {
        GraphNode iNode;
        GraphNode jNode;
        iNode = graphNodes.get(i);
        for (int j = i + 1; j < graphNodes.size(); j++) {
            jNode = graphNodes.get(j);
            boolean pass = true;
            for (Obstacle o : obstacles) {
                if(o.hasCollided(iNode,jNode)){
                    pass = false;
                    break;
                }
            }
            if(pass){
                if(jNode != iNode){
                    iNode.neighbors.add(jNode);
                    jNode.neighbors.add(iNode);
//                            printPathSegment(iNode,jNode);
                }
            }
        }
    }


}
