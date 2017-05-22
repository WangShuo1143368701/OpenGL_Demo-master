uniform mat4 u_Matrix;                 //投影矩阵
uniform float u_Time;                  //当前时间

attribute vec3 a_Position;              //位置
attribute vec3 a_Color;                 //颜色
attribute vec3 a_DirectionVector;       //方向向量
attribute float a_ParticleStartTime;    //创建时间

varying vec3 v_Color;                  //片段着色器需要的 颜色 属性
varying float v_ElapsedTime;           //片段着色器需要的 存在时间 属性

void main()                    
{                                	  	  
    v_Color = a_Color;
    v_ElapsedTime = u_Time - a_ParticleStartTime;
    //float gravityFactor = v_ElapsedTime * v_ElapsedTime / 8.0;  //重力加速因子
    vec3 currentPosition = a_Position + (a_DirectionVector * v_ElapsedTime);//当前位置 即方向向量与运行时间的乘积
    //currentPosition.y -= gravityFactor;  //一直递减的Y值
    gl_Position = u_Matrix * vec4(currentPosition, 1.0);   //把粒子用矩阵进行投影
    /*
    gl_PointSize = 10.0;
    */
    gl_PointSize = 25.0;
}   
