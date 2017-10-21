package com.androidprofessional.gallery.msgallery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GalleryActivity extends AppCompatActivity {
    GridView mGrid;
    public ArrayList<HashMap<String, Object>> allImagesList;
    static ArrayList selectedImgsList;
    AppsAdapter appsAdapter;
    int selectedCount;
    ActionMode actionMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        allImagesList=GalleryUtils.loadImages(this);
        mGrid = (GridView) findViewById(R.id.myGrid);
        selectedImgsList=new ArrayList();
        appsAdapter=new AppsAdapter();
        mGrid.setAdapter(appsAdapter);
        mGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        mGrid.setMultiChoiceModeListener(new GalleryActivity.MultiChoiceModeListener());
        if(savedInstanceState!=null){
            selectedCount=savedInstanceState.getInt("selectedCount");
            setTitle("" + selectedCount + " items selected");
        }
    }

    public class AppsAdapter extends BaseAdapter {
        public AppsAdapter() {
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            CheckableLayout l;
            ImageView galleryImageView;
            if (convertView == null) {
                galleryImageView = new ImageView(GalleryActivity.this);
                //galleryImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                galleryImageView.setLayoutParams(new ViewGroup.LayoutParams(120, 120));
                l = new GalleryActivity.CheckableLayout(GalleryActivity.this);
                l.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT,
                        GridView.LayoutParams.WRAP_CONTENT));
                l.addView(galleryImageView);
            } else {
                l = (GalleryActivity.CheckableLayout) convertView;
                galleryImageView = (ImageView) l.getChildAt(0);
            }
            String filePath=allImagesList.get(position).get("path").toString();
            Uri mImageUri=Uri.fromFile(new File(filePath));
            Picasso.with(GalleryActivity.this).load(mImageUri).resize(100,100).into(galleryImageView);
            return l;
        }
        public final int getCount() {
            return allImagesList.size();
        }
        public final Object getItem(int position) {
            return allImagesList.get(position);
        }
        public final long getItemId(int position) {
            return position;
        }
    }
    public class CheckableLayout extends FrameLayout implements Checkable {
        private boolean mChecked;
        public CheckableLayout(Context context) {
            super(context);
        }
        public void setChecked(boolean checked) {
            mChecked = checked;
            setForeground(checked ?
                    getResources().getDrawable(R.drawable.blue)
                    : null);
            /*setBackgroundDrawable(checked ?
                    getResources().getDrawable(R.drawable.blue)
                    : null);
*/        }
        public boolean isChecked() {
            return mChecked;
        }
        public void toggle() {
            setChecked(!mChecked);
        }
    }
    public class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {
      @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //mode.setTitle("Select Items");
            //mode.setSubtitle("One item selected");
          MenuInflater menuInflater = mode.getMenuInflater();
          menuInflater.inflate(R.menu.menu_main,menu);
          return true;
        }
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("" + mGrid.getCheckedItemCount() + " items selected");
            return true;
        }
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_ok:
                    //selectedImgsList=new
                    SparseBooleanArray checkedItems = mGrid.getCheckedItemPositions();
                    for(int i =0;i<checkedItems.size();i++){
                        if(checkedItems.valueAt(i) == true){
                            selectedImgsList.add(allImagesList.get(i).get("path"));
                        }
                    }
                    Toast.makeText(GalleryActivity.this,selectedImgsList+" ", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(GalleryActivity.this,MainActivity.class);
                    setResult(RESULT_OK,intent);
                    finish();
                    //appsAdapter.notifyDataSetChanged();
                    return true;
                default:
                    return false;
            }
        }
        public void onDestroyActionMode(ActionMode mode) {
         //mode.setTitle("" + selectedCount + " items selected");
            Log.d("callscall","ondelete");
          mode.finish();
        }
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
                                              boolean checked) {
            selectedCount = mGrid.getCheckedItemCount();
            actionMode=mode;
            switch (selectedCount) {
                case 1:
                    //setTitle("One item selected");
                    mode.setTitle("1 item selected");
                    break;
                default:
                    //setTitle("" + selectCount + " items selected");
                    mode.setTitle("" + selectedCount + " items selected");
                    break;
            }
          //mode.finish();
            //Bundle bundle=new Bundle();
            //bundle.putInt("selected",selectedCount);
            //onSaveInstanceState(bundle);
        }
    }
}
