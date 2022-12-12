package RBtree;

public class TestRB {
    public static void main(String[] args) {
        RBTree<Integer> tree = new RBTree<>();
        for (int i = 0; i < 1000; i++) {
            tree.insert(i);
        }
        tree.printValue();
        System.out.println(tree.getSize());
        System.out.println(tree.contain(10));
    }
}
