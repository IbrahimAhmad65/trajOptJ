package Math.RRT;

import sun.misc.Unsafe;

public class BadIdeasGoHere {
    public static void main(String[] args) {
        Unsafe unsafe = Unsafe.getUnsafe();
        long imStupid = unsafe.allocateMemory(8);
        System.out.println( unsafe.getByte(imStupid));
    }
}
