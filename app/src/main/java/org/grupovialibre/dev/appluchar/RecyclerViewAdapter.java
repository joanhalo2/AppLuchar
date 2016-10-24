package org.grupovialibre.dev.appluchar;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
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
        holder.postTitle.setText(reports.get(position).getLocation());
        holder.descTitle.setText(reports.get(position).getActors());
    }
    @Override
    public int getItemCount() {
        return this.reports.size();
    }
}
