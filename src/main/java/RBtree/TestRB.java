package RBtree;

import java.util.Random;

public class TestRB {
    public static void main(String[] args) {
        Random random = new Random();
        RBTree<Integer> tree = new RBTree<>();
//        for (int i = 0; i < 1000; i++) {
//            int val = random.nextInt(11);
//            if (!tree.contain(val)){
//                System.out.println(val);
//            }
//            tree.insert(val);
//        }
        int[] arr = new int[]{3,8,6,7,9,0,4,2,5,10,1};
        for (int i : arr) {
            tree.insert(i);
        }
        tree.printValue();
        System.out.println(tree.getSize());
        System.out.println(tree.contain(10));
    }
}
