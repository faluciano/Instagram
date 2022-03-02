package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private EditText etDescription;
    private Button btnPicture;
    private ImageView ivPicture;
    private Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDescription = findViewById(R.id.etDescription);
        btnPicture = findViewById(R.id.btnPicture);
        ivPicture = findViewById(R.id.ivPicture);
        btnSubmit = findViewById(R.id.btnSubmit);
        
        queryPosts();
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e!=null){
                    Log.e(TAG,"Query wrong",e);
                    return;
                }
                for(Post post : posts){
                    Log.i(TAG,"Post: "+post.getDescription()+", username: "+post.getUser().getUsername());
                }
            }
        });
    }
}