import java.util.*;

class FlightEdge {
    String dest;
    double cost; // time or fuel

    FlightEdge(String dest, double cost) {
        this.dest = dest;
        this.cost = cost;
    }
}

public class Q10_FlightGraph {
    Map<String, List<FlightEdge>> adj; // adjacency list

    Q10_FlightGraph() {
        adj = new HashMap<>();
    }

    void addAirport(String code) {
        adj.putIfAbsent(code, new ArrayList<>());
    }

    void addFlight(String from, String to, double cost) {
        addAirport(from);
        addAirport(to);
        adj.get(from).add(new FlightEdge(to, cost));
        // If bidirectional:
        // adj.get(to).add(new FlightEdge(from, cost));
    }

    void printGraph() {
        for (String src : adj.keySet()) {
            System.out.print(src + " -> ");
            for (FlightEdge e : adj.get(src)) {
                System.out.print("(" + e.dest + ", " + e.cost + ") ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Q10_FlightGraph g = new Q10_FlightGraph();
        g.addFlight("DEL", "BOM", 2.0);
        g.addFlight("DEL", "BLR", 2.5);
        g.addFlight("BOM", "GOI", 1.0);
        g.printGraph();
    }
}