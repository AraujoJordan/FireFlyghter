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
        vec3 lightColor = vec3(0.5,0.5,0.5);


        // Ambient
        vec4 ambientColor = vec4(0.15,0.15,0.15,1.0);

        // Difuse
        vec3 norm = normalize(a_Normal);
        vec3 FragPos = vec3(u_MVMatrix * a_Position);
        vec3 lightDir = normalize(u_LightPos - FragPos);
        // float distance = length(u_LightPos - FragPos);
        float diff = max(dot(norm, lightDir), 0.0);
        vec3 diffuse = diff * lightColor;

        // Specular
        float specularStrength = 1.0;
        vec3 viewDir = normalize(-FragPos); // The viewer is at (0,0,0) so viewDir is (0,0,0) - Position => -Position
        vec3 reflectDir = reflect(-lightDir, norm);
        float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);
        vec3 specular = specularStrength * spec * lightColor;

        v_Color = (ambientColor + vec4(diffuse,1.0) + vec4(specular,1.0)) * a_Color;

        gl_Position = u_MVP * a_Position;
    }