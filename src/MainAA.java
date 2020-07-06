import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Video by Professors Greenwell on Canvas. It was used as reference
 *
 * @author carolinabado
 */
public class MainAA {

  public static void main(String[] args) throws IOException {

    File sampleFile = new File("./src/sampleFile.csv");
    BufferedReader br;

    if (!sampleFile.exists()) {
      //If file doesn't exist sends error message
      System.out.println("File does not exist");
      System.exit(1);
    }
    /**
     * Reader and Line for file reading.
     * @param totalTransactions = total number of transactions.
     */
    br = new BufferedReader(new FileReader(sampleFile));
    String line = null;

    int totalTransactions = 0;

    String[] features = br.readLine().split(",");
    int numberOfFeatures = features.length;

    List<int[]> sampleResults = new ArrayList<>();

    while ((line = br.readLine()) != null) {
      //splits each line into an array then + array to sampleResults
      sampleResults.add(Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray());
      totalTransactions++;
    }
    br.close();

    Map<String, Integer> fullResults = new HashMap<>();
    Map<HashSet<String>, Integer> validResults = new HashMap<>();

    for (int[] sample : sampleResults) {
      for (int premise = 0; premise < numberOfFeatures; premise++) {
        if (sample[premise] == 1) {
          fullResults.put(features[premise], fullResults.getOrDefault(features[premise], 0) + 1);
        }
        for (int conclusion = 0; conclusion < numberOfFeatures; conclusion++) {
          if (conclusion == premise) {
            continue;
          }
          if (sample[conclusion] == 1) {
            validResults
                .put(new HashSet<String>(Arrays.asList(features[premise], features[conclusion])),
                    validResults.getOrDefault(
                        new HashSet<String>(Arrays.asList(features[premise], features[conclusion])),
                        0) + 1);
          }
        }
      }
    }

    for (HashSet<String> featureSet : validResults.keySet()) {
      List<String> featureList = featureSet.stream().collect(Collectors.toList());

      double confidence =
          (double) fullResults.get(featureList.get(0)) / validResults.get(featureSet);
      double support = (double) validResults.get(featureSet) / totalTransactions;

      System.out.printf("We show a confidence of %f that a person who " +
              "bought a book from the %s genre will also buy a book from the %s%n      "
              + "genre and a support of %f that " +
              "a person will buy these items together at all.%n",
          confidence, featureList.get(0), featureList.get(1), support);
    }
  }
}
