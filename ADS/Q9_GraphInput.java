import java.util.*;

public class Q9_GraphInput {
    static class GraphAdjList {
        int V;
        List<List<Integer>> adj;

        GraphAdjList(int V) {
            this.V = V;
            adj = new ArrayList<>();
            for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
        }

        void addEdge(int u, int v) {
            adj.get(u).add(v);
            adj.get(v).add(u); // undirected
        }
    }

    static class GraphAdjMatrix {
        int V;
        int[][] mat;

        GraphAdjMatrix(int V) {
            this.V = V;
            mat = new int[V][V];
        }

        void addEdge(int u, int v) {
            mat[u][v] = 1;
            mat[v][u] = 1;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Number of vertices: ");
        int V = sc.nextInt();
        System.out.print("Number of edges: ");
        int E = sc.nextInt();

        GraphAdjList gList = new GraphAdjList(V);
        GraphAdjMatrix gMat = new GraphAdjMatrix(V);

        System.out.println("Enter undirected edges (u v): ");
        for (int i = 0; i < E; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            gList.addEdge(u, v);
            gMat.addEdge(u, v);
        }

        System.out.println("Adjacency List:");
        for (int i = 0; i < V; i++) {
            System.out.print(i + ": ");
            for (int v : gList.adj.get(i)) System.out.print(v + " ");
            System.out.println();
        }

        System.out.println("Adjacency Matrix:");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                System.out.print(gMat.mat[i][j] + " ");
            }
            System.out.println();
        }
    }
}