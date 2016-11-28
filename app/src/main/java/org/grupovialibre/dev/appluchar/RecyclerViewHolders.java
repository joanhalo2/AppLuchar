package org.grupovialibre.dev.appluchar;

/**
 * Created by joan on 22/10/16.
 */
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.grupovialibre.dev.appluchar.entities.Report;

import java.util.List;

public class RecyclerViewHolders extends RecyclerView.ViewHolder{
    private static final String TAG = RecyclerViewHolders.class.getSimpleName();
    public Report report;

    public ImageView markIcon;

    public TextView mPostDesc;
    public TextView mPostPlace;
    public TextView mPostDate;
    public TextView mPostCategory;
    public TextView mPostActors;
    public TextView mPostType;
    public TextView mPostUser;


    private List<Report> taskObject;

    public RecyclerViewHolders(final View itemView, final Context context,final List<Report> taskObject) {
        super(itemView);
        this.taskObject = taskObject;

        markIcon = (ImageView)itemView.findViewById(R.id.postImage);

        mPostDesc = (TextView)itemView.findViewById(R.id.postTitle);
        mPostPlace = (TextView)itemView.findViewById(R.id.postPlace);
        mPostActors = (TextView)itemView.findViewById(R.id.postActors);
        mPostCategory = (TextView)itemView.findViewById(R.id.postCategory);
        mPostType = (TextView)itemView.findViewById(R.id.postType);
        mPostDate = (TextView)itemView.findViewById(R.id.postDate);
        mPostUser = (TextView)itemView.findViewById(R.id.postUser);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                if(report!=null){
                    Intent mIntent = new Intent(context, ReportActivity.class);
                    mIntent.putExtra("currentReport",report);
                    context.startActivity(mIntent);
                }

            }
        });


        /*deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Delete icon has been clicked", Toast.LENGTH_LONG).show();
                String taskTitle = taskObject.get(getAdapterPosition()).getTask();
                Log.d(TAG, "Task Title " + taskTitle);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.orderByChild("task").equalTo(taskTitle);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });*/

    }
}
