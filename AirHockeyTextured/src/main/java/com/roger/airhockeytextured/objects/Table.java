package com.roger.airhockeytextured.objects;

import android.opengl.GLES20;
import com.roger.airhockeytextured.data.Constands;
import com.roger.airhockeytextured.data.VertexArray;
import com.roger.airhockeytextured.programs.TextureShaderProgram;

/**
 * Created by Administrator on 2016/7/5.
 */
public class Table {
  private static final int POSITION_COMPONENT_COUNT = 2;
  private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
  private static final int STRIDE =
      (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constands.BYTES_PER_FLOAT;

  private static final float[] VERTEX_DATA = {//桌子的6个顶点数据 为 X Y S T
         0f,  0f, 0.5f, 0.5f,
      -0.5f,-0.8f, 0f, 0.9f,
       0.5f,-0.8f, 1f, 0.9f,
       0.5f, 0.8f, 1f, 0.1f,
      -0.5f, 0.8f, 0f, 0.1f,
      -0.5f,-0.8f, 0f, 0.9f
  };

  private final VertexArray vertexArray;

  public Table() {
    //构造函数 把数据复制到内存
    vertexArray = new VertexArray(VERTEX_DATA);
  }
  //从内存中读出绑定数据到着色器
  public void bindData(TextureShaderProgram textureProgram) {
    //从着色器程序获取每个属性的位置。
    vertexArray.setVertexAttribPointer(0, textureProgram.getPositionAttributeLocation(),
        POSITION_COMPONENT_COUNT, STRIDE);
//把位置数据绑定到被引用的着色器属性上
    vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,
        textureProgram.getTextureCoordinatesAttributeLocation(),//把纹理坐标数据绑定到被引用的着色器属性上
        TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
  }

  public void draw() {
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);//画桌子
  }
}
