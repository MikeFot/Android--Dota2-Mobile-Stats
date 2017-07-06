package com.michaelfotiadis.dota2viewer.ui.activity.details.graph.factory;

import android.graphics.Color;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.michaelfotiadis.dota2viewer.ui.activity.details.MatchContainer;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaGeneralUtils;
import com.michaelfotiadis.dota2viewer.utils.format.FormattingUtils;
import com.michaelfotiadis.steam.data.dota2.model.hero.AbilityUpgrade;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;
import com.michaelfotiadis.steam.data.dota2.model.player.PlayerDetails;
import com.michaelfotiadis.steam.data.dota2.types.StatType;

import java.util.ArrayList;
import java.util.List;

/**
 * Graph utilities
 * Created by Mike on 16/06/2015.
 */
public class DotaMatchGraphFactory {


    private final MatchContainer mMatchContainer;
    private final PlayerDetails mPlayerDetails;

    public DotaMatchGraphFactory(final MatchContainer matchContainer, final PlayerDetails playerDetails) {
        mMatchContainer = matchContainer;
        mPlayerDetails = playerDetails;
    }


    public LineData generatePlayerTimeLevelLineData(final int[] colors) {
        final List<AbilityUpgrade> abilityUpgrades = mPlayerDetails.getAbilityUpgrades();
        final ArrayList<String> xVals = new ArrayList<>();
        final ArrayList<Entry> yVals1 = new ArrayList<>();
        int playerLevel = 0;

        final int maxTimeMinutes = abilityUpgrades.get(abilityUpgrades.size() - 1).getTime() / 60;

        for (int i = 0; i < maxTimeMinutes + 1; i++) {
            xVals.add(i + "");

            for (final AbilityUpgrade upgrade : abilityUpgrades) {
                if (upgrade.getTime() / 60 == i) {
                    playerLevel++;
                }
            }
            yVals1.add(new Entry(playerLevel, i));
        }

        // create a dataset and give it a type
        final LineDataSet set1 = new LineDataSet(
                yVals1,
                DotaGeneralUtils.getHeroForId(mPlayerDetails.getHeroId(), mMatchContainer.getHeroes()).getLocalizedName());
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setCircleSize(1f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
        set1.setDrawValues(false);
//        set1.setVisible(false);
//        set1.setCircleHoleColor(Color.BLACK);

        if (colors.length >= 2) {
            if (mPlayerDetails.getPlayerSlot() < 5) {
                set1.setColor(colors[0]);
            } else {
                set1.setColor(colors[1]);
            }
        }

        final ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        final LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        return data;
    }

    public LineData generateTeamTimeLevelLineData(final int[] colors) {

        final List<AbilityUpgrade> radiantUpgrades = DotaGeneralUtils.getLevelProgression(mMatchContainer.getMatchDetails(), true);
        final List<AbilityUpgrade> direUpgrades = DotaGeneralUtils.getLevelProgression(mMatchContainer.getMatchDetails(), false);

        final ArrayList<String> xVals = new ArrayList<>();
        final ArrayList<Entry> yVals1 = new ArrayList<>();
        final ArrayList<Entry> yVals2 = new ArrayList<>();

        int radiantLevel = 0;
        int direLevel = 0;
        final int maxTimeMinutes = Math.max(
                direUpgrades.get(direUpgrades.size() - 1).getTime(),
                radiantUpgrades.get(radiantUpgrades.size() - 1).getTime()
        ) / 60;

        for (int i = 0; i < maxTimeMinutes + 1; i++) {
            xVals.add(i + "");

            for (final AbilityUpgrade upgrade : radiantUpgrades) {
                if (upgrade.getTime() / 60 == i) {
                    radiantLevel++;
                }
            }
            for (final AbilityUpgrade upgrade : direUpgrades) {
                if (upgrade.getTime() / 60 == i) {
                    direLevel++;
                }
            }
            yVals1.add(new Entry(radiantLevel, i));
            yVals2.add(new Entry(direLevel, i));
        }

        // create a dataset and give it a type
        final LineDataSet set1 = new LineDataSet(yVals1, "Radiant");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setCircleSize(1f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
        set1.setDrawValues(false);
//        set1.setVisible(false);
//        set1.setCircleHoleColor(Color.BLACK);

        // create a dataset and give it a type
        final LineDataSet set2 = new LineDataSet(yVals2, "Dire");
        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.BLACK);
        set2.setLineWidth(2f);
        set2.setCircleSize(1f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        set2.setDrawValues(false);

        if (colors.length >= 2) {
            set1.setColor(colors[0]);
            set2.setColor(colors[1]);
        }

        final ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set2);
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        final LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        return data;
    }

    public PieData generateComplexPieData(final int[] colors,
                                          final StatType dotaStatType) {

        final ArrayList<Entry> entries = new ArrayList<>();
        final ArrayList<String> xVals = new ArrayList<>();

        int i = 0;
        for (final PlayerDetails player : mMatchContainer.getMatchDetails().getPlayers()) {
            final Hero hero = DotaGeneralUtils.getHeroForId(player.getHeroId(), mMatchContainer.getHeroes());
            if (hero != null) {
                final int value = DotaGeneralUtils.getValueForStatType(player, dotaStatType);
                if (value > 0) {
                    xVals.add(hero.getLocalizedName());
                    entries.add(new Entry(value, i));

                }
                i++;
            }
        }
        final PieDataSet pieDataSet = new PieDataSet(entries, dotaStatType.name());
        if (colors.length >= 10) {
            pieDataSet.setColors(colors);
        } else {
            pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        }

        pieDataSet.setSliceSpace(4f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(8f);
        pieDataSet.setValueFormatter(new IntegerValueFormatter());

        return new PieData(xVals, pieDataSet);
    }

    public PieData generateSimplePieData(final int[] colors,
                                         final StatType dotaStatType) {
        final ArrayList<Entry> entries = new ArrayList<>();
        final ArrayList<String> xVals = new ArrayList<>();

        final MatchDetails match = mMatchContainer.getMatchDetails();

        xVals.add("Radiant");
        xVals.add("Dire");

        String chartTitle = "";

        if (dotaStatType == StatType.GOLD) {
            entries.add(new Entry(DotaGeneralUtils.calculateSumValue(match, true, StatType.GOLD_SPENT) +
                    DotaGeneralUtils.calculateSumValue(match, true, StatType.GOLD), 0));
            entries.add(new Entry(DotaGeneralUtils.calculateSumValue(match, false, StatType.GOLD_SPENT) +
                    DotaGeneralUtils.calculateSumValue(match, false, StatType.GOLD), 1));
            chartTitle = "Total Gold";
        } else if (dotaStatType == StatType.KILLS) {
            entries.add(new Entry(DotaGeneralUtils.calculateSumValue(match, true, StatType.KILLS), 0));
            entries.add(new Entry(DotaGeneralUtils.calculateSumValue(match, false, StatType.KILLS), 1));
            chartTitle = "Total Kills";
        } else if (dotaStatType == StatType.LEVEL) {
            entries.add(new Entry(DotaGeneralUtils.calculateSumValue(match, true, StatType.LEVEL), 0));
            entries.add(new Entry(DotaGeneralUtils.calculateSumValue(match, false, StatType.LEVEL), 1));
            chartTitle = "Total Levels";
        }

        final PieDataSet pieDataSet = new PieDataSet(entries, chartTitle);
        if (colors.length >= 2) {
            pieDataSet.setColors(colors);
        } else {
            pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        }

        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setValueFormatter(new IntegerValueFormatter());

        return new PieData(xVals, pieDataSet);
    }

    public BarData generateStackedBarData(
            final int[] colors,
            final StatType type1,
            final StatType type2,
            final StatType type3) {
        final ArrayList<String> xVals = new ArrayList<>();
        final ArrayList<BarEntry> yVals1 = new ArrayList<>();
        int i = 0;
        for (final PlayerDetails player : mMatchContainer.getMatchDetails().getPlayers()) {
            xVals.add(DotaGeneralUtils.getHeroForId(player.getHeroId(), mMatchContainer.getHeroes()).getLocalizedName());
            yVals1.add(new BarEntry(new float[]{
                    DotaGeneralUtils.getValueForStatType(player, type1),
                    DotaGeneralUtils.getValueForStatType(player, type2),
                    DotaGeneralUtils.getValueForStatType(player, type3)
            }, i));
            i++;
        }

        final BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setColors(colors);
        set1.setValueTextColor(Color.BLACK);
        set1.setStackLabels(new String[]{
                FormattingUtils.formatStringToLowerCaseNoSpecial(type1.name()).replaceAll("_", " "),
                FormattingUtils.formatStringToLowerCaseNoSpecial(type2.name()).replaceAll("_", " "),
                FormattingUtils.formatStringToLowerCaseNoSpecial(type3.name()).replaceAll("_", " ")
        });

        final ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        final BarData data = new BarData(xVals, dataSets);
        data.setValueFormatter(new ThousandsValueFormatter());
        data.setValueTextSize(12f);

        return data;
    }

    public RadarData generatePlayerRadarData(final int[] colors) {
        final ArrayList<Entry> yVals1 = new ArrayList<>();
        yVals1.add(new Entry(DotaGeneralUtils.getValueForStatType(mPlayerDetails, StatType.KILLS), 0));
        yVals1.add(new Entry(DotaGeneralUtils.getValueForStatType(mPlayerDetails, StatType.DEATHS), 1));
        yVals1.add(new Entry(DotaGeneralUtils.getValueForStatType(mPlayerDetails, StatType.ASSISTS), 2));

        final ArrayList<String> xVals = new ArrayList<>();
        xVals.add("Kills");
        xVals.add("Deaths");
        xVals.add("Assists");

        final RadarDataSet set1 = new RadarDataSet(
                yVals1,
                DotaGeneralUtils.getHeroForId(mPlayerDetails.getHeroId(), mMatchContainer.getHeroes()).getLocalizedName());
        set1.setColor(colors[0]);
        set1.setDrawFilled(true);
        set1.setLineWidth(2f);

        final ArrayList<RadarDataSet> sets = new ArrayList<>();
        sets.add(set1);


        final RadarData data = new RadarData(xVals, sets);
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.BLACK);
        data.setValueFormatter(new IntegerValueFormatter());
        data.setDrawValues(false);

        return data;
    }

    public RadarData generateTeamRadarData(final int[] colors) {
        final ArrayList<Entry> yVals1 = new ArrayList<>();
        yVals1.add(new Entry(DotaGeneralUtils.calculateSumValue(mMatchContainer.getMatchDetails(), true, StatType.KILLS), 0));
        yVals1.add(new Entry(DotaGeneralUtils.calculateSumValue(mMatchContainer.getMatchDetails(), true, StatType.DEATHS), 1));
        yVals1.add(new Entry(DotaGeneralUtils.calculateSumValue(mMatchContainer.getMatchDetails(), true, StatType.ASSISTS), 2));
        final ArrayList<Entry> yVals2 = new ArrayList<>();
        yVals2.add(new Entry(DotaGeneralUtils.calculateSumValue(mMatchContainer.getMatchDetails(), false, StatType.KILLS), 0));
        yVals2.add(new Entry(DotaGeneralUtils.calculateSumValue(mMatchContainer.getMatchDetails(), false, StatType.DEATHS), 1));
        yVals2.add(new Entry(DotaGeneralUtils.calculateSumValue(mMatchContainer.getMatchDetails(), false, StatType.ASSISTS), 2));


        final ArrayList<String> xVals = new ArrayList<>();
        xVals.add("Kills");
        xVals.add("Deaths");
        xVals.add("Assists");

        final RadarDataSet set1 = new RadarDataSet(yVals1, "Radiant");
        set1.setColor(colors[0]);
        set1.setDrawFilled(true);
        set1.setLineWidth(2f);

        final RadarDataSet set2 = new RadarDataSet(yVals2, "Dire");
        set2.setColor(colors[1]);
        set2.setDrawFilled(true);
        set2.setLineWidth(2f);

        final ArrayList<RadarDataSet> sets = new ArrayList<>();
        // add set 2 first because it is a darker color
        sets.add(set2);
        sets.add(set1);


        final RadarData data = new RadarData(xVals, sets);
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.BLACK);
        data.setValueFormatter(new IntegerValueFormatter());
        data.setDrawValues(false);

        return data;

    }


}
