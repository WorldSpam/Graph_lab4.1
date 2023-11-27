package com.lab.graph_lab41;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Cube {
    private final Context mActivityContext;
    private final FloatBuffer mCubePositions;
    private final FloatBuffer mCubeColors;
    //private final FloatBuffer mCubeNormals;
    //private final ShortBuffer drawListBuffer;
    private final FloatBuffer mCubeTextureCoordinates;// textureCoordinatesBuffer
    static final int COORDS_PER_VERTEX = 3;
    private final int mProgram;

    //private int positionHandle;//positionHandle
    private int mColorHandle;//colorHandle
    private int mMVPMatrixHandle;//vPMatrixHandle
    private int mTextureUniformHandle;//textureUniformHandle
    //private int mNormalHandle;//normalHandle
    private int mTextureCoordinateHandle;//textureCoordinateHandle
    private int mPositionHandle;
    ShortBuffer indexBuffer = null;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private int mTextureDataHandle[] = new int[6];//textureDataHandle
/*
    private final float[][] cubeColorData = {  // Colors of the 6 faces
            {1.0f, 0.5f, 0.0f, 1.0f},  // 0. orange
            {1.0f, 0.0f, 1.0f, 1.0f},  // 1. violet
            {0.0f, 1.0f, 0.0f, 1.0f},  // 2. green
            {0.0f, 0.0f, 1.0f, 1.0f},  // 3. blue
            {1.0f, 0.0f, 0.0f, 1.0f},  // 4. red
            {1.0f, 1.0f, 0.0f, 1.0f}   // 5. yellow
    };*/
    /*
    private final float[] cubeColorData = {  // Colors of the 6 faces
        1.0f, 0.5f, 0.0f, 1.0f,  // 0. orange
        1.0f, 0.0f, 1.0f, 1.0f,  // 1. violet
        0.0f, 1.0f, 0.0f, 1.0f,  // 2. green
        0.0f, 0.0f, 1.0f, 1.0f,  // 3. blue
        1.0f, 0.0f, 0.0f, 1.0f,  // 4. red
        1.0f, 1.0f, 0.0f, 1.0f   // 5. yellow
    };
     */
    private final float[] cubeColorData =
        {
                // Front face (red)
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,

                // Right face (green)
                0.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,

                // Back face (blue)
                0.0f, 0.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f,

                // Left face (yellow)
                1.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 0.0f, 1.0f,

                // Top face (cyan)
                0.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 1.0f, 1.0f,

                // Bottom face (magenta)
                1.0f, 0.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 1.0f
        };
    /*
    static float cubeCoords[] = {
            -0.5f,  -0.5f, -0.5f,   // 0 bottom left back
            -0.5f, -0.5f, 0.5f,     // 1 bottom left front
            0.5f, -0.5f, -0.5f,      // 2 bottom right back
            0.5f,  -0.5f, 0.5f,      // 3 bottom right front
            -0.5f,  0.5f, -0.5f,   // 4 top left back
            -0.5f, 0.5f, 0.5f,     // 5 top left front
            0.5f, 0.5f, -0.5f,      // 6 top right back
            0.5f,  0.5f, 0.5f,      // 7 top right front
    };
    private short drawOrder[] = {
            0, 1, 3, 3, 2, 0,//bottom
            0, 1, 4, 4, 1, 5,//left
            5, 7, 6, 6, 4, 5,//top
            5, 1, 3, 5, 3, 7,//forward
            7, 3, 2, 7, 2, 6,//right
            6, 2, 0, 0, 4, 6,//back
    };

    final float[] cubeTextureCoordinateData =
            {
                    -0.5f, -0.5f,
                    -0.5f, 0.5f,
                    0.5f, -0.5f,
                    -0.5f, 0.5f,
                    0.5f, 0.5f,
                    0.5f, -0.5f,
            };

     */
    /*
    final float[] cubePositionData =
            {
                    // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
                    // if the points are counter-clockwise we are looking at the "front". If not we are looking at
                    // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
                    // usually represent the backside of an object and aren't visible anyways.

                    // Front face
                    -1.0f, 1.0f, 1.0f,
                    -1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    -1.0f, -1.0f, 1.0f,
                    1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,

                    // Right face
                    1.0f, 1.0f, 1.0f,
                    1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, -1.0f,
                    1.0f, -1.0f, 1.0f,
                    1.0f, -1.0f, -1.0f,
                    1.0f, 1.0f, -1.0f,

                    // Back face
                    1.0f, 1.0f, -1.0f,
                    1.0f, -1.0f, -1.0f,
                    -1.0f, 1.0f, -1.0f,
                    1.0f, -1.0f, -1.0f,
                    -1.0f, -1.0f, -1.0f,
                    -1.0f, 1.0f, -1.0f,

                    // Left face
                    -1.0f, 1.0f, -1.0f,
                    -1.0f, -1.0f, -1.0f,
                    -1.0f, 1.0f, 1.0f,
                    -1.0f, -1.0f, -1.0f,
                    -1.0f, -1.0f, 1.0f,
                    -1.0f, 1.0f, 1.0f,

                    // Top face
                    -1.0f, 1.0f, -1.0f,
                    -1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, -1.0f,
                    -1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, -1.0f,

                    // Bottom face
                    1.0f, -1.0f, -1.0f,
                    1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, -1.0f,
                    1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, -1.0f,
            };
            */

/*
    // R, G, B, A
    final float[] cubeColorData =
            {
                    // Front face (red)
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,

                    // Right face (green)
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,

                    // Back face (blue)
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,

                    // Left face (yellow)
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,

                    // Top face (cyan)
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,

                    // Bottom face (magenta)
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f
            };
 */
    /*
    static float cubePositionData[] = {
            -0.5f,  -0.5f, -0.5f,   // 0 bottom left back
            -0.5f, -0.5f, 0.5f,     // 1 bottom left front
            0.5f, -0.5f, -0.5f,      // 2 bottom right back
            0.5f,  -0.5f, 0.5f,      // 3 bottom right front
            -0.5f,  0.5f, -0.5f,   // 4 top left back
            -0.5f, 0.5f, 0.5f,     // 5 top left front
            0.5f, 0.5f, -0.5f,      // 6 top right back
            0.5f,  0.5f, 0.5f,      // 7 top right front
    };*/
    /*
    final float[] cubePositionData = {
            // X, Y, Z,

            -0.5f, -0.5f,  0.5f,
            0.5f, -0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,

            0.5f, -0.5f,  0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            0.5f,  0.5f,  0.5f,

            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,

            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f,

            -0.5f,  0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,

            -0.5f, -0.5f,  0.5f,
            0.5f, -0.5f,  0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,

    };*/
final float[] cubePositionData =
        {
                // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
                // if the points are counter-clockwise we are looking at the "front". If not we are looking at
                // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
                // usually represent the backside of an object and aren't visible anyways.

                // Front face
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,

                // Right face
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,

                // Back face
                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,

                // Left face
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,

                // Top face
                -0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,

                // Bottom face
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
        };
    /*
    private short indeces[] = {
            0, 1, 3, 3, 2, 0,//bottom
            0, 1, 4, 4, 1, 5,//left
            5, 7, 6, 6, 4, 5,//top
            5, 1, 3, 5, 3, 7,//forward
            7, 3, 2, 7, 2, 6,//right
            6, 2, 0, 0, 4, 6,//back
    };*/
    private short[] indeces = {
            0, 1, 2, 2, 3, 0,
            4, 5, 7, 5, 6, 7,
            8, 9, 11, 9, 10, 11,
            12, 13, 15, 13, 14, 15,
            16, 17, 19, 17, 18, 19,
            20, 21, 23, 21, 22, 23,
    };


    // S, T (or X, Y)
    // Texture coordinate data.
    // Because images have a Y axis pointing downward (values increase as you move down the image) while
    // OpenGL has a Y axis pointing upward, we adjust for that here by flipping the Y axis.
    // What's more is that the texture coordinates are the same for every face.
    /*
    final float[] cubeTextureCoordinateData =
            {
                    //front face
                    0.0f, 0.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 1.0f,


                    // Right face
                    0.0f, 0.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 1.0f,

                    // Back face
                    0.0f, 0.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 1.0f,

                    // Left face
                    0.0f, 0.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 1.0f,

                    // Top face
                    0.0f, 0.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 1.0f,
                    // Bottom face
                    0.0f, 0.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 1.0f,
            };*/
    final float[] cubeTextureCoordinateData =
            {
                    // Front face
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,

                    // Right face
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,

                    // Back face
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,

                    // Left face
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,

                    // Top face
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,

                    // Bottom face
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f
            };

    private final String vertexShaderCode =
                "uniform mat4 u_MVPMatrix;"+

                "attribute vec4 a_Position;"+		// Per-vertex position information we will pass in.
                "attribute vec4 a_Color;"+
                "attribute vec2 a_TexCoordinate;"+ // Per-vertex texture coordinate information we will pass in.
                "varying vec2 v_TexCoordinate;"+   // This will be passed into the fragment shader.
                "varying vec4 v_Color;"+
                    "void main() {" +
                    "  v_Color = a_Color;"+
                    "  v_TexCoordinate = a_TexCoordinate;"+
                    "  gl_Position = u_MVPMatrix * a_Position;" +
                    "}";
    private final String fragmentShaderCode =
                    "precision mediump float;" +

                    "uniform sampler2D u_Texture;"+
                    "varying vec4 v_Color;"+
                    "varying vec2 v_TexCoordinate;"+
                    "void main() {" +
                    "  gl_FragColor = (v_Color * texture2D(u_Texture, v_TexCoordinate));" +
                    //"  gl_FragColor = texture2D(u_Texture, v_TexCoordinate);" +
                    "}";


    public Cube(final Context activityContext){
        mActivityContext = activityContext;

        // initialize vertex byte buffer for shape coordinates
        // (# of coordinate values * 4 bytes per float)
        mCubePositions = ByteBuffer.allocateDirect(cubePositionData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubePositions.put(cubePositionData).position(0);

        mCubeColors = ByteBuffer.allocateDirect(cubeColorData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeColors.put(cubeColorData).position(0);


        indexBuffer = ByteBuffer.allocateDirect(indeces.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        indexBuffer.put(indeces).position(0);

        mCubeTextureCoordinates = ByteBuffer.allocateDirect(cubeTextureCoordinateData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeTextureCoordinates.put(cubeTextureCoordinateData).position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();            // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);  // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);// add the fragment shader to program

        // Bind attributes
        GLES20.glBindAttribLocation(mProgram, 0, "a_Position");
        GLES20.glBindAttribLocation(mProgram, 1, "a_Color");
        GLES20.glBindAttribLocation(mProgram, 2, "a_TexCoordinate");

        GLES20.glLinkProgram(mProgram);                 // creates OpenGL ES program executables

        //Load texture
        mTextureDataHandle[0] = loadTexture(mActivityContext, R.drawable.bumpy_bricks_public_domain);
        mTextureDataHandle[1] = loadTexture(mActivityContext, R.drawable.bumpy_bricks_public_domain);
        mTextureDataHandle[2] = loadTexture(mActivityContext, R.drawable.bumpy_bricks_public_domain);
        mTextureDataHandle[3] = loadTexture(mActivityContext, R.drawable.bumpy_bricks_public_domain);
        mTextureDataHandle[4] = loadTexture(mActivityContext, R.drawable.bumpy_bricks_public_domain);
        mTextureDataHandle[5] = loadTexture(mActivityContext, R.drawable.bumpy_bricks_public_domain);
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // Set program handles for cube drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate");
        ////////////////////////////////////////////////////////////////
/*
        // Pass in the position information
        mCubePositions.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                vertexStride, mCubePositions);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
*/
        /*
        // Pass in the color information.
        mCubeColors.position(16*i);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false,
                0, mCubeColors);
        GLES20.glEnableVertexAttribArray(mColorHandle);*/

        // Pass in the modelview matrix.
/*
        mCubeTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false,
                0, mCubeTextureCoordinates);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
*/
        //GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        ////////////////////////////////////////////////////////////////
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle[0]);
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        //draw(mCubePositions, 0);
        ///////////////////////////////////////////////////////////////
        //for (int face = 0; face < 6; face++){
            // Pass in the position information. each vertex needs 3 values and each face of the
//		cube needs 4 vertices. so total 3*4 = 12
        //    mCubePositions.position(12*face);
            GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                    0, mCubePositions);

            GLES20.glEnableVertexAttribArray(mPositionHandle);

            // Pass in the color information. every vertex colr is defined by 4 values and each cube
//        face has 4 vertices so 4*4 = 16
        //    mCubeColors.position(4*face);
            GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false,
                    0, mCubeColors);

            GLES20.glEnableVertexAttribArray(mColorHandle);

            // Pass in the texture coordinate information. every vertex needs 2 values to define texture
//        for each face of the cube we need 4 textures . so 4*2=8
        //    mCubeTextureCoordinates.position(8*face);
            GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false,
                    0, mCubeTextureCoordinates);

            GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
//*each face of the cube is drawn using 2 triangles. so 2*3=6 lines
            //GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
        /*
            // Set the active texture unit to texture unit 0.
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0+face);
            // Bind the texture to this unit.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle[face]);
            // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
            GLES20.glUniform1i(mTextureUniformHandle, face);

            mCubeTextureCoordinates.position(8*face);
            GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false,
                    0, mCubeTextureCoordinates);
            GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

            //GLES20.glUniform1i(mTextureUniformHandle, 0);
            GLES20.glUniform4fv(mColorHandle, 1, cubeColorData[face], 0);

            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

            indexBuffer.position(face * 6);
            // draw each face
            GLES20.glDrawElements(
                    GLES20.GL_TRIANGLES, 6,
                    GLES20.GL_UNSIGNED_SHORT, indexBuffer);*/
        //}
        // Draw the cube.
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
        // Disable vertex array
        //GLES20.glDisableVertexAttribArray(mPositionHandle);
        ////////////////////////////////////////////////////////////////
    }

    public static int loadTexture(final Context context, final int resourceId){
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1,textureHandle,0);
        if(textureHandle[0] != 0){
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false; //No pre-scaling
            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                    resourceId,options);
            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);
            // Load the bitmap into the bound texture
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0,bitmap,0);
            // Recycle the bitmap, since its data has been loaded into OpenGL
            bitmap.recycle();
        }
        if(textureHandle[0] == 0){
            throw new RuntimeException("Error loading texture.");
        }
        return textureHandle[0];
    }
}
