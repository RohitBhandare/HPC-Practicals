import threading
from queue import Queue

class Graph:
    def __init__(self, vertices):
        self.V = vertices
        self.adj = [[] for _ in range(vertices)]

    def add_edge(self, u, v):
        self.adj[u].append(v)
        self.adj[v].append(u)

def bfs(graph, start):
    thread_id = threading.get_ident()
    print(f"BFS thread ID: {thread_id}")
    visited = [False] * graph.V
    q = Queue()
    q.put(start)
    visited[start] = True
    while not q.empty():
        u = q.get()
        print(f"BFS processing node {u}")
        for v in graph.adj[u]:
            if not visited[v]:
                visited[v] = True
                q.put(v)

def dfs(graph, u, visited):
    thread_id = threading.get_ident()
    print(f"DFS thread ID: {thread_id}")
    if visited[u]:
        return
    visited[u] = True
    print(f"DFS processing node {u}")
    for v in graph.adj[u]:
        dfs(graph, v, visited)

if __name__ == "__main__":
    # Example graph
    g = Graph(6)
    g.add_edge(0, 1)
    g.add_edge(0, 2)
    g.add_edge(1, 3)
    g.add_edge(1, 4)
    g.add_edge(2, 5)

    # BFS
    bfs_thread = threading.Thread(target=bfs, args=(g, 0))
    bfs_thread.start()

    # DFS
    visited_dfs = [False] * g.V
    dfs_thread = threading.Thread(target=dfs, args=(g, 0, visited_dfs))
    dfs_thread.start()
