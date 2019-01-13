package com.example.kaise.team10bookshop;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.List;

public class BookList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Intent i = getIntent();
        String categoryId = i.getStringExtra("id");

        new AsyncTask<String, Void, List<Book>>() {
            @Override
            protected List<Book> doInBackground(String... params) {

                return Book.listBooksByCategory(params[0]);
            }

            @Override
            protected void onPostExecute(List<Book> result) {
                ListView list = findViewById(R.id.listView2);
                list.setAdapter(new SimpleAdapter(getApplicationContext(), result, R.layout.row, new String[]{"Name", "Author"}, new int[]{R.id.textView1, R.id.textView2}));
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> av, View view, int position, long id) {
                        Book selected = (Book) av.getAdapter().getItem(position);
                        Intent intent = new Intent(getApplicationContext(), BookDetails.class);
                        intent.putExtra("BookID", selected.get("BookID"));
                        startActivityForResult(intent, 123);
                    }
                });
            }
        }.execute(categoryId);
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        if (resultCode == RESULT_OK && requestCode == 123) {
            if (data.hasExtra("edited")) {
                int result = data.getExtras().getInt("edited");
                if (result == 1) {
                    this.recreate();
                    Toast.makeText(getApplicationContext(), "Book updated", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
