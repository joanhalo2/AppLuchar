package org.grupovialibre.dev.appluchar;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by joan on 22/10/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private List<Report> reports;
    protected Context context;

    public RecyclerViewAdapter(Context context, List<Report> reports) {
        this.reports = reports;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolders viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_row, parent, false);
        viewHolder = new RecyclerViewHolders(layoutView, context, reports);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {

        holder.report = reports.get(position);
        holder.mPostDesc.setText(reports.get(position).getDescription().toUpperCase());
        holder.mPostPlace.setText(reports.get(position).getPlace());
        holder.mPostDate.setText(reports.get(position).getDate());
        holder.mPostType.setText(reports.get(position).getType());
        holder.mPostCategory.setText(reports.get(position).getSection());
        holder.mPostActors.setText(reports.get(position).getActors());
        holder.mPostUser.setText(reports.get(position).getUserName());




    }

    @Override
    public int getItemCount() {
        return this.reports.size();
    }
}
