precision mediump float; 

uniform samplerCube u_TextureUnit;//立方体纹理
varying vec3 v_Position;
	    	   								
void main()                    		
{
	gl_FragColor = textureCube(u_TextureUnit, v_Position);    
}
