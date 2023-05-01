package org.bolyuk.launcher.ui.windows;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.bolyuk.launcher.DesktopActivity;
import org.bolyuk.launcher.R;
import org.bolyuk.launcher.ui.Utill;

import java.util.ArrayList;

public class Window {
    ArrayList<StateChange> listeners = new ArrayList<>();

    ImageView close;
    ImageView fullscreen;
    ImageView minimize;
    ImageView resize;
    boolean isOpened=false;
    boolean isFullscreen=false;
    boolean isSpawnFullScreen=false;

    boolean canFullscreen=true;
    boolean canResize=true;
    boolean canMinimize=true;

    boolean canDrag=true;

    TextView title;
    Context _context;
    int width, height, min_width, min_height;

    FrameLayout _win;
    View _body;

    float centerX, centerY;


    @SuppressLint("ClickableViewAccessibility")
    public Window(Context context, int width, int height){
        _context=context;
        this.width = Utill.dp(width, context);
        this.height =Utill.dp(height, context);
        min_width= this.width;
        min_height= this.height;

        _win =(FrameLayout) LayoutInflater.from(context).inflate(R.layout.window, null);

        _win.setLayoutParams(new FrameLayout.LayoutParams(this.width, this.height));
        _win.setOnLongClickListener(v -> {

            if(!isFullscreen && canDrag) {
                // Instantiates the drag shadow builder.
                View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);
                Object[] d = {v, null, 0f, 0f};

                // Starts the drag
                v.startDrag(null       // data to be dragged
                        , dragshadow  // drag shadow
                        , d           // local data about the drag and drop operation
                        , 0          // flags set to 0 because not using currently
                );
                return false;
            }
            return true;
        });

        close = _win.findViewById(R.id.win_close);
        fullscreen = _win.findViewById(R.id.win_fullscreen);
        minimize = _win.findViewById(R.id.win_minimize);
        title = _win.findViewById(R.id.win_name);
        resize = _win.findViewById(R.id.win_resize);
        title.setText(null);

        close.setOnClickListener(view -> close());
        minimize.setOnClickListener(view -> {if(canMinimize)setVisibility(false);});

        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFullScreen(isFullscreen);
                isFullscreen=!isFullscreen;
            }
        });

        resize.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                     centerX = event.getRawX();
                     centerY = event.getRawY();
                }
                if(event.getAction() == MotionEvent.ACTION_MOVE){

                    int dX= (int) (event.getRawX()-centerX);
                    int dY= (int) (event.getRawY()-centerY);

                    centerX=event.getRawX();
                    centerY=event.getRawY();

                    FrameLayout.LayoutParams l = (FrameLayout.LayoutParams) _win.getLayoutParams();

                    int n_width =l.width+dX;
                    int n_height =l.height+dY;

                    l.width=(n_width> min_width)?n_width:min_width;
                    l.height=(n_height> min_height)?n_height:min_height;

                    Window.this.width =l.width;
                    Window.this.height =l.height;

                    _win.requestLayout();
                }
                return true;
            }});
    }

    public void openFullScreen(boolean value){
        if(value){
            fullscreen.setImageResource(R.drawable.baseline_fullscreen_24);
            FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) _win.getLayoutParams();
            p.width= Window.this.width;
            p.height= Window.this.height;
        } else {
            DisplayMetrics d = Utill.getDisplayMetrics(_context);
            fullscreen.setImageResource(R.drawable.baseline_fullscreen_exit_24);
            _win.setX(0);
            _win.setY(0);
            FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) _win.getLayoutParams();
            p.width=d.widthPixels;
            p.height=d.heightPixels;
        }
        _win.requestLayout();
    }


    public void setWindowSize(int width, int height){
        this.height =height;
        this.width =width;
        FrameLayout.LayoutParams l = (FrameLayout.LayoutParams) _win.getLayoutParams();
        l.height=Utill.dp(height,_context);
        l.width=Utill.dp(width,_context);
        _win.requestLayout();
    }

    public Window setWindowMinSize(int width, int height){
        min_height =height;
        min_width =width;
        return this;
    }


    public View getWindowView(){
        return _win;
    }

    public void addStateChangeListener(StateChange l){
        listeners.add(l);
    }

    public void removeStateChangeListener(StateChange l){
        listeners.remove(l);
    }

    public Window setTitle(String text){
        title.setText(text);
        return this;
    }

    public void onStateChanged(Window v){
        listeners.forEach(stateChange -> stateChange.onChanged(v));
    }

    public Window setBody(View body){
            ((FrameLayout) (_win.findViewById(R.id.window_content))).removeView(_body);
        ((FrameLayout) (_win.findViewById(R.id.window_content))).addView(body);
        this._body=body;
        return this;
    }

    public boolean isOpened(){
        return isOpened;
    }

    public boolean isVisible(){
        return _win.getVisibility() == View.VISIBLE;
    }

    public void setVisibility(boolean value){
            _win.setVisibility(value?View.VISIBLE:View.GONE);
            onStateChanged(this);
    }

    public void setCanFullscreen(boolean value){
        canFullscreen=value;
        fullscreen.setVisibility(canFullscreen?View.VISIBLE:View.INVISIBLE);
    }

    public void setCanDrag(boolean value){
        canDrag=value;
    }

    public void setCanMinimize(boolean value){
        canMinimize=value;
        minimize.setVisibility(canMinimize?View.VISIBLE:View.GONE);
    }

    public void setCanResize(boolean value){
        canResize=value;
        resize.setVisibility(canResize?View.VISIBLE:View.GONE);
    }


    public void spawn(){
             resize.setVisibility(canResize?View.VISIBLE:View.GONE);
             fullscreen.setVisibility(canFullscreen?View.VISIBLE:View.GONE);
             minimize.setVisibility(canMinimize?View.VISIBLE:View.GONE);
             title.setVisibility(title.getText() == null?View.INVISIBLE:View.VISIBLE);

            _win.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) _win.getLayoutParams();
            params.height = height;
            params.width = width;

        ((DesktopActivity)_context).spawnView(_win);
            isOpened = true;
        onStateChanged(this);
        openFullScreen(isSpawnFullScreen);
    }

    public void spawnFullScreen(boolean value){
        isSpawnFullScreen=value;
    }

    public void close(){
        isOpened=false;
        ((DesktopActivity)_context).removeView(_win);
        onStateChanged(this);
        listeners.clear();
    }

    public interface StateChange{
        public void onChanged(Window w);
    }


}
