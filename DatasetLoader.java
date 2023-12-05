import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatasetLoader {
    public static int[] loadDataset(int datasetLength) {
        List<Integer> dataset = new ArrayList<>();
        try {
            java.nio.file.Path path = java.nio.file.Paths.get("dataset/" + datasetLength + ".txt");
            java.util.List<String> lines = java.nio.file.Files.readAllLines(path);
            for (String line : lines) {
                dataset.add(Integer.parseInt(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[] convertedDataset = dataset.stream().mapToInt(Integer::intValue).toArray();
        return convertedDataset;
    }
}
