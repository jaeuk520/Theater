import entity.Entity;

public class TestSimpleEntity extends Entity<String> {

    int number;

    public TestSimpleEntity(String s, int number) {
        super(s);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
