package com.example.snapcam;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


    public class picActivity extends AppCompatActivity {

        private RecyclerView recyclerView;
        private ImageAdapter adapter;
        
        private List<ImageItem> imageItemList = new ArrayList<>(); // Use List<ImageItem> here

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pic);
            FirebaseApp.initializeApp(this);

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ImageAdapter(this, imageItemList); // Pass the context and the list of ImageItem
            recyclerView.setAdapter(adapter);
            ProgressBar progressBar = findViewById(R.id.progressBar);
            LottieAnimationView animationView = findViewById(R.id.animationView1);
//            LottieAnimationView animationView = findViewById(R.id.animationView1);

            
            FirebaseApp app = FirebaseApp.getApps(this).stream()
                    .filter(existingApp -> existingApp.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                    .findFirst()
                    .orElseGet(() -> FirebaseApp.initializeApp(this, new FirebaseOptions.Builder()
                            .setApiKey("AIzaSyBcEJmwEBgQ0IjVmRQ6uT3cDkG8i9Fcs78")
                            .setProjectId("snapcam-2005")
                            .setApplicationId("1:41101210837:android:4cf69b879cecb5c0df5e5c")
                            .setStorageBucket("snapcam-2005.appspot.com")
                            .build()));

            FirebaseStorage storage = FirebaseStorage.getInstance(app);
            StorageReference storageRef = storage.getReference().child("images"); // Replace "images" with your Firebase Storage path

            storageRef.listAll()
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            int position = 0; // Initialize position to 0

                            for (StorageReference item : listResult.getItems()) {
                                final int finalPosition = position; // Create a final variable for capturing the position

                                item.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    ImageItem imageItem = new ImageItem(imageUrl, finalPosition);
                                    imageItemList.add(imageItem); // Add the ImageItem to the list
                                    adapter.notifyDataSetChanged(); // Notify adapter when new ImageItems are added

                                    // Check if all images have been loaded
                                    if (imageItemList.size() == listResult.getItems().size()) {
                                        // All images have been loaded, hide the progress bar
//                                        myLayout.setVisibility(View.GONE);

                                        animationView.setVisibility(View.GONE);
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }).addOnFailureListener(exception -> {
                                    // Handle any errors that occurred while fetching the download URL
                                    Toast.makeText(picActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                                position++; // Increment position for the next image
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors that occurred while listing the items in the storage
                            Toast.makeText(picActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

// Initially, show the progress bar until the images are loaded
            animationView.playAnimation();
            Log.e("Animation","Animation Started");
            progressBar.setVisibility(View.VISIBLE);
//            animationView.setVisibility(View.VISIBLE);




        }
    }
