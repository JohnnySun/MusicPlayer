package bob.sun.bender.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bob.sun.bender.R;
import bob.sun.bender.model.MiBandDevice;

/**
 * Created by bmy001 on 西暦17/04/01.
 */

public class SearchBandAdapter extends BaseAdapter {
    List<MiBandDevice> data;

    public SearchBandAdapter() {
        data = new ArrayList<>();
    }

    public void add(MiBandDevice device) {
        boolean found = false;
        for (MiBandDevice item : data) {
            if (item.getBandMac().equals(device.getBandMac())) {
                found = true;
                // 更新Rssi
                item.setRssi(device.getRssi());
            }
        }
        if (!found) {
            data.add(device);
        }
        Collections.sort(data, new DataComparator());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public MiBandDevice getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.band_search_item, null);
            holder = new ViewHolder();
            holder.bandMac = (TextView) convertView.findViewById(R.id.id_bandmac);
            holder.rssi = (TextView) convertView.findViewById(R.id.id_rssi);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String rssi = Integer.toString(getItem(position).getRssi());
        holder.bandMac.setText(getItem(position).getBandMac());
        holder.rssi.setText(rssi);
        return convertView;
    }

    class ViewHolder {
        private TextView bandMac;
        private TextView rssi;
    }

    class DataComparator implements Comparator<MiBandDevice> {

        @Override
        public int compare(MiBandDevice o1, MiBandDevice o2) {
            if (o1.getRssi() > o2.getRssi()) {
                return 1;
            } else if (o1.getRssi() == o2.getRssi()) {
                return 0;
            } else {
                return -1;
            }
        }

    }
}
