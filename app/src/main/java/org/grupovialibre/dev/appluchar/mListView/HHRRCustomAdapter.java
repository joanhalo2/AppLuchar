package org.grupovialibre.dev.appluchar.mListView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.grupovialibre.dev.appluchar.R;
import org.grupovialibre.dev.appluchar.entities.Denunciation;

import java.util.ArrayList;

/**
 * Created by joan on 20/11/16.
 */

public class HHRRCustomAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<Denunciation> denuncias;

    private LayoutInflater inflater;

    public HHRRCustomAdapter(Context context, ArrayList<Denunciation> denuncias) {
        //super();
        //inflater = LayoutInflater.from(context);
        this.context = context;
        this.denuncias = denuncias;
    }


    @Override
    public int getCount() {
        return denuncias.size();
    }

    @Override
    public Object getItem(int position) {
        return denuncias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Denunciation denuncia = denuncias.get(position);

        if(inflater==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        if(convertView==null){
            convertView = inflater.inflate(R.layout.hhrr_model,parent,false);
        }

        TextView dateTimeField = (TextView) convertView.findViewById(R.id.dateTimeField);
        TextView denunciationUserField = (TextView) convertView.findViewById(R.id.denunciationUserField);
        TextView denunciationCorpseField = (TextView) convertView.findViewById(R.id.denuncationCorpseField);


        String header = denuncia.getDateTime()+"-"+denuncia.getUserName()+" escríbio:";
        String title = denuncia.getTitle();
        String corpse = denuncia.getCorpse();

        dateTimeField.setText(header);
        denunciationUserField.setText(title);
        denunciationCorpseField.setText(corpse);

        Log.w("VIEW",header+"/"+title);



        return convertView;
    }
}
