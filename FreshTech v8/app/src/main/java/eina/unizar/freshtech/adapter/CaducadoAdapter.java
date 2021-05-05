package eina.unizar.freshtech.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import eina.unizar.freshtech.R;

public class CaducadoAdapter extends ArrayAdapter<CaducadoItem> {

    private Context mContext;
    private int mResource;

    public CaducadoAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CaducadoItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);

        ImageView imageView = convertView.findViewById(R.id.iconoListaCaducados);

        TextView txtName = convertView.findViewById(R.id.text_nombre_caducados);

        imageView.setImageResource(getItem(position).getIcono());

        txtName.setText(getItem(position).getNombre());

        return convertView;
    }
}
