package com.example.aviral.medicinelist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryViewAdapter extends RecyclerView.Adapter<HistoryViewAdapter.ViewHolder> {

    private ArrayList<String> mMedicineName = new ArrayList<>();
    private ArrayList<String> mDosage = new ArrayList<>();
    private ArrayList<String> mAmountPurchased = new ArrayList<>();
    private Context context;

    public HistoryViewAdapter(Context context, ArrayList<String> mMedicineName, ArrayList<String> mDosage, ArrayList<String> mAmountPurchased) {
        this.mMedicineName = mMedicineName;
        this.mDosage = mDosage;
        this.mAmountPurchased = mAmountPurchased;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewAdapter.ViewHolder viewHolder, int i) {
        viewHolder.medicinename.setText(mMedicineName.get(i));
        viewHolder.dosage.setText(mDosage.get(i));
        viewHolder.amount_purchased.setText(mAmountPurchased.get(i));
        viewHolder.share.setText("SHARE");
    }

    @Override
    public int getItemCount() {
        return mMedicineName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView medicinename,dosage,amount_purchased,share;
        LinearLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medicinename = itemView.findViewById(R.id.medicine_name);
            dosage = itemView.findViewById(R.id.dosage);
            amount_purchased = itemView.findViewById(R.id.amount_purchased);
            share = itemView.findViewById(R.id.share);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
