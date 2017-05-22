/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package com.roger.particles.programs;

import android.content.Context;
import com.roger.particles.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class ParticleShaderProgram extends ShaderProgram {
  // Uniform locations
  private final int uMatrixLocation;
  private final int uTimeLocation;

  // Attribute locations
  private final int aPositionLocation;
  private final int aColorLocation;
  private final int aDirectionVectorLocation;
  private final int aParticleStartTimeLocation;
  private final int uTextureUnitLocation;

  public ParticleShaderProgram(Context context) {
    super(context, R.raw.particle_vertex_shader, R.raw.particle_fragment_shader);

    // 获取着色器里面属性值的映射
    // Retrieve uniform locations for the shader program.
    uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
    uTimeLocation = glGetUniformLocation(program, U_TIME);
    uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

    // Retrieve attribute locations for the shader program.
    aPositionLocation = glGetAttribLocation(program, A_POSITION);//获取 A_POSITION 在 shader 中的位置
    aColorLocation = glGetAttribLocation(program, A_COLOR);
    aDirectionVectorLocation = glGetAttribLocation(program, A_DIRECTION_VECTOR);
    aParticleStartTimeLocation = glGetAttribLocation(program, A_PARTICLE_START_TIME);
  }

  /*
  public void setUniforms(float[] matrix, float elapsedTime) {
   */
  public void setUniforms(float[] matrix, float elapsedTime, int textureId) {
    glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);//传递矩阵给它的 uniform
    glUniform1f(uTimeLocation, elapsedTime);
    glActiveTexture(GL_TEXTURE0);//把活动的纹理单元设置为纹理单元 0
    glBindTexture(GL_TEXTURE_2D, textureId);//把纹理绑定到这个单元
    glUniform1i(uTextureUnitLocation, 0);//把被选定的纹理单元传递给片段着色器中的 u_TextureUnit 。
  }

  public int getPositionAttributeLocation() {
    return aPositionLocation;
  }

  public int getColorAttributeLocation() {
    return aColorLocation;
  }

  public int getDirectionVectorAttributeLocation() {
    return aDirectionVectorLocation;
  }

  public int getParticleStartTimeAttributeLocation() {
    return aParticleStartTimeLocation;
  }
}
