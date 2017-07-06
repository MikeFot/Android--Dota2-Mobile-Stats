package com.michaelfotiadis.dota2viewer.injection;

public class Injector {

    private static ComponentStore mComponentStore;

    public static ComponentStore getComponentStore() {
        return mComponentStore;
    }

    public static void setComponentStore(final ComponentStore componentStore) {
        mComponentStore = componentStore;
    }

}
