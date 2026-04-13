//import java.util.Arrays;

public class EditDistance{

    public static int editDist(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        
        //mxn array for memoized calc
        int[][] A = new int[m + 1][n + 1];
        
        //Base cases
        for (int i = 0; i <= m; i++) {
            A[i][0] = i; //length of T
        }
        for (int j = 0; j <= n; j++) {
            A[0][j] = j; //length of S
        }
        
        //Fill in the array according to the specifications
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                //check if the very last characters are the same
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    A[i][j] = A[i - 1][j - 1];
                } else {
                    //opt
                    int insert = A[i][j - 1];
                    int delete = A[i - 1][j];
                    int replace = A[i - 1][j - 1];
                    A[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
                }
            }
        }
        System.out.println("Array");
        for (int i =0; i <= m; i++){
            for (int j= 0; j <=n; j++){
                System.out.println(A[i][j] + "\t");
            }
            System.out.println();
        }
        
        // The bottom-right cell contains the edit distance for s1 and s2
        return A[m][n];
    }
    // sample
    public static void main(String[] args) {
        String s1 = "dean";
        String s2 = "alice";

        long startTime = System.currentTimeMillis();
        int result = editDist(s1, s2);
        long endTime = System.currentTimeMillis();
        
        System.out.println("Edit Distance: " + result);
        System.out.println("Runtime (in milliseconds): " + (endTime - startTime) + " ms");
    }
}