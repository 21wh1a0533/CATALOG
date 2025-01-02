import org.json.JSONObject;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ShamirsSecretSharing {
    public static void main(String[] args) {
        try {
            
            String input1 = new String(Files.readAllBytes(Paths.get("input.json")));
            String input2 = new String(Files.readAllBytes(Paths.get("input2.json")));

            JSONObject json1 = new JSONObject(input1);
            JSONObject json2 = new JSONObject(input2);

           
            BigInteger secret1 = findSecret(json1);
            BigInteger secret2 = findSecret(json2);

          
            System.out.println("Secret for Test Case 1: " + secret1);
            System.out.println("Secret for Test Case 2: " + secret2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BigInteger findSecret(JSONObject json) {
        int n = json.getJSONObject("keys").getInt("n");
        int k = json.getJSONObject("keys").getInt("k");

     
        List<BigInteger[]> points = new ArrayList<>();
        for (String key : json.keySet()) {
            if (!key.equals("keys")) {
                int x = Integer.parseInt(key);
                JSONObject point = json.getJSONObject(key);
                int base = point.getInt("base");
                String value = point.getString("value");

                BigInteger y = new BigInteger(value, base);
                points.add(new BigInteger[]{BigInteger.valueOf(x), y});
            }
        }

        
        return lagrangeInterpolation(points, k);
    }

    private static BigInteger lagrangeInterpolation(List<BigInteger[]> points, int k) {
        BigInteger constant = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger xi = points.get(i)[0];
            BigInteger yi = points.get(i)[1];

            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger xj = points.get(j)[0];
                    numerator = numerator.multiply(xj.negate());
                    denominator = denominator.multiply(xi.subtract(xj));
                }
            }

          
            BigInteger li = numerator.divide(denominator);
            constant = constant.add(li.multiply(yi));
        }

        return constant.abs(); 
    }
}
