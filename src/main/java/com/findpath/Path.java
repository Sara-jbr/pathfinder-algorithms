package com.findpath;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("paths")
public class Path {

    @Id
    private String id;

    @DBRef
    private Location from;

    @DBRef
    private Location to;

    private int weight;

    public Path() {}

    public Path(Location from, Location to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String getId() { return id; }

    public Location getFrom() { return from; }
    public void setFrom(Location from) { this.from = from; }

    public Location getTo() { return to; }
    public void setTo(Location to) { this.to = to; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
}
