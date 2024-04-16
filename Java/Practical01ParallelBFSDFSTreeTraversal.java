import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.ArrayDeque;
import java.util.Deque;

class Node {
    int data;
    Node left, right;

    Node(int data) {
        this.data = data;
        left = right = null;
    }
}

// RecursiveAction subclass for parallel breadth-first search
class ParallelBFS extends RecursiveAction {
    private final Node root;
    private final AtomicBoolean found;
    private final int target;

    // Constructor
    ParallelBFS(Node root, AtomicBoolean found, int target) {
        this.root = root;
        this.found = found;
        this.target = target;
    }

    // Method to perform BFS traversal
    @Override
    protected void compute() {
        if (root == null || found.get()) {
            return;
        }

        Deque<Node> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Logging visiting node
            System.out.println("BFS Visited node: " + current.data + " Thread: " + Thread.currentThread().getName());

            if (current.data == target) {
                found.set(true);
                return;
            }

            if (current.left != null) {
                queue.add(current.left);
            }

            if (current.right != null) {
                queue.add(current.right);
            }
        }
    }
}

// RecursiveAction subclass for parallel depth-first search
class ParallelDFS extends RecursiveAction {
    private final Node node;
    private final AtomicReference<Node> result;
    private final int target;

    // Constructor
    ParallelDFS(Node node, AtomicReference<Node> result, int target) {
        this.node = node;
        this.result = result;
        this.target = target;
    }

    // Method to perform DFS traversal
    @Override
    protected void compute() {
        if (node == null || result.get() != null) {
            return;
        }

        // Logging visiting node
        System.out.println("DFS Visited node: " + node.data + " Thread: " + Thread.currentThread().getName());

        if (node.data == target) {
            result.set(node);
            return;
        }

        ParallelDFS leftTask = new ParallelDFS(node.left, result, target);
        ParallelDFS rightTask = new ParallelDFS(node.right, result, target);

        leftTask.fork(); // Forking left subtree traversal
        rightTask.compute(); // Continuing with right subtree traversal
        leftTask.join(); // Waiting for completion of left subtree traversal
    }
}

public class Practical01ParallelBFSDFSTreeTraversal {
    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);

        int target = 5;

        // Creating ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();

        // Performing parallel BFS
        AtomicBoolean bfsFound = new AtomicBoolean(false);
        pool.invoke(new ParallelBFS(root, bfsFound, target));
        System.out.println("BFS: Node " + (bfsFound.get() ? "found" : "not found"));

        // Performing parallel DFS
        AtomicReference<Node> dfsResult = new AtomicReference<>();
        pool.invoke(new ParallelDFS(root, dfsResult, target));
        System.out.println("DFS: Node " + (dfsResult.get() != null ? "found" : "not found"));
    }
}
