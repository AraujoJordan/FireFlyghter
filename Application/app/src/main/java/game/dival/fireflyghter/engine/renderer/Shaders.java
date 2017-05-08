package game.dival.fireflyghter.engine.renderer;

/**
 * Created by araujojordan on 08/05/17.
 */

public class Shaders {
    public static String grid_fragment_shader = "precision mediump float;\n" +
            "varying vec4 v_Color;\n" +
            "varying vec3 v_Grid;\n" +
            "\n" +
            "void main() {\n" +
            "    float depth = gl_FragCoord.z / gl_FragCoord.w; // Calculate world-space distance.\n" +
            "\n" +
            "    if ((mod(abs(v_Grid.x), 10.0) < 0.1) || (mod(abs(v_Grid.z), 10.0) < 0.1)) {\n" +
            "        gl_FragColor = max(0.0, (90.0-depth) / 90.0) * vec4(1.0, 1.0, 1.0, 1.0)\n" +
            "                + min(1.0, depth / 90.0) * v_Color;\n" +
            "    } else {\n" +
            "        gl_FragColor = v_Color;\n" +
            "    }\n" +
            "}\n";

    public static String light_vertex = "uniform mat4 u_Model;\n" +
            "uniform mat4 u_MVP;\n" +
            "uniform mat4 u_MVMatrix;\n" +
            "uniform vec3 u_LightPos;\n" +
            "\n" +
            "attribute vec4 a_Position;\n" +
            "attribute vec4 a_Color;\n" +
            "attribute vec3 a_Normal;\n" +
            "\n" +
            "varying vec4 v_Color;\n" +
            "varying vec3 v_Grid;\n" +
            "\n" +
            "void main() {\n" +
            "   v_Grid = vec3(u_Model * a_Position);\n" +
            "\n" +
            "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);\n" +
            "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));\n" +
            "\n" +
            "   float distance = length(u_LightPos - modelViewVertex);\n" +
            "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);\n" +
            "   float diffuse = max(dot(modelViewNormal, lightVector), 0.5);\n" +
            "\n" +
            "   diffuse = diffuse * (1.0 / (1.0 + (0.00001 * distance * distance)));\n" +
            "   v_Color = vec4(a_Color.rgb * diffuse, a_Color.a);\n" +
            "   gl_Position = u_MVP * a_Position;\n" +
            "}";

    public static String passthrough_fragment = "precision mediump float;\n" +
            "varying vec4 v_Color;\n" +
            "\n" +
            "void main() {\n" +
            "    gl_FragColor = v_Color;\n" +
            "}";
}
