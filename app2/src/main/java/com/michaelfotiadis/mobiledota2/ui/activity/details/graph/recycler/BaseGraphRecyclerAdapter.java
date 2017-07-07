package com.michaelfotiadis.mobiledota2.ui.activity.details.graph.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.activity.details.MatchContainer;
import com.michaelfotiadis.mobiledota2.ui.activity.details.graph.factory.DotaGeneralGraphFactory;
import com.michaelfotiadis.mobiledota2.ui.activity.details.graph.factory.DotaMatchGraphFactory;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.mobiledota2.ui.view.graph.CustomMarkerView;
import com.michaelfotiadis.steam.data.dota2.model.player.PlayerDetails;

public abstract class BaseGraphRecyclerAdapter extends BaseRecyclerViewAdapter<Integer, BaseChartViewHolder> {

    protected static final int VIEW_TYPE_PIE = 0;
    protected static final int VIEW_TYPE_RADAR = 1;
    protected static final int VIEW_TYPE_BAR = 2;
    protected static final int VIEW_TYPE_LINE = 3;

    protected final DotaMatchGraphFactory mMatchGraphFactory;
    protected final DotaGeneralGraphFactory mGeneralGraphFactory;


    protected final int[] mPieSimpleColors;
    protected final int[] mPieComplexColors;
    protected final int[] mBarStackedColors;
    protected final int mCenterTextColor;
    protected final CustomMarkerView mMarkerView;

    protected BaseGraphRecyclerAdapter(final Context context,
                                       final MatchContainer matchContainer,
                                       final PlayerDetails playerDetails) {
        super(context);

        mMatchGraphFactory = new DotaMatchGraphFactory(matchContainer, playerDetails);
        mGeneralGraphFactory = new DotaGeneralGraphFactory();

        mPieSimpleColors = getContext().getResources().getIntArray(R.array.chart_colors_two);
        mPieComplexColors = getContext().getResources().getIntArray(R.array.chart_colors_ten);
        mBarStackedColors = getContext().getResources().getIntArray(R.array.chart_colors_three);
        mCenterTextColor = getContext().getResources().getColor(R.color.primary_text);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        mMarkerView = new CustomMarkerView(getContext().getApplicationContext(), R.layout.layout_marker_view);

    }

    @Override
    protected boolean isItemValid(final Integer item) {
        return item != null;
    }

    @Override
    public BaseChartViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        switch (viewType) {
            case VIEW_TYPE_PIE:
                return new PieChartViewHolder(LayoutInflater.from(getContext()).inflate(PieChartViewHolder.getLayoutId(), parent, false));
            case VIEW_TYPE_RADAR:
                return new RadarChartViewHolder(LayoutInflater.from(getContext()).inflate(RadarChartViewHolder.getLayoutId(), parent, false));
            case VIEW_TYPE_BAR:
                return new BarChartViewHolder(LayoutInflater.from(getContext()).inflate(BarChartViewHolder.getLayoutId(), parent, false));
            case VIEW_TYPE_LINE:
                return new LineChartViewHolder(LayoutInflater.from(getContext()).inflate(LineChartViewHolder.getLayoutId(), parent, false));
            default:
                return new PieChartViewHolder(parent);
        }
    }

}
