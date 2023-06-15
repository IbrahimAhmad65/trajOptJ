package Math;

import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class Main {

    static  DoubleArraySubscriber ySub = null;
    // use an AtomicReference to make updating the value thread-safe
    static final AtomicReference<double[]> yValue = new AtomicReference<>();
    // retain listener handles for later removal
    static int connListenerHandle;
    static int valueListenerHandle;
    static int topicListenerHandle;

        // get the default instance of NetworkTables


    public static void main(String[] args) {


//        NetworkTableInstance inst = NetworkTableInstance.getDefault();
//
//        // add a connection listener; the first parameter will cause the
//        // callback to be called immediately for any current connections
//        connListenerHandle = inst.addConnectionListener(true, event -> {
//            if (event.is(NetworkTableEvent.Kind.kConnected)) {
//                System.out.println("Connected to " + event.connInfo.remote_id);
//            } else if (event.is(NetworkTableEvent.Kind.kDisconnected)) {
//                System.out.println("Disconnected from " + event.connInfo.remote_id);
//            }
//        });
//
//        // get the subtable called "datatable"
//        NetworkTable datatable = inst.getTable("datatable");
//
//        // subscribe to the topic in "datatable" called "Y"
//        ySub = datatable.getDoubleArrayTopic("dataIn").subscribe(new double[]{});
//
//        // add a listener to only value changes on the Y subscriber
//        valueListenerHandle = inst.addListener(
//                ySub,
//                EnumSet.of(NetworkTableEvent.Kind.kValueAll),
//                event -> {
//                    // can only get doubles because it's a DoubleSubscriber, but
//                    // could check value.isDouble() here too
//                    yValue.set(event.valueData.value.getDoubleArray());
//                });
//
//        // add a listener to see when new topics are published within datatable
//        // the string array is an array of topic name prefixes.
//        topicListenerHandle = inst.addListener(
//                new String[] { datatable.getPath() + "/" },
//                EnumSet.of(NetworkTableEvent.Kind.kTopic),
//                event -> {
//                    if (event.is(NetworkTableEvent.Kind.kPublish)) {
//                        // topicInfo.name is the full topic name, e.g. "/datatable/X"
//                        System.out.println("newly published " + event.topicInfo.name);
//                    }
//                });
//
//        while (true){
//            datatable.getEntry("dataIn").setDoubleArray(new double[]{Math.random()});
//            double[] value = yValue.getAndSet(null);
//            if (value != null) {
//                System.out.println("got new value " + value);
//            }
//        }


    }

}
