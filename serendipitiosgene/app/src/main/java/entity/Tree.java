package entity;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Tree {

   

    private final Node rootNode;
    private final List<String> labels;

    public Tree(String name) {
        rootNode = new Node(name);
        labels = new ArrayList<>(Collections.singletonList(name));
    }

    public Tree(Node rootNode) {
        this.rootNode = rootNode;
        labels = new ArrayList<>();
    }

    public Tree(String name, List<Node> children) {
        this.rootNode = new Node(name);
        rootNode.getChildren().addAll(children);
        labels = new ArrayList<>(Collections.singletonList(name));
    }

    private Tree(Node rootNode, List<String> labels) {
        this.rootNode = rootNode;
        this.labels = labels;
    }

    public Tree merge(Tree b, double distance) {
       
        List<String> labels = new ArrayList<>(this.getLabels());
        labels.addAll(b.getLabels());
        Node node = new Node(String.valueOf(distance));
        node.getChildren().add(this.getRootNode());
        node.getChildren().add(b.getRootNode());
        Tree tree = new Tree(node, labels);
        
        return tree;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public List<String> getLabels() {
        return labels;
    }

    public int getSize() {
        return labels.size();
    }

    @Override
    public String toString() {
        return rootNode.toString();
    }
}