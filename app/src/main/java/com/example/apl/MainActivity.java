package com.example.apl;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BookAdapter bookAdapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        FloatingActionButton fabAddBook = findViewById(R.id.fabAddBook);

        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this);
        recyclerViewBooks.setAdapter(bookAdapter);

        firestore = FirebaseFirestore.getInstance();
        fetchBooks();

        fabAddBook.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddBookActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchBooks();
    }

    private void fetchBooks() {
        firestore.collection("books")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Book> books = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Book book = document.toObject(Book.class);
                        if (book.getId() == null || book.getId().isEmpty()) {
                            book.setId(document.getId());
                        }
                        books.add(book);
                    }
                    bookAdapter.setBooks(books);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load books: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
