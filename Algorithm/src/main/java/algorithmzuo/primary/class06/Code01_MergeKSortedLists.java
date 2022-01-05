package algorithmzuo.primary.class06;

import java.util.Comparator;
import java.util.PriorityQueue;

// 测试链接：https://leetcode.com/problems/merge-k-sorted-lists/
public class Code01_MergeKSortedLists {

    public static class ListNode {
        public int val;
        public ListNode next;
    }

    public static class ListNodeComparator implements Comparator<ListNode> {

        @Override
        public int compare(ListNode o1, ListNode o2) {
            return o1.val - o2.val;
        }

    }

    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null) {
            return null;
        }
        // 小根堆
        PriorityQueue<ListNode> heap = new PriorityQueue<>(new ListNodeComparator());
        for (ListNode list : lists) {
            if (list != null) {
                heap.add(list);
            }
        }
        if (heap.isEmpty()) {
            return null;
        }
        ListNode head = heap.poll(); // 先出来个头
        ListNode pre = head; // 搞个指针，用于后面循环一个一个走下去
        if (pre.next != null) { // 前面出来了个头，堆里面没有这根链表了，这边补充一个进堆
            heap.add(pre.next);
        }
        while (!heap.isEmpty()) { // 一直搞到堆空了
            ListNode cur = heap.poll(); // 出推
            pre.next = cur; // 链上去
            pre = cur; // 指针移动
            if (cur.next != null) { // 补充一个进堆
                heap.add(cur.next);
            }
        }
        return head;
    }

}