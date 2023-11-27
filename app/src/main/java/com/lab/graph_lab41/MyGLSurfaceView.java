package com.lab.graph_lab41;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer renderer;
    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setEGLConfigChooser( true );
        renderer = new MyGLRenderer(context);
        setRenderer(renderer);
    }

    private float previousX;
    private float previousY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = (x - previousX)  / 2f;
                float dy = (y - previousY)  / 2f;
                renderer.setmDeltaX(renderer.getmDeltaX() + dx);
                renderer.setmDeltaY(renderer.getmDeltaY() + dy);

                //requestRender();
        }

        previousX = x;
        previousY = y;
        return true;
    }
}
