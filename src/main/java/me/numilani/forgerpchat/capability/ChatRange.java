package me.numilani.forgerpchat.capability;

import net.minecraft.nbt.CompoundTag;

public class ChatRange {
    public static final String NBT_DEFRANGE = "defaultchatrange";

    private String range = "global";

    public String getRange(){
        return range;
    }

    public void setRange(String r){
        range = r;
    }

    public void copyFrom(ChatRange src){
        range = src.range;
    }

    public void saveNPT(CompoundTag ctag){
        ctag.putString(NBT_DEFRANGE, range);
    }

    public void loadNPT(CompoundTag ctag){
        range = ctag.getString(NBT_DEFRANGE);
    }
}
