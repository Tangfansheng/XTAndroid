package com.example.XTproject.model;

public class Icon {
    private int IconId;
    private String IconName;

    public Icon(int iconId, String iconName) {
        IconId = iconId;
        IconName = iconName;
    }

    public int getIconId() {
        return IconId;
    }

    public void setIconId(int iconId) {
        IconId = iconId;
    }

    public String getIconName() {
        return IconName;
    }

    public void setIconName(String iconName) {
        IconName = iconName;
    }
}
