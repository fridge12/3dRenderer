public class Matrix {

//    public static void main(String args[]){
//
//        double a[][] = {
//                {1,0,0},
//                {0,1,0},
//                {0,0,1}
//        };
//
//        double b[][] = {
//                {1,2,3},
//                {4,5,6},
//                {7,8,9}
//        };
//
//        printMat(crossProduct(a,b));
//
//    }

    public static double[][] crossProduct(double a[][],double b[][]){
        if(a[0].length != b.length){
            return null;
        }

        double c[][] = new double[a.length][b[0].length];

        for(int i =0;i< c.length;i++){

            for(int j = 0;j<c[0].length;j++){
                c[i][j] = 0;
                for(int k = 0; k < b.length;k++){

                    c[i][j] += a[i][k]*b[k][j];
                }

            }

        }


        return  c;
    }

    public static double[][] scalarProduct(double a[][], double scalar){

        for(int i =0;i<a.length;i++){
            for(int j =0;j<a[0].length;j++){
                a[i][j] *= scalar;
            }
        }
        return  a;
    }

    public static void printMat(double a[][]){

        for(int i =0;i<a.length;i++){
            for(int j =0;j<a[0].length;j++){
                System.out.print(a[i][j]+" ");
            }
            System.out.println();
        }

    }
}
