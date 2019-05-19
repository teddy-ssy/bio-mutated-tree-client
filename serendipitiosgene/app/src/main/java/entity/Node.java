package entity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node {
    private List<Node> children;
    private String label;
    private double distanceToParent = Double.NaN;

    public Node(String label) {
        this.label = label;
    }

    public List<Node> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    @Override
    public String toString() {
        return "[" + label + (getChildren().size() > 0 ? ":" + Arrays.toString(getChildren().toArray()) : "") + "]";
    }

    public int getNumberOfNodes() {
        return 1 + getChildren().stream().mapToInt(Node::getNumberOfNodes).sum();
    }

    public String printableLable() {
        return (Double.isNaN(distanceToParent) ? "" : distanceToParent + "-<-") + "[" + label + "]";
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDistanceToParent(double distanceToParent) {
        this.distanceToParent = distanceToParent;
    }
}
