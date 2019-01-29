package gerber.benjamin.lucidio;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by ben on 1/23/18.
 */

public class DataFragment extends Fragment {

    private LineGraphSeries<DataPoint> series1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_data, container, false);

        //Graph last EOG Data
        int x,y;
        GraphView graph = (GraphView) v.findViewById(R.id.sleep_graph);
        series1 = new LineGraphSeries<>();
        for(int i = 0; i < ((MainActivity)getActivity()).mEogData.size(); i++) {
            y = ((MainActivity)getActivity()).mEogData.get(i);
            x = i;
            series1.appendData(new DataPoint(x, y), true, ((MainActivity)getActivity()).mEogData.size());
        }

        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(256);

        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(((MainActivity)getActivity()).mEogData.size());

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);

        graph.addSeries(series1);
        return v;
    }


}
