# 🗺️ A* Pathfinding API with Spring Boot

This project provides a simple and powerful API for finding the shortest path between two points in a graph (map). The API supports multiple pathfinding algorithms, allowing you to choose the most suitable one for your use case. The algorithms include:

A*: A heuristic-based algorithm that finds the most efficient path by combining the cost to reach a location and the estimated distance to the destination. It’s optimal when using an admissible heuristic.

Dijkstra: A classic algorithm that calculates the shortest path based solely on the path's costs. It's optimal for weighted graphs with non-negative edge weights.

DFS (Depth First Search): A traversal algorithm that explores as deeply as possible along each branch before backtracking. While it’s useful for exploring all possible paths, it's not guaranteed to find the shortest path.

BFS (Breadth First Search): A traversal algorithm that explores all neighbors at the current depth level before moving on to nodes at the next depth level. BFS guarantees the shortest path in unweighted graphs.

The project is built using Spring Boot, MongoDB, and REST API technologies, making it easy to integrate and extend. It’s a great exercise for understanding different search algorithms and their applications in real-world software systems, especially for navigation systems, game development, and map-related applications.

---

## 🚀 Features

- 📍 **A\*** algorithm implementation for shortest path finding
- 📡 API for finding the path between two points
- 🧠 Uses **Heuristic** for optimized pathfinding
- 🗃️ Data stored in **MongoDB**
- ✅ Tested with sample locations (A, B, C, D)

---

### Run APP

```bash
./gradlew bootRun
```

## Key Differences

| **Feature**              | **Dijkstra**                           | **A\***                                  | **DFS**                                  | **BFS**                                  |
|--------------------------|----------------------------------------|-----------------------------------------|------------------------------------------|------------------------------------------|
| **Search Strategy**       | Shortest path based on costs, no heuristic | Heuristic-based, combines cost and estimated distance | Depth-first traversal, no consideration for path length | Level-by-level search, no consideration for cost |
| **Optimality**            | Optimal (with non-negative weights)    | Optimal (with admissible heuristic)     | Not optimal                             | Optimal (for unweighted graphs)          |
| **Pathfinding Efficiency**| Efficient for weighted graphs, but slower than A* | Most efficient (if heuristic is good)   | Can be inefficient, may miss shorter paths | Efficient for unweighted graphs          |
| **Memory Usage**          | High (stores distances and previous nodes) | High (stores distances and previous nodes) | Low (stores only current path)          | Moderate (stores nodes at the current level) |
| **Best for**              | Shortest path in weighted graphs       | Shortest path with heuristic, complex search problems | Searching all paths, puzzles           | Shortest path in unweighted graphs       |
| **Use Case**              | Weighted graphs, network routing       | Route planning, games, map searches     | Puzzles, graph traversal, tree exploration | Shortest path in unweighted graphs, finding connected components |

---