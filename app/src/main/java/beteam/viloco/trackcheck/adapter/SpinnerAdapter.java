package beteam.viloco.trackcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import beteam.viloco.trackcheck.R;
import beteam.viloco.trackcheck.dto.SpinnerCustom;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private List<SpinnerCustom> list;
    private LayoutInflater mInflater;

    public SpinnerAdapter(Context context, List<SpinnerCustom> list) {
        this.list = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SpinnerCustom getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setError(View v, CharSequence s) {
        TextView textView = (TextView) v.findViewById(R.id.SpinnerTemplateDescription);
        textView.setError(s);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View template;

        if (convertView == null) {
            template = mInflater.inflate(R.layout.spinner_template, parent, false);
        } else {
            template = convertView;
        }

        SpinnerCustom item = getItem(position);

        ((TextView) template.findViewById(R.id.SpinnerTemplateDescription)).setText(item.Description);

        return template;
    }
}