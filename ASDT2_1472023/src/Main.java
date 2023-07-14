import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String url = "https://dummyjson.com/products/category/smartphones";
        String consId = "harber123";
        String userKey = "_tabc4XbR";

        try {
            String json = fetchDataFromUrl(url, consId, userKey);
            System.out.println("JSON Response:");
            System.out.println(json);

            List<Product> products = parseJson(json);
            System.out.println("Daftar Produk:");
            for (Product product : products) {
                System.out.println(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String fetchDataFromUrl(String urlString, String consId, String userKey) throws IOException {
        StringBuilder response = new StringBuilder();

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Cons-ID", consId);
        connection.setRequestProperty("X-userkey", userKey);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();

        return response.toString();
    }

    private static List<Product> parseJson(String json) {
        List<Product> products = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("id");
                String title = jsonObject.getString("title");
                String description = jsonObject.getString("description");
                double price = jsonObject.getDouble("price");
                double discountPercentage = jsonObject.getDouble("discountPercentage");
                double rating = jsonObject.getDouble("rating");
                int stock = jsonObject.getInt("stock");
                String brand = jsonObject.getString("brand");
                String category = jsonObject.getString("category");
                String thumbnail = jsonObject.getString("thumbnail");

                List<String> images = new ArrayList<>();
                JSONArray imagesArray = jsonObject.getJSONArray("images");
                for (int j = 0; j < imagesArray.length(); j++) {
                    String imageUrl = imagesArray.getString(j);
                    images.add(imageUrl);
                }

                Product product = new Product(id, title, description, price, discountPercentage, rating, stock, brand, category, thumbnail, images);
                products.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return products;
    }
}