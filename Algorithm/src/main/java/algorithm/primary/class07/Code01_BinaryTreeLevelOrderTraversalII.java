package algorithm.primary.class07;

// 测试链接：https://leetcode.com/problems/binary-tree-level-order-traversal-ii
public class Code01_BinaryTreeLevelOrderTraversalII {

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    public static class Info {
        public boolean isBalanced;
        public int height;

        public Info(boolean i, int h) {
            isBalanced = i;
            height = h;
        }
    }

    public boolean isBalanced(TreeNode root) {
        return getTreeNodeInfo(root).isBalanced;
    }

    public Info getTreeNodeInfo(TreeNode root) {
        if (root == null) {
            return new Info(true, 0);
        }

        Info leftInfo = getTreeNodeInfo(root.left);
        Info rightInfo = getTreeNodeInfo(root.right);

        boolean isBalanced = leftInfo.isBalanced && rightInfo.isBalanced
                && Math.abs(leftInfo.height - rightInfo.height) < 2;
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;

        return new Info(isBalanced, height);
    }

}