package com.michaelfotiadis.dota2viewer.ui.activity.details.match.view;

import android.content.Context;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.view.binder.BaseViewBinder;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaGeneralUtils;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaMatchHelper;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaResourceUtils;
import com.michaelfotiadis.dota2viewer.utils.format.FormattingUtils;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;
import com.michaelfotiadis.steam.data.dota2.model.player.PlayerDetails;
import com.michaelfotiadis.steam.data.dota2.types.LeaverStatus;
import com.michaelfotiadis.steam.data.dota2.types.StatType;

import java.util.List;
import java.util.Locale;

public class DotaMatchDetailsViewBinder extends BaseViewBinder<MatchStatsRowViewHolder, PlayerDetails> {

    private final ImageLoader mImageLoader;
    private final DotaMatchHelper mHelper;
    private final List<Hero> mHeroes;
    private final List<GameItem> mGameItems;


    public DotaMatchDetailsViewBinder(final Context context,
                                      final ImageLoader imageLoader,
                                      final DotaMatchHelper dotaMatchHelper,
                                      final List<Hero> heroes,
                                      final List<GameItem> gameItems) {
        super(context);
        mImageLoader = imageLoader;
        mHelper = dotaMatchHelper;
        mHeroes = heroes;
        mGameItems = gameItems;
    }


    public void setUpMatchDetailsHeader(final TextView view) {
        if (mHelper.getMatch().getRadiantWin()) {
            view.setText(R.string.title_radiant_victory);
            view.setTextColor(getColor(R.color.map_green_radiant_dark));
        } else {
            view.setText(R.string.title_dire_victory);
            view.setTextColor(getColor(R.color.map_red_dire_dark));
        }
    }

    public void setUpPlayerStatsRowViewHolder(final MatchStatsRowViewHolder holder, final int position) {
        final PlayerDetails player = mHelper.getMatch().getPlayers().get(position);
        this.setData(holder, player);
    }

    private void setUpItemImageView(final int code, final ImageView imageView) {

        final GameItem gameItem = DotaGeneralUtils.getItemForCode(code, mGameItems);
        if (gameItem == null) {
            imageView.setImageResource(R.drawable.ic_default);
        } else {
            mImageLoader.loadItem(imageView, gameItem.getName());
        }

    }


    public void setUpMatchDetailsFooter(final MatchStatsRowViewHolder holder, final boolean isRadiant) {

        final MatchDetails matchDetails = mHelper.getMatch();

        final int level = DotaGeneralUtils.calculateSumValue(matchDetails, isRadiant, StatType.LEVEL);
        holder.textLevel.setText(String.valueOf(level));
        final int kills = DotaGeneralUtils.calculateSumValue(matchDetails, isRadiant, StatType.KILLS);
        holder.textKills.setText(String.valueOf(kills));
        final int deaths = DotaGeneralUtils.calculateSumValue(matchDetails, isRadiant, StatType.DEATHS);
        holder.textDeaths.setText(String.valueOf(deaths));
        final int assists = DotaGeneralUtils.calculateSumValue(matchDetails, isRadiant, StatType.ASSISTS);
        holder.textAssists.setText(String.valueOf(assists));
        final int gold = DotaGeneralUtils.calculateSumValue(matchDetails, isRadiant, StatType.GOLD);
        holder.textGold.setText(FormattingUtils.formatThousandsNumberString(gold));
        final int lastHits = DotaGeneralUtils.calculateSumValue(matchDetails, isRadiant, StatType.LAST_HITS);
        holder.textLastHits.setText(String.valueOf(lastHits));
        final int denies = DotaGeneralUtils.calculateSumValue(matchDetails, isRadiant, StatType.DENIES);
        holder.textDenies.setText(String.valueOf(denies));
        final int xpm = DotaGeneralUtils.calculateSumValue(matchDetails, isRadiant, StatType.XP_PER_MIN);
        holder.textXpm.setText(String.valueOf(xpm));
        final int gpm = DotaGeneralUtils.calculateSumValue(matchDetails, isRadiant, StatType.GOLD_PER_MIN);
        holder.textGpm.setText(String.valueOf(gpm));
        final int heroDamage = DotaGeneralUtils.calculateSumValue(matchDetails, isRadiant, StatType.HERO_DAMAGE);
        holder.textHd.setText(FormattingUtils.formatThousandsNumberString(heroDamage));
        final int heroHealing = DotaGeneralUtils.calculateSumValue(matchDetails, isRadiant, StatType.HERO_HEALING);
        holder.textHh.setText(FormattingUtils.formatThousandsNumberString(heroHealing));
        final int towerDamage = DotaGeneralUtils.calculateSumValue(matchDetails, isRadiant, StatType.TOWER_DAMAGE);
        holder.textTd.setText(FormattingUtils.formatThousandsNumberString(towerDamage));
        final int goldSpent = DotaGeneralUtils.calculateSumValue(matchDetails, isRadiant, StatType.GOLD_SPENT);
        holder.textSpent.setText(FormattingUtils.formatThousandsNumberString(goldSpent));

    }

    @Override
    protected void setData(final MatchStatsRowViewHolder holder, final PlayerDetails player) {
        final Hero hero = DotaGeneralUtils.getHeroForId(player.getHeroId(), mHeroes);

        final boolean isThisTheUsersRow = player.getAccountId() == mHelper.getAccountId();

        if (hero != null) {
            mImageLoader.loadHero(holder.imageHero, hero.getName());
            final LeaverStatus leaverStatus = player.getLeaverStatusAsEnum();
            final String nameText;
            final int leaverRes = DotaResourceUtils.getLeaverStatus(leaverStatus);
            if (leaverRes == 0) {
                nameText = hero.getLocalizedName();
            } else {
                holder.textHeroName.setTextColor(getColor(R.color.md_red_300));
                nameText = String.format(Locale.US, "%s - %s", hero.getLocalizedName(), getString(leaverRes));
            }
            holder.textHeroName.setText(nameText.trim());
        }


        if (isThisTheUsersRow) {
            final ViewParent parent = holder.textHeroName.getParent();
            if (parent instanceof TableRow) {
                ((TableRow) parent).setBackgroundColor(getColor(R.color.md_light_blue_300));
            }
        }
        holder.textLevel.setText(String.valueOf(player.getLevel()));
        holder.textKills.setText(String.valueOf(player.getKills()));
        holder.textDeaths.setText(String.valueOf(player.getDeaths()));
        holder.textAssists.setText(String.valueOf(player.getAssists()));
        holder.textGold.setText(FormattingUtils.formatThousandsNumberString(player.getGold()));
        holder.textSpent.setText(FormattingUtils.formatThousandsNumberString(player.getGoldSpent()));
        holder.textLastHits.setText(String.valueOf(player.getLastHits()));
        holder.textDenies.setText(String.valueOf(player.getDenies()));
        holder.textXpm.setText(String.valueOf(player.getXpPerMin()));
        holder.textGpm.setText(String.valueOf(player.getGoldPerMin()));
        holder.textHd.setText(FormattingUtils.formatThousandsNumberString(player.getHeroDamage()));
        holder.textHh.setText(FormattingUtils.formatThousandsNumberString(player.getHeroHealing()));
        holder.textTd.setText(FormattingUtils.formatThousandsNumberString(player.getTowerDamage()));

        setUpItemImageView(player.getItem0(), (ImageView) (holder.itemView.findViewById(R.id.image_item_1)));
        setUpItemImageView(player.getItem1(), (ImageView) (holder.itemView.findViewById(R.id.image_item_2)));
        setUpItemImageView(player.getItem2(), (ImageView) (holder.itemView.findViewById(R.id.image_item_3)));
        setUpItemImageView(player.getItem3(), (ImageView) (holder.itemView.findViewById(R.id.image_item_4)));
        setUpItemImageView(player.getItem4(), (ImageView) (holder.itemView.findViewById(R.id.image_item_5)));
        setUpItemImageView(player.getItem5(), (ImageView) (holder.itemView.findViewById(R.id.image_item_6)));
    }


}
