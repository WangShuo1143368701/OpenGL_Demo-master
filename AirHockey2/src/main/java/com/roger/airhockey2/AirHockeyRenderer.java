package com.roger.airhockey2;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.roger.airhockey2.util.LoggerConfig;
import com.roger.airhockey2.util.ShaderHelper;
import com.roger.airhockey2.util.TextResourceReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ws on 2016/6/30.
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

  private static final int BYTES_PER_FLOAT = 4;
  private final FloatBuffer vertexData;
  private static final int POSITION_COMOPNENT_COUNT = 2;
  private Context context;
  private int program;
  float[] tableVertices = {
      //Order of coordinates:X,Y,R,G,B


      //Triangle Fan //前两个是顶点 后三个是颜色 X,Y,R,G,B
      0f,0f,1f,1f,1f,
      -0.5f,-0.5f,0.7f,0.7f,0.7f,
      0.5f,-0.5f,0.7f,0.7f,0.7f,
      0.5f,0.5f,0.7f,0.7f,0.7f,
      -0.5f,0.5f,0.7f,0.7f,0.7f,
      -0.5f,-0.5f,0.7f,0.7f,0.7f,

      -0.5f,0f,1f,0f,0f,
      0.5f,0f,1f,0f,0f,

      0f,-0.25f,0f,0f,1f,
      0f,0.25f,1f,0f,0f
  };

  private static final String A_POSITION = "a_Position";
  private int aPostionLocation;

  private static final String A_COLOR = "a_Color";
  private static final int COLOR_COMPONENT_COUNT = 3;
  private static final int STRIDE = (POSITION_COMOPNENT_COUNT+COLOR_COMPONENT_COUNT)*BYTES_PER_FLOAT;//STRIDE 内存跨距即每行的内存长度
  private int aColorLocation;

  public AirHockeyRenderer(Context context) {
    this.context = context;
    vertexData = ByteBuffer.allocateDirect(tableVertices.length*BYTES_PER_FLOAT).order(
        ByteOrder.nativeOrder()).asFloatBuffer();
    vertexData.put(tableVertices);
  }

  @Override public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    String vertexShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_vertex_shader);
    String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_fragment_shader);

    int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
    int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

    program = ShaderHelper.linkProgram(vertexShader,fragmentShader);

    if(LoggerConfig.ON){
      ShaderHelper.validateProgram(program);//验证生成的program 是否可用
    }

    GLES20.glUseProgram(program);
    aColorLocation = GLES20.glGetAttribLocation(program,A_COLOR);
    aPostionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
//开始读顶点
    vertexData.position(0);//从索引0处读 每隔STRIDE内存读一次
    GLES20.glVertexAttribPointer(aPostionLocation,POSITION_COMOPNENT_COUNT,GLES20.GL_FLOAT,false,STRIDE,vertexData);
    GLES20.glEnableVertexAttribArray(aPostionLocation);
//读颜色 从索引2处读
    vertexData.position(POSITION_COMOPNENT_COUNT);//从索引2处读 每隔STRIDE内存读一次
    GLES20.glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GLES20.GL_FLOAT,false,STRIDE,vertexData);
    GLES20.glEnableVertexAttribArray(aColorLocation);
  }

  @Override public void onSurfaceChanged(GL10 gl10, int i, int i1) {
    GLES20.glViewport(0, 0, i, i1);
  }

  @Override public void onDrawFrame(GL10 gl10) {
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    /* glDrawArrays 来绘制桌子了，第一个参数 GL_TRIANGLE_FAN 说明我们想绘制三角形扇，
    第二个参数表示告诉 OpenGL 从第几个顶点开始读，第三个参数是告诉 OpenGL 读入六个顶点。
    */
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);

    GLES20.glDrawArrays(GLES20.GL_LINES,6,2);

    GLES20.glDrawArrays(GLES20.GL_POINTS,8,1);

    GLES20.glDrawArrays(GLES20.GL_POINTS,9,1);

  }
}
