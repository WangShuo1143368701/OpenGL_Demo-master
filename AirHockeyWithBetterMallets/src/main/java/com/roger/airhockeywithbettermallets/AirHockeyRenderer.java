package com.roger.airhockeywithbettermallets;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import com.roger.airhockeywithbettermallets.objects.Mallet;
import com.roger.airhockeywithbettermallets.objects.Puck;
import com.roger.airhockeywithbettermallets.objects.Table;
import com.roger.airhockeywithbettermallets.programs.ColorShaderProgram;
import com.roger.airhockeywithbettermallets.programs.TextureShaderProgram;
import com.roger.airhockeywithbettermallets.util.MatrixHelper;
import com.roger.airhockeywithbettermallets.util.TextureHelper;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2016/6/30.
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

  private final Context context;

  private final float[] projectionMatrix = new float[16];
  private final float[] modelMatrix = new float[16];

  private Table table;
  private Mallet mallet;

  private TextureShaderProgram textureProgram;
  private ColorShaderProgram colorProgram;
  private int texture;

  private final float[] viewMatrix = new float[16];
  private final float[] viewProjectionMatrix = new float[16];
  private final float[] modelViewProjectionMatrix = new float[16];

  private Puck puck;

  public AirHockeyRenderer(Context context) {
    this.context = context;
  }

  @Override public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    table = new Table();
    mallet = new Mallet(0.08f, 0.15f, 32);
    puck = new Puck(0.06f, 0.02f, 32);

    textureProgram = new TextureShaderProgram(context);
    colorProgram = new ColorShaderProgram(context);

    texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
  }

  @Override public void onSurfaceChanged(GL10 gl10, int width, int height) {
    GLES20.glViewport(0, 0, width, height);

    MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);

    Matrix.setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);//创建相机矩阵/也加视图矩阵
    /*调用这个方法时，把眼睛设为(0,1.2,2.2)，这意味着眼睛的位置在 x-z 平面上方的1.2个单位，并向后2.2个单位。
    换句话说，场景中的所有东西都出现在你下面 1.2 个单位和你前面 2.2 个单位的地方。
    把中心设为（0,0,0），以为这你将向下看你前面的原点，并把指向设为（0,1,0），以为着你的头是笔直向上的。*/
  }

  @Override public void onDrawFrame(GL10 gl10) {

    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

    Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

    positionTableInScene();
    textureProgram.useProgram();
    textureProgram.setUniforms(modelViewProjectionMatrix, texture);
    table.bindData(textureProgram);
    table.draw();

    positionObjectInScene(0f, mallet.height / 2f, -0.4f);
    colorProgram.useProgram();
    colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
    mallet.bindData(colorProgram);
    mallet.draw();

    positionObjectInScene(0f, mallet.height / 2f, 0.4f);
    colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f);
    mallet.draw();

    positionObjectInScene(0f, puck.height / 2f, 0f);
    colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
    puck.bindData(colorProgram);
    puck.draw();
  }
/*在 positionTableInScene 中，由于这个桌子是以 x y 坐标定义的，
因此要使它平放到地面上，我们需要让它绕 x 轴向后旋转90度。
最后通过把 viewProjectionMatrix 和 modelMatrix 相乘将所有的矩阵都合并到一起，
通过 modelViewProjectionMatrix 并传输给着色器程序。 positionObjectInScene 也是如此。*/
  private void positionTableInScene() {
    Matrix.setIdentityM(modelMatrix, 0);
    Matrix.rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
    Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
  }

  private void positionObjectInScene(float x, float y, float z) {
    Matrix.setIdentityM(modelMatrix, 0);
    Matrix.translateM(modelMatrix, 0, x, y, z);
    Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
  }
}
