package com.test.my.excutortest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ImageListActivity extends AppCompatActivity {
    public static final String TAG = ImageListActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        String img1 = "http://img002.21cnimg.com/photos/album/20150702/m600/BAED6B1806F805B809928FCA075D7A87.jpeg";
        String img2 = "http://img003.21cnimg.com/photos/album/20150702/m600/5980E492BB5F914964BFA2CC704D8F7B.jpeg";
        String img3 = "http://img002.21cnimg.com/photos/album/20150702/m600/9B38E3A95AB7194A065C57A10C7F761F.jpeg";
        String img4 = "http://img002.21cnimg.com/photos/album/20150702/m600/2D79154370E073A2BA3CD4D07868861D.jpeg";
        String img5 = "http://img001.21cnimg.com/photos/album/20150702/m600/6DF24E3EC442DDB3B299766314DC3BD8.jpeg";
        String img6 = "http://img003.21cnimg.com/photos/album/20150702/m600/97487C8B6DBF3AE71F8E01486FB1818D.jpeg";
        String img7 = "http://img001.21cnimg.com/photos/album/20150702/m600/C139B3782BF290B0A20E482191969974.jpeg";
        String img8 = "http://img002.21cnimg.com/photos/album/20150702/m600/8CC910F7D1BDA768D574F4F4C4C27D9C.jpeg";
        String img9 = "http://img003.21cnimg.com/photos/album/20150702/m600/9027553A6AC8368134F52221D3D87656.jpeg";
        String img10 = "http://img001.21cnimg.com/photos/album/20150702/m600/D5D096903FB243E26F0174B40D6BFAB0.jpeg";
        String img11 = "http://img001.21cnimg.com/photos/album/20150702/m600/F6DA392C856D17EAC2B1FAF757DCDF6F.jpeg";
        String img12 = "http://img001.21cnimg.com/photos/album/20150702/m600/F19BE9EEE072F47EFC9E6304E891600D.jpeg";

        String[] arr = new String[]{img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12};

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new MyAdapter(arr));
    }

    private class MyAdapter extends RecyclerView.Adapter<MyVH> {
        private String[] arr;

        MyAdapter(String[] arr) {
            this.arr = arr;
        }

        @Override
        public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, null);

            return new MyVH(parent.getContext(), view);
        }

        @Override
        public void onBindViewHolder(final MyVH holder, final int position) {
            final String url = arr[position];

            final int width = holder.view.getWidth();

            Transformation transformation = new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    if (source != null && width != 0) {
                        if (source.getWidth() >= width) {
                            try {
                                float scale = source.getHeight() / (float) source.getWidth();
                                int newHeight = (int) (scale * width);
                                Log.d(TAG, "width:" + source.getWidth() + " ,height:" + source.getHeight() + ";new width:" + width + " ,new height:" + newHeight);
                                Bitmap newSource = Bitmap.createScaledBitmap(source, width, newHeight, false);
                                if (newSource != null) {
                                    if (newSource != source) {
                                        source.recycle();
                                    }
                                    return newSource;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d(TAG, "transform error");
                                return source;
                            }
                        }

                    } else
                        Log.d(TAG, "transform null");
                    return source;
                }

                @Override
                public String key() {
                    return "transformation" + " desiredWidth";
                }
            };
            //加载图片
            Picasso.with(holder.context).load(url).transform(transformation).into(holder.imageView);


            //预览图片
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.context, ImageDetailActivity.class);
                    intent.putExtra(ImageDetailActivity.url_extra, url);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arr == null ? 0 : arr.length;
        }
    }

    private class MyVH extends RecyclerView.ViewHolder {
        private View view;
        private Context context;
        private ImageView imageView;
        private CardView cardView;

        public MyVH(Context context, View itemView) {
            super(itemView);
            this.view = itemView;
            this.context = context;
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }
}
