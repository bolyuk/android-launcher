package org.bolyuk.launcher.ui;

import android.widget.FrameLayout;

public abstract class DesktopItem {
    abstract void afterJson();
    abstract void add(FrameLayout layout);
}
