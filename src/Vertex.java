public class Vertex {
    double x,y,z;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex(Vertex v){
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vertex() {
        x = 0;
        y = 0;
        z = 0;
    }

    public static double dotProduct(Vertex vert1, Vertex vert2){
        return (vert1.x*vert2.x) + (vert1.y*vert2.y) + (vert1.z*vert2.z);
    }

    public static double[][] vertextTo4dVector(Vertex vert){
        double vec[][] =  {{vert.x, vert.y, vert.z, 1}};
        return vec;
    }

    public static Vertex vectorToVertex(double[][] vec){
        return new Vertex(vec[0][0],vec[0][1],vec[0][2]);
    }

    public static void printVertex(Vertex vert){

        System.out.println("x: "+ vert.x);
        System.out.println("y: "+ vert.y);
        System.out.println("z: "+ vert.z);
    }
}
