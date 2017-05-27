uniform mat4 u_Model;
uniform mat4 u_MVP;
uniform mat4 u_MVMatrix;
uniform vec3 u_LightPos;

attribute vec4 a_Position;
attribute vec4 a_Color;
attribute vec3 a_Normal;

varying vec4 v_Color;

void main()
    {
        vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);
        vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));
        float distance = length(u_LightPos - modelViewVertex);
        vec3 lightVector = normalize(u_LightPos - modelViewVertex);
        float diffuse = max(dot(modelViewNormal, lightVector), 0.1);
        diffuse = diffuse * (1.0 / (1.0 + (0.000025 * distance * distance)));

        vec4 difuseColor = a_Color * diffuse;
        difuseColor.a = 1.0;

        vec4 ambientColor = vec4(0.07f,0.07f,0.07f,1.0f);

        v_Color = ambientColor+difuseColor;


        gl_Position = u_MVP * a_Position;
    }