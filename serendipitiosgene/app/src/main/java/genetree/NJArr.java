package genetree;


import android.util.Log;

import entity.Node;
import entity.Point;
import entity.Tree;
import util.Labels;
import util.MatrixArr;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NJArr {

    private final MatrixArr distances;

    public NJArr(MatrixArr initialDistances) {

        this.distances = initialDistances;
    }

    /**
     * despite the fact that NJ returns unrooted tree, here it is rooted with the root on the last-added artificial node
     */
    public Tree run() {

        int n = distances.getData().size();
        ArrayList<Tree> clusters = new ArrayList<Tree>();
        for (int i = 0; i < n; i++) {
            clusters.add(new Tree(distances.getHeader().get(i)));
        }

        while (n > 2) {
            Log.i("start with i",String.valueOf(n));
            ArrayList<ArrayList<Double>> oldDistances = distances.getData();
            //Log.i("distances.getData()",String.valueOf(distances.getData().size()));
            //DataPrint(distances.getData());

            ArrayList<ArrayList<Double>> qMatrix = calculateQMatrix(distances.getData(), clusters);
            //Log.i("qMatrix",String.valueOf(qMatrix.size()));
            //DataPrint(qMatrix);

            Point point = MatrixArr.findLowestValuePoint(qMatrix);

            ArrayList<Integer> closests = point.positionAsArray();
            int f = closests.get(0);
            int g = closests.get(1);

            clusters = updateClusters(oldDistances, clusters, f, g);
            //DataPrint(distances.getData());

            n--;
            updateDistances(n, oldDistances, f, g);
            //distances.print();
        }
        Tree tree = generateFinalTree(clusters);

        return tree;
    }

    private void DataPrint(ArrayList<ArrayList<Double>> data){
        for (int i=0;i<data.size();i++){
            for(int j =0;j<data.get(i).size();j++){
                Log.i("data.get("+i+").get("+j+")",String.valueOf(data.get(i).get(j)));
            }
        }
        Log.i("end","end");
    }

    private ArrayList<ArrayList<Double>> calculateQMatrix(ArrayList<ArrayList<Double>> d, ArrayList<Tree> clusters) {

        int n = d.size();
        ArrayList<ArrayList<Double>> q = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < n; i++) {
            ArrayList<Double> temp = new ArrayList<Double>();
            for (int j = 0; j < n; j++) {

                if (i == j) {
                    temp.add(.0d);
                    //= .0d;
                } else {

                    double s1 = 0, s2 = 0;
                    for (int k = 0; k < n; k++) {
                        s1 += d.get(i).get(k);
                    }
                    for (int k = 0; k < n; k++) {
                        s2 += d.get(j).get(k);
                    }
                    temp.add((n - 2) * d.get(i).get(j) - s1 - s2);
                }
            }
            q.add(temp);
        }

        return q;
    }

    private ArrayList<Tree> updateClusters(ArrayList<ArrayList<Double>> oldDistances, ArrayList<Tree> clusters, int f, int g) {
        Node fNode = clusters.get(f).getRootNode();
        Node gNode = clusters.get(g).getRootNode();
        ArrayList<Double> distances = distancesToNewNode(oldDistances, f, g);
        fNode.setDistanceToParent(distances.get(0));
        gNode.setDistanceToParent(distances.get(1));

        List<Node> children = Arrays.asList(fNode, gNode);
        String newLabel = Labels.next(this.distances.getLastColumnName());
        this.distances.removeTwoAddOne(f, g, newLabel);
        Tree cluster = new Tree(newLabel, children);

        Tree[] newClusters = new Tree[clusters.size() - 1];
        for (int i = 0, j = 0; i < clusters.size(); i++) {
            if (f != i && g != i) {
                newClusters[j++] = clusters.get(i);
            }
        }
        clusters = new ArrayList<Tree>();
        for(Tree tree:newClusters){
            clusters.add(tree);
        }
        //clusters = newClusters;
        clusters.set(clusters.size()-1,cluster);
        //[clusters.length - 1] = cluster;
        return clusters;
    }

    private void updateDistances(int n, ArrayList<ArrayList<Double>> oldDistances, int f, int g) {
        int min = Math.min(f, g);
        int max = Math.max(f, g);

        for (int i = 0; i < n - 1; i++) {
            double distance = Double.NaN;
            if (i < min) {
                distance = distanceFromNewNode(oldDistances, f, g, i);
            } else if (i + 1 > min && i + 1 < max) {
                distance = distanceFromNewNode(oldDistances, f, g, i + 1);
            } else if (i + 2 > max) {
                distance = distanceFromNewNode(oldDistances, f, g, i + 2);
            }

            distances.getData().get(n-1).set(i,distance);
            //[n - 1][i] = distance;
            distances.getData().get(i).set(n-1,distance);
            //[i][n - 1] = distance;
        }
        distances.getData().get(n-1).set(n-1,.0d);
        //[n - 1][n - 1] = .0d;
    }

    private Tree generateFinalTree(ArrayList<Tree> clusters) {
        Node node = clusters.get(1).getRootNode();
        node.setDistanceToParent(distances.getData().get(0).get(1));//only one distance is left
        clusters.get(0).getRootNode().getChildren().add(node);
        return clusters.get(0);
    }

    private ArrayList<Double> distancesToNewNode(ArrayList<ArrayList<Double>> d, int f, int g) {

        int n = d.size();
        double s1 = .0d, s2 = .0d;
        for (int k = 0; k < n; k++) {
            //s1 += d[f][k];
            s1 += d.get(f).get(k);
        }
        for (int k = 0; k < n; k++) {
            //s2 += d[g][k];
            s2 += d.get(g).get(k);
        }
        //double fu = ((1.0d / 2) * d[f][g]) + (1.0d / (2.0d * (n - 2))) * (s1 - s2);
        double fu = ((1.0d / 2) * d.get(f).get(g)) + (1.0d / (2.0d * (n - 2))) * (s1 - s2);
        //double gu = d[f][g] - fu;
        double gu = d.get(f).get(g) - fu;
        ArrayList<Double> result = new ArrayList<Double>();
        result.add(fu);
        result.add(gu);
        //{fu, gu};
        return result;
    }

    private double distanceFromNewNode(ArrayList<ArrayList<Double>> d, int f, int g, int k) {

        //double distance = (1.0d / 2.0d) * (d[f][k] + d[g][k] - d[f][g]);
        double distance = (1.0d / 2.0d) * (d.get(f).get(k) + d.get(g).get(k) - d.get(f).get(g));
        //Log.i("new distance",String.valueOf(distance));

        return distance;
    }

}
