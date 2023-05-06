package me.numilani.forgerpchat.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISelectedChatRange extends INBTSerializable<CompoundTag> {
    String getDefaultChatRange();
    void setDefaultChatRange(String value);
}

