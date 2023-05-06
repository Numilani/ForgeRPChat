package me.numilani.forgerpchat.capability;

import net.minecraft.nbt.CompoundTag;

public class SelectedChatRangeImpl implements ISelectedChatRange {
    private String rangeString = "global";

    @Override
    public String getDefaultChatRange() {
        return rangeString;
    }

    @Override
    public void setDefaultChatRange(String value) {
        rangeString = value;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putString("defaultChatRange", rangeString);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        rangeString = nbt.getString("defaultChatRange");
    }
}
