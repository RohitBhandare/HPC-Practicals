#include <iostream>
#include <vector>
#include <queue>
#include <stack>
#include <omp.h>

using namespace std;

// Data structure to represent a graph using adjacency list representation
class Graph {
    int V; // Number of vertices

    // Adjacency list representation of the graph
    vector<vector<int>> adj;

public:
    Graph(int V) : V(V) {
        adj.resize(V);
    }

    // Function to add an edge to the graph
    void addEdge(int v, int w) {
        adj[v].push_back(w);
    }

    // Breadth-First Search algorithm
    void BFS(int start) {
        vector<bool> visited(V, false);
        queue<int> q;

        visited[start] = true;
        q.push(start);

        while (!q.empty()) {
            int u = q.front();
            q.pop();
            cout << u << " "; // Print the current vertex

            // Enqueue all adjacent vertices of dequeued vertex u
            // and mark them visited
            #pragma omp parallel for // Parallelize the loop
            for (int v : adj[u]) {
                if (!visited[v]) {
                    visited[v] = true;
                    q.push(v);
                }
            }
        }
    }

    // Depth-First Search algorithm
    void DFS(int start) {
        vector<bool> visited(V, false);
        stack<int> s;

        s.push(start);

        while (!s.empty()) {
            int u = s.top();
            s.pop();

            if (!visited[u]) {
                cout << u << " "; // Print the current vertex
                visited[u] = true;

                // Traverse all adjacent vertices of u
                #pragma omp parallel for // Parallelize the loop
                for (int v : adj[u]) {
                    if (!visited[v]) {
                        s.push(v);
                    }
                }
            }
        }
    }
};

int main() {
    Graph g(5); // Example graph with 5 vertices

    // Add edges to the graph
    g.addEdge(0, 1);
    g.addEdge(0, 2);
    g.addEdge(1, 3);
    g.addEdge(2, 4);

    cout << "BFS Traversal: ";
    g.BFS(0); // Start BFS from vertex 0
    cout << endl;

    cout << "DFS Traversal: ";
    g.DFS(0); // Start DFS from vertex 0
    cout << endl;

    return 0;
}
