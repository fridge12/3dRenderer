public class Triangle {

    Vertex vert1;
    Vertex vert2;
    Vertex vert3;

    public Triangle(Vertex vert1, Vertex vert2, Vertex vert3) {
        this.vert1 = vert1;
        this.vert2 = vert2;
        this.vert3 = vert3;
    }
    public Triangle(){}

   public Triangle(Triangle t){
        this.vert1 = new Vertex(t.vert1);
        this.vert2 = new Vertex(t.vert2);
        this.vert3 = new Vertex(t.vert3);
   }

   public static Vertex normalOfTriangle(Triangle tri){
        Vertex u = new Vertex((tri.vert2.x - tri.vert1.x),(tri.vert2.y - tri.vert1.y),(tri.vert2.z - tri.vert1.z));
        Vertex v = new Vertex((tri.vert3.x - tri.vert1.x),(tri.vert3.y - tri.vert1.y),(tri.vert3.z - tri.vert1.z));
        Vertex normal = new Vertex();

        normal.x = (u.y * v.z) - (u.z * v.y);
        normal.y = (u.z * v.x) - (u.x * v.z);
        normal.z = (u.x * v.y) - (u.y * v.x);

        //still need to normalise the vector
        return normal;
   }

    public static void printTriangle(Triangle tri){
        Vertex.printVertex(tri.vert1);
        Vertex.printVertex(tri.vert2);
        Vertex.printVertex(tri.vert3);
    }
}
