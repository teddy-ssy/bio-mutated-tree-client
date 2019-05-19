package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Point {
    private final int x;
    private final int y;
    private final double value;

    public Point(int x, int y, double value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public ArrayList<Integer> positionAsArray() {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        temp.add(x);
        temp.add(y);
        return temp;
    }

    public List<Integer> positionAsList() {
        return Arrays.asList(x, y);
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ":" + value + "]";
    }
}
