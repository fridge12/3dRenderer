import java.io.FileNotFoundException;

public class Render {

    final static char CHARS[] = {' ','.',',','!','*','a','%','&','@','#','\u2588'};
    static boolean LOOP = true;
    static int delay =0;

    static int screenHeight = 100;
    static int screenWidth = 200;
    static int screen[][] = new int[screenHeight][screenWidth];

    static Vertex VertexBuffer[]  ;
    static int indexBuffer[][] = new int[12][3];
    //I don't think this is necessary
    static Triangle mesh[] ;

    static double projectionMatrix[][] = new double[4][4];
    static double fovRad =90 * (Math.PI/180d);
    static double aspectRatio = (double) screenHeight/(double) screenWidth;
    static double zNear = 0.1;
    static double zFar = 1000;

    static double xRotationMatrix[][] = new double[4][4];
    static double yRotationMatrix[][] = new double[4][4];
    static double zRotationMatrix[][] = new double[4][4];

    static Vertex cameraNormal = new Vertex(0,0,-1);

    public static void main(String args[]) throws InterruptedException, FileNotFoundException {
        int x =0;
        int y =0;

        FileParser.parseFile("C:\\Users\\ahanw\\git\\3dRenderer\\src\\ship.obj");
        delay = 0;

//        for(int i =0;i< VertexBuffer.length;i++){
//            Vertex.printVertex(VertexBuffer[i]);
//        }
//        System.exit(0);

//        VertexBuffer[0] = new Vertex(-0.5,-0.5,0);
//        VertexBuffer[1] = new Vertex(-0.5,0.5,0);
//        VertexBuffer[2] = new Vertex(0.5,-0.5,0);
//        VertexBuffer[3] = new Vertex(0.5,0.5,0);
//        VertexBuffer[4] = new Vertex(-0.5,-0.5,1);
//        VertexBuffer[5] = new Vertex(-0.5,0.5,1);
//        VertexBuffer[6] = new Vertex(0.5,-0.5,1 );
//        VertexBuffer[7] = new Vertex(0.5,0.5,1);
//
//        indexBuffer[0][0] = 0;indexBuffer[0][1] = 1;indexBuffer[0][2] = 2;
//
//        indexBuffer[1][0] = 1;indexBuffer[1][1] = 3;indexBuffer[1][2] = 2;
//
//        indexBuffer[2][0] = 4;indexBuffer[2][1] = 5;indexBuffer[2][2] = 0;
//
//        indexBuffer[4][0] = 5;indexBuffer[4][1] = 1;indexBuffer[4][2] = 0;
//
//        indexBuffer[5][0] = 6;indexBuffer[5][1] = 7;indexBuffer[5][2] = 4;
//
//        indexBuffer[6][0] = 7;indexBuffer[6][1] = 5;indexBuffer[6][2] = 4;
//
//        indexBuffer[7][0] = 2;indexBuffer[7][1] = 3;indexBuffer[7][2] = 6;
//
//        indexBuffer[8][0] = 3;indexBuffer[8][1] = 7;indexBuffer[8][2] = 6;
//
//        indexBuffer[9][0] = 1;indexBuffer[9][1] = 5;indexBuffer[9][2] = 3;
//
//        indexBuffer[10][0] = 5;indexBuffer[10][1] = 7;indexBuffer[10][2] = 3;
//
//        indexBuffer[11][0] = 4;indexBuffer[11][1] = 0;indexBuffer[11][2] = 6;
//
//        indexBuffer[3][0] = 0;indexBuffer[3][1] = 2;indexBuffer[3][2] = 6;



        generateProjectionMatrix(fovRad,aspectRatio,zNear,zFar,projectionMatrix);
        generateXRotationMatrix(0);
        generateYRotationMatrix(0);
        generateZRotationMatrix(0);


        //generateMesh();

        //continuously render the screen
        double thetaRad = 0;
        do {
            clear(screen);

            generateZRotationMatrix(thetaRad);
            //generateYRotationMatrix(thetaRad );
            generateXRotationMatrix(-Math.PI/2);
            thetaRad += 0.1;

            //setting the cube to random rotations
//            generateXRotationMatrix(2*Math.PI*Math.random());
//            generateYRotationMatrix(2*Math.PI*Math.random());
//            generateZRotationMatrix(2*Math.PI*Math.random());
//            delay = 1000;

            drawMesh(projectionMatrix, mesh, screen);

            //drawLine(screen[0].length-x-1, screen.length-y-1,x,y, screen);
            print(screen);
            Thread.sleep(100+delay);
        } while(LOOP);
    }




    public static void drawMesh(double[][] projectionMatrix, Triangle[] mesh,int a[][]){
        for (Triangle tri:mesh) {
//            System.out.println("triangle");
//            Triangle.printTriangle(tri);

            tri = rotateTriangle(tri,Matrix.crossProduct(xRotationMatrix,Matrix.crossProduct(yRotationMatrix,zRotationMatrix)));

//            tri = rotateTriangle(tri,xRotationMatrix);
//            tri = rotateTriangle(tri,yRotationMatrix);
//            tri = rotateTriangle(tri,zRotationMatrix);

//            System.out.println("triangle rotated");
//            Triangle.printTriangle(tri);

            tri = translateZTriangle(tri,5);
            tri = translateYTriangle(tri,  0.5);


            tri = projectTriangle(tri,projectionMatrix);

//            System.out.println("triangle projected");
//            Triangle.printTriangle(tri);

            tri = scaleIntoView(tri);

//            System.out.println("triangle scaled");
//            Triangle.printTriangle(tri);

            if(Vertex.dotProduct(cameraNormal,Triangle.normalOfTriangle(tri)) >= 0) {
                drawTriangle(tri, a);
            }
//            System.out.println("triangle drawn");
//            Triangle.printTriangle(tri);

        }
    }

    public static void generateXRotationMatrix(double thetaRad){
        xRotationMatrix[0][0] = 1;
        xRotationMatrix[1][1] = Math.cos(thetaRad);
        xRotationMatrix[2][1] = Math.sin(thetaRad);
        xRotationMatrix[1][2] = -Math.sin(thetaRad);
        xRotationMatrix[2][2] = Math.cos(thetaRad);
        xRotationMatrix[3][3] = 1;
    }

    public static void generateYRotationMatrix(double thetaRad){
        yRotationMatrix[1][1] = 1;
        yRotationMatrix[0][0] = Math.cos(thetaRad);
        yRotationMatrix[0][2] = Math.sin(thetaRad);
        yRotationMatrix[2][0] = -Math.sin(thetaRad);
        yRotationMatrix[2][2] = Math.cos(thetaRad);
        yRotationMatrix[3][3] = 1;
    }

    public static void generateZRotationMatrix(double thetaRad){
        zRotationMatrix[2][2] = 1;
        zRotationMatrix[0][0] = Math.cos(thetaRad);
        zRotationMatrix[0][1] = Math.sin(thetaRad);
        zRotationMatrix[1][0] = -Math.sin(thetaRad);
        zRotationMatrix[1][1] = Math.cos(thetaRad);
        zRotationMatrix[3][3] = 1;
    }


    public static Triangle scaleIntoView(Triangle tri){

        tri.vert1.x += 1d;
        tri.vert2.x += 1d;
        tri.vert3.x += 1d;

        tri.vert1.y += 1d;
        tri.vert2.y += 1d;
        tri.vert3.y += 1d;

        tri.vert1.x *= 0.5d * screenWidth;
        tri.vert2.x *= 0.5d * screenWidth;
        tri.vert3.x *= 0.5d * screenWidth;

        tri.vert1.y *= 0.5d * screenHeight;
        tri.vert2.y *= 0.5d * screenHeight;
        tri.vert3.y *= 0.5d * screenHeight;

        return tri;
    }

    public static Triangle translateZTriangle(Triangle tri, double translate){
        Triangle transTri = new Triangle(tri);

        transTri.vert1.z += translate;
        transTri.vert2.z += translate;
        transTri.vert3.z += translate;
        return transTri;
    }

    public static Triangle translateYTriangle(Triangle tri, double translate){
        Triangle transTri = new Triangle(tri);

        transTri.vert1.y += translate;
        transTri.vert2.y += translate;
        transTri.vert3.y += translate;
        return transTri;
    }

    public static Triangle translateXTriangle(Triangle tri, double translate){
        Triangle transTri = new Triangle(tri);

        transTri.vert1.x += translate;
        transTri.vert2.x += translate;
        transTri.vert3.x += translate;
        return transTri;
    }

    public static Triangle rotateTriangle (Triangle tri, double rotationMatrix[][]){
        Triangle rotTri = new Triangle();

        double[][] vert1 = Matrix.crossProduct(Vertex.vertextTo4dVector(tri.vert1),rotationMatrix);
        rotTri.vert1 = Vertex.vectorToVertex(vert1);

        double[][] vert2 = Matrix.crossProduct(Vertex.vertextTo4dVector(tri.vert2),rotationMatrix);
        rotTri.vert2 = Vertex.vectorToVertex(vert2);

        double[][] vert3 = Matrix.crossProduct(Vertex.vertextTo4dVector(tri.vert3),rotationMatrix);
        rotTri.vert3 = Vertex.vectorToVertex(vert3);

        return rotTri;
    }


    public static Triangle projectTriangle (Triangle tri, double projectionMatrix[][]){
        Triangle projTri = new Triangle();

        double[][] vert1 = Matrix.crossProduct(Vertex.vertextTo4dVector(tri.vert1),projectionMatrix);
        if(vert1[0][3] != 0 ){
            vert1 = Matrix.scalarProduct(vert1,1d/vert1[0][3]);
        }
        projTri.vert1 = Vertex.vectorToVertex(vert1);

        double[][] vert2 = Matrix.crossProduct(Vertex.vertextTo4dVector(tri.vert2),projectionMatrix);
        if(vert2[0][3] != 0 ){
            vert2 = Matrix.scalarProduct(vert2,1d/vert2[0][3]);
        }
        projTri.vert2 = Vertex.vectorToVertex(vert2);

        double[][] vert3 = Matrix.crossProduct(Vertex.vertextTo4dVector(tri.vert3),projectionMatrix);
        if(vert3[0][3] != 0 ){
            vert3 = Matrix.scalarProduct(vert3,1d/vert3[0][3]);
        }
        projTri.vert3 = Vertex.vectorToVertex(vert3);




        return projTri;
    }

    public static void generateProjectionMatrix(double fovRad, double aspectRatio,double zNear,double zFar, double projectionMatrix[][]){
        projectionMatrix[0][0] = aspectRatio * (1d / Math.tan(fovRad/2d));
        projectionMatrix[1][1] = (1d / Math.tan(fovRad/2d));
        projectionMatrix[2][2] = zFar / (zFar - zNear);

        //swapped their locations
        projectionMatrix[3][2] = -zNear * (zFar / (zFar - zNear));
        projectionMatrix[2][3] = 1d;
    }

    //don't think this is necessary
//    public static void generateMesh(){
//        for(int i =0;i<indexBuffer.length;i++){
//            int vert1 =indexBuffer[i][0];
//            int vert2 = indexBuffer[i][1];
//            int vert3 = indexBuffer[i][2];
//            mesh[i] = new Triangle(VertexBuffer[vert1],VertexBuffer[vert2],VertexBuffer[vert3]);
//        }
//    }

//    public static void drawMesh(int a[][]){
////        for(int i =0;i< mesh.length;i++){
////            drawTriangle(mesh[i],a);
////        }
//        for(int i =0;i< indexBuffer.length;i++){
//            int vert1 =indexBuffer[i][0];
//            int vert2 = indexBuffer[i][1];
//            int vert3 = indexBuffer[i][2];
//            drawTriangle(VertexBuffer[vert1],VertexBuffer[vert2],VertexBuffer[vert3],a);
//        }
//
//    }

    public static void drawLine(int x1, int y1, int x2, int y2, int a[][]){
        drawLine(x1,y1,x2,y2,a,5);
    }

    public static void drawLine(int x1, int y1, int x2, int y2, int a[][],int brightness){
        if(x1> x2 ){
            drawLine(x2, y2, x1, y1, a,brightness);
            return;
        }

        float riseOverRun = (float)(y2-y1)/ (float)(x2-x1);
        float yCovered = y1;
        for(;x1<=x2 && x1<a[0].length && y1<a.length;x1++){
            yCovered += riseOverRun;

            if(x1<0 || y1 < 0)
                continue;

            a[y1][x1] = brightness;
            if(riseOverRun>=0) {
                for (; y1 <= y2 && y1 < yCovered && y1 < a.length; y1++) {
//                    if(y1<0)
//                        continue;
                    a[y1][x1] = brightness;
                }
            }
            else{
                for (; y1 >= y2 && y1 > yCovered && y1 >0; y1--) {
//                    if(y1<0)
//                        continue;
                    a[y1][x1] = brightness;
                }
            }

        }
    }

    public static void drawTriangle(Triangle t, int a[][]){

        drawLine((int) (t.vert1.x/t.vert1.z),(int)(t.vert1.y/t.vert1.z),(int)(t.vert2.x/t.vert2.z),(int)(t.vert2.y/t.vert2.z),a);
        drawLine((int) (t.vert2.x/t.vert2.z),(int)(t.vert2.y/t.vert2.z),(int)(t.vert3.x/t.vert3.z),(int)(t.vert3.y/t.vert3.z),a);
        drawLine((int) (t.vert3.x/t.vert3.z),(int)(t.vert3.y/t.vert3.z),(int)(t.vert1.x/t.vert1.z),(int)(t.vert1.y/t.vert1.z),a);

    }

//    public static void drawTriangle( Vertex vert1,Vertex vert2, Vertex vert3, int a[][]){
//        drawLine((int) vert1.x,(int)vert1.y,(int)vert2.x,(int)vert2.y,a);
//        drawLine((int) vert2.x,(int)vert2.y,(int)vert3.x,(int)vert3.y,a);
//        drawLine((int) vert3.x,(int)vert3.y,(int)vert1.x,(int)vert1.y,a);
//    }

    public static void clear(int a[][]){
        for(int i =0;i< a.length;i++){
            for(int j = 0;j<a[0].length;j++){
                a[i][j]= 0;
            }
        }
    }

    public static void print(int a[][]){
        StringBuffer print = new StringBuffer("");
        for(int i =0;i< a.length;i++){
            for(int j = 0;j<a[0].length;j++){
                print.append(CHARS[Math.min(a[i][j], CHARS.length-1)]);
            }
            print.append("\n");
        }

        System.out.println("\033[33m");
        System.out.print("\033[H\033[2J");
        System.out.println(print.toString());
        System.out.println("\033[39m");
        System.out.println("\ntest");
    }


}
