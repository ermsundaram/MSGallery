package com.androidprofessional.gallery.msgallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView mList;
    ArrayList<HashMap<String, Object>> allImagesList;
    SparseBooleanArray selectedImgsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadApps();
        setContentView(R.layout.activity_main);
        allImagesList = GalleryUtils.loadImages(this);
        mList = (ListView) findViewById(R.id.myList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void callGallery(View view) {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ArrayList imgLists = GalleryActivity.selectedImgsList;
                mList.setAdapter(new AppsAdapter(imgLists));
            }
        }
    }

    public class AppsAdapter extends BaseAdapter {
        ArrayList imgLists;

        public AppsAdapter(ArrayList imageLists) {
            imgLists = imageLists;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout l;
            ImageView galleryImageView;
            if (convertView == null) {
                galleryImageView = new ImageView(MainActivity.this);
                //galleryImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                galleryImageView.setLayoutParams(new ViewGroup.LayoutParams(120, 120));
                l = new RelativeLayout(MainActivity.this);
                l.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT,
                        GridView.LayoutParams.WRAP_CONTENT));
                l.addView(galleryImageView);
            } else {
                l = (RelativeLayout) convertView;
                galleryImageView = (ImageView) l.getChildAt(0);
            }
            String filePath = imgLists.get(position).toString();
            Uri mImageUri = Uri.fromFile(new File(filePath));
            Picasso.with(MainActivity.this).load(mImageUri).resize(100, 100).into(galleryImageView);
            return l;
        }

        public final int getCount() {
            return imgLists.size();
        }

        public final Object getItem(int position) {
            return imgLists.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }

}