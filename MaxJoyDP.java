import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaxJoyDP {
    public static void main(String[] args) {
        // Input values
        int budget = 90;
        int[] prices = {16, 42, 5, 33, 7, 23, 6, 20, 50, 48};
        int[] joys = {67, 19, 1, 18, 6, 17, 3, 14, 24, 23};
        int n = prices.length;

        // DP table: dp[i][b] represents max joy with the first i items and budget b
        int[][] dp = new int[n + 1][budget + 1]; 

        // Fill the DP table
        for (int i = 1; i <= n; i++) {
            for (int b = 0; b <= budget; b++) {
                if (prices[i - 1] > b) {
                    // Can't afford the current candy
                    dp[i][b] = dp[i - 1][b];
                } else {
                    // Max of including or excluding the current candy
                    dp[i][b] = Math.max(dp[i - 1][b], dp[i - 1][b - prices[i - 1]] + joys[i - 1]);
                }
            }
        }

        // Print the DP table
        System.out.println("DP Table (Rows: Items, Columns: Budget):");
        for (int i = 0; i <= n; i++) {
            System.out.println(Arrays.toString(dp[i]));
        }

        // Maximum joy
        System.out.println("\nMaximum Joy: " + dp[n][budget]);

        // Traceback to find the optimal subset
        List<Integer> optimalSubset = new ArrayList<>();
        int b = budget;
        for (int i = n; i > 0 && b > 0; i--) {
            if (dp[i][b] != dp[i - 1][b]) {
                // This item was included
                optimalSubset.add(i - 1); // Store the index (0-based)
                b -= prices[i - 1]; // Reduce the budget by the price of this item
            }
        }

        // Print the traceback
        System.out.println("Items included in the bestCart: " + optimalSubset);
        for (int idx : optimalSubset) {
            System.out.println("Item " + idx + " (Price: " + prices[idx] + ", Joy: " + joys[idx] + ")");
        }
    }
}

