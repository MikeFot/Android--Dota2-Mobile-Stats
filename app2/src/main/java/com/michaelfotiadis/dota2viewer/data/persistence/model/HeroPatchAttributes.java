package com.michaelfotiadis.dota2viewer.data.persistence.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HeroPatchAttributes implements Parcelable {

    @SerializedName("hero")
    private final String hero;
    @SerializedName("name")
    private final String name;
    @SerializedName("attr")
    private final Integer attribute;
    @SerializedName("str")
    private final Integer strength;
    @SerializedName("str_plus")
    private final Float strengthGain;
    @SerializedName("str25")
    private final Float strengthAt25;
    @SerializedName("agi")
    private final Integer agility;
    @SerializedName("agi_plus")
    private final Float agilityGain;
    @SerializedName("agi25")
    private final Float agilityAt25;
    @SerializedName("int")
    private final Integer intelligence;
    @SerializedName("int_plus")
    private final Float intelligenceGain;
    @SerializedName("int25")
    private final Float intelligenceAt25;
    @SerializedName("total_starting_attr")
    private final Integer totalStartingAttr;
    @SerializedName("total_attr_growth")
    private final Float totalAttrGrowth;
    @SerializedName("t25")
    private final Float totalAttrAt25;
    @SerializedName("ms")
    private final Integer movementSpeed;
    @SerializedName("ar")
    private final Float startingArmor;
    @SerializedName("dmg_min")
    private final Integer dmgMin;
    @SerializedName("dmg_max")
    private final Integer dmgMax;
    @SerializedName("attack_range")
    private final Integer attackRange;
    @SerializedName("attack_time")
    private final Float attackTime;
    @SerializedName("attack_point")
    private final Float attackPoint;
    @SerializedName("attack_backswing")
    private final Float attackBackswing;
    @SerializedName("vision_day")
    private final Integer visionDay;
    @SerializedName("vision_night")
    private final Integer visionNight;
    @SerializedName("turn_rate")
    private final Float turnRate;
    @SerializedName("collision_size")
    private final Integer collisionSize;
    @SerializedName("hp_regen")
    private final Float hpRegen;
    @SerializedName("legs")
    private final Integer legs;

    private HeroPatchAttributes(final Builder builder) {
        hero = builder.hero;
        name = builder.name;
        attribute = builder.attribute;
        strength = builder.strength;
        strengthGain = builder.strengthGain;
        strengthAt25 = builder.strengthAt25;
        agility = builder.agility;
        agilityGain = builder.agilityGain;
        agilityAt25 = builder.agilityAt25;
        intelligence = builder.intelligence;
        intelligenceGain = builder.intelligenceGain;
        intelligenceAt25 = builder.intelligenceAt25;
        totalStartingAttr = builder.totalStartingAttr;
        totalAttrGrowth = builder.totalAttrGrowth;
        totalAttrAt25 = builder.totalAttrAt25;
        movementSpeed = builder.movementSpeed;
        startingArmor = builder.startingArmor;
        dmgMin = builder.dmgMin;
        dmgMax = builder.dmgMax;
        attackRange = builder.attackRange;
        attackTime = builder.attackTime;
        attackPoint = builder.attackPoint;
        attackBackswing = builder.attackBackswing;
        visionDay = builder.visionDay;
        visionNight = builder.visionNight;
        turnRate = builder.turnRate;
        collisionSize = builder.collisionSize;
        hpRegen = builder.hpRegen;
        legs = builder.legs;
    }

    protected HeroPatchAttributes(final Parcel in) {
        this.hero = in.readString();
        this.name = in.readString();
        this.attribute = (Integer) in.readValue(Integer.class.getClassLoader());
        this.strength = (Integer) in.readValue(Integer.class.getClassLoader());
        this.strengthGain = (Float) in.readValue(Float.class.getClassLoader());
        this.strengthAt25 = (Float) in.readValue(Float.class.getClassLoader());
        this.agility = (Integer) in.readValue(Integer.class.getClassLoader());
        this.agilityGain = (Float) in.readValue(Float.class.getClassLoader());
        this.agilityAt25 = (Float) in.readValue(Float.class.getClassLoader());
        this.intelligence = (Integer) in.readValue(Integer.class.getClassLoader());
        this.intelligenceGain = (Float) in.readValue(Float.class.getClassLoader());
        this.intelligenceAt25 = (Float) in.readValue(Float.class.getClassLoader());
        this.totalStartingAttr = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalAttrGrowth = (Float) in.readValue(Float.class.getClassLoader());
        this.totalAttrAt25 = (Float) in.readValue(Float.class.getClassLoader());
        this.movementSpeed = (Integer) in.readValue(Integer.class.getClassLoader());
        this.startingArmor = (Float) in.readValue(Float.class.getClassLoader());
        this.dmgMin = (Integer) in.readValue(Integer.class.getClassLoader());
        this.dmgMax = (Integer) in.readValue(Integer.class.getClassLoader());
        this.attackRange = (Integer) in.readValue(Integer.class.getClassLoader());
        this.attackTime = (Float) in.readValue(Float.class.getClassLoader());
        this.attackPoint = (Float) in.readValue(Float.class.getClassLoader());
        this.attackBackswing = (Float) in.readValue(Float.class.getClassLoader());
        this.visionDay = (Integer) in.readValue(Integer.class.getClassLoader());
        this.visionNight = (Integer) in.readValue(Integer.class.getClassLoader());
        this.turnRate = (Float) in.readValue(Float.class.getClassLoader());
        this.collisionSize = (Integer) in.readValue(Integer.class.getClassLoader());
        this.hpRegen = (Float) in.readValue(Float.class.getClassLoader());
        this.legs = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public String getHero() {
        return hero;
    }

    public Integer getAttribute() {
        return attribute;
    }

    public Integer getStrength() {
        return strength;
    }

    public Float getStrengthGain() {
        return strengthGain;
    }

    public Float getStrengthAt25() {
        return strengthAt25;
    }

    public Integer getAgility() {
        return agility;
    }

    public Float getAgilityGain() {
        return agilityGain;
    }

    public Float getAgilityAt25() {
        return agilityAt25;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public Float getIntelligenceGain() {
        return intelligenceGain;
    }

    public Float getIntelligenceAt25() {
        return intelligenceAt25;
    }

    public Integer getTotalStartingAttr() {
        return totalStartingAttr;
    }

    public Float getTotalAttrGrowth() {
        return totalAttrGrowth;
    }

    public Float getTotalAttrAt25() {
        return totalAttrAt25;
    }

    public Integer getMovementSpeed() {
        return movementSpeed;
    }

    public Float getStartingArmor() {
        return startingArmor;
    }

    public Integer getDmgMin() {
        return dmgMin;
    }

    public Integer getDmgMax() {
        return dmgMax;
    }

    public Integer getAttackRange() {
        return attackRange;
    }

    public Float getAttackTime() {
        return attackTime;
    }

    public Float getAttackPoint() {
        return attackPoint;
    }

    public Float getAttackBackswing() {
        return attackBackswing;
    }

    public Integer getVisionDay() {
        return visionDay;
    }

    public Integer getVisionNight() {
        return visionNight;
    }

    public Float getTurnRate() {
        return turnRate;
    }

    public Integer getCollisionSize() {
        return collisionSize;
    }

    public Float getHpRegen() {
        return hpRegen;
    }

    public Integer getLegs() {
        return legs;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(this.hero);
        dest.writeString(this.name);
        dest.writeValue(this.attribute);
        dest.writeValue(this.strength);
        dest.writeValue(this.strengthGain);
        dest.writeValue(this.strengthAt25);
        dest.writeValue(this.agility);
        dest.writeValue(this.agilityGain);
        dest.writeValue(this.agilityAt25);
        dest.writeValue(this.intelligence);
        dest.writeValue(this.intelligenceGain);
        dest.writeValue(this.intelligenceAt25);
        dest.writeValue(this.totalStartingAttr);
        dest.writeValue(this.totalAttrGrowth);
        dest.writeValue(this.totalAttrAt25);
        dest.writeValue(this.movementSpeed);
        dest.writeValue(this.startingArmor);
        dest.writeValue(this.dmgMin);
        dest.writeValue(this.dmgMax);
        dest.writeValue(this.attackRange);
        dest.writeValue(this.attackTime);
        dest.writeValue(this.attackPoint);
        dest.writeValue(this.attackBackswing);
        dest.writeValue(this.visionDay);
        dest.writeValue(this.visionNight);
        dest.writeValue(this.turnRate);
        dest.writeValue(this.collisionSize);
        dest.writeValue(this.hpRegen);
        dest.writeValue(this.legs);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private Integer attribute;
        private Integer strength;
        private Float strengthGain;
        private Float strengthAt25;
        private Integer agility;
        private Float agilityGain;
        private Float agilityAt25;
        private Integer intelligence;
        private Float intelligenceGain;
        private Float intelligenceAt25;
        private Integer totalStartingAttr;
        private Float totalAttrGrowth;
        private Float totalAttrAt25;
        private Integer movementSpeed;
        private Float startingArmor;
        private Integer dmgMin;
        private Integer dmgMax;
        private Integer attackRange;
        private Float attackTime;
        private Float attackPoint;
        private Float attackBackswing;
        private Integer visionDay;
        private Integer visionNight;
        private Float turnRate;
        private Integer collisionSize;
        private Float hpRegen;
        private Integer legs;
        private String hero;

        private Builder() {
        }

        public Builder withName(final String val) {
            name = val;
            return this;
        }

        public Builder withAttribute(final Integer val) {
            attribute = val;
            return this;
        }

        public Builder withStrength(final Integer val) {
            strength = val;
            return this;
        }

        public Builder withStrengthGain(final Float val) {
            strengthGain = val;
            return this;
        }

        public Builder withStrengthAt25(final Float val) {
            strengthAt25 = val;
            return this;
        }

        public Builder withAgility(final Integer val) {
            agility = val;
            return this;
        }

        public Builder withAgilityGain(final Float val) {
            agilityGain = val;
            return this;
        }

        public Builder withAgilityAt25(final Float val) {
            agilityAt25 = val;
            return this;
        }

        public Builder withIntelligence(final Integer val) {
            intelligence = val;
            return this;
        }

        public Builder withIntelligenceGain(final Float val) {
            intelligenceGain = val;
            return this;
        }

        public Builder withIntelligenceAt25(final Float val) {
            intelligenceAt25 = val;
            return this;
        }

        public Builder withTotalStartingAttr(final Integer val) {
            totalStartingAttr = val;
            return this;
        }

        public Builder withTotalAttrGrowth(final Float val) {
            totalAttrGrowth = val;
            return this;
        }

        public Builder withTotalAttrAt25(final Float val) {
            totalAttrAt25 = val;
            return this;
        }

        public Builder withMovementSpeed(final Integer val) {
            movementSpeed = val;
            return this;
        }

        public Builder withStartingArmor(final Float val) {
            startingArmor = val;
            return this;
        }

        public Builder withDmgMin(final Integer val) {
            dmgMin = val;
            return this;
        }

        public Builder withDmgMax(final Integer val) {
            dmgMax = val;
            return this;
        }

        public Builder withAttackRange(final Integer val) {
            attackRange = val;
            return this;
        }

        public Builder withAttackTime(final Float val) {
            attackTime = val;
            return this;
        }

        public Builder withAttackPoint(final Float val) {
            attackPoint = val;
            return this;
        }

        public Builder withAttackBackswing(final Float val) {
            attackBackswing = val;
            return this;
        }

        public Builder withVisionDay(final Integer val) {
            visionDay = val;
            return this;
        }

        public Builder withVisionNight(final Integer val) {
            visionNight = val;
            return this;
        }

        public Builder withTurnRate(final Float val) {
            turnRate = val;
            return this;
        }

        public Builder withCollisionSize(final Integer val) {
            collisionSize = val;
            return this;
        }

        public Builder withHpRegen(final Float val) {
            hpRegen = val;
            return this;
        }

        public Builder withLegs(final Integer val) {
            legs = val;
            return this;
        }

        public Builder withHero(final String val) {
            hero = val;
            return this;
        }

        public HeroPatchAttributes build() {
            return new HeroPatchAttributes(this);
        }

    }

    public static final Creator<HeroPatchAttributes> CREATOR = new Creator<HeroPatchAttributes>() {
        @Override
        public HeroPatchAttributes createFromParcel(final Parcel source) {
            return new HeroPatchAttributes(source);
        }

        @Override
        public HeroPatchAttributes[] newArray(final int size) {
            return new HeroPatchAttributes[size];
        }
    };
}