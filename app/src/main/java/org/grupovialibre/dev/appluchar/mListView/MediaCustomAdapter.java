package org.grupovialibre.dev.appluchar.mListView;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.grupovialibre.dev.appluchar.R;
import org.grupovialibre.dev.appluchar.entities.Denunciation;
import org.grupovialibre.dev.appluchar.entities.Media;

import java.util.ArrayList;

/**
 * Created by joan on 20/11/16.
 */

public class MediaCustomAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<Media> mediaObjects;

    private LayoutInflater inflater;

    public MediaCustomAdapter(Context context, ArrayList<Media> mediaObjects) {
        this.context = context;
        this.mediaObjects = mediaObjects;
    }


    @Override
    public int getCount() {
        return mediaObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return mediaObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Media media = mediaObjects.get(position);

        if(inflater==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        if(convertView==null){
            convertView = inflater.inflate(R.layout.report_media_model,parent,false);
        }

        TextView mediaHeader = (TextView) convertView.findViewById(R.id.mediaHeaderField);
        TextView mediaDesc = (TextView) convertView.findViewById(R.id.mediaDescField);
        ImageView mediaImage = (ImageView) convertView.findViewById(R.id.mediaImageView);

        String header = media.getDateTime()+"-"+media.getUserName()+" subio:";
        String desc = media.getDescription();

        mediaHeader.setText(header);
        mediaDesc.setText(desc);
        Picasso.with(convertView.getContext()).load(media.getImageURI()).into(mediaImage);

        return convertView;
    }
}
