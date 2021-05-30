package com.example.allergyintolerances;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllergyItemAdapter extends RecyclerView.Adapter<AllergyItemAdapter.ViewHolder> implements Filterable {

    private ArrayList<AllergyItem> mAllergyItemsData;
    private ArrayList<AllergyItem> mAllergyItemsDataAll;
    private Context mContext;
    private int lastPos = -1;


    AllergyItemAdapter(Context context, ArrayList<AllergyItem> itemsData){
        this.mAllergyItemsDataAll=itemsData;
        this.mAllergyItemsData=itemsData;
        this.mContext=context;
    }

    @Override
    public Filter getFilter() {
        return allergyFilter;
    }

    private Filter allergyFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<AllergyItem> filteredList = new ArrayList<>();
            FilterResults result = new FilterResults();
            if(constraint==null || constraint.length()==0){
                result.count=mAllergyItemsDataAll.size();
                result.values=mAllergyItemsDataAll;
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(AllergyItem item : mAllergyItemsDataAll){
                    if(item.getPatients().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
                result.count=filteredList.size();
                result.values=filteredList;
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mAllergyItemsData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllergyItem currentItem = mAllergyItemsData.get(position);
        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPos){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPos=holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mAllergyItemsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mPatient;
        private TextView mManifestation;
        private TextView mType;
        private TextView mSubstance;

        public ViewHolder(View view){
            super(view);

             mPatient = view.findViewById(R.id.patientName);
             mManifestation=view.findViewById(R.id.patientManifestation);
             mType=view.findViewById(R.id.type);
             mSubstance=view.findViewById(R.id.substance);


        }



        public void bindTo(AllergyItem currentItem) {
            mPatient.setText(currentItem.getPatients());
            mManifestation.setText(currentItem.getManifestation());
            mType.setText(currentItem.getType());
            mSubstance.setText(currentItem.getSubstance());

            itemView.findViewById(R.id.buttonUpdate).setOnClickListener(view -> ((AllergyIntolerance)mContext).updateItem(currentItem));
            itemView.findViewById(R.id.buttonDelete).setOnClickListener(view -> ((AllergyIntolerance)mContext).deleteItem(currentItem));

        }
    }


}






