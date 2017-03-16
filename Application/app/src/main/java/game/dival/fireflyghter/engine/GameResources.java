package game.dival.fireflyghter.engine;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

import game.dival.fireflyghter.engine.draw.Color;
import game.dival.fireflyghter.engine.draw.Pixel;
import game.dival.fireflyghter.engine.math.Vector3D;

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

    public void addOBJ(Activity act,String idLabel, String fileName) {
        if (isLoaded)
            throw new RuntimeException("Can't create a 3d object now, create before");
        try {
            object3dList.put(idLabel, new Object3D(act.getAssets().open(fileName)));
        } catch (IOException error) {
            throw new RuntimeException("Can't create a 3d object, file exists? \n"+error.getMessage());
        }
    }

    public Object3D get3DModel(String idLabel) {
        return object3dList.get(idLabel);
    }

    public void isLoaded() {
        isLoaded = true;
    }

    public class Object3D {
        public final Vector3D center;
        public ArrayList<Pixel[]> faces;
        private float width, height, depth;

        public Object3D(InputStream inputStream) throws IOException {

            ArrayList<Pixel> vertexs = new ArrayList<>();
            faces = new ArrayList<>();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            int lineNo = 0;
            int vertexNumber = 1;

            float minWidth = 0, maxWidth = 0;
            float minHeight = 0, maxHeight = 0;
            float minDepth = 0, maxDepth = 0;

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
                    Color color = new Color();
                    switch (vertexNumber) {
                        case 1:
                            color = new Color(255, 0, 0, 255);
                            break;
                        case 2:
                            color = new Color(0, 255, 0, 255);
                            break;
                        case 3:
                            color = new Color(0, 0, 255, 255);
                            break;
                        case 4:
                            color = new Color(255, 255, 0, 255);
                            break;
                        case 5:
                            color = new Color(0, 255, 255, 255);
                            break;
                        case 6:
                            color = new Color(255, 0, 255, 255);
                            break;
                        case 7:
                            color = new Color(128, 255, 128, 255);
                            break;
                        case 8:
                            color = new Color(255, 128, 128, 255);
                    }
                    float x = Float.valueOf(tokens[1]);
                    float y = Float.valueOf(tokens[2]);
                    float z = Float.valueOf(tokens[3]);
                    Pixel vertex = new Pixel(x, y, z, color);

                    if (vertexNumber == 1) {
                        minWidth = maxWidth = x;
                        minHeight = maxHeight = y;
                        minDepth = maxDepth = z;
                    } else {
                        if(x < minWidth)
                            minWidth = x;
                        if(x > maxWidth)
                            maxWidth = x;
                        if(y < minHeight)
                            minHeight = y;
                        if(y > maxHeight)
                            maxHeight = y;
                        if(z < minDepth)
                            minDepth = z;
                        if(z > maxDepth)
                            maxDepth = z;
                    }

                    Log.d("Pixel " + vertexNumber++, " x:" + tokens[1] + " y:" + tokens[2] + " z:" + tokens[3]);
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

                    Log.d("Face", line);


                    for (int i = 0; i < face.length; i++) {
                        String[] faceValues = tokens[i + 1].split("//");
                        int vertexIndex = Integer.parseInt(faceValues[0]);
                        face[i] = vertexs.get(vertexIndex - 1); //the list begin with 0, different from the obj index
                        Log.d("Pixel " + vertexIndex, " x:" + face[i].xyz[0] + " y:" + face[i].xyz[1] + " z:" + face[i].xyz[2]);
                    }
                    faces.add(face);
                }
            }
            width = maxWidth - minWidth;
            height = maxHeight - minHeight;
            depth = maxDepth - minDepth;

            center = new Vector3D((maxWidth-minWidth)/2,(maxHeight-minHeight)/2,(maxDepth-minDepth)/2);

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
