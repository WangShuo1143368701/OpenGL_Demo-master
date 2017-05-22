uniform mat4 u_Matrix;
attribute vec3 a_Position;  
varying vec3 v_Position;

void main()                    
{                                	  	          
    v_Position = a_Position;	//把顶点位置传给片段着色器
    // Make sure to convert from the right-handed coordinate system of the
    // world to the left-handed coordinate system of the cube map, otherwise,
    // our cube map will still work but everything will be flipped.
    v_Position.z = -v_Position.z; //反转Z分量。把右手坐标系转化为左手坐标系
	           
    gl_Position = u_Matrix * vec4(a_Position, 1.0);//成u_Matrix即用投影~
    gl_Position = gl_Position.xyww;//把Z值变成W，这样透视除法之后为1，即Z始终在1的远平面上。Z=1最远，即在别的物体的后面，就像是背景。
}    
