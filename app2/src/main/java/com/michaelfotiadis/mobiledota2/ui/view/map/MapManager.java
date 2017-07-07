package com.michaelfotiadis.mobiledota2.ui.view.map;

import com.michaelfotiadis.mobiledota2.utils.BinaryUtils;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 */
public class MapManager {

    private final Map<MapItemName, MapContainer> mMapMap = new EnumMap<>(MapItemName.class);

    private enum MapItemName {
        RADIANT_TOP_TIER_ONE(0),
        RADIANT_TOP_TIER_TWO(1),
        RADIANT_TOP_TIER_THREE(2),
        RADIANT_MID_TIER_ONE(3),
        RADIANT_MID_TIER_TWO(4),
        RADIANT_MID_TIER_THREE(5),
        RADIANT_BOT_TIER_ONE(6),
        RADIANT_BOT_TIER_TWO(7),
        RADIANT_BOT_TIER_THREE(8),
        RADIANT_FOUNTAIN_NORTH(9),
        RADIANT_FOUNTAIN_SOUTH(10),
        RADIANT_TOP_RAX_MELEE(0),
        RADIANT_TOP_RAX_RANGED(1),
        RADIANT_MID_RAX_MELEE(2),
        RADIANT_MID_RAX_RANGED(3),
        RADIANT_BOT_RAX_MELEE(4),
        RADIANT_BOT_RAX_RANGED(5),
        DIRE_TOP_TIER_ONE(0),
        DIRE_TOP_TIER_TWO(1),
        DIRE_TOP_TIER_THREE(2),
        DIRE_MID_TIER_ONE(3),
        DIRE_MID_TIER_TWO(4),
        DIRE_MID_TIER_THREE(5),
        DIRE_BOT_TIER_ONE(6),
        DIRE_BOT_TIER_TWO(7),
        DIRE_BOT_TIER_THREE(8),
        DIRE_FOUNTAIN_NORTH(9),
        DIRE_FOUNTAIN_SOUTH(10),
        DIRE_TOP_RAX_MELEE(0),
        DIRE_TOP_RAX_RANGED(1),
        DIRE_MID_RAX_MELEE(2),
        DIRE_MID_RAX_RANGED(3),
        DIRE_BOT_RAX_MELEE(4),
        DIRE_BOT_RAX_RANGED(5);
        private final int mBitPosition;

        private MapItemName(final int bit) {
            mBitPosition = bit;
        }

        public int getBitPosition() {
            return mBitPosition;
        }
    }

    private void setTowerStatus(final long statusValue, final MapItemName mapItemName) {
        final boolean isDestroyed = !BinaryUtils.checkBitStatus(statusValue, mapItemName.getBitPosition());
//        Logger.d(TAG, "Map Object " + mapItemName + " is destroyed " + isDestroyed);
        setDestroyedStatus(mapItemName, isDestroyed);
    }


    public MapManager() {
        final float mapWidth = 1024f;
        final float mapHeight = 1024f;

        mMapMap.put(MapItemName.RADIANT_TOP_TIER_ONE, new MapContainer(123 / mapWidth, 447 / mapHeight, false, true, true));
        mMapMap.put(MapItemName.RADIANT_TOP_TIER_TWO, new MapContainer(121 / mapWidth, 590 / mapHeight, false, true, true));
        mMapMap.put(MapItemName.RADIANT_TOP_TIER_THREE, new MapContainer(102 / mapWidth, 740 / mapHeight, false, true, true));

        mMapMap.put(MapItemName.RADIANT_MID_TIER_ONE, new MapContainer(425 / mapWidth, 610 / mapHeight, false, true, true));
        mMapMap.put(MapItemName.RADIANT_MID_TIER_TWO, new MapContainer(312 / mapWidth, 717 / mapHeight, false, true, true));
        mMapMap.put(MapItemName.RADIANT_MID_TIER_THREE, new MapContainer(225 / mapWidth, 795 / mapHeight, false, true, true));

        mMapMap.put(MapItemName.RADIANT_BOT_TIER_ONE, new MapContainer(825 / mapWidth, 917 / mapHeight, false, true, true));
        mMapMap.put(MapItemName.RADIANT_BOT_TIER_TWO, new MapContainer(485 / mapWidth, 937 / mapHeight, false, true, true));
        mMapMap.put(MapItemName.RADIANT_BOT_TIER_THREE, new MapContainer(297 / mapWidth, 923 / mapHeight, false, true, true));

        mMapMap.put(MapItemName.RADIANT_TOP_RAX_RANGED, new MapContainer(78 / mapWidth, 777 / mapHeight, false, false, true));
        mMapMap.put(MapItemName.RADIANT_TOP_RAX_MELEE, new MapContainer(124 / mapWidth, 777 / mapHeight, false, false, true));


        mMapMap.put(MapItemName.RADIANT_MID_RAX_RANGED, new MapContainer(187 / mapWidth, 801 / mapHeight, false, false, true));
        mMapMap.put(MapItemName.RADIANT_MID_RAX_MELEE, new MapContainer(232 / mapWidth, 827 / mapHeight, false, false, true));

        mMapMap.put(MapItemName.RADIANT_BOT_RAX_RANGED, new MapContainer(252 / mapWidth, 898 / mapHeight, false, false, true));
        mMapMap.put(MapItemName.RADIANT_BOT_RAX_MELEE, new MapContainer(252 / mapWidth, 942 / mapHeight, false, false, true));

        mMapMap.put(MapItemName.RADIANT_FOUNTAIN_NORTH, new MapContainer(154 / mapWidth, 847 / mapHeight, false, true, true));
        mMapMap.put(MapItemName.RADIANT_FOUNTAIN_SOUTH, new MapContainer(177 / mapWidth, 873 / mapHeight, false, true, true));

        mMapMap.put(MapItemName.DIRE_FOUNTAIN_NORTH, new MapContainer(831 / mapWidth, 214 / mapHeight, false, true, false));
        mMapMap.put(MapItemName.DIRE_FOUNTAIN_SOUTH, new MapContainer(866 / mapWidth, 231 / mapHeight, false, true, false));

        mMapMap.put(MapItemName.DIRE_TOP_TIER_ONE, new MapContainer(201 / mapWidth, 133 / mapHeight, false, true, false));
        mMapMap.put(MapItemName.DIRE_TOP_TIER_TWO, new MapContainer(502 / mapWidth, 138 / mapHeight, false, true, false));
        mMapMap.put(MapItemName.DIRE_TOP_TIER_THREE, new MapContainer(737 / mapWidth, 147 / mapHeight, false, true, false));

        mMapMap.put(MapItemName.DIRE_MID_TIER_ONE, new MapContainer(574 / mapWidth, 485 / mapHeight, false, true, false));
        mMapMap.put(MapItemName.DIRE_MID_TIER_TWO, new MapContainer(668 / mapWidth, 370 / mapHeight, false, true, false));
        mMapMap.put(MapItemName.DIRE_MID_TIER_THREE, new MapContainer(774 / mapWidth, 279 / mapHeight, false, true, false));
        mMapMap.put(MapItemName.DIRE_BOT_TIER_ONE, new MapContainer(903 / mapWidth, 625 / mapHeight, false, true, false));
        mMapMap.put(MapItemName.DIRE_BOT_TIER_TWO, new MapContainer(907 / mapWidth, 494 / mapHeight, false, true, false));
        mMapMap.put(MapItemName.DIRE_BOT_TIER_THREE, new MapContainer(906 / mapWidth, 327 / mapHeight, false, true, false));

        mMapMap.put(MapItemName.DIRE_TOP_RAX_RANGED, new MapContainer(784 / mapWidth, 127 / mapHeight, false, false, false));
        mMapMap.put(MapItemName.DIRE_TOP_RAX_MELEE, new MapContainer(784 / mapWidth, 178 / mapHeight, false, false, false));
        mMapMap.put(MapItemName.DIRE_MID_RAX_RANGED, new MapContainer(760 / mapWidth, 253 / mapHeight, false, false, false));
        mMapMap.put(MapItemName.DIRE_MID_RAX_MELEE, new MapContainer(811 / mapWidth, 277 / mapHeight, false, false, false));
        mMapMap.put(MapItemName.DIRE_BOT_RAX_RANGED, new MapContainer(887 / mapWidth, 310 / mapHeight, false, false, false));
        mMapMap.put(MapItemName.DIRE_BOT_RAX_MELEE, new MapContainer(934 / mapWidth, 310 / mapHeight, false, false, false));
    }

    public Collection<MapContainer> getAsList() {
        return mMapMap.values();
    }

    public void setUpRadiantTowers(final long radiantTowerStatus) {
        setTowerStatus(radiantTowerStatus, MapItemName.RADIANT_TOP_TIER_ONE);
        setTowerStatus(radiantTowerStatus, MapItemName.RADIANT_TOP_TIER_TWO);
        setTowerStatus(radiantTowerStatus, MapItemName.RADIANT_TOP_TIER_THREE);
        setTowerStatus(radiantTowerStatus, MapItemName.RADIANT_MID_TIER_ONE);
        setTowerStatus(radiantTowerStatus, MapItemName.RADIANT_MID_TIER_TWO);
        setTowerStatus(radiantTowerStatus, MapItemName.RADIANT_MID_TIER_THREE);
        setTowerStatus(radiantTowerStatus, MapItemName.RADIANT_BOT_TIER_ONE);
        setTowerStatus(radiantTowerStatus, MapItemName.RADIANT_BOT_TIER_TWO);
        setTowerStatus(radiantTowerStatus, MapItemName.RADIANT_BOT_TIER_THREE);
        setTowerStatus(radiantTowerStatus, MapItemName.RADIANT_FOUNTAIN_NORTH);
        setTowerStatus(radiantTowerStatus, MapItemName.RADIANT_FOUNTAIN_SOUTH);
    }

    public void setUpRadiantRax(final long radiantRaxStatus) {
        setTowerStatus(radiantRaxStatus, MapItemName.RADIANT_TOP_RAX_MELEE);
        setTowerStatus(radiantRaxStatus, MapItemName.RADIANT_TOP_RAX_RANGED);
        setTowerStatus(radiantRaxStatus, MapItemName.RADIANT_MID_RAX_MELEE);
        setTowerStatus(radiantRaxStatus, MapItemName.RADIANT_MID_RAX_RANGED);
        setTowerStatus(radiantRaxStatus, MapItemName.RADIANT_BOT_RAX_MELEE);
        setTowerStatus(radiantRaxStatus, MapItemName.RADIANT_BOT_RAX_RANGED);
    }

    public void setUpDireRax(final long direRaxStatus) {
        setTowerStatus(direRaxStatus, MapItemName.DIRE_TOP_RAX_MELEE);
        setTowerStatus(direRaxStatus, MapItemName.DIRE_TOP_RAX_RANGED);
        setTowerStatus(direRaxStatus, MapItemName.DIRE_MID_RAX_MELEE);
        setTowerStatus(direRaxStatus, MapItemName.DIRE_MID_RAX_RANGED);
        setTowerStatus(direRaxStatus, MapItemName.DIRE_BOT_RAX_MELEE);
        setTowerStatus(direRaxStatus, MapItemName.DIRE_BOT_RAX_RANGED);
    }

    public void setUpDireTowers(final long direTowerStatus) {
        setTowerStatus(direTowerStatus, MapItemName.DIRE_TOP_TIER_ONE);
        setTowerStatus(direTowerStatus, MapItemName.DIRE_TOP_TIER_TWO);
        setTowerStatus(direTowerStatus, MapItemName.DIRE_TOP_TIER_THREE);
        setTowerStatus(direTowerStatus, MapItemName.DIRE_MID_TIER_ONE);
        setTowerStatus(direTowerStatus, MapItemName.DIRE_MID_TIER_TWO);
        setTowerStatus(direTowerStatus, MapItemName.DIRE_MID_TIER_THREE);
        setTowerStatus(direTowerStatus, MapItemName.DIRE_BOT_TIER_ONE);
        setTowerStatus(direTowerStatus, MapItemName.DIRE_BOT_TIER_TWO);
        setTowerStatus(direTowerStatus, MapItemName.DIRE_BOT_TIER_THREE);
        setTowerStatus(direTowerStatus, MapItemName.DIRE_FOUNTAIN_NORTH);
        setTowerStatus(direTowerStatus, MapItemName.DIRE_FOUNTAIN_SOUTH);
    }

    public void setDestroyedStatus(final MapItemName name, final boolean isDestroyed) {
        if (!mMapMap.containsKey(name)) {
            return;
        }
        final MapContainer existingMapObject = mMapMap.get(name);
        mMapMap.put(name, new MapContainer(
                existingMapObject.getX(),
                existingMapObject.getY(),
                isDestroyed,
                existingMapObject.isTower(),
                existingMapObject.isRadiant()));
    }
}
