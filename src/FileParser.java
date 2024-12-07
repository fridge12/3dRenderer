import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class FileParser {

    public static void parseFile(String path) throws FileNotFoundException {

        File f  = new File(path);
        Scanner sc = new Scanner(f);

        ArrayList<Vertex> vertexBuffer = new ArrayList<>();
        ArrayList<Triangle> mesh = new ArrayList<>();


        while(sc.hasNext()){
            String ch = sc.next();
            if(ch.equals("v")){
                vertexBuffer.add(new Vertex(sc.nextDouble(),sc.nextDouble(),sc.nextDouble()));
            }
            if(ch.equals("f")){
                mesh.add(new Triangle(
                        vertexBuffer.get(sc.nextInt()-1),
                        vertexBuffer.get(sc.nextInt()-1),
                        vertexBuffer.get(sc.nextInt()-1)
                ));
            }
            if(sc.hasNext()){
               sc.nextLine();}
        }

        Render.VertexBuffer = new Vertex[vertexBuffer.size()];
        Render.VertexBuffer = vertexBuffer.toArray(Render.VertexBuffer);

        Render.mesh = new Triangle[mesh.size()];
        Render.mesh = mesh.toArray(Render.mesh);

    }

}
