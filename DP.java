// A dynamic programming based Java program for partition
// problem
// (dengan modifikasi)
import java.util.*;
 
public class DP {
    public static void main(String[] args) {
        int[] lengths = {10, 40, 80};
        for (int length : lengths) {
            System.out.println("=================================================================================");
            System.out.println("Getting elapsed time for length: " + length);
            getElapsedTimes(length);
        }

        for (int length : lengths) {
            System.out.println("=================================================================================");
            System.out.println("Getting memory usage for length: " + length);
            getMemoryUsage(length);
        }
    }
 
    // Returns true if arr[] can be partitioned in two
    // subsets of equal sum, otherwise false
    static List<List<Integer>> findPartition(int arr[], int n)
    {
        int sum = 0;
        int i = 0;
        int j = 0;
        List<List<Integer>> result = new ArrayList<>();
 
        // Calculate sum of all elements
        for (i = 0; i < n; i++)
            sum += arr[i];
 
        if (sum % 2 != 0)
            return result;
 
        boolean part[][] = new boolean[sum / 2 + 1][n + 1];
 
        // initialize top row as true
        for (i = 0; i <= n; i++)
            part[0][i] = true;
 
        // initialize leftmost column, except part[0][0], as
        // 0
        for (i = 1; i <= sum / 2; i++)
            part[i][0] = false;
 
        // Fill the partition table in bottom up manner
        for (i = 1; i <= sum / 2; i++) {
            for (j = 1; j <= n; j++) {
                part[i][j] = part[i][j - 1];
                if (i >= arr[j - 1])
                    part[i][j]
                        = part[i][j]
                          || part[i - arr[j - 1]][j - 1];
            }
        }
 
        // uncomment this part to print table
        // for (i = 0; i <= sum/2; i++)
        // {
        //     for (j = 0; j <= n; j++)
        //         System.out.printf("%-5s", part[i][j]);
        //     System.out.printf("\n");
        // }

        if (!part[sum / 2][n]) {
            return result;
        };

        List<Integer> leftSubset = new ArrayList<>();
        List<Integer> rightSubset = new ArrayList<>();
        
        i--;
        j--;
        while (i >= 0 && j >= 1) {
            boolean shouldInclude = !(part[i][j - 1]);
            if (!shouldInclude) {
                rightSubset.add(arr[j - 1]);
            } else {
                leftSubset.add(arr[j - 1]);
                i = i - arr[j - 1];
            }
            j--;
        }
        result.add(leftSubset);
        result.add(rightSubset);

        return result;
    }

    public static void getElapsedTimes(int datasetLength) {
        final int EPOCH = 10;
        long timeSum = 0;
        int[] dataset = DatasetLoader.loadDataset(datasetLength);
        for (int i = 0; i < EPOCH; i++) {
            long start = System.nanoTime();
            List<List<Integer>> partitions = findPartition(dataset, dataset.length);
            long end = System.nanoTime();
            long elapsed = end - start;
            timeSum += elapsed;

            System.out.println("Epoch " + (i + 1) + " result: " + partitions);
        }
        double avg = (double) timeSum / EPOCH;
        System.out.println("Average time for Partition Problem with DP approach (n = " + datasetLength + "): " + avg + " ns");
    }

    public static void getMemoryUsage(int datasetLength) {
        int[] dataset = DatasetLoader.loadDataset(datasetLength);
        System.gc(); // Request garbage collection to free up memory
        System.gc(); // Request garbage collection to free up memory
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = (runtime.totalMemory() - runtime.freeMemory());
        findPartition(dataset, dataset.length);
        long usedMemoryAfter = (runtime.totalMemory() - runtime.freeMemory());
        System.gc(); // Request garbage collection to free up memory
        System.gc(); // Request garbage collection to free up memory

        long currentSize = usedMemoryAfter - usedMemoryBefore;
        long peakSize = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory usage for Partition Problem with DP approach (n = " + datasetLength + "):");
        System.out.println("Current Size: " + currentSize + " bytes, Peak Size: " + peakSize + " bytes");
    }
}