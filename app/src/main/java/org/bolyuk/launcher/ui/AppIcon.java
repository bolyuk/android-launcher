package org.bolyuk.launcher.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.bolyuk.launcher.DesktopActivity;
import org.bolyuk.launcher.R;

public class AppIcon extends DesktopItem {
    public String name;
    public String id;
    public String packageName;
    transient public Drawable icon;
    transient LinearLayout l;
    float x, y;

    public AppIcon(){
    }

    @Override
    void afterJson() {

    }

    @Override
    public void add(FrameLayout layout) {
        Context context = layout.getContext();
        PackageManager pm =context.getPackageManager();

        l = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.icon_dskt, null);
        ImageView icon = l.findViewById(R.id.icon);

        TextView name = l.findViewById(R.id.name);
        name.setText(this.name);
        if(this.icon != null)
            icon.setImageDrawable(this.icon);
        else {
            try {
                icon.setImageDrawable(pm.getApplicationInfo(packageName, 0).loadIcon(pm));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        layout.addView(l);
        l.getLayoutParams().width=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
        l.getLayoutParams().height=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, context.getResources().getDisplayMetrics());
        l.setX(x);
        l.setY(y);

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                context.startActivity(launchIntent);
            }
        });

        l.setOnLongClickListener(v -> {

            if(DesktopActivity.DragAndDrop){
                View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);

                Object[] d = {v, this, 0.0f, 0.0f};
                // Starts the drag
                v.startDrag(null       // data to be dragged
                        , dragshadow  // drag shadow
                        , d          // local data about the drag and drop operation
                        , 0          // flags set to 0 because not using currently
                );
            } else {
                PopupMenu m = new PopupMenu(context, l);

                m.getMenu().add("delete");

                m.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("delete")) {
                            ((DesktopActivity)context).removeView(l);
                            DesktopActivity.apps.remove(id);
                        } else {

                        }
                        return true;
                    }
                });

                m.show();
            }
                return false;
            });

    }

    public void updateCoordinate(){
        if(l.getX() != x || l.getY() != y){
            x=l.getX();
            y=l.getY();
            DesktopActivity.apps.write(id, this);
        }
    }
}
