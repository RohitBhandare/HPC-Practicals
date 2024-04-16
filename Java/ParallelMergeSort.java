import java.util.Arrays;

class Practical02ParallelSorts {
    private static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = Arrays.copyOfRange(arr, left, mid + 1);
        int[] R = Arrays.copyOfRange(arr, mid + 1, right + 1);

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    private static void parallelMergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            Thread leftThread = new Thread(() -> parallelMergeSort(arr, left, mid));
            Thread rightThread = new Thread(() -> parallelMergeSort(arr, mid + 1, right));

            leftThread.start();
            rightThread.start();

            try {
                leftThread.join();
                rightThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            merge(arr, left, mid, right);
        }
    }
    
    private static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] arrCopy = Arrays.copyOf(arr, arr.length);

        System.out.println("Original array: " + Arrays.toString(arr));

        // Create threads for parallel merge sort and bubble sort
        Thread mergeSortThread = new Thread(() -> parallelMergeSort(arr, 0, arr.length - 1));
        Thread bubbleSortThread = new Thread(() -> bubbleSort(arrCopy));

        mergeSortThread.start();
        bubbleSortThread.start();

        try {
            mergeSortThread.join();
            bubbleSortThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Sorted array using parallel merge sort: " + Arrays.toString(arr));
        System.out.println("Sorted array using parallel bubble sort: " + Arrays.toString(arrCopy));
    }
}
