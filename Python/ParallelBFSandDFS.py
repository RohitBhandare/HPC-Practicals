import multiprocessing
from queue import Queue

class Node:
    def __init__(self, data):
        self.data = data
        self.left = None
        self.right = None

# Parallel breadth-first search
def parallel_bfs(root, result_queue):
    visited = set()  # Set to keep track of visited nodes
    queue = Queue()  # Queue for BFS traversal
    queue.put(root)  # Start BFS from the root node

    while not queue.empty():
        current = queue.get()  # Get the next node from the queue

        visited.add(current.data)  # Mark the current node as visited
        print("BFS Visited node:", current.data, "Process:", multiprocessing.current_process().name)  # Print the visited node and process name

        # Add the children of the current node to the queue if they are not visited
        if current.left and current.left.data not in visited:
            queue.put(current.left)
        
        if current.right and current.right.data not in visited:
            queue.put(current.right)
    
    result_queue.put(True)  # Put True in the result queue to indicate completion of BFS

# Parallel depth-first search
def parallel_dfs(node, result_queue):
    if node is None:
        return
    
    print("DFS Visited node:", node.data, "Process:", multiprocessing.current_process().name)  # Print the visited node and process name

    # Recursively explore the left and right subtrees in parallel
    left_process = multiprocessing.Process(target=parallel_dfs, args=(node.left, result_queue))
    right_process = multiprocessing.Process(target=parallel_dfs, args=(node.right, result_queue))
    
    left_process.start()
    right_process.start()
    
    left_process.join()
    right_process.join()
    
    result_queue.put(True)  # Put True in the result queue to indicate completion of DFS

if __name__ == "__main__":
    # Create a binary tree
    root = Node(1)
    root.left = Node(2)
    root.right = Node(3)
    root.left.left = Node(4)
    root.left.right = Node(5)
    root.right.left = Node(6)
    root.right.right = Node(7)

    # Perform parallel BFS
    bfs_result_queue = multiprocessing.Queue()
    bfs_process = multiprocessing.Process(target=parallel_bfs, args=(root, bfs_result_queue))
    bfs_process.start()
    bfs_process.join()  # Wait for BFS process to complete

    print("BFS traversal completed.")

    # Perform parallel DFS
    dfs_result_queue = multiprocessing.Queue()
    dfs_process = multiprocessing.Process(target=parallel_dfs, args=(root, dfs_result_queue))
    dfs_process.start()
    dfs_process.join()  # Wait for DFS process to complete

    print("DFS traversal completed.")
