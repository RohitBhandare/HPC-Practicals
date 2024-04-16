
import java.util.LinkedList;
import java.util.Queue;

class Node {
    int data;
    Node left, right;

    Node(int data) {
        this.data = data;
        this.left = this.right = null;
    }
}

public class ParallelDFSBFSThreads {
    // Parallel Breadth-First Search
    private static void parallelBFS(Node root) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            System.out.println("BFS Visited node: " + current.data);

            if (current.left != null)
                queue.add(current.left);
            if (current.right != null)
                queue.add(current.right);
        }
    }

    // Parallel Depth-First Search
    private static void parallelDFS(Node node) {
        if (node == null)
            return;

        System.out.println("DFS Visited node: " + node.data);

        Thread leftThread = new Thread(() -> parallelDFS(node.left));
        Thread rightThread = new Thread(() -> parallelDFS(node.right));

        leftThread.start();
        rightThread.start();

        try {
            leftThread.join();
            rightThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);

        // Create threads for parallel BFS and parallel DFS
        Thread bfsThread = new Thread(() -> parallelBFS(root));
        Thread dfsThread = new Thread(() -> parallelDFS(root));

        // Start both threads
        bfsThread.start();
        dfsThread.start();

        try {
            // Wait for both threads to finish
            bfsThread.join();
            dfsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
