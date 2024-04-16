import threading
import queue

class Node:
    def __init__(self, data):
        self.data = data
        self.left = None
        self.right = None

# Parallel Breadth-First Search
def parallel_bfs(root):
    q = queue.Queue()
    q.put(root)

    while not q.empty():
        current = q.get()
        print("BFS Visited node:", current.data)

        if current.left:
            q.put(current.left)
        if current.right:
            q.put(current.right)

# Parallel Depth-First Search
def parallel_dfs(node):
    if not node:
        return

    left_thread = threading.Thread(target=parallel_dfs, args=(node.left,))
    right_thread = threading.Thread(target=parallel_dfs, args=(node.right,))

    left_thread.start()
    right_thread.start()

    left_thread.join()
    right_thread.join()

    print("DFS Visited node:", node.data)

if __name__ == "__main__":
    # Constructing the binary tree
    root = Node(1)
    root.left = Node(2)
    root.right = Node(3)
    root.left.left = Node(4)
    root.left.right = Node(5)
    root.right.left = Node(6)
    root.right.right = Node(7)

    # Create threads for parallel BFS and parallel DFS
    bfs_thread = threading.Thread(target=parallel_bfs, args=(root,))
    dfs_thread = threading.Thread(target=parallel_dfs, args=(root,))

    # Start both threads
    bfs_thread.start()
    bfs_thread.join()  # Ensure BFS finishes before starting DFS
    dfs_thread.start()

    # Wait for DFS thread to finish
    dfs_thread.join()
