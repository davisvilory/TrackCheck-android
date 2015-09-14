package beteam.viloco.trackcheck.adapter;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import beteam.viloco.trackcheck.R;
import beteam.viloco.trackcheck.dto.DataPhotoDTO;

import java.util.List;

public class PhotosListViewAdapter extends BaseAdapter {
    private List<DataPhotoDTO> list;
    private LayoutInflater inflater;

    public PhotosListViewAdapter(Context context, List<DataPhotoDTO> list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setListData(List<DataPhotoDTO> list) {
        this.list = list;
    }

    public void deleteItem(int position) {
        list.remove(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public DataPhotoDTO getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View template;

        if (convertView == null) {
            template = inflater.inflate(R.layout.expandable_height_list_view_photos, parent, false);
        } else {
            template = convertView;
        }

        DataPhotoDTO item = getItem(position);

        //byte[] byteArray = Base64.decode(String.valueOf(item.Photo), Base64.DEFAULT);
        //Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ((ImageView) template.findViewById(R.id.ExpandableHLVPPhoto)).setImageBitmap(item.bitmap);
        (template.findViewById(R.id.ExpandableHLVPLess)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });

        return template;
    }
}
