package com.example.kaise.team10bookshop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Book extends HashMap<String, String> {
    static String host = "192.168.1.88";
    static String projName = "team10books";
    static String baseURL;
    static String imageURL;

    static {
        baseURL = String.format("http://%s/%s/api", host, projName);
        imageURL = String.format("http://%s/%s/images", host, projName);
    }

    public Book(String id, String name, String ISBN, String author, String stock, String price, String category) {
        put("BookID", id);
        put("Name", name);
        put("ISBN", ISBN);
        put("Author", author);
        put("Stock", stock);
        put("Price", price);
        put("Category", category);
    }

    public static List<Book> listBooksByCategory(String categoryId) {
        List<Book> list = new ArrayList<Book>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/Category/%s", baseURL, categoryId));

        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(new Book(String.valueOf(b.getInt("BookID")),
                        b.getString("Title"),
                        b.getString("ISBN"),
                        b.getString("Author"),
                        String.valueOf(b.getInt("Stock")),
                        String.valueOf(b.getDouble("Price")),
                        String.valueOf(b.getInt("CategoryID")))
                );
            }
        } catch (Exception e) {
            Log.e("Book", "JSONArray error");
        }
        return (list);
    }

    public static Book getBook(String id) {

        try {
            JSONObject a = JSONParser.getJSONFromUrl(String.format("%s/book/%s", baseURL, id));
            Book b = new Book(String.valueOf(a.getInt("BookID")),
                    a.getString("Title"),
                    a.getString("ISBN"),
                    a.getString("Author"),
                    String.valueOf(a.getInt("Stock")),
                    String.valueOf(a.getDouble("Price")),
                    String.valueOf(a.getInt("CategoryID")));
            return b;
        } catch (Exception e) {
            Log.e("Book", "JSONArray error");
        }
        return (null);
    }

    public static Bitmap getPhoto(String isbn) {
        try {
            URL url = new URL(String.format("%s/%s.jpg", imageURL, isbn));
            URLConnection conn = url.openConnection();
            InputStream ins = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(ins);
            ins.close();
            return bitmap;
        } catch (Exception e) {
            Log.e("Book.getBook()", "Bitmap error");
        }
        return null;
    }

    public static void saveBook(Book b) {
        JSONObject jBook = new JSONObject();
        try {
            jBook.put("BookID", Integer.parseInt(b.get("BookID")));
            jBook.put("Title", b.get("Name"));
            jBook.put("CategoryID", Integer.parseInt(b.get("Category")));
            jBook.put("ISBN", b.get("ISBN"));
            jBook.put("Author", b.get("Author"));
            jBook.put("Stock", Integer.parseInt(b.get("Stock")));
            jBook.put("Price", Double.parseDouble(b.get("Price")));
            JSONParser.postStream(baseURL + "/book/update", jBook.toString());
        } catch (Exception e) {
        }
    }
}