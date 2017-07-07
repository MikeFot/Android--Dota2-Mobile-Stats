package com.michaelfotiadis.mobiledota2.ui.activity.details.graph.recycler;

import android.content.Context;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.activity.details.MatchContainer;
import com.michaelfotiadis.steam.data.dota2.model.player.PlayerDetails;
import com.michaelfotiadis.steam.data.dota2.types.StatType;

public class MultipleGraphsRecyclerAdapter extends BaseGraphRecyclerAdapter {

    public MultipleGraphsRecyclerAdapter(final Context context,
                                         final MatchContainer matchContainer,
                                         final PlayerDetails playerDetails) {
        super(context, matchContainer, playerDetails);
    }

    @Override
    public int getItemViewType(final int position) {
        switch (position) {
            case 0:
                return VIEW_TYPE_RADAR;
            case 1:
                return VIEW_TYPE_BAR;
            case 2:
                return VIEW_TYPE_LINE;
            default:
                return VIEW_TYPE_PIE;
        }
    }

    @Override
    public void onBindViewHolder(final BaseChartViewHolder holder, final int position) {
        final int initialPositionForPieCharts = 3;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_PIE:
                final PieChartViewHolder viewHolderPieChart = (PieChartViewHolder) holder;
                if (position == initialPositionForPieCharts) {
                    mGeneralGraphFactory.setUpPieChart(viewHolderPieChart.mChart,
                            mMatchGraphFactory.generateSimplePieData(mPieSimpleColors, StatType.KILLS),
                            "Total Kills",
                            mCenterTextColor);
                } else if (position == initialPositionForPieCharts + 1) {
                    mGeneralGraphFactory.setUpPieChart(viewHolderPieChart.mChart,
                            mMatchGraphFactory.generateComplexPieData(mPieComplexColors, StatType.KILLS),
                            "Hero Kills",
                            mCenterTextColor);
                } else if (position == initialPositionForPieCharts + 3) {
                    mGeneralGraphFactory.setUpPieChart(viewHolderPieChart.mChart,
                            mMatchGraphFactory.generateComplexPieData(mPieComplexColors, StatType.XP_PER_MIN),
                            "Hero Experience/Minute",
                            mCenterTextColor);
                } else if (position == initialPositionForPieCharts + 2) {
                    mGeneralGraphFactory.setUpPieChart(viewHolderPieChart.mChart,
                            mMatchGraphFactory.generateComplexPieData(mPieComplexColors, StatType.GOLD_PER_MIN),
                            "Hero Gold/Minute",
                            mCenterTextColor);
                } else if (position == initialPositionForPieCharts + 4) {
                    mGeneralGraphFactory.setUpPieChart(viewHolderPieChart.mChart,
                            mMatchGraphFactory.generateComplexPieData(mPieComplexColors, StatType.LEVEL),
                            "Hero Levels",
                            mCenterTextColor);
                }
                break;
            case VIEW_TYPE_RADAR:
                final RadarChartViewHolder viewHolderRadarChart = (RadarChartViewHolder) holder;
                if (position == 0) {
                    mGeneralGraphFactory.setUpRadarChart(
                            viewHolderRadarChart.mChart,
                            mMatchGraphFactory.generateTeamRadarData(mPieSimpleColors),
                            mMarkerView);
                }
                break;
            case VIEW_TYPE_BAR:
                final BarChartViewHolder viewHolderBarChart = (BarChartViewHolder) holder;
                if (position == 1) {
                    mGeneralGraphFactory.setUpHorizontalBarChart(
                            viewHolderBarChart.mChart,
                            mMatchGraphFactory.generateStackedBarData(
                                    mBarStackedColors,
                                    StatType.TOWER_DAMAGE,
                                    StatType.HERO_HEALING,
                                    StatType.HERO_DAMAGE),
                            getContext().getResources().getColor(R.color.grey_light)
                    );
                }
                break;
            case VIEW_TYPE_LINE:
                final LineChartViewHolder viewHolderLineChart = (LineChartViewHolder) holder;
                if (position == 2) {
                    mGeneralGraphFactory.setUpLineChart(viewHolderLineChart.mChart,
                            mMatchGraphFactory.generateTeamTimeLevelLineData(mPieSimpleColors),
                            mMarkerView);
                }
                break;
            default:
                break;
        }
    }
}