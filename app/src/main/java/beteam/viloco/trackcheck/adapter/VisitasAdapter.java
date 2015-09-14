package beteam.viloco.trackcheck.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import beteam.viloco.trackcheck.R;
import beteam.viloco.trackcheck.dto.DataDTO;

import java.util.List;

public class VisitasAdapter extends BaseAdapter {
    private final List<DataDTO> list;
    private LayoutInflater inflater;

    public VisitasAdapter(Context context, List<DataDTO> list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public DataDTO getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View template;

        if (convertView == null) {
            template = inflater.inflate(R.layout.expandable_height_list_view_pendientes, parent, false);
        } else {
            template = convertView;
        }

        DataDTO item = getItem(position);

//        ImageView iv = (ImageView) template.findViewById(R.id.ExpandableHLVPPhoto);
//        byte[] byteArray = Base64.decode(String.valueOf(item.DataPhoto), Base64.DEFAULT);
//        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        iv.setImageBitmap(bmp);

        ((TextView) template.findViewById(R.id.VisitasBusiness)).setText(String.valueOf(item.BusinessName));
        ((TextView) template.findViewById(R.id.VisitasStreet)).setText(String.valueOf(item.Street));
        ((TextView) template.findViewById(R.id.VisitasLatitud)).setText(String.valueOf(item.Latitude));
        ((TextView) template.findViewById(R.id.VisitasLongitud)).setText(String.valueOf(item.Longitude));

        return template;
    }
}
