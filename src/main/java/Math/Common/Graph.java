package Math.Common;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    public List<GraphNode> listOfNodes = new ArrayList<>();
    public static void printPath(List<GraphNode> fullPath) {
        for (int j = 0; j < fullPath.size() - 1; j++) {
            GraphNode current = fullPath.get(j);
            GraphNode next = fullPath.get(j + 1);
            System.out.println("\\operatorname{polygon}\\left(\\left(" + Math.floor(current.x * 1000) / 1000. + "," + Math.floor(current.y * 1000) / 1000 + "\\right),\\left(" + Math.floor(next.x * 1000) / 1000 + "," + Math.floor(next.y * 1000) / 1000 + "\\right)\\right)");
        }
    }
}
