import java.util.*;

class RouteEdge {
    int to;
    double dist;

    RouteEdge(int to, double dist) {
        this.to = to;
        this.dist = dist;
    }
}

public class Q12_ShortestRoute {
    int V;
    List<List<RouteEdge>> adj;

    Q12_ShortestRoute(int V) {
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
    }

    void addEdge(int u, int v, double w) {
        adj.get(u).add(new RouteEdge(v, w));
        adj.get(v).add(new RouteEdge(u, w)); // assuming undirected
    }

    double[] dijkstra(int src) {
        double[] dist = new double[V];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[src] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1]));
        pq.offer(new int[]{src, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[0];
            double d = curr[1];
            if (d > dist[u]) continue;

            for (RouteEdge e : adj.get(u)) {
                int v = e.to;
                double nd = dist[u] + e.dist;
                if (nd < dist[v]) {
                    dist[v] = nd;
                    pq.offer(new int[]{v, (int) nd});
                }
            }
        }
        return dist;
    }

    public static void main(String[] args) {
        Q12_ShortestRoute g = new Q12_ShortestRoute(5);
        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 1);
        g.addEdge(2, 1, 2);
        g.addEdge(1, 3, 1);
        g.addEdge(2, 3, 5);
        g.addEdge(3, 4, 3);

        int S = 0; // start city index
        double[] dist = g.dijkstra(S);
        System.out.println("Shortest distances from " + S + ":");
        for (int i = 0; i < dist.length; i++) {
            System.out.println("To " + i + " : " + dist[i]);
        }
    }
}