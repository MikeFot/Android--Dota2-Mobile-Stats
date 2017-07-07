package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.recycler;

import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.animation.CustomItemAnimator;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.animation.PredefinedAnimations;

public class MatchesItemAnimator extends CustomItemAnimator {

    public MatchesItemAnimator() {
        this.setItemRemoveCustomizer(PredefinedAnimations.SHRINK);
        this.setItemAddCustomizer(PredefinedAnimations.GROW);
        this.setAddDuration(200L);
        this.setRemoveDuration(200L);
    }
}
