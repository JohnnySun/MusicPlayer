package bob.sun.bender.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;

import com.huami.mibandscan.MiBandScanResult;
import com.huami.mibandscan.MiBandScanStatus;

import bob.sun.bender.R;
import bob.sun.bender.adapters.SearchBandAdapter;
import bob.sun.bender.controller.OnBandFoundListener;
import bob.sun.bender.controller.OnTickListener;
import bob.sun.bender.model.MIBandSearchInstance;
import bob.sun.bender.model.MiBandDevice;
import bob.sun.bender.model.SelectionDetail;

/**
 * Created by Johnny on 西暦17/04/01.
 */

public class BandConnectFragment extends Fragment implements OnTickListener, OnBandFoundListener {
    private ListView listView;
    private Button searchBtn;
    private MIBandSearchInstance miBandSearchInstance;

    SearchBandAdapter searchBandAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View ret =  inflater.inflate(R.layout.layout_connect_band,parent,false);
        listView = (ListView) ret.findViewById(R.id.id_list_band);
        searchBtn = (Button) ret.findViewById(R.id.id_search_button);
        searchBandAdapter = new SearchBandAdapter();
        listView.setAdapter(searchBandAdapter);

        miBandSearchInstance = MIBandSearchInstance.getInstance();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!miBandSearchInstance.isStartScan()) {
                    if (miBandSearchInstance.startScan(BandConnectFragment.this)) {
                        searchBtn.setText(R.string.stop_scan);
                    }
                } else {
                    miBandSearchInstance.stopScan();
                    searchBtn.setText(R.string.start_scan);
                }
            }
        });

        return ret;
    }

    @Override
    public void onStop() {
        super.onStop();
        miBandSearchInstance.stopScan();
    }

    @Override
    public void onNextTick() {
        if (listView == null)
            return;
    }

    @Override
    public void onPreviousTick() {
        if (listView == null)
            return;
    }

    @Override
    public SelectionDetail getCurrentSelection() {
        SelectionDetail selectionDetail = new SelectionDetail();
        selectionDetail.setMenuType(SelectionDetail.MENU_TYPE_UNUSED);
        return selectionDetail;
    }

    @Override
    public void onData(MiBandDevice device) {
        searchBandAdapter.add(device);
    }

    @Override
    public void onStatus(MiBandScanStatus scanStatus) {
        // 处理手环sdk的状态回调
    }
}

