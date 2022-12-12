package RBtree;


import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class RBTree<N extends Comparable<N>> {
    public static final boolean RED = true;
    public static final boolean BLACK = false;
    public static final boolean LEFT = true;
    public static final boolean RIGHT = false;
    public static final class TreeNode<T extends Comparable<T>>{
        T value;
        TreeNode<T> left;
        TreeNode<T> right;
        TreeNode<T> parent;
        boolean color;

        public TreeNode(T value) {
            this.value = value;
            this.color = RED;
        }

        public TreeNode(T value, boolean color) {
            this.value = value;
            this.color = color;
        }

        public TreeNode() {
        }

        @Override
        public String toString() {
            return "{" +
                    "value=" + value +
                    ", left=" + (left == null ? null : left.value) +
                    ", right=" + (right == null ? null : right.value) +
                    ", parent=" + (parent == null ? null : parent.value) +
                    ", color=" + (color ? "red" : "black") +
                    '}';
        }
    }

    private TreeNode<N> root;

    private int size;

    // 父亲叔叔节点都是红色
    private void parentRedAndUncleRed(TreeNode<N> node) {
        TreeNode<N> gp = node.parent.parent;
        gp.left.color = BLACK;
        gp.right.color = BLACK;
        gp.color = RED;
        // 继续递归处理
        afterInsertResetTree(gp);
    }

    // 父亲节点是黑的
    private void parentBlack(TreeNode<N> node) {
        node.color = RED;
    }

    // 父亲节点是红的，叔叔节点是黑的
    private void parentRedAndUncleBlack(TreeNode<N> node, boolean pIsGpLeftOrRight, boolean nIsPLeftOrRight) {
        if (pIsGpLeftOrRight == LEFT && nIsPLeftOrRight == LEFT) {
            sameLeft(node);
        }
        else if (pIsGpLeftOrRight == RIGHT && nIsPLeftOrRight == RIGHT) {
            sameRight(node);
        }
        else if (pIsGpLeftOrRight == LEFT && nIsPLeftOrRight == RIGHT) {
            nodeRightParentLeft(node);
        }
        else if (pIsGpLeftOrRight == RIGHT && nIsPLeftOrRight == LEFT) {
            nodeLeftParentRight(node);
        }
    }
    // 两个都是接在左边
    private void sameLeft(TreeNode<N> node) {
        rightR(node.parent);
        // 染色
        node.parent.color = BLACK;
        node.parent.right.color = RED;
    }
    // 两个都在右边
    private void sameRight(TreeNode<N> node) {
        leftR(node.parent);
        // 染色
        node.parent.color = BLACK;
        node.parent.left.color = RED;
    }
    // 父节点是右节点，当前节点是左节点
    private void nodeLeftParentRight(TreeNode<N> node) {
        rightR(node);
        leftR(node);
        // 染色
        node.color = BLACK;
        node.left.color = RED;
        node.right.color = RED;
    }
    // 父节点左节点，当前节点右节点
    private void nodeRightParentLeft(TreeNode<N> node) {
        leftR(node);
        rightR(node);
        // 染色
        node.color = BLACK;
        node.left.color = RED;
        node.right.color = RED;
    }

    // 左旋
    private void leftR(TreeNode<N> node) {
        TreeNode<N> p = node.parent;
        TreeNode<N> gp = p.parent;

        TreeNode<N> temp = node.left;
        node.left = p;
        p.parent = node;
        p.right = temp;
        if (temp != null) {
            temp.parent = p;
        }
        if (gp == null) {
            root = node;
            node.parent = null;
        }
        else if (gp.left == p) {
            gp.left = node;
            node.parent = gp;
        }
        else if (gp.right == p) {
            gp.right = node;
            node.parent = gp;
        }
    }

    // 右旋
    private void rightR(TreeNode<N> node) {
        TreeNode<N> p = node.parent;
        TreeNode<N> gp = p.parent;

        TreeNode<N> temp = node.right;
        node.right = p;
        p.parent = node;
        p.left = temp;
        if (temp != null) {
            temp.parent = p;
        }
        if (gp == null) {
            root = node;
            node.parent = null;
        }
        else if (gp.left == p) {
            gp.left = node;
            node.parent = gp;
        }
        else if (gp.right == p) {
            gp.right = node;
            node.parent = gp;
        }
    }


    private boolean isBlackAndNotNull (TreeNode<N> node) {
        return node != null && node.color == BLACK;
    }

    // null算黑色
    private boolean isBlack(TreeNode<N> node) {
        return node == null || node.color == BLACK;
    }

    private boolean isRed(TreeNode<N> node) {
        return !isBlack(node);
    }
    // 插入后重整这棵树
    private void afterInsertResetTree(TreeNode<N> node) {
        // 根节点直接设置黑色
        if (node == root) {
            node.color = BLACK;
            return;
        }
        // Grandfather节点
        TreeNode<N> gp = node.parent.parent;
        // 父节点
        TreeNode<N> p = node.parent;
        // 没有gp节点直接不处理
        if (gp == null) {
            return;
        }
        // 父节点是黑色直接接上去
        if (isBlackAndNotNull(p)) {
            parentBlack(node);
            return;
        }
        // 两个父节点都是红色
        if (isRed(gp.left) && isRed(gp.right)) {
            parentRedAndUncleRed(node);
        }
        // 这里父节点一定为RED，叔节点一定为黑色
        else{
            // 判断N的父节点是GP节点的左边还是右边
            boolean pIsGpLeftOrRight = RIGHT;
            boolean nIsPLeftOrRight = RIGHT;
            if (gp.left == p) {
                pIsGpLeftOrRight = LEFT;
            }
            // 判断N是父节点的左边还是右边
            if (p.left == node) {
                nIsPLeftOrRight = LEFT;
            }
            parentRedAndUncleBlack(node, pIsGpLeftOrRight, nIsPLeftOrRight);
        }
    }

    private N tryInsert(N val, TreeNode<N> node) {
        N nodeValue = node.value;
        int compareValue = val.compareTo(nodeValue);
        TreeNode<N> needInsert = null;
        // 插入值小于节点值
        if (compareValue < 0) {
            if (node.left != null) {
                return tryInsert(val, node.left);
            }
            TreeNode<N> insertNode = new TreeNode<>(val, RED);
            insertNode.parent = node;
            node.left = insertNode;
            needInsert = insertNode;

        }
        else if (compareValue > 0) {
            if (node.right != null) {
                return tryInsert(val, node.right);
            }
            TreeNode<N> insertNode = new TreeNode<>(val, RED);
            insertNode.parent = node;
            node.right = insertNode;
            needInsert = insertNode;
        }
        if (needInsert != null) {
            afterInsertResetTree(needInsert);
            return val;
        }
        return null;
    }

    public N insert(N val) {
        // 空的话直接root节点设置
        if (root == null) {
            root = new TreeNode<>(val);
            size++;
            return val;
        }
        N insertValue = tryInsert(val, root);
        if (insertValue != null) {
            size++;
        }
        return insertValue;
    }

    public N remove(N val) {
        return null;
    }

    private boolean find(TreeNode<N> node, N val) {
        if (node == null) {
            return false;
        }
        if (val.equals(node.value)) {
            return true;
        }
        return find(node.left, val) || find(node.right, val);
    }

    public boolean contain(N val) {
        return find(root, val);
    }

    public int getSize() {
        return size;
    }

    public void printValue() {
        if (root == null) {
            return;
        }
        Deque<TreeNode<N>> deque = new LinkedList<>();
        deque.offer(root);
        while (!deque.isEmpty()) {
            List<TreeNode<N>> temp = new LinkedList<>();
            while (!deque.isEmpty()) {
                TreeNode<N> node = deque.poll();
                System.out.print(node + " ");
                if (node.left != null) {
                    temp.add(node.left);
                }
                if (node.right != null) {
                    temp.add(node.right);
                }
            }
            for (TreeNode<N> node : temp) {
                deque.offerLast(node);
            }
            System.out.println();
        }
    }
}
