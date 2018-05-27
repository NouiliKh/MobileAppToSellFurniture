package com.marketingservice.gomni.furnituremarketingservice.activities;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.marketingservice.gomni.furnituremarketingservice.R;

import com.marketingservice.gomni.furnituremarketingservice.modal.Product;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<Product> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ProductNameEditText;
        TextView DescriptionEditText;
        TextView CategoryEditText;
        TextView priceEditText;
        ImageView PhotoEditText;

        public MyViewHolder(View itemView) {
            super(itemView);
            ProductNameEditText = (TextView) itemView.findViewById(R.id.productName);
            DescriptionEditText = (TextView) itemView.findViewById(R.id.description);
            CategoryEditText = (TextView) itemView.findViewById(R.id.category);
            priceEditText = (TextView) itemView.findViewById(R.id.price);
            PhotoEditText = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public CustomAdapter(List<Product> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_consulting, parent, false);

        view.setOnClickListener(ConsultingActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int i) {

          holder.ProductNameEditText.setText(dataSet.get(i).productname);
          holder.DescriptionEditText.setText(dataSet.get(i).description);
          holder.CategoryEditText.setText(dataSet.get(i).category);
          holder.priceEditText.setText(dataSet.get(i).price);
         Bitmap bitmap = BitmapFactory.decodeByteArray(dataSet.get(i).photo, 0, dataSet.get(i).photo.length);
          holder.PhotoEditText.setImageBitmap(bitmap);

//        textViewName.setText(dataSet.get(listPosition).getName());
//        textViewVersion.setText(dataSet.get(listPosition).getVersion());
//        imageView.setImageResource(dataSet.get(listPosition).getImage());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}