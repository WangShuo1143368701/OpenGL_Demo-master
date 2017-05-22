package com.roger.particles.programs;

import android.content.Context;
import android.opengl.GLES20;
import com.roger.particles.util.ShaderHelper;
import com.roger.particles.util.TextResourceReader;


public class ShaderProgram {//封装的着色器程序

  protected static final String U_MATRIX = "u_Matrix";
  protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
  protected static final String U_TIME = "u_Time";


  protected static final String A_POSITION = "a_Position";
  protected static final String A_COLOR = "a_Color";
  protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

  protected static final String U_COLOR = "u_Color";

  protected static final String A_DIRECTION_VECTOR = "a_DirectionVector";
  protected static final String A_PARTICLE_START_TIME = "a_ParticleStartTime";

  protected final int program;
//获取到了着色器
  protected ShaderProgram(Context context, int vertexShaderResourceId,
      int fragmentShaderResourceId) {
    program = ShaderHelper.buildProgram(
        TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
        TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
  }
  //告诉 OpenGL 在绘制任何东西在屏幕上的时候要使用这里定义的程序。
  public void useProgram() {
    GLES20.glUseProgram(program);
  }
}
