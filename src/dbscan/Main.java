package dbscan;

import java.util.List;
import java.util.Map;


public class Main {

    static boolean verbose;

    public static void run_algorithm(int algorithm, int n, double epsilon, int minPoints){
        long startTime, endTime, totalTime;
        int totalClusters;
        Map<Integer, List<Point>> clusters;
        Grid cg;

        String inputFileName = "uniform_fill/" + n;

        List<Point> pointList = FileOperations.readFile(inputFileName);

        if(algorithm==1){
            // ORIGINAL DBSCAN
           pointList = FileOperations.readFile(inputFileName);
           startTime = System.currentTimeMillis();
           Original_DBSCAN o_dbscan = new Original_DBSCAN(pointList, epsilon);
           clusters = o_dbscan.DBSCAN_CALL(minPoints);
           endTime = System.currentTimeMillis();
           totalTime = endTime - startTime;
        }
        else if(algorithm==2){
            // ORIGINAL + GRID DBSCAN
            pointList = FileOperations.readFile(inputFileName);
            startTime = System.currentTimeMillis();
            cg = FileOperations.constructGrid(pointList, epsilon);
            Original_grid_DBSCAN og_dbscan = new Original_grid_DBSCAN(cg, pointList);
            clusters = og_dbscan.DBSCAN_CALL(minPoints, epsilon);
            endTime = System.currentTimeMillis();
            totalTime = endTime - startTime;
        }
        else{
            // FAST DBSCAN
            startTime = System.currentTimeMillis();
            cg = FileOperations.constructGrid(pointList, epsilon);
            Fast_DBSCAN f_dbscan = new Fast_DBSCAN(cg);
            clusters = f_dbscan.DBSCAN_CALL(minPoints, epsilon);
            endTime = System.currentTimeMillis();
            totalTime = endTime - startTime;
        }
        totalClusters = clusters.size();
        FileOperations.writeClustersToFile(clusters, "output/"+algorithm+"_"+n+"_"+epsilon+"_"+minPoints);

        //System.out.println(algorithm+","+n+","+epsilon+","+ minPoints +","+ totalTime);
        if(verbose)
            System.out.println(algorithm+","+n+","+epsilon+","+ minPoints +","+ totalTime +","+ totalTime/(n*Math.log(n)));
        else
            System.out.println(algorithm+","+n+","+epsilon+","+ minPoints +","+ totalTime);
        //System.out.println("Total number of clusters is " + totalClusters);
    }

    public static void main(String[] args) {




        verbose = false;

        System.out.println("Dependency of the running time on ε");
        System.out.println("algorithm,n,epsilon,minPoints,time(ms)");
        double[] epsList = {0.5, 1.0, 2.0, 4.0, 6.0, 8.0};
        for(int i=0; i < epsList.length;i++)
            run_algorithm(1, 10000, epsList[i], 4);
        System.out.println();
        for(int i=0; i < epsList.length;i++)
            run_algorithm(2, 100000, epsList[i], 4);
        System.out.println();
        for(int i=0; i < epsList.length;i++)
            run_algorithm(3, 100000, epsList[i], 4);
        /*
            algorithm,n,epsilon,minPoints,time(ms)
            1,10000,0.5,4,1711
            1,10000,1.0,4,1883
            1,10000,2.0,4,1640
            1,10000,4.0,4,1633
            1,10000,6.0,4,1223
            1,10000,8.0,4,1437
            2,100000,0.5,4,558
            2,100000,1.0,4,398
            2,100000,2.0,4,393
            2,100000,4.0,4,846
            2,100000,6.0,4,1956
            2,100000,8.0,4,4460
            3,100000,0.5,4,1557
            3,100000,1.0,4,449
            3,100000,2.0,4,400
            3,100000,4.0,4,174
            3,100000,6.0,4,101
            3,100000,8.0,4,94
        */



        System.out.println("\nDependency of the running time on minPts");
        System.out.println("algorithm,n,epsilon,minPoints,time(ms)");
        int[] minPts = {1,2,4,8,16,32};
        for(int i=0; i < minPts.length;i++)
            run_algorithm(1, 10000, 2.0, minPts[i]);
        System.out.println();
        for(int i=0; i < minPts.length;i++)
            run_algorithm(2, 100000, 2.0, minPts[i]);
        System.out.println();
        for(int i=0; i < minPts.length;i++)
            run_algorithm(3, 100000, 2.0, minPts[i]);

        /*
            algorithm,n,epsilon,minPoints,time(ms)
            1,10000,2.0,1,1914
            1,10000,2.0,2,1707
            1,10000,2.0,4,1415
            1,10000,2.0,8,1727
            1,10000,2.0,16,1378
            1,10000,2.0,32,1870
            2,100000,2.0,1,1094
            2,100000,2.0,2,512
            2,100000,2.0,4,334
            2,100000,2.0,8,392
            2,100000,2.0,16,683
            2,100000,2.0,32,830
            3,100000,2.0,1,671
            3,100000,2.0,2,875
            3,100000,2.0,4,317
            3,100000,2.0,8,313
            3,100000,2.0,16,385
            3,100000,2.0,32,324
        */

        verbose = true;

        System.out.println("\nDependency of the running time on n");
        System.out.println("algorithm,n,epsilon,minPoints,time(ms),time/(nlog(n)");
        int[] n = {100,500,1000,2000,5000,10000,20000,100000,250000,500000,1000000};
        for(int i=0; i < n.length-4;i++)
            run_algorithm(1, n[i], 2.0, 4);
        System.out.println();
        for(int i=0; i < n.length;i++)
            run_algorithm(2, n[i], 2.0, 4);
        System.out.println();
        for(int i=0; i < n.length;i++)
            run_algorithm(3, n[i], 2.0, 4);
        /*
            algorithm,n,epsilon,minPoints,time(ms),time/(nlog(n)
            1,100,2.0,4,18,0.039086503371292665
            1,500,2.0,4,22,0.007080092469736109
            1,1000,2.0,4,68,0.009844008256473708
            1,2000,2.0,4,111,0.007301764533279329
            1,5000,2.0,4,664,0.015591991048628313
            1,10000,2.0,4,1481,0.016079753192467898
            1,20000,2.0,4,8104,0.040914879515977644
            2,100,2.0,4,5,0.010857362047581295
            2,500,2.0,4,13,0.004183691004844064
            2,1000,2.0,4,14,0.0020267075822151754
            2,2000,2.0,4,27,0.0017761048864733504
            2,5000,2.0,4,58,0.0013619510253319912
            2,10000,2.0,4,100,0.0010857362047581296
            2,20000,2.0,4,108,5.45262461466632E-4
            2,100000,2.0,4,349,3.0313754836846975E-4
            2,250000,2.0,4,1026,3.301897669976931E-4
            2,500000,2.0,4,2129,3.244842318062872E-4
            2,1000000,2.0,4,3851,2.7874467496823716E-4
            3,100,2.0,4,4,0.008685889638065035
            3,500,2.0,4,7,0.0022527566949160347
            3,1000,2.0,4,16,0.002316237236817343
            3,2000,2.0,4,25,0.0016445415615493986
            3,5000,2.0,4,42,9.862403976542006E-4
            3,10000,2.0,4,52,5.645828264742274E-4
            3,20000,2.0,4,77,3.887519401197284E-4
            3,100000,2.0,4,350,3.0400613733227625E-4
            3,250000,2.0,4,916,2.9478930464901256E-4
            3,500000,2.0,4,2005,3.055851971684386E-4
            3,1000000,2.0,4,4006,2.8996394908407117E-4
        */
        //run_algorithm(int algorithm, int n, double epsilon, int minPoints);

    }

}
