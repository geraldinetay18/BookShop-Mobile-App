package com.example.kaise.team10bookshop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class BookDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        Intent intent = getIntent();
        String id = intent.getStringExtra("BookID");
        new AsyncTask<String, Void, Book>() {
            @Override
            protected Book doInBackground(String... params) {
                return Book.getBook(params[0]);
            }

            @Override
            protected void onPostExecute(Book result) {
                show(result);
            }
        }.execute(id);

        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdit();
                Intent data = new Intent();
                data.putExtra("edited", 1);
                setResult(RESULT_OK, data); // we have finished this Activity
                finish();
            }
        });

    }

    void show(Book book) {
        int[] dest = new int[]{R.id.etBookName, R.id.etBookId, R.id.etISBN, R.id.etCategory, R.id.etAuthor, R.id.etStock, R.id.etPrice};
        String[] src = new String[]{"Name", "BookID", "ISBN", "Category", "Author", "Stock", "Price"};

        for (int i = 0; i < dest.length; i++) {
            EditText text = findViewById(dest[i]);
            text.setText(book.get(src[i]));
        }
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                return Book.getPhoto(params[0]);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                ImageView image = findViewById(R.id.imageView);
                image.setImageBitmap(result);
            }

        }.execute(book.get("ISBN"));
    }

    void saveEdit() {
        int[] src = new int[]{R.id.etBookName, R.id.etBookId, R.id.etISBN, R.id.etCategory, R.id.etAuthor, R.id.etStock, R.id.etPrice};
        String[] dest = new String[7];
        for (int n = 0; n < dest.length; n++) {
            EditText txt = findViewById(src[n]);
            dest[n] = txt.getText().toString();
        }
        Book book = new Book(dest[1], dest[0], dest[2], dest[4], dest[5], dest[6], dest[3]);
        new AsyncTask<Book, Void, Void>() {
            @Override
            protected Void doInBackground(Book... params) {
                Book.saveBook(params[0]);
                return null;
            }
        }.execute(book);
    }
}