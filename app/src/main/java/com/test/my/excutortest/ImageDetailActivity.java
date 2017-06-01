package com.test.my.excutortest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ImageDetailActivity extends AppCompatActivity {
    public static final String url_extra = "key_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        if (getIntent().hasExtra(url_extra)) {
            String url = getIntent().getStringExtra(url_extra);
            ImageView imageView = (ImageView) findViewById(R.id.imageView2);

            Picasso.with(this).load(url).into(imageView);
        } else
            Toast.makeText(this, "image null", Toast.LENGTH_LONG).show();
    }
}
