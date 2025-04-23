package com.findpath;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PathFinderController {


    private final PathFinderService pathFinderService;

    private final LocationRepository locationRepository;


    private final PathRepository pathRepository;

    public PathFinderController(PathFinderService pathFinderService, LocationRepository locationRepository, PathRepository pathRepository) {
        this.pathFinderService = pathFinderService;
        this.locationRepository = locationRepository;
        this.pathRepository = pathRepository;
    }

    @PostMapping("/locations")
    public Location addLocation(@RequestBody Location location) {
        return locationRepository.save(location);
    }

    @PostMapping("/paths")
    public Path addPath(@RequestBody Path path) {
        return pathRepository.save(path);
    }

    @GetMapping("/locations")
    public List<Location> getLocations() {
        return locationRepository.findAll();
    }

    @GetMapping("/paths")
    public List<Path> getPaths() {
        return pathRepository.findAll();
    }

    @GetMapping("/shortest-path")
    public List<Location> getShortestPath(@RequestParam String startLocationId, @RequestParam String endLocationId) {
        return pathFinderService.findShortestPath(startLocationId, endLocationId);
    }

    @GetMapping("/dfs")
    public List<Location> getDFS(@RequestParam String startLocationId) {
        return pathFinderService.depthFirstSearch(startLocationId);
    }

    @GetMapping("/bfs")
    public List<Location> getBFS(@RequestParam String startLocationId) {
        return pathFinderService.breadthFirstSearch(startLocationId);
    }

    @GetMapping("/astar")
    public List<Location> getAStar(@RequestParam String startLocationId, @RequestParam String endLocationId) {
        return pathFinderService.aStarSearch(startLocationId, endLocationId);
    }

}

