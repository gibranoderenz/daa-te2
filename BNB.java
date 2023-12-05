import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BNB {
     public static void main(String[] args) {
        
        int[] lengths = {10};
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

    public static void getElapsedTimes(int datasetLength) {
        int[] dataset = DatasetLoader.loadDataset(datasetLength);
        int startIndex = 0;
        int totalValue = Arrays.stream(dataset).sum();
        int unassignedValue = totalValue;
        boolean[] testAssignment = new boolean[dataset.length];
        int testValue = 0;
        boolean[] bestAssignment = new boolean[dataset.length];
        double[] bestErr = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};

        long start = System.nanoTime();
        List<List<Integer>> partitions = getPartitionResults(dataset, startIndex, totalValue, unassignedValue, testAssignment, testValue, bestAssignment, bestErr);
        long end = System.nanoTime();
        long elapsed = end - start;

        System.out.println("Result: " + partitions);
        System.out.println("Time for Partition Problem with BnB approach (n = " + datasetLength + "): " + elapsed + " ns");
    }

    public static void getMemoryUsage(int datasetLength) {
        int[] dataset = DatasetLoader.loadDataset(datasetLength);
        int startIndex = 0;
        int totalValue = Arrays.stream(dataset).sum();
        int unassignedValue = totalValue;
        boolean[] testAssignment = new boolean[dataset.length];
        int testValue = 0;
        boolean[] bestAssignment = new boolean[dataset.length];
        double[] bestErr = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};

        System.gc(); // Request garbage collection to free up memory
        System.gc(); // Request garbage collection to free up memory
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        
        getPartitionResults(dataset, startIndex, totalValue, unassignedValue, testAssignment, testValue, bestAssignment, bestErr);

        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.gc(); // Request garbage collection to free up memory
        System.gc(); // Request garbage collection to free up memory
        
        long currentSize = usedMemoryAfter - usedMemoryBefore;
        long peakSize = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory usage for Partition Problem with BnB approach (n = " + datasetLength + "):");
        System.out.println("Current Size: " + currentSize + " bytes, Peak Size: " + peakSize + " bytes");
    }

    public static List<List<Integer>> getPartitionResults(int[] values, int startIndex, int totalValue, int unassignedValue, boolean[] testAssignment, int testValue, boolean[] bestAssignment, double[] bestErr) {
        partitionValuesFromIndex(values, startIndex, totalValue, unassignedValue, testAssignment, testValue, bestAssignment, bestErr);
        
        double err = bestErr[0];
        if (err != 0) {
            return new ArrayList<>();
        }

        List<Integer> leftSubset = new ArrayList<>();
        List<Integer> rightSubset = new ArrayList<>();
        for (int i = 0; i < bestAssignment.length; i++) {
            if (bestAssignment[i]) {
                leftSubset.add(values[i]);
            } else {
                rightSubset.add(values[i]);
            }
        }

        List<List<Integer>> result = new ArrayList<>();
        result.add(leftSubset);
        result.add(rightSubset);
        return result;
    }

    // Partition the values with those before index start_index fixed.
    // test_assignment is the test assignment so far.
    // test_value = total value of the first set in test_assignment.
    // Initially best assignment and its error are in
    //     best_assignment and best_err.
    // Update those to reflect any improved solution we find.
    public static void partitionValuesFromIndex(int[] values, int startIndex, int totalValue, int unassignedValue, boolean[] testAssignment, int testValue, boolean[] bestAssignment, double[] bestErr) {
        if (startIndex >= values.length) {
            double testErr = Math.abs(2 * testValue - totalValue);
            if (testErr < bestErr[0]) {
                System.arraycopy(testAssignment, 0, bestAssignment, 0, testAssignment.length);
                bestErr[0] = testErr;
            }
        } else {
            // See if there's any way we can assign
            // the remaining items to improve the solution.
            double testErr = Math.abs(2 * testValue - totalValue);
            if (testErr - unassignedValue < bestErr[0]) {
                // There's a chance we can make an improvement.
                // We will now assign the next item.
                unassignedValue -= values[startIndex];

                // Try adding values[startIndex] to set 1.
                testAssignment[startIndex] = true;
                partitionValuesFromIndex(values, startIndex + 1, totalValue, unassignedValue, testAssignment, testValue + values[startIndex], bestAssignment, bestErr);
                
                // Try adding values[startIndex] to set 2.
                testAssignment[startIndex] = false;
                partitionValuesFromIndex(values, startIndex + 1, totalValue, unassignedValue, testAssignment, testValue, bestAssignment, bestErr);
            }
        }
    }
}