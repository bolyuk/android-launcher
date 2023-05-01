package org.bolyuk.launcher.ui.tab;

import android.content.Context;
import android.view.View;

import org.bolyuk.launcher.R;
import org.bolyuk.launcher.ui.windows.Window;

public class WindowOpenButton extends TabButton{
    Window _win;

    Window.StateChange s = new Window.StateChange() {
        @Override
        public void onChanged(Window w) {
            if(_win.isOpened() && _win.isVisible()) {
                setBackgroundColor(getResources().getColor(R.color.teal_700));
                return;
            }
            if(_win.isOpened() && !_win.isVisible()){
                setBackgroundColor(getResources().getColor(R.color.teal_200));
                return;
            }

            if(!_win.isOpened()) {
                if(isPinned) {
                    setBackgroundColor(getResources().getColor(R.color.black));
                }else{
                    remove();
                }
            }
        }
    };

    public WindowOpenButton(Context context, int icon_id, Window win) {
        super(context, icon_id);
        _win=win;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_win.isOpened() && _win.isVisible()) {
                    _win.setVisibility(false);
                    return;
                }
                if(_win.isOpened() && !_win.isVisible()){
                    _win.setVisibility(true);
                    return;
                }

                if(!_win.isOpened()) {
                    _win.addStateChangeListener(s);
                    _win.spawn();
                }
            }
        });
        _win.addStateChangeListener(s);
    }
}
