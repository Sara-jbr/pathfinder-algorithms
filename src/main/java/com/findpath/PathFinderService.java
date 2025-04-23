package com.findpath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

// Service to find paths between locations using multiple algorithms
@Service
public class PathFinderService {

    @Autowired // Injecting LocationRepository
    private LocationRepository locationRepository;

    @Autowired // Injecting PathRepository
    private PathRepository pathRepository;

    // Dijkstra’s algorithm to find the shortest path
    public List<Location> findShortestPath(String startLocationId, String endLocationId) {
        Map<String, Integer> distances = new HashMap<>(); // To store distance from start
        Map<String, String> previousLocations = new HashMap<>(); // To reconstruct path
        PriorityQueue<LocationWrapper> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(lw -> lw.distance)); // Min-heap based on distance

        List<Location> locations = locationRepository.findAll(); // Fetch all locations
        for (Location location : locations) {
            int dist = location.getId().equals(startLocationId) ? 0 : Integer.MAX_VALUE;
            distances.put(location.getId(), dist);
            previousLocations.put(location.getId(), null);
            priorityQueue.add(new LocationWrapper(location, dist)); // Add to priority queue
        }

        while (!priorityQueue.isEmpty()) {
            LocationWrapper currentWrapper = priorityQueue.poll(); // Get location with min distance
            Location currentLocation = currentWrapper.location;

            if (currentLocation.getId().equals(endLocationId)) break; // Reached destination

            List<Path> paths = pathRepository.findAll(); // Fetch all paths
            for (Path path : paths) {
                if (path.getFrom().getId().equals(currentLocation.getId())) {
                    Location neighbor = path.getTo();
                    int newDist = distances.get(currentLocation.getId()) + path.getWeight(); // Calculate new distance
                    if (newDist < distances.get(neighbor.getId())) {
                        distances.put(neighbor.getId(), newDist);
                        previousLocations.put(neighbor.getId(), currentLocation.getId());
                        priorityQueue.add(new LocationWrapper(neighbor, newDist)); // Update priority queue
                    }
                }
            }
        }

        List<Location> shortestPath = new ArrayList<>(); // Reconstruct path
        String currentId = endLocationId;
        while (currentId != null) {
            Location location = locationRepository.findById(currentId).orElse(null);
            if (location != null) {
                shortestPath.add(location);
            }
            currentId = previousLocations.get(currentId);
        }
        Collections.reverse(shortestPath); // Reverse to get correct order
        return shortestPath;
    }

    // DFS algorithm for exploring all reachable nodes
    public List<Location> depthFirstSearch(String startLocationId) {
        Map<String, Boolean> visited = new HashMap<>();
        List<Location> visitedLocations = new ArrayList<>();
        Stack<Location> stack = new Stack<>();

        Location startLocation = locationRepository.findById(startLocationId).orElse(null);
        if (startLocation == null) {
            return visitedLocations;
        }

        stack.push(startLocation);

        while (!stack.isEmpty()) {
            Location currentLocation = stack.pop();

            if (!visited.getOrDefault(currentLocation.getId(), false)) {
                visited.put(currentLocation.getId(), true);
                visitedLocations.add(currentLocation);

                List<Path> paths = pathRepository.findAll();
                for (Path path : paths) {
                    if (path.getFrom().getId().equals(currentLocation.getId())) {
                        Location neighbor = path.getTo();
                        if (!visited.getOrDefault(neighbor.getId(), false)) {
                            stack.push(neighbor); // Explore unvisited neighbor
                        }
                    }
                }
            }
        }

        return visitedLocations;
    }

    // BFS algorithm for level-wise traversal
    public List<Location> breadthFirstSearch(String startLocationId) {
        Map<String, Boolean> visited = new HashMap<>();
        List<Location> visitedLocations = new ArrayList<>();
        Queue<Location> queue = new LinkedList<>();

        Location startLocation = locationRepository.findById(startLocationId).orElse(null);
        if (startLocation == null) {
            return visitedLocations;
        }

        queue.add(startLocation);
        visited.put(startLocation.getId(), true);

        while (!queue.isEmpty()) {
            Location currentLocation = queue.poll();
            visitedLocations.add(currentLocation);

            List<Path> paths = pathRepository.findAll();
            for (Path path : paths) {
                if (path.getFrom().getId().equals(currentLocation.getId())) {
                    Location neighbor = path.getTo();
                    if (!visited.getOrDefault(neighbor.getId(), false)) {
                        queue.add(neighbor); // Add to queue
                        visited.put(neighbor.getId(), true);
                    }
                }
            }
        }

        return visitedLocations;
    }

    // A* search algorithm using heuristic (currently simple)
    public List<Location> aStarSearch(String startLocationId, String endLocationId) {
        Map<String, Integer> gScores = new HashMap<>(); // Cost from start
        Map<String, Integer> fScores = new HashMap<>(); // Estimated total cost
        Map<String, String> previousLocations = new HashMap<>();

        List<Location> locations = locationRepository.findAll();
        for (Location location : locations) {
            gScores.put(location.getId(), Integer.MAX_VALUE);
            fScores.put(location.getId(), Integer.MAX_VALUE);
            previousLocations.put(location.getId(), null);
        }

        gScores.put(startLocationId, 0);
        fScores.put(startLocationId, heuristic(startLocationId, endLocationId)); // Estimate from start to end

        PriorityQueue<Location> openSet = new PriorityQueue<>(
                Comparator.comparingInt(loc -> fScores.getOrDefault(loc.getId(), Integer.MAX_VALUE))
        );

        Location startLocation = locations.stream()
                .filter(loc -> loc.getId().equals(startLocationId))
                .findFirst()
                .orElse(null);

        if (startLocation == null) {
            throw new IllegalArgumentException("Start location not found.");
        }

        openSet.add(startLocation); // Start A*

        List<Path> allPaths = pathRepository.findAll();

        while (!openSet.isEmpty()) {
            Location currentLocation = openSet.poll(); // Location with lowest fScore

            if (currentLocation.getId().equals(endLocationId)) {
                break; // Found goal
            }

            for (Path path : allPaths) {
                if (path.getFrom() == null || path.getTo() == null) continue;

                if (path.getFrom().getId().equals(currentLocation.getId())) {
                    Location neighbor = path.getTo();
                    int tentativeGScore = gScores.get(currentLocation.getId()) + path.getWeight();

                    if (tentativeGScore < gScores.get(neighbor.getId())) {
                        previousLocations.put(neighbor.getId(), currentLocation.getId());
                        gScores.put(neighbor.getId(), tentativeGScore);
                        fScores.put(neighbor.getId(), tentativeGScore + heuristic(neighbor.getId(), endLocationId)); // Update estimate
                        openSet.add(neighbor); // Add to open set
                    }
                }
            }
        }

        // Reconstruct final path
        List<Location> finalPath = new ArrayList<>();
        String currentId = endLocationId;

        while (currentId != null) {
            Location loc = locationRepository.findById(currentId).orElse(null);
            if (loc != null) {
                finalPath.add(loc);
            }
            currentId = previousLocations.get(currentId);
        }

        Collections.reverse(finalPath); // Reverse for correct order
        return finalPath;
    }

    // Heuristic function – currently always returns 0
    private int heuristic(String currentLocationId, String endLocationId) {
        return 0;
    }

}
