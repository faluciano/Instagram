package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.Post;
import com.example.instagram.PostsAdapter;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {

    public static final String TAG = "PostsFragmnet";
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    protected SwipeRefreshLayout swipeContainer;
    protected boolean loading = true;
    int pastVisibleItems,visibleItemCount,totalItemcount;

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(),allPosts);
        rvPosts.setAdapter(adapter);
        LinearLayoutManager postLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(postLayoutManager);
        queryPosts();

        swipeContainer.setOnRefreshListener(() -> {
            adapter.clear();
            totalItemcount = 0;
            loading = false;
            queryPosts();
        });

        rvPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0){
                    visibleItemCount = postLayoutManager.getChildCount();
                    totalItemcount = postLayoutManager.getItemCount();
                    pastVisibleItems = postLayoutManager.findFirstCompletelyVisibleItemPosition();

                    if(loading){
                        if((visibleItemCount+pastVisibleItems)>=totalItemcount){
                            loading = false;
                            queryPosts();
                        }
                    }
                }
            }
        });
    }
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.setSkip(totalItemcount);
        query.findInBackground((posts, e) -> {
            if (e!=null){
                Log.e(TAG,"Query wrong",e);
                return;
            }
            for(Post post : posts){
                Log.i(TAG,"Post: "+post.getDescription()+", username: "+post.getUser().getUsername());
            }
            swipeContainer.setRefreshing(false);
            if(loading){
                Log.i(TAG,"posts cleared");
                adapter.clear();
            }
            adapter.addAll(posts);
            loading = true;
        });
    }
}