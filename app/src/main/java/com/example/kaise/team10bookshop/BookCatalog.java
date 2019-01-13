package com.example.kaise.team10bookshop;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;

public class BookCatalog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_catalog);

        new AsyncTask<Void, Void, List<Category>>() {
            @Override
            protected List<Category> doInBackground(Void... params) {
                return Category.listCategories();
            }

            @Override
            protected void onPostExecute(List<Category> result) {
                ListView list = findViewById(R.id.listView1);
                list.setAdapter(new SimpleAdapter(getApplicationContext(), result, R.layout.row2, new String[]{"CategoryId", "Name"}, new int[]{R.id.textView1, R.id.textView2}));
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> av, View view, int position, long id) {
                        Category selected = (Category) av.getAdapter().getItem(position);
                        Intent intent = new Intent(getApplicationContext(), BookList.class);
                        intent.putExtra("id", selected.get("CategoryId"));
                        startActivity(intent);
                    }
                });
            }
        }.execute();
    }
}
