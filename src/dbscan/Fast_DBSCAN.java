package dbscan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Fast_DBSCAN {

    Grid cg;

    Map<Integer,List<Point>> clusters;
    static int clusterCounter;
    
    Fast_DBSCAN(Grid cg){
        this.cg = cg;
        clusters = new HashMap<Integer,List<Point>>();
        clusterCounter = 0;        
    }

    
    // START DETERMINE CORE POINTS
    public int numNeighbourHoodPointsFromCell(Point p, long neighbouringKey) {				
        List<Point> pointList = cg.grid.get(neighbouringKey);
        int result = 0;
        if(pointList == null)
            return result;

        for(Point point: pointList)	        	   
             if(p.findDistanceFrom(point) <= cg.epsilon)
                 result++;
        return result;
    }

    public int numNeighbourHoodPoints(Point p,List<Long> neighbourCells){
        int result = 0;
        for(long neighbourCell: neighbourCells){
            result += numNeighbourHoodPointsFromCell(p,neighbourCell);
        }
        return result;
    }

    public void markCorePoints(int minPoints){
        Map<Long, List<Point>> grid = cg.grid;

        for(long key : grid.keySet()){
          List<Point> pointList = grid.get(key);
          int listSize = pointList.size();
          if(listSize > minPoints){
             for(Point p : pointList)
                p.Classifier = "CORE";
          }
          else{
            List<Long> neighbourCells = cg.getNeighbourKeys(key);
            for(Point p : pointList){
                int listSizeOfPoint = listSize - 1 + numNeighbourHoodPoints(p,neighbourCells);
                if(listSizeOfPoint >= minPoints)
                   p.Classifier = "CORE";
            }
          }
       }
    }    
    


    
    //START MERGE CLUSTERS
    void addPointsToClusterFromCell(long key, List<Point> clusterPoints){
        for(Point p : cg.grid.get(key)){
            p.clusterId = clusterCounter;

            if(p.Classifier.equals("UNCLASSIFIED")){
                p.Classifier = "BORDER";
            }
            clusterPoints.add(p);
        }
    }
	
    public List<Point> expandCluster(long key){
    	 Stack<Long> st = new Stack<Long>();
    	 List<Point> clusterPoints = new ArrayList<Point>();
    	 st.push(key);
    	 addPointsToClusterFromCell(key,clusterPoints);
    	 long popKey;
    	 
    	 while(!(st.empty())){
            popKey = st.pop();
            List<Long> aroundKeys = cg.getNeighbourKeys(popKey);
            for(long nkey : aroundKeys){
                if(cg.grid.containsKey(nkey)){
                    if(!(cg.isCellClassified(nkey))){
                        if(cg.cellHasCorePoint(nkey)){			 
                            if(cg.areCellsNeighbour(popKey,nkey)){
                                st.push(nkey);
                                addPointsToClusterFromCell(nkey,clusterPoints);
                            }
                        }
                    }
                }
            } 
    	 }
         return clusterPoints;	
    }
    
    public void mergeClusters(){
        Map<Long,List<Point>> grid = cg.grid;
        for(long key: grid.keySet()){
            if(!(cg.isCellClassified(key))){ //clusterId==0
                if(cg.cellHasCorePoint(key)){ //cell has atleast one core point
                    clusterCounter = clusterCounter + 1;
                    List<Point> clusterElements = expandCluster(key);
                    clusters.put(clusterCounter, clusterElements);
                }    	 	 
            }
        }
    }    



    // START DETERMINE BORDER POINTS
    List<Integer> getNeibouringClusterIdList(Point p, List<Long> neighbourCells){
        List<Integer> neighbourClusterIdList = new ArrayList<Integer>();
        for(long neighbourCell : neighbourCells){
            if(cg.grid.containsKey(neighbourCell)){
                List<Point> pointList = cg.grid.get(neighbourCell);
                for(Point np : pointList){
                    if(np.Classifier == "CORE"){	
                        if(p.findDistanceFrom(np) <= cg.epsilon){
                            neighbourClusterIdList.add(np.clusterId);
                        }
                    }
                }
            }
        }
        return neighbourClusterIdList;	
    }
	
    public void classifyBorderPoints(){
        Map<Long, List<Point>> grid = cg.grid;				
        int flag;

        for(long cell : grid.keySet()){
            if(cg.isCellClassified(cell)) continue;

            List<Point> pointList = grid.get(cell);
            List<Long> neighbourCells = cg.getNeighbourKeys(cell);

            for(Point p : pointList){		
               //list of clusterIds of core points in neighbourhood of p
               List<Integer> clusterIdList = getNeibouringClusterIdList(p, neighbourCells);
               
               if(clusterIdList != null){
                    for(int clusterId : clusterIdList){
                        //clusterId = itr.next();
                        if(clusterId == 0) continue;
                        p.clusterId = clusterId;
                        clusters.get(clusterId).add(p);
                        break;
                    }
                }
             }
        }
    }
    
    public Map<Integer, List<Point>> DBSCAN_CALL(int minPoints, double epsilon){
        //DetermineCorePoints cp = new DetermineCorePoints(cg);
        markCorePoints(minPoints);

        //MergeClusters mgc = new MergeClusters(cg);

        mergeClusters();

        //DetermineBorderPoints dbp = new DetermineBorderPoints(cg);
        classifyBorderPoints();

        return clusters;
    }
}
