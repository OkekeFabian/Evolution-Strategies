import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        int dimension = 3;
        int t = 0;
        int lambda = 1000;
        double accuracy = 0.001;
        double[][] population = new double[lambda][2 * dimension + 1];
        double[][] populationT1 = new double[lambda][2 * dimension + 1];
        double[][] populationT2;
        double[][] offSpring = new double[2 * lambda][2 * dimension + 1];
        int row = 2 * dimension + 1;
        double t2[];
        System.out.println("lambda of " + lambda);
        System.out.println("With accuracy of " + accuracy);

        fillMatrix(population, row, lambda);

        populationT2 = population;
        System.out.println("a\t" + "b\t" + "c \t" + "finest value\t");
        do {
            fillNextPopulation(populationT1, population, lambda, row + 1);
            int colOfSpring = 2 * lambda;
            offSpring(offSpring, populationT1, population, colOfSpring, 2 * dimension + 1);
            System.out.printf("%d)\t", t + 1);
            t2 = bestSolution(offSpring, colOfSpring);
            Double[][] temp = toDoubleArray(offSpring);
            sortOffspring(temp);
            offSpring = toPrimitiveDoubleArray(temp);
            updateParent(populationT2, offSpring, lambda, row);
            t++;
        } while (t < 30);
        System.out.printf("It took %d iteration\n", t);
        System.out.println(Arrays.toString(t2));
        copyToFile(t2[0], t2[1], t2[2], String.valueOf(t));
    }

    private static void fillMatrix(double[][] population, int dimension, int lambda) {
        Random random = new Random();
        for (int i = 0; i < lambda; i++) {
            for (int j = 0; j < dimension; j++) {
                if (j > 2) {
                    population[i][j] = random.nextDouble() * 10;
                } else {
                    double r = random.nextDouble();
                    population[i][j] = -10.000 + (r * (10.00 + 10.000));
                    // range from -10 to 10
                }
            }
            population[i][6] = check(population[i][0], population[i][1], population[i][2]);
        }
    }

    private static void fillNextPopulation(double[][] populationT1,
                                           double[][] population,
                                           int col, int row) {
        int dimension = 3;
        double tau1 = 1.0 / Math.sqrt(2 * dimension);
        double tau2 = 1.0 / Math.sqrt(2 * Math.sqrt(dimension));
        Random random = new Random();

        for (int i = 0; i < col; i++) {
            Random randn = new Random();
            double R = randn.nextGaussian();
            for (int j = 0; j < row; j++) {
                if (j < 3) {
                    double normalRandom = random.nextGaussian();
                    //perform operation for ai(t+1),bi(t+1),ci(t+1)
                    populationT1[i][j] = population[i][j]
                            + (population[i][j + 3] * normalRandom);
                } else if (j < 6) {
                    populationT1[i][j] = population[i][j] *
                            Math.exp(tau1 * R) *
                            Math.exp(tau2 * random.nextGaussian());
                }
            }
            populationT1[i][6] = check(populationT1[i][0], populationT1[i][1], populationT1[i][2]);
        }

    }

    private static double readFileAndOperate(double a, double b, double c) {
        Scanner scanner;
        double function = 0;
        try {
            scanner = new Scanner(new FileReader("Aldata14.dat"));
            while (scanner.hasNextLine()) {
                Double x = scanner.nextDouble();
                Double y = scanner.nextDouble();
                function = function
                        + Math.pow((y - a * (Math.pow(x, 2) - b * Math.cos(c * Math.PI * x))), 2);
            }
            return function / 101.0;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0.0;
        }

    }

    private static double check(
            double a, double b, double c) {
        return readFileAndOperate(a, b, c);

    }

    private static void offSpring(double[][] offSpring,
                                  double[][] populationT1,
                                  double[][] population
            , int lambda, int col) {
        for (int i = 0; i < lambda; i++) {
            for (int j = 0; j < col; j++) {
                if (i < (lambda / 2)) {
                    offSpring[i][j] = population[i][j];
                } else {
                    offSpring[i][j] = populationT1[i - (lambda / 2)][j];
                }
            }
        }
    }

    private static double[] bestSolution(double[][] offSpring, int row) {

        double data[] = new double[4];
        int index = leastNumber(offSpring, row);
        data[0] = offSpring[index][0];
        data[1] = offSpring[index][1];
        data[2] = offSpring[index][2];
        data[3] = offSpring[index][6];
        System.out.printf("%.2f|\t", data[0]);
        System.out.printf(" %.2f|\t", data[1]);
        System.out.printf(" %.2f|\t", data[2]);
        System.out.printf(" %.2f|\t", data[3]);
        System.out.println();
        System.out.println("----------------------------------------------------");
        return data;
    }

    private static int leastNumber(double[][] twoDMatrix, int row) {
        int lowest = 0;
        double minn = twoDMatrix[0][6];

        for (int i = 0; i < row; i++) {
            if (minn > twoDMatrix[i][6]) {
                lowest = i;
                minn = twoDMatrix[i][6];
            }
        }
        return lowest;
    }

    private static void updateParent(double[][] parent,
                                     double[][] offSpring, int row, int col) {
        for (int i = 0; i < row; i++) {
            System.arraycopy(offSpring[i], 0, parent[i], 0, col);
        }
    }

    private static Double[][] toDoubleArray(double[][] values) {
        Double[][] objArray = new Double[values.length][];

        for (int i = 0; i < values.length; i++) {
            objArray[i] = new Double[values[i].length];
            for (int j = 0; j < values[i].length; j++) {
                objArray[i][j] = values[i][j];
            }
        }

        return objArray;
    }

    private static double[][] toPrimitiveDoubleArray(Double[][] values) {
        double[][] objArray = new double[values.length][];

        for (int i = 0; i < values.length; i++) {
            objArray[i] = new double[values[i].length];
            for (int j = 0; j < values[i].length; j++) {
                objArray[i][j] = values[i][j];
            }
        }

        return objArray;
    }

    private static void sortOffspring(Double[][] toFilter) {
        Arrays.sort(toFilter, (o1, o2) -> {
            Double numOfKeys1 = o1[6];
            Double numOfKeys2 = o2[6];
            return numOfKeys1.compareTo(numOfKeys2);
        });


    }

    private static void copyToFile(double a, double b, double c, String iteration) {
        double y;
        Scanner scanner;
        FileWriter fileWriter = null;
        try {
            scanner = new Scanner(new FileReader("Aldata14.dat"));
            while (scanner.hasNextLine()) {
                Double x = scanner.nextDouble();
                scanner.nextLine();
                y = a * (Math.pow(x, 2) - b * Math.cos(c * Math.PI * x));
                try {
                    String filename = "result" + iteration + ".dat";
                    fileWriter = new FileWriter(filename, true);
                    fileWriter.append(String.valueOf(y)).append("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        assert fileWriter != null;
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
