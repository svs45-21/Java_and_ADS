import java.util.*;

class Edge {
    int to;
    double weight;

    Edge(int to, double weight) {
        this.to = to;
        this.weight = weight;
    }
}

public class Q11_MinimumSpanningTree {
    int V;
    List<List<Edge>> adj;

    Q11_MinimumSpanningTree(int V) {
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
    }

    void addEdge(int u, int v, double w) {
        adj.get(u).add(new Edge(v, w));
        adj.get(v).add(new Edge(u, w));
    }

    double primMST() {
        boolean[] inMST = new boolean[V];
        double[] key = new double[V];
        int[] parent = new int[V];
        Arrays.fill(key, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1]));
        key[0] = 0;
        pq.offer(new int[]{0, 0}); // {vertex, key}

        double totalCost = 0;

        while (!pq.isEmpty()) {
            int u = pq.poll()[0];
            if (inMST[u]) continue;
            inMST[u] = true;
            totalCost += key[u];

            for (Edge e : adj.get(u)) {
                int v = e.to;
                double w = e.weight;
                if (!inMST[v] && w < key[v]) {
                    key[v] = w;
                    parent[v] = u;
                    pq.offer(new int[]{v, (int) w});
                }
            }
        }

        System.out.println("MST edges:");
        for (int v = 1; v < V; v++) {
            System.out.println(parent[v] + " - " + v + " (cost " + key[v] + ")");
        }
        System.out.println("Total cost: " + totalCost);
        return totalCost;
    }

    public static void main(String[] args) {
        Q11_MinimumSpanningTree g = new Q11_MinimumSpanningTree(4);
        g.addEdge(0, 1, 10);
        g.addEdge(0, 2, 6);
        g.addEdge(0, 3, 5);
        g.addEdge(1, 3, 15);
        g.addEdge(2, 3, 4);
        g.primMST();
    }
}