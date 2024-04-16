import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelMatrixMultiplication {

    public static void main(String[] args) {
        int[][] matrix1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[][] matrix2 = {{9, 8, 7}, {6, 5, 4}, {3, 2, 1}};
        int[][] result = parallelMatrixMultiply(matrix1, matrix2);
        printMatrix(result);
    }

    public static int[][] parallelMatrixMultiply(int[][] matrix1, int[][] matrix2) {
        int m = matrix1.length;
        int n = matrix2[0].length;
        int[][] result = new int[m][n];
        
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);

        try {
            Future<?>[] futures = new Future[m];
            for (int i = 0; i < m; i++) {
                futures[i] = executor.submit(new MatrixRowMultiplier(matrix1, matrix2, result, i));
            }

            for (Future<?> future : futures) {
                future.get(); // Wait for all tasks to complete
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return result;
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    static class MatrixRowMultiplier implements Callable<Void> {
        private final int[][] matrix1;
        private final int[][] matrix2;
        private final int[][] result;
        private final int row;

        public MatrixRowMultiplier(int[][] matrix1, int[][] matrix2, int[][] result, int row) {
            this.matrix1 = matrix1;
            this.matrix2 = matrix2;
            this.result = result;
            this.row = row;
        }

        @Override
        public Void call() {
            for (int j = 0; j < matrix2[0].length; j++) {
                int sum = 0;
                for (int k = 0; k < matrix2.length; k++) {
                    sum += matrix1[row][k] * matrix2[k][j];
                }
                result[row][j] = sum;
            }
            return null;
        }
    }
}
