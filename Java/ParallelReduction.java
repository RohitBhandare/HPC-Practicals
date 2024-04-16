import java.util.Arrays;
import java.util.concurrent.*;

public class ParallelReduction {

    public static void main(String[] args) {
        int[] array = {4, 2, 7, 1, 9, 5, 3, 8, 6};

        int min = parallelMin(array);
        int max = parallelMax(array);
        int sum = parallelSum(array);
        double average = parallelAverage(array);

        System.out.println("Array: " + Arrays.toString(array));
        System.out.println("Minimum: " + min);
        System.out.println("Maximum: " + max);
        System.out.println("Sum: " + sum);
        System.out.println("Average: " + average);
    }

    public static int parallelMin(int[] array) {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        int[] result = new int[processors];

        try {
            for (int i = 0; i < processors; i++) {
                int startIndex = i * (array.length / processors);
                int endIndex = (i == processors - 1) ? array.length : (i + 1) * (array.length / processors);
                executor.execute(new MinTask(array, startIndex, endIndex, result, i));
            }
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Arrays.stream(result).min().orElse(Integer.MAX_VALUE);
    }

    public static int parallelMax(int[] array) {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        int[] result = new int[processors];

        try {
            for (int i = 0; i < processors; i++) {
                int startIndex = i * (array.length / processors);
                int endIndex = (i == processors - 1) ? array.length : (i + 1) * (array.length / processors);
                executor.execute(new MaxTask(array, startIndex, endIndex, result, i));
            }
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Arrays.stream(result).max().orElse(Integer.MIN_VALUE);
    }

    public static int parallelSum(int[] array) {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        int[] result = new int[processors];

        try {
            for (int i = 0; i < processors; i++) {
                int startIndex = i * (array.length / processors);
                int endIndex = (i == processors - 1) ? array.length : (i + 1) * (array.length / processors);
                executor.execute(new SumTask(array, startIndex, endIndex, result, i));
            }
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Arrays.stream(result).sum();
    }

    public static double parallelAverage(int[] array) {
        int sum = parallelSum(array);
        return (double) sum / array.length;
    }

    static class MinTask implements Runnable {
        private final int[] array;
        private final int startIndex;
        private final int endIndex;
        private final int[] result;
        private final int index;

        public MinTask(int[] array, int startIndex, int endIndex, int[] result, int index) {
            this.array = array;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.result = result;
            this.index = index;
        }

        @Override
        public void run() {
            int min = Integer.MAX_VALUE;
            for (int i = startIndex; i < endIndex; i++) {
                min = Math.min(min, array[i]);
            }
            result[index] = min;
        }
    }

    static class MaxTask implements Runnable {
        private final int[] array;
        private final int startIndex;
        private final int endIndex;
        private final int[] result;
        private final int index;

        public MaxTask(int[] array, int startIndex, int endIndex, int[] result, int index) {
            this.array = array;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.result = result;
            this.index = index;
        }

        @Override
        public void run() {
            int max = Integer.MIN_VALUE;
            for (int i = startIndex; i < endIndex; i++) {
                max = Math.max(max, array[i]);
            }
            result[index] = max;
        }
    }

    static class SumTask implements Runnable {
        private final int[] array;
        private final int startIndex;
        private final int endIndex;
        private final int[] result;
        private final int index;

        public SumTask(int[] array, int startIndex, int endIndex, int[] result, int index) {
            this.array = array;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.result = result;
            this.index = index;
        }

        @Override
        public void run() {
            int sum = 0;
            for (int i = startIndex; i < endIndex; i++) {
                sum += array[i];
            }
            result[index] = sum;
        }
    }
}
