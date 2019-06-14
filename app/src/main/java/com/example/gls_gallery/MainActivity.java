package com.example.gls_gallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    public static final int REQ_PERMISSION = 120;
    File[] listFileTemp;
    ArrayList<File> listFile, listFolder;
    ArrayList<Image_List> imageLists;
    RecyclerView recyclerView_image;
    ImageAdapter imageAdapter;
    BitmapFactory.Options opts;
    LinearLayout emptyView;
    TextView folderName;
    ImageView navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reqpermission();

        listFile = new ArrayList<>();
        listFolder = new ArrayList<>();
        recyclerView_image = findViewById(R.id.recycler_view);
        emptyView = findViewById(R.id.empty_view);
        folderName = findViewById(R.id.tv_folderName);
        navigator = findViewById(R.id.navigator);
        navigator.setColorFilter(Color.parseColor("#ffffff"));
        imageLists = new ArrayList<>();
        opts = new BitmapFactory.Options();
        recyclerView_image.setHasFixedSize(true);
        recyclerView_image.setLayoutManager(new GridLayoutManager(this,2));

        int reqEX = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(reqEX == PackageManager.PERMISSION_GRANTED){
            checkBasicandAdvanceFolderAvailable();
            GalleryLayout("/Basic");
        }
    }

    public void GalleryLayout(final String path){
        clear();
        final File picture = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+path);

        //ganti judul header app dengan nama folder
        folderName.setText(picture.getName());

        //dapatkan semua file yang ada pada folder
        listFileTemp = picture.listFiles();

        if(picture.getName().equals("Basic") || picture.getName().equals("Advance")){
            //tombol navigation drawer
            navigator.setImageDrawable(getResources().getDrawable(R.drawable.baseline_menu_black_18dp));

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_layout);
            final DrawerLayout drawerLayout = findViewById(R.id.drawer);

            navigator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    int id = menuItem.getItemId();
                    if(id == R.id.Basic){
                        GalleryLayout("/Basic");
                    }
                    else
                    if(id == R.id.Advance){
                        GalleryLayout("/Advance");
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            });

        }
        else
        {
            //tombol back
            navigator.setImageDrawable(getResources().getDrawable(R.drawable.baseline_arrow_back_black_18dp));
            navigator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String backPath = path.replace("/"+picture.getName(),"");
                    GalleryLayout(backPath);
                }
            });
        }

        if(listFileTemp.length == 0){
            //kalau tidak ada file yang ditemukan
            //munculkan emptyView
            recyclerView_image.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else
        {
            //kalau ada file, munculkan recyclerView
            recyclerView_image.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);

            for(int i = 0; i<listFileTemp.length;i++){
                if(listFileTemp[i].getName().endsWith(".jpg") || listFileTemp[i].getName().endsWith(".jpeg") || listFileTemp[i].getName().endsWith(".png")){
                    listFile.add(listFileTemp[i]);
                }
                else
                if(listFileTemp[i].isDirectory()){
                    listFolder.add(listFileTemp[i]);
                }
            }


            //pengurutan listFolder dan listFile
            Collections.sort(listFolder);
            Collections.sort(listFile);


            for(int i=0;i<listFolder.size();i++){
                imageLists.add(new Image_List(listFolder.get(i).getName()));
            }

            if(imageLists.size()%2 == 1){
                imageLists.add(new Image_List(""));
            }

            for(int i = 0 ; i<listFile.size();i++){
                final String imageName = listFile.get(i).getName();
                imageLists.add(new Image_List(imageName,Uri.fromFile(listFile.get(i)).toString()));
            }

            imageAdapter = new ImageAdapter(imageLists,new CustomItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(imageLists.get(position).getFolderName().equals("")){
                        Intent i = new Intent(MainActivity.this,ImageView.class);
                        i.putExtra("imagePath",imageLists.get(position).getImagePath());
                        i.putExtra("imageName",imageLists.get(position).getImageName());
                        startActivity(i);
                    }
                    else{
                        GalleryLayout(path+"/"+imageLists.get(position).getFolderName());
                    }
                }
            });

            recyclerView_image.setAdapter(imageAdapter);
        }
    }

    //untuk hapus semua data sebelumnya
    public void clear(){
        listFile.clear();
        listFolder.clear();
        imageLists.clear();
    }

    //untuk storage permission
    public void reqpermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int reqEX = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (reqEX != PackageManager.PERMISSION_GRANTED) {
                //requestPermission = Asynchronous
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_PERMISSION) {
            int grantResultsLength = grantResults.length;
            if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Storage Permission Granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Storage Permission Denied", Toast.LENGTH_LONG).show();
            }
        }

        checkBasicandAdvanceFolderAvailable();
        GalleryLayout("/Basic");

    }

    public void checkBasicandAdvanceFolderAvailable(){
        File basic = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Basic");
        File advance = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Advance");
        if(!basic.exists()){
            basic.mkdirs();
        }
        if(!advance.exists()){
            advance.mkdirs();
        }
    }

}
