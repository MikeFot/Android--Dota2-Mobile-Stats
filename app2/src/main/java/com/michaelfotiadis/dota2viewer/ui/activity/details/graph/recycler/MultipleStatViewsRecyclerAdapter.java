package com.michaelfotiadis.dota2viewer.ui.activity.details.graph.recycler;

import android.content.Context;

import com.michaelfotiadis.dota2viewer.ui.activity.details.MatchContainer;
import com.michaelfotiadis.steam.data.dota2.model.player.PlayerDetails;

public class MultipleStatViewsRecyclerAdapter extends BaseGraphRecyclerAdapter {


    private static final int POSITION_LINE_LEVEL = 0;
    private static final int POSITION_BAR_KDA = 1;


    public MultipleStatViewsRecyclerAdapter(final Context context,
                                            final MatchContainer matchContainer,
                                            final PlayerDetails playerDetails) {
        super(context, matchContainer, playerDetails);
    }


    @Override
    public int getItemViewType(final int position) {
        switch (position) {
            case POSITION_LINE_LEVEL:
                return VIEW_TYPE_LINE;
            case POSITION_BAR_KDA:
                return VIEW_TYPE_RADAR;
            default:
                return VIEW_TYPE_RADAR;
        }
    }

    @Override
    protected boolean isItemValid(final Integer item) {
        return item != null;
    }

    @Override
    public void onBindViewHolder(final BaseChartViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_PIE:
                final PieChartViewHolder pieChartViewHolder = (PieChartViewHolder) holder;
                break;
            case VIEW_TYPE_RADAR:
                final RadarChartViewHolder viewHolderRadarChart = (RadarChartViewHolder) holder;
                if (position == POSITION_BAR_KDA) {

                    mGeneralGraphFactory.setUpRadarChart(viewHolderRadarChart.mChart,
                            mMatchGraphFactory.generatePlayerRadarData(mPieSimpleColors),
                            mMarkerView);
                }
                break;
            case VIEW_TYPE_BAR:
                final BarChartViewHolder viewHolderBarChart = (BarChartViewHolder) holder;
                break;
            case VIEW_TYPE_LINE:
                if (position == POSITION_LINE_LEVEL) {
                    final LineChartViewHolder viewHolderLineChart = (LineChartViewHolder) holder;
                    mGeneralGraphFactory.setUpLineChart(
                            viewHolderLineChart.mChart,
                            mMatchGraphFactory.generatePlayerTimeLevelLineData(mPieSimpleColors),
                            mMarkerView
                    );
                }
            default:
                break;
        }
    }
}