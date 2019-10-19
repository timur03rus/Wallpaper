package com.example.wallpaper.adapter;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.wallpaper.R;
import com.example.wallpaper.model.HitsList;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;

public class HitsAdapter extends RecyclerView.Adapter<HitsAdapter.HitsViewHolder> {

    Context context;
    HitsList hitList;

    public HitsAdapter(Context context, HitsList hitList) {
        this.context = context;
        this.hitList = hitList;
    }

    @NonNull
    @Override
    public HitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hit_item, parent, false);

        return new HitsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HitsViewHolder holder, int position) {
        Glide.with(context).load(hitList.getHits().get(position).getWebformatURL()).into(holder.imageView);

        holder.imageView.setOnLongClickListener(v -> {

            AlertDialog.Builder materialAlertDialogBuilder = new AlertDialog.Builder(context)
                    .setTitle("Title")
                    .setMessage("Вы точно хотите установить картинку как обои?")
                    .setPositiveButton("Да", (dialog, which) ->
                            Glide.with(context)
                            .asBitmap()
                            .load(hitList.getHits().get(position).getWebformatURL())
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    try {
                                        WallpaperManager.getInstance(context).setBitmap(resource);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            })).setNegativeButton("Нет", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = materialAlertDialogBuilder.create();
            alertDialog.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return hitList.getHits().size();
    }

    public class HitsViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public HitsViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
        }
    }
}
