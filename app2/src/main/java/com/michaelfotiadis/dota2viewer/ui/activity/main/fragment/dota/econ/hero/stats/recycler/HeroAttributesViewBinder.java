package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.hero.stats.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.persistence.model.HeroPatchAttributes;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.steam.provider.image.Size;

import java.text.DecimalFormat;

class HeroAttributesViewBinder extends BaseRecyclerViewBinder<HeroAttributesViewHolder, HeroPatchAttributes> {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.0");

    private final ImageLoader mImageLoader;
    private final OnItemSelectedListener<HeroPatchAttributes> mListener;

    HeroAttributesViewBinder(final Context context,
                             final ImageLoader imageLoader,
                             final OnItemSelectedListener<HeroPatchAttributes> listener) {
        super(context);
        mImageLoader = imageLoader;
        mListener = listener;
    }

    @Override
    protected void reset(final HeroAttributesViewHolder holder) {
        holder.getRoot().setOnClickListener(null);
    }

    @Override
    protected void setData(final HeroAttributesViewHolder holder, final HeroPatchAttributes item) {

        mImageLoader.loadHero(holder.mImageHero, item.getName(), Size.FULL_VERTICAL);

        holder.mName.setText(item.getHero());

        setAttributePip(holder, item);

        holder.mAttrStrength.setText(getAttributeText(item.getStrength(), item.getStrengthGain(), item.getStrengthAt25()));
        holder.mAttrAgility.setText(getAttributeText(item.getAgility(), item.getAgilityGain(), item.getAgilityAt25()));
        holder.mAttrIntelligence.setText(getAttributeText(item.getIntelligence(), item.getIntelligenceGain(), item.getIntelligenceAt25()));

        setStartingAttributes(holder, item);
        setStartingArmor(holder, item);
        setMovementSpeed(holder, item);
        setMinMaxDamage(holder, item);
        setAttackRange(holder, item);
        setAttackPoint(holder, item);
        setAttackBackswing(holder, item);
        setVision(holder, item);
        setTurnRate(holder, item);
        setHpRegen(holder, item);

        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onListItemSelected(v, item);
            }
        });


    }

    private void setHpRegen(final HeroAttributesViewHolder holder, final HeroPatchAttributes item) {
        if (item.getHpRegen() != null) {
            showView(holder.mRegenLayout, true);
            holder.mRegenContent.setText(DECIMAL_FORMAT.format(item.getHpRegen()));
        } else {
            showView(holder.mRegenLayout, false);
        }
    }

    private void setTurnRate(final HeroAttributesViewHolder holder, final HeroPatchAttributes item) {
        if (item.getTurnRate() != null) {
            showView(holder.mTurnLayout, true);
            holder.mTurnContent.setText(DECIMAL_FORMAT.format(item.getTurnRate()));
        } else {
            showView(holder.mTurnLayout, false);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setMovementSpeed(final HeroAttributesViewHolder holder, final HeroPatchAttributes item) {

        if (item.getMovementSpeed() != null) {
            showView(holder.mSpeedLayout, true);
            holder.mSpeedContent.setText(item.getMovementSpeed().toString());
        } else {
            showView(holder.mSpeedLayout, false);
        }

    }

    private void setStartingArmor(final HeroAttributesViewHolder holder, final HeroPatchAttributes item) {

        if (item.getStartingArmor() != null) {
            showView(holder.mArmorLayout, true);
            holder.mArmorContent.setText(DECIMAL_FORMAT.format(item.getStartingArmor()));
        } else {
            showView(holder.mArmorLayout, false);
        }

    }

    private void setStartingAttributes(final HeroAttributesViewHolder holder, final HeroPatchAttributes item) {

        if (item.getTotalStartingAttr() != null) {
            showView(holder.mStartingAttrLayout, true);
            holder.mStartingAttrContent.setText(getContext().getString(R.string.placeholder_attr,
                    item.getTotalStartingAttr(), DECIMAL_FORMAT.format(item.getTotalAttrGrowth())));
        } else {
            showView(holder.mStartingAttrLayout, false);
        }

    }

    private void setVision(final HeroAttributesViewHolder holder, final HeroPatchAttributes item) {

        if (item.getVisionDay() != null && item.getVisionNight() != null) {
            showView(holder.mVisionLayout, true);
            holder.mVisionContent.setText(getContext().getString(R.string.slash_numbers, item.getVisionDay(), item.getVisionNight()));
        } else {
            showView(holder.mVisionLayout, false);
        }

    }

    private void setMinMaxDamage(final HeroAttributesViewHolder holder, final HeroPatchAttributes item) {
        if (item.getDmgMin() != null && item.getDmgMax() != null) {
            showView(holder.mDamageLayout, true);
            final String damageRange = getContext().getString(R.string.hyphen_numbers, item.getDmgMin(), item.getDmgMax());
            holder.mDamageContent.setText(damageRange);
        } else {
            showView(holder.mDamageLayout, false);
        }
    }

    private void setAttackRange(final HeroAttributesViewHolder holder, final HeroPatchAttributes item) {
        if (item.getAttackRange() != null) {
            showView(holder.mRangeLayout, true);
            holder.mRangeContent.setText(String.valueOf(item.getAttackRange()));
        } else {
            showView(holder.mRangeLayout, false);
        }
    }

    private void setAttackPoint(final HeroAttributesViewHolder holder, final HeroPatchAttributes item) {
        if (item.getAttackPoint() != null) {
            showView(holder.mPointLayout, true);
            holder.mPointContent.setText(DECIMAL_FORMAT.format(item.getAttackPoint()));
        } else {
            showView(holder.mPointLayout, false);
        }
    }

    private void setAttackBackswing(final HeroAttributesViewHolder holder, final HeroPatchAttributes item) {
        if (item.getAttackBackswing() != null) {
            showView(holder.mBackswingLayout, true);
            holder.mBackswingContent.setText(DECIMAL_FORMAT.format(item.getAttackBackswing()));
        } else {
            showView(holder.mBackswingLayout, false);
        }
    }

    private void setAttributePip(final HeroAttributesViewHolder holder, final HeroPatchAttributes item) {
        final int attrColor;
        switch (item.getAttribute()) {
            case 0:
                attrColor = R.color.hero_card_str;
                break;
            case 1:
                attrColor = R.color.hero_card_agi;
                break;
            default:
                attrColor = R.color.hero_card_int;
        }
        holder.mName.setTextColor(getColor(attrColor));
    }

    private String getAttributeText(final Integer initial, final Float gain, final Float max) {
        return initial != null && gain != null && max != null
                ? getContext().getString(R.string.placeholder_attr_max, initial, DECIMAL_FORMAT.format(gain), DECIMAL_FORMAT.format(max))
                : "";
    }

}
