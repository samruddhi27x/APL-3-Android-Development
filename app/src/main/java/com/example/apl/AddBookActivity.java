package com.example.apl;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddBookActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etAuthor;
    private EditText etGenre;
    private EditText etContactInfo;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etGenre = findViewById(R.id.etGenre);
        etContactInfo = findViewById(R.id.etContactInfo);
        Button btnSaveBook = findViewById(R.id.btnSaveBook);

        firestore = FirebaseFirestore.getInstance();

        btnSaveBook.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String genre = etGenre.getText().toString().trim();
        String contactInfo = etContactInfo.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author)
                || TextUtils.isEmpty(genre) || TextUtils.isEmpty(contactInfo)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> bookData = new HashMap<>();
        bookData.put("title", title);
        bookData.put("author", author);
        bookData.put("genre", genre);
        bookData.put("ownerContact", contactInfo);
        bookData.put("imageUrl", "");

        firestore.collection("books")
                .add(bookData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Book saved", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
