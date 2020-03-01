package dbscan;
import java.io.*;
import java.util.*;


public class Original_grid_DBSCAN {
    Grid cg;
    
    Map<Integer,List<Point>> clusters;
    List<Point> points = new ArrayList<Point>();
    double eps;

    Original_grid_DBSCAN(Grid cg, List<Point> pointList){
        this.cg = cg;
        eps = cg.epsilon;
        clusters = new HashMap<Integer,List<Point>>();
        this.points = pointList;        
    }
    
    public List<Point> getPointsList() throws IOException {
        List<Point> l = new ArrayList<Point>();
        String ipFile = "new_input.txt";
        BufferedReader buffrd = new BufferedReader(new FileReader(ipFile));
        String checkStr = "";

        while ((checkStr = buffrd.readLine()) != null && checkStr != "") {
            String[] pt = checkStr.split(",");

            double xaxis = Double.parseDouble(pt[0]);
            double yaxis = Double.parseDouble(pt[1]);
            l.add(new Point(xaxis,yaxis));
        }
        buffrd.close();
        return l;
    }
    
     
    private double distFunc(Point q, Point p) {
        double x_diff = (q.x-p.x);
        double y_diff = (q.y-p.y);
        return Math.sqrt(x_diff*x_diff + y_diff*y_diff);
    }
    
    private Stack<Point> RangeQuery(Point q) {         
        Stack<Point> Neighbors = new Stack<Point>();

        int[] location = cg.getCellFromPoint(q);
     	long qCell = (long)location[0] * (long)(cg.nCols + 1) + (long)location[1];

        List<Long> neighbourCells = cg.getNeighbourKeys(qCell);
        neighbourCells.add(qCell);
        
        for(Long cell: neighbourCells){
            if(!cg.grid.containsKey(cell)) continue;
            List<Point> candidates = cg.grid.get(cell);
            for(Point p: candidates) {    
                if(p.x==q.x && p.y==q.y) continue;
                if(distFunc(q, p) <= eps){  
                    Neighbors.add(p);
                } 
            }
        }
        return Neighbors;
    }
     

     private void updateMap(Point p, int C){
        if(clusters.containsKey(C)){
            clusters.get(C).add(p);
        }
        else{
            List<Point> pl = new ArrayList<Point>();
            pl.add(p);
            clusters.put(C, pl);  
        }         
     }
     
    public Map<Integer, List<Point>> DBSCAN_CALL(int minPts, double epsilon) {
        int C = 0;                                     
        for(Point p: points) {
            if(!p.Classifier.equals("UNCLASSIFIED")) continue;
            Stack<Point> N = RangeQuery(p);    
            if(N.size() < minPts ) {           
               p.Classifier = "Noise";
               continue;
            }
            C = C + 1;                                        

            p.clusterId = C;
            p.Classifier = "CLASSIFIED";
            updateMap(p, C);


            while(!N.empty()){
                Point Q = N.pop();

                if(Q.Classifier.equals("Noise")) {
                    Q.clusterId = C;
                    Q.Classifier = "CLASSIFIED";
                    updateMap(Q, C);
                }

                if(!Q.Classifier.equals("UNCLASSIFIED")) continue;
                Q.clusterId = C;
                Q.Classifier = "CLASSIFIED";
                updateMap(Q, C);

                Stack<Point> N1 = RangeQuery(Q);   
                if(N1.size() >= minPts){
                    for(Point n1:N1){
                        N.add(n1);       
                    }
                }
            }
        }
        return clusters;
    }

     
    private void outputCluster() {    
        int idx = 1;
        for(List<Point> entry: clusters.values()){
            if (entry.isEmpty()) continue;
            for (Point point : entry ){
                System.out.println(point.toString()+"[cluster:"+idx+"]");
            }
            idx++;
        }
        System.out.println("total number of cluster formed:");
        System.out.println(idx++ -1);
    }
         
    public void main(String[] args) throws FileNotFoundException,IOException  {
        long execTime=System.currentTimeMillis();        
        //DBSCAN_CALL(minPoints, epsilon);
        long endTime= System.currentTimeMillis();

        outputCluster();
        
        long elapseTime= endTime - execTime;
        System.out.println("time taken to execute(milliseconds):");
        System.out.println(elapseTime);
    }
}
