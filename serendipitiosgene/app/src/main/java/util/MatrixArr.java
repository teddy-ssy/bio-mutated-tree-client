package util;


import android.util.Log;

import java.util.ArrayList;
import entity.Point;

public class MatrixArr {


    private ArrayList<String> Header;
    private ArrayList<ArrayList<Double>> Data;

    public MatrixArr(ArrayList<String> header, ArrayList<ArrayList<Double>> data) {
        this.Header = header;
        this.Data = data;
    }

    public MatrixArr(int size) {
        this.Header = new ArrayList<String>();
        this.Data = initQuadraticWithValue(size, .0d);
    }

    public int establishPosition(String label) {
        for (int i = 0; i < Header.size(); i++) {
            if (Header.get(i) != null && Header.get(i).equals(label)) {
                return i;
            } else if (Header.get(i) == null) {
                Header.set(i,label);
                return i;
            }
        }
        throw new RuntimeException("something went wrong when establishing position");
    }

    public ArrayList<String> getHeader() {
        return Header;
    }

    public ArrayList<ArrayList<Double>> getData() {
        return Data;
    }

    public String getLastColumnName() {
        return Header.get(Header.size()-1);
    }

    public void removeTwoAddOne(int remove1, int remove2, String newLabel) {

        int min = Math.min(remove1, remove2);
        int max = Math.max(remove1, remove2);

        /*
        String[] tmpHeader = header;
        header = new String[header.length - 1];
        System.arraycopy(tmpHeader, 0, header, 0, min);
        System.arraycopy(tmpHeader, min + 1, header, min, max - min - 1);
        System.arraycopy(tmpHeader, max + 1, header, max - 1, tmpHeader.length - max - 1);
        header[header.length - 1] = newLabel;
        */
        String[]tmpHeader = new String[Header.size()];
        for(int i=0;i<Header.size();i++){
            tmpHeader[i]= Header.get(i);
        }
        String[] header =new String[Header.size() - 1];
        System.arraycopy(tmpHeader, 0, header, 0, min);
        System.arraycopy(tmpHeader, min + 1, header, min, max - min - 1);
        System.arraycopy(tmpHeader, max + 1, header, max - 1, tmpHeader.length - max - 1);
        header[header.length - 1] = newLabel;
        Header = new ArrayList<String>();
        for(int i=0;i<header.length;i++){
            Header.add(header[i]);
        }
        /*
        double[][] tmpData = data;
        data = initQuadraticWithValue(tmpData.length - 1, Double.NaN);
        for (int i = 0; i < tmpData.length; i++) {
            for (int j = 0; j < tmpData.length; j++) {
                if (i != min && i != max || j != min && j != max) {
                    if (i > max && j > max) {
                        data[i - 2][j - 2] = tmpData[i][j];
                    } else if (i > max) {
                        data[i - 2][j] = tmpData[i][j];
                    } else if (j > max) {
                        data[i][j - 2] = tmpData[i][j];
                    } else if (i > min && j > min) {
                        data[i - 1][j - 1] = tmpData[i][j];
                    } else if (i > min) {
                        data[i - 1][j] = tmpData[i][j];
                    } else if (j > min) {
                        data[i][j - 1] = tmpData[i][j];
                    } else if (i < min && j < min) {
                        data[i][j] = tmpData[i][j];
                    }
                }
            }
        }
        */
       ArrayList<ArrayList<Double>> tmpData = Data;
        Data = initQuadraticWithValue(tmpData.size(),Double.NaN);

        for (int i = 0; i < tmpData.size(); i++) {
            for (int j = 0; j < tmpData.size(); j++) {
                if (i != min && i != max || j != min && j != max) {
                    if (i > max && j > max) {
                        Data.get(i-2).set(j-2,tmpData.get(i).get(j));
                        //)[i - 2][j - 2] = tmpData[i][j];
                    } else if (i > max) {
                        Data.get(i-2).set(j,tmpData.get(i).get(j));
                        //)[i - 2][j] = tmpData[i][j];
                    } else if (j > max) {
                        Data.get(i).set(j-2,tmpData.get(i).get(j));
                        //))[i][j - 2] = tmpData[i][j];
                    } else if (i > min && j > min) {
                        Data.get(i-1).set(j-1,tmpData.get(i).get(j));
                        //)[i - 1][j - 1] = tmpData[i][j];
                    } else if (i > min) {
                        Data.get(i-1).set(j,tmpData.get(i).get(j));
                        //)[i - 1][j] = tmpData[i][j];
                    } else if (j > min) {
                        Data.get(i).set(j-1,tmpData.get(i).get(j));
                        //[i][j - 1] = tmpData[i][j];
                    } else if (i < min && j < min) {
                        Data.get(i).set(j,tmpData.get(i).get(j));
                        //[i][j] = tmpData[i][j];
                    }
                }
            }
        }
        ArrayList<ArrayList<Double>> Temp = initQuadraticWithValue(tmpData.size()-1,Double.NaN);
        for(int i=0;i<tmpData.size()-1;i++){
            for(int j=0;j<tmpData.size()-1;j++){
                Temp.get(i).set(j,Data.get(i).get(j));
            }
        }
        Data = new ArrayList<ArrayList<Double>>();
        Data = Temp;
    }

    private ArrayList<ArrayList<Double>> format(ArrayList<ArrayList<String>> matrix) {

        ArrayList<ArrayList<Double>> doubleMatrix = new ArrayList<ArrayList<Double>>();

        for (int i = 0; i < matrix.size(); i++) {
            ArrayList<Double> temp = new ArrayList<Double>();
            for (int j = 0; j < matrix.get(i).size(); j++) {
                //doubleMatrix[i][j] = Double.parseDouble(matrix[i][j]);
                temp.add(Double.parseDouble(matrix.get(i).get(j)));
            }
            doubleMatrix.add(temp);
        }

        return doubleMatrix;
    }

    public static ArrayList<ArrayList<Double>> initQuadraticWithValue(int size, double value) {

        ArrayList<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < size; i++) {
            ArrayList<Double> temp = new ArrayList<Double>();
            for (int j = 0; j < size; j++) {
                temp.add(value);
            }
            matrix.add(temp);
        }

        return matrix;
    }

    public static Point findLowestValuePoint(ArrayList<ArrayList<Double>> matrix) {

        int n = matrix.size(); //18
        Point minPoint = new Point(0, 0, Double.MAX_VALUE);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i != j) {
                    double distance = matrix.get(i).get(j);
                    //Log.i("distance of:"+ i+"and"+j,String.valueOf(distance));
                    if (distance < minPoint.getValue()) {
                        minPoint = new Point(i, j, distance);
                    }
                }
            }
        }

        return minPoint;
    }

    public void print() {

        for (int i = 0; i < Data.size(); i++) {

            for (int j = 0; j < Data.get(i).size(); j++) {
                System.out.print(Data.get(i).get(j));
                Log.i("Data.get("+i+").get("+j+")",String.valueOf(Data.get(i).get(j)));
            }
        }
    }
}
