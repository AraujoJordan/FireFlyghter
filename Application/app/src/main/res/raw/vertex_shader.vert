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
        // Ambient
        vec4 ambientColor = vec4(0.1,0.1,0.1,1.0);

        // Difuse
        vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);
        vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));
        float distance = length(u_LightPos - modelViewVertex);
        vec3 lightVector = normalize(u_LightPos - modelViewVertex);
        float diffuse = max(dot(modelViewNormal, lightVector), 0.5);
        diffuse = diffuse * (1.0 / (1.0 + (0.0000025 * distance * distance)));
        vec4 difuseColor = a_Color * diffuse;
        difuseColor.a = 1.0;

        // Specular
        float specularStrength = 1.0;
        vec3 viewDir = normalize(-modelViewVertex); // The viewer is at (0,0,0) so viewDir is (0,0,0) - Position => -Position
        vec3 norm = normalize(a_Normal);
        vec3 reflectDir = reflect(-lightVector, norm);
        float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);
        vec3 specular = specularStrength * spec * vec3(1.0,1.0,1.0);

        v_Color = ambientColor+difuseColor*vec4(specular,1.0);

        gl_Position = u_MVP * a_Position;
    }