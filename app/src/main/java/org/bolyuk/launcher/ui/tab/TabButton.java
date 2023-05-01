package org.bolyuk.launcher.ui.tab;

import android.content.Context;

import org.bolyuk.launcher.DesktopActivity;

public class TabButton extends androidx.appcompat.widget.AppCompatImageView {

    boolean isPinned=false;
    public TabButton(Context context, int icon_id) {
        super(context);
        this.setImageDrawable(context.getDrawable(icon_id));
    }

    public boolean isPinned(){
        return isPinned;
    }

    public TabButton setPinned(boolean value){
        isPinned=value;
        return this;
    }

    public void spawn(){
        ((DesktopActivity)getContext()).spawnTab(this);
    }

    public void remove() { ((DesktopActivity)getContext()).removeTab(this);}
}
