package com.test.my.excutortest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //
    public static final String IMG_URL = "http://img002.21cnimg.com/photos/album/20150702/m600/9B38E3A95AB7194A065C57A10C7F761F.jpeg";
    public static final String IMG_URL2 = "http://img003.21cnimg.com/photos/album/20150702/m600/9027553A6AC8368134F52221D3D87656.jpeg";

    public static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.btn1://周期性单线程服务,每10秒执行一次；模拟心跳包操作

                ScheduledExecutorService executors = Executors.newSingleThreadScheduledExecutor();
                executors.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "down...");
                        downImage(IMG_URL);
                        downImage(IMG_URL2);
                    }
                }, 0, 10 * 1000, TimeUnit.MILLISECONDS);

                break;

            case R.id.btn5:

                ExecutorService single = Executors.newSingleThreadExecutor();
                for (int i = 0; i < 4; i++) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "run--" + Thread.currentThread().getName());
                        }
                    };
                    single.execute(runnable);
                }

                break;

            case R.id.btn2://固定线程数的线程池
                ExecutorService fix = Executors.newFixedThreadPool(3);
                for (int i = 0; i < 4; i++) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "run--" + Thread.currentThread().getName());
                        }
                    };
                    fix.execute(runnable);
                }
                break;

            case R.id.btn4://可变线程数的线程池

                ExecutorService cache = Executors.newCachedThreadPool();
                for (int i = 0; i < 4; i++) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "run--" + Thread.currentThread().getName());
                        }
                    };
                    cache.execute(runnable);
                }

                break;

            case R.id.btn3://AsyncTask顺序线程池

                AsyncTask task = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        try {
                            Thread.sleep(2 * 1000);
                            Log.d(TAG, "run--" + params[0]);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
                task.execute("task1");

                break;

            case R.id.btn6://Picasso加载图片缓存处理

                startActivity(new Intent(this, ImageListActivity.class));

                break;
        }
    }

    private void downImage(String imageUrl) {
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageRequest request = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                Log.d(TAG, "width:" + bitmap.getWidth() + " ,height:" + bitmap.getHeight());
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "down error");
            }
        });
        queue.add(request);
        queue.start();
    }
}
