package beteam.viloco.trackcheck.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import beteam.viloco.trackcheck.R;
import beteam.viloco.trackcheck.dto.ModulosDTO;

import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    private final List<ModulosDTO> list;
    private LayoutInflater inflater;

    public NavigationDrawerAdapter(Context context, List<ModulosDTO> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void delete(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ModulosDTO current = list.get(position);
        holder.title.setText(current.Nombre);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ModulosDTO getItem(int position) {
        return list.get(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
