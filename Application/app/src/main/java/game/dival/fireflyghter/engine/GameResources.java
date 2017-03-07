package game.dival.fireflyghter.engine;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

import game.dival.fireflyghter.engine.draw.Color;
import game.dival.fireflyghter.engine.draw.Pixel;

/**
 * Created by arauj on 24/02/2017.
 * Here it will be load image and sound resources of the game, this will run on the begin,
 * so it will not slow down the performance on the runtime
 */
public class GameResources {

    private Hashtable<String, Object3D> object3dList;
    private boolean isLoaded;

    public GameResources() {
        isLoaded = false;
        object3dList = new Hashtable<String, Object3D>();

    }

    public void addOBJ(String idLabel, InputStream inputStream) throws IOException {
        if (isLoaded)
            throw new RuntimeException("Can't create a 3d object now, create before");
        object3dList.put(idLabel, new Object3D(inputStream));
    }

    public Object3D get3DModel(String idLabel) {
        return object3dList.get(idLabel);
    }

    public void isLoaded() {
        isLoaded = true;
    }

    public class Object3D {
        public ArrayList<Pixel[]> faces;
        private float width, height, depth;

        public Object3D(InputStream inputStream) throws IOException {

            ArrayList<Pixel> vertexs = new ArrayList<>();
            faces = new ArrayList<>();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            int lineNo = 0;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                line = line.trim();
                // handle line continuation marker \
                while (line.endsWith("\\")) {
                    line = line.substring(0, line.length() - 1);
                    final String s = reader.readLine();
                    if (s != null) {
                        line += s;
                        line = line.trim();
                    }
                }

                // ignore comments. goto next line
                if (line.length() > 0 && line.charAt(0) == '#') {
                    continue;
                }

                // tokenize line
                final String[] tokens = line.split("\\s+");

                // no tokens? must be an empty line. goto next line
                if (tokens.length == 0) {
                    continue;
                }

                final String keyword = tokens[0];

                // if vertex
                if ("v".equals(keyword)) {
                    Pixel vertex = new Pixel(
                            Float.valueOf(tokens[1]),
                            Float.valueOf(tokens[2]),
                            Float.valueOf(tokens[3]),
                            new Color(255, 0, 0, 255));
                    Log.d("Pixel"," x:"+tokens[1]+" y:"+tokens[2]+" z:"+tokens[3]);
                    vertexs.add(vertex);
                }

                // if face
                if (("f".equals(keyword) || "fo".equals(keyword)) && tokens.length > 1) {
                    if (tokens.length < 4) {
                        throw new IOException("Wrong number of args.  f must have at least 3 vertices." +
                                "(line " + lineNo + ") " + line);
                    }

                    // Each token corresponds to 1 vertex entry and possibly one texture entry and normal entry.
                    Pixel[] face = new Pixel[3];
                    int faceIndex = 0;
                    Log.d("Face", " ");
                    for (int i = 1; i < tokens.length; i++) {
                        String[] fValues = tokens[i].split("//");
                        int vertexIndex = Integer.valueOf(fValues[0]);
                        face[faceIndex] = vertexs.get(vertexIndex);
                        Log.d("Pixel"+i, " x:" + face[faceIndex].xyz[0] + " y:" + face[faceIndex].xyz[1] + " z:" + face[faceIndex].xyz[2]);
                        faceIndex++;
                    }
                    faces.add(face);
                }
            }
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

        public float getDepth() {
            return depth;
        }

    }
}
