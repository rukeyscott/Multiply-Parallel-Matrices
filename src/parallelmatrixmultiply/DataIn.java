/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parallelmatrixmultiply;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import static java.util.concurrent.ForkJoinTask.invokeAll;
import java.util.concurrent.RecursiveAction;

/**
 *
 * @author Scott Purcell
 * Project 6 ParallelMatrixMultiply
 * cs 3259
 * 11/19/2014
 */
public class DataIn {

    String returnValue = "";

    int xChar;
    int yChar;
            
    double matrixDouble;
    double matrixDouble2;
    char c;
    int iSize = 0;
    int iSize2 = 0;
    int jSize = 0;
    int jSize2 = 0;
    String line = "";
    String temp = "";
    String line2 = "";
    FileReader file = null;
    FileReader file2 = null;
    public int place[][];
    
    int size = 0;
    final static int SIZE = 10000000;
    public static double[][] list1;
    public static double[][] list2;
    
    
    public  String setMatrix(String fileArg,String fileArg2) {

      //System.out.println("file ags1 "+ args[0]);
        //System.out.println("file ags2 "+ args[1]);
        
        int i = 0;

        try {
            file = new FileReader(fileArg);
            BufferedReader reader = new BufferedReader(file);
            file2 = new FileReader(fileArg2);
            BufferedReader reader2 = new BufferedReader(file2);

            // get first line and turn it into an int, set to variables iSize and jSize
            int res = 0;
            line = reader.readLine();
            Scanner in = new Scanner(line);
            in.useLocale(Locale.US);
            //System.out.println("Start Scanner\n");
            iSize = in.nextInt();
            jSize = in.nextInt();

            line2 = reader2.readLine();
            Scanner in2 = new Scanner(line2);
            in2.useLocale(Locale.US);
            //System.out.println("Start Scanner\n");
            iSize2 = in2.nextInt();
            jSize2 = in2.nextInt();

           //System.out.println("Scanner read in the size of the matrix");
            //Scanner scanner = new Scanner(line);
            list1 = new double[iSize][jSize];
            list2 = new double[iSize2][jSize2];
           //System.out.println("Created Matrix");
            // loop for the whole scanner

            line = reader.readLine();
            line2 = reader2.readLine();
            //add line of int to a 2d array then increment row
            for (int k = 0; k < iSize; k++) {
                  //System.out.println("\nrow: "+k );

                String[] parts = line.split(" ");
                String[] parts2 = line2.split(" ");
                for (int q = 0; q < jSize; q++) {
                    matrixDouble = Double.parseDouble(parts[q]);
                    list1[k][q] = matrixDouble;
                    matrixDouble2 = Double.parseDouble(parts2[q]);
                    list2[k][q] = matrixDouble2;
                    //System.out.print(" " + matrixDouble);
                }
                if (k < iSize) {
                    line = reader.readLine();
                    line2 = reader2.readLine();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    // Ignore issues during closing 
                }
            }

        }
        //System.out.println("End of setMatrix");
        String ret=timer(list1, list2);
        return ret;

    }//end of setMatrix

    public double[][] sequentialMatrixMultiplication(double[][] m1, double[][] m2) {
        int m1ColLength = m1[0].length; // m1 columns length
        int m2RowLength = m2.length;    // m2 rows length
        if (m1ColLength != m2RowLength) {
            return null; // matrix multiplication is not possible
        }
        int mRRowLength = m1.length;    // m result rows length
        int mRColLength = m2[0].length; // m result columns length
        double[][] mResult = new double[mRRowLength][mRColLength];
        for (int i = 0; i < mRRowLength; i++) {         // rows from m1
            for (int j = 0; j < mRColLength; j++) {     // columns from m2
                for (int k = 0; k < m1ColLength; k++) { // columns from m1
                    mResult[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return mResult;
    }

    public String timer(double[][] list1, double[][] list2) {
        double[][] c = new double[list1.length][list1.length];
        //System.out.println("Start MatrixMaker");
        // time parallel sort
        
        long startTime1 = System.currentTimeMillis();
        c = parallelMultiplication(list1, list2, c);
        //System.out.println("Done with parallel fork/join");
        long endTime1 = System.currentTimeMillis();
        
        

        // time sequential sort
        long startTime2 = System.currentTimeMillis();

        double[][] list3 = sequentialMatrixMultiplication(list1, list2);//send it to an array
        long endTime2 = System.currentTimeMillis();

        String ret=("\nProject 6 Parallel Processing by scott Purcell\nPARALLEL ALGORITHM\nC(1,1)= "+c[1][1]+"\nParallel time is: " + (endTime1 - startTime1)+
                ( " milliseconds\nThe number of processors is " + Runtime.getRuntime().availableProcessors()+ "\n\nSEQUENTIAL ALGORITHM\nC(1,1)= " + list3[1][1] + "\nSequential time is "+(endTime2 - startTime2) + " milliseconds"));
                return ret;
        
    }

    public double[][] parallelMultiplication(double[][] list1, double[][] list2, double[][] list3) {

        RecursiveAction mainTask = new Multiply(list1, list2, list3);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(mainTask);
        return list3;

    }

    private class Multiply extends RecursiveAction {

        private double[][] list1;
        private double[][] list2;
        private double[][] list3;

        Multiply(double[][] list1, double[][] list2, double[][] list3) {
            this.list1 = list1;
            this.list2 = list2;
            this.list3 = list3;
        }

        @Override
        public void compute() {
            RecursiveAction[] mainTask = new RecursiveAction[list1.length];

            for (int i = 0; i < list1.length; i++) {
                mainTask[i] = new OneRowRecursion(i);
            }
            invokeAll(mainTask);

        }

        public class OneRowRecursion extends RecursiveAction {

            private int row;

            public OneRowRecursion(int row) {
                this.row = row;
            }

            @Override
            public void compute() {
                for (int q = 0; q < list1[0].length; q++) {
                    list3[row][q] = 0;
                    for (int k = 0; k < list1[0].length; k++) {
                        list3[row][q] += list1[row][k] * list2[k][q];
                    }
                }
            }

        }

    }

}
