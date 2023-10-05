package entity;

public class Movie extends Entity<String>{

    private final String name;
    private final int runningTime;

    public Movie(String id, String name, int runningTime) {
        super(id);
        this.name = name;
        this.runningTime = runningTime;
    }

    public String getName() {
        return name;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public String toString() {
        return id + "$" + runningTime + "$" + name + '\n';
    }
}
