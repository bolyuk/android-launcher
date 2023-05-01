package org.bolyuk.launcher.screenView;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {
    private boolean runFlag = false;
    private SurfaceHolder surfaceHolder;

    public DrawThread(SurfaceHolder surfaceHolder, Resources resources) {
        this.surfaceHolder = surfaceHolder;
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    public void draw(Canvas canvas){
        canvas.drawColor(Color.BLACK);
    }

    @Override
    public void run() {
        Canvas canvas;
        while (runFlag) {
            // получаем текущее время и вычисляем разницу с предыдущим
            // сохраненным моментом времени

            canvas = null;
            try {
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                  draw(canvas);
                }
                sleep(100);
            } catch (InterruptedException e) {

            } finally {
                if (canvas != null) {
                    // отрисовка выполнена. выводим результат на экран
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
