package com.example.gls_gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Image_List> image_list;
    CustomItemClickListener listener;
    public int DisplaySize;

    public ImageAdapter(ArrayList<Image_List> image_list, CustomItemClickListener listener) {
        this.image_list = image_list;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if(!image_list.get(position).getFolderName().equals("")){
            return 0;
        }
        else
        if(!image_list.get(position).getImageName().equals("")){
            return 1;
        }
        else
            return 2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == 0)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.folder_layout,viewGroup,false);
            return new ViewHolder1(v);
        }
        else
        if(i == 1){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_layout,viewGroup,false);
            DisplaySize = viewGroup.getMeasuredWidth();
            return new ViewHolder2(v);
        }
        else
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout,viewGroup,false);
            return new ViewHolder3(v);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder.getItemViewType() == 0){
            String validateimagename  = image_list.get(i).getFolderName();
            //validasi untuk nama folder yang terlalu panjang
            if(validateimagename.length() > 12){
                validateimagename = validateimagename.substring(0,12)+"...";
            }
            ((ViewHolder1)viewHolder).folderName.setText(validateimagename);
        }
        else if(viewHolder.getItemViewType() == 1)
        {
            //load image dengan menggunakan Picasso
            Picasso.get().load(image_list.get(i).getImagePath()).resize(500,500).centerCrop().into(((ViewHolder2)viewHolder).image);

            //atur ukuran gambar
            float width = DisplaySize/2;
            ((ViewHolder2) viewHolder).image.getLayoutParams().height = Math.round(4*width/5);
            ((ViewHolder2) viewHolder).image.requestLayout();

            //validasi untuk nama file gambar yang terlalu panjang
            String validateimagename = image_list.get(i).getImageName();
            if(validateimagename.length() > 18){
                validateimagename = validateimagename.substring(0,18)+"...";
            }
            ((ViewHolder2)viewHolder).imageName.setText(validateimagename);


        }
    }

    @Override
    public int getItemCount() {
        return (image_list == null)? 0:image_list.size();
    }


    public class ViewHolder1 extends RecyclerView.ViewHolder{
        public TextView folderName;
        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folderName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view,getPosition());
                }
            });
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView imageName;
        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageLayout);
            imageName = itemView.findViewById(R.id.imageName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view,getPosition());
                }
            });
        }
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder{
        public ViewHolder3(@NonNull View itemView) {
            super(itemView);
        }
    }

}


