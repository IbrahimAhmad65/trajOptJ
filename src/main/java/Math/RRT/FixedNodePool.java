package Math.RRT;

public class FixedNodePool {
    private final Node[] nodePool;
    int head;
    public FixedNodePool(int NodePoolSize){
        head = 0;
        nodePool = new Node[NodePoolSize];
        for (int i = 0; i < nodePool.length; i++) {
            nodePool[i] = new Node(0,0);
        }
    }

    public Node getNewNode(double x, double y){
        Node node = nodePool[head];
        node.x = x;
        node.y = y;
        head++;
        return node;
//        return new Node(x, y);
    }
    public void reset(){
        head = 0;
    }
}
