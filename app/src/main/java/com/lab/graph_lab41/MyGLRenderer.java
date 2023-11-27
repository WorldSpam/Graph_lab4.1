package com.lab.graph_lab41;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private final Context mActivityContext;
    private Cube mCube;

    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] rotationMatrix = new float[16];
    private final float[] rotationMatrix1 = new float[16];
    private final float[] rotationMatrix2 = new float[16];

    private volatile float mDeltaX;
    private volatile float mDeltaY;
    public float getmDeltaX() {
        return mDeltaX;
    }
    public void setmDeltaX(float mDeltaX) {
        this.mDeltaX = mDeltaX;
    }
    public float getmDeltaY() {
        return mDeltaY;
    }
    public void setmDeltaY(float mDeltaY) {
        this.mDeltaY = mDeltaY;
    }

    public MyGLRenderer(final Context a){
        mActivityContext = a;
    }
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        // Enable depth testing
        GLES20.glEnable( GLES20.GL_DEPTH_TEST );
        GLES20.glDepthFunc( GLES20.GL_LEQUAL );
        GLES20.glDepthMask( true );

        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        mCube = new Cube(mActivityContext);
    }

    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        Matrix.setRotateM(rotationMatrix1, 0, mDeltaY, 1.0f, 0.0f, 0.0f);
        Matrix.setRotateM(rotationMatrix2, 0, mDeltaX, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(rotationMatrix, 0, rotationMatrix1, 0, rotationMatrix2, 0);

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        mCube.draw(scratch);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
