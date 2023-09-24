package entity;

public class Movie {

    private final String id;
    private final String name;
    private final int runningTime;

    public Movie(String id, String name, int runningTime) {
        this.id = id;
        this.name = name;
        this.runningTime = runningTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public String toString() {
        return id + "$" + runningTime + "$" + name;
    }
}
