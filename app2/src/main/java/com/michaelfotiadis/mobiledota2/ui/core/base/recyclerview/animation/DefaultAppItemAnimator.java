package com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.animation;

public class DefaultAppItemAnimator extends CustomItemAnimator {

    public DefaultAppItemAnimator() {
        this.setItemRemoveCustomizer(PredefinedAnimations.SHRINK);
        this.setItemAddCustomizer(PredefinedAnimations.GROW);
        this.setAddDuration(200L);
        this.setRemoveDuration(200L);
    }
}
