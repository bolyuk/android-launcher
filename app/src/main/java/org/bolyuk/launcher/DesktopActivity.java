package org.bolyuk.launcher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;

import org.bolyuk.bkcmmnlib.TinyDB;
import org.bolyuk.launcher.ui.AppIcon;
import org.bolyuk.launcher.ui.Utill;
import org.bolyuk.launcher.ui.tab.TabButton;
import org.bolyuk.launcher.ui.windows.AppManagerWindow;
import org.bolyuk.launcher.ui.windows.CodeWindow;
import org.bolyuk.launcher.ui.tab.WindowOpenButton;

import java.util.function.Consumer;

public class DesktopActivity extends AppCompatActivity {
     ImageView tab_button;
     View tab;
     LinearLayout tab_content;

   public FrameLayout desktop;

   public static TinyDB<AppIcon> apps;
   public static boolean DragAndDrop = false;
    public static boolean Grid = false;

    @Override
    protected void onRestart() {
        System.out.println("RESTART!");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        System.out.println("PAUSE!");
        super.onPause();
    }

    @Override
    protected void onStart() {
        System.out.println("START!");
        super.onStart();
    }

    @Override
    protected void onStop() {
        System.out.println("STOP!");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        System.out.println("DESTROY!");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        System.out.println("RESUME!");
        desktop.setVisibility(View.VISIBLE);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("CREATE!");
        setContentView(R.layout.activity_launcher);

        apps = new TinyDB<AppIcon>(this.getExternalFilesDir(null).getAbsolutePath(),
                new TypeToken<AppIcon>(){}.getType());

        tab_button = findViewById(R.id.tab_button);
        tab = findViewById(R.id.tab);
        tab.setVisibility(View.GONE);
        tab_content = findViewById(R.id.tab_content);
        desktop = findViewById(R.id.desktop);

        desktop.setOnDragListener((v, event) -> {
            // Defines a variable to store the action type for the incoming
            // event
            final int action = event.getAction();
            float X, Y;
            Object[] l = (Object[]) event.getLocalState();
            View view = (View) l[0];
            float dx = Float.parseFloat(l[2].toString());
            float dy = Float.parseFloat(l[3].toString());
            // Handles each of the expected events
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    l[2]=event.getX()-view.getX();
                    l[3]=event.getY()-view.getY();
                    break;
                case DragEvent.ACTION_DROP:
                    X = event.getX();
                    Y = event.getY();
                    view.setX(X-(view.getWidth()/2));
                    view.setY(Y-(view.getHeight()/2));

                    if(Grid){
                        view.setX(view.getX()-view.getX()% Utill.dp(50, this));
                        view.setY(view.getY()-view.getY()%Utill.dp(70, this));
                    }
                    // Invalidates the view to force a redraw
                    if(l[1] != null)
                        ((AppIcon)l[1]).updateCoordinate();
                    view.invalidate();

                    // Returns true. DragEvent.getResult() will return true.
            }
            return true;
        });

        tab_button.setOnClickListener(view -> {
            if(view.getRotation() == 0){
                tab.setVisibility(View.VISIBLE);
                view.setRotation(180);
            } else {
                tab.setVisibility(View.GONE);
                view.setRotation(0);
            }
        });

        new WindowOpenButton(this,
                R.drawable.baseline_apps_24,
                new AppManagerWindow(DesktopActivity.this)).setPinned(true).spawn();
        new WindowOpenButton(this, R.drawable.baseline_code_24,
                new CodeWindow(DesktopActivity.this)).setPinned(true).spawn();

        TabButton drag = new TabButton(DesktopActivity.this, R.drawable.baseline_draw_24).setPinned(true);
        drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DragAndDrop = !DragAndDrop;
                if(DragAndDrop)
                    drag.setImageResource(R.drawable.baseline_api_24);
                else
                    drag.setImageResource(R.drawable.baseline_draw_24);
            }
        });
        drag.spawn();
        TabButton grid = new TabButton(DesktopActivity.this, R.drawable.baseline_grid_off_24).setPinned(true);
        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Grid = !Grid;
                if(Grid)
                    grid.setImageResource(R.drawable.baseline_grid_on_24);
                else
                    grid.setImageResource(R.drawable.baseline_grid_off_24);
            }
        });
        grid.spawn();

        apps.getAll().forEach(new Consumer<AppIcon>() {
            @Override
            public void accept(AppIcon appIcon) {
                appIcon.add(desktop);
            }
        });
    }

    public void spawnTab(View view){
        tab_content.post(new Runnable() {
            @Override
            public void run() {
                tab_content.addView(view);
            }
        });
    }

    public void removeTab(View view){
        tab_content.post(new Runnable() {
            @Override
            public void run() {
                tab_content.removeView(view);
            }
        });
    }

    public void spawnView(View view){
        desktop.post(new Runnable() {
            @Override
            public void run() {
                desktop.addView(view);
            }
        });
    }

    public void removeView(View view){
        desktop.post(new Runnable() {
            @Override
            public void run() {
                desktop.removeView(view);
            }
        });
    }
}