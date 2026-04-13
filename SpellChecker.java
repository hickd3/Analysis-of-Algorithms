import java.io.*;
import java.util.*;

public class SpellChecker{

    public static int editDistance(String w1, String w2) {
        int m = w1.length();
        int n = w2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (w1.charAt(i - 1) == w2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        return dp[m][n];
    }

    public static Map<String, List<String>> spellCheck(String T, Set<String> D) {
        Map<String, List<String>> suggestions = new HashMap<>();
        String[] w_T = T.split("\\s+");

        for (String w : w_T) {
            if (!D.contains(w)) {
                List<Map.Entry<String, Integer>> distances = new ArrayList<>();
                for (String dictWord : D) {
                    int distance = editDistance(w, dictWord);
                    distances.add(new AbstractMap.SimpleEntry<>(dictWord, distance));
                }

                distances.sort(Comparator.comparingInt(Map.Entry::getValue));
                List<String> topSuggestions = new ArrayList<>();
                for (int i = 0; i < Math.min(5, distances.size()); i++) {
                    topSuggestions.add(distances.get(i).getKey());
                }
                suggestions.put(w, topSuggestions);
            }
        }
        return suggestions;
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: java SpellChecker <dictionaryFile1> <dictionaryFile2> <textFile>");
            return;
        }

        Set<String> D = new HashSet<>();
        loadDictionary(args[0], D);
        loadDictionary(args[1], D);

        StringBuilder text = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(args[2]))) {
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line).append(" ");
            }
        } catch (IOException e) {
            System.err.println("Error reading the text file: " + e.getMessage());
            return;
        }

        Map<String, List<String>> result = spellCheck(text.toString(), D);

        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            System.out.println("Suggestions for '" + entry.getKey() + "': " + String.join(", ", entry.getValue()));
        }
    }

    public static void loadDictionary(String dictionaryFilePath, Set<String> dictionary) {
        try (BufferedReader br = new BufferedReader(new FileReader(dictionaryFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                dictionary.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Error reading the dictionary file " + dictionaryFilePath + ": " + e.getMessage());
        }
    }
}