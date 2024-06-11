package xd.arkosammy.monkeyconfigtest;

import net.minecraft.util.StringIdentifiable;

public enum EnumClass implements StringIdentifiable {
    HAPPY("happy"),
    SAD("sad"),
    ANGRY("angry");

    private final String id;

    EnumClass(String id) {
        this.id = id;
    }

    @Override
    public String asString() {
        return this.id;
    }
}
