package com.example.kaise.team10bookshop;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Category extends HashMap<String, String> {
    final static String host = "192.168.1.88";
    final static String projName = "team10books";
    static String baseURL;

    static {
        baseURL = String.format("http://%s/%s/api/Category/", host, projName);
    }

    public Category(String categoryId, String categoryName) {
        put("CategoryId", categoryId);
        put("Name", categoryName);
    }

    public static List<Category> listCategories() {
        List<Category> list = new ArrayList<>();
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl(baseURL);
            for (int i = 0; i < a.length(); i++) {
                JSONObject jobj = a.getJSONObject(i);
                list.add(new Category(jobj.getString("CategoryID"), jobj.getString("Name")));
            }
        } catch (Exception e) {
            Log.e("Category", "JSONArray error");
        }
        return (list);
    }

}
