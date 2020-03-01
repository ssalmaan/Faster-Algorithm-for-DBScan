package dbscan;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Grid { 
	 	 
    Map<Long,List<Point>> grid = new HashMap<Long,List<Point>>();
    double epsilon;
    double minX;
    double minY;
    double maxX;
    double maxY;
    double cellWidth;
    int nRows;
    int nCols;
    
    public void setParams(double minX, double minY, double maxX, double maxY, double epsilon){
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.epsilon = epsilon;
        this.cellWidth = epsilon / Math.sqrt(2);
        this.nRows = (int) Math.floor(((maxX - minX)/cellWidth) + 1);
        this.nCols = (int) Math.floor(((maxY - minY)/cellWidth) + 1);        
    }
	
    public void addPointToGrid(Point p) {
    	int[] location = getCellFromPoint(p);

     	long cell = (long)location[0] * (long)(nCols + 1) + (long)location[1];
     	      
        if(grid.containsKey(cell)){
            grid.get(cell).add(p);
            return;
        }
        List<Point> points = new ArrayList<Point>();
        points.add(p);
        grid.put(cell,points);
    }
    
    public int[] getLocationInGridFromKey(long key){	
    	int x_coord = (int)(key / (long)(nCols + 1));
    	int y_coord = (int)(key % (long)(nCols + 1));
    	
    	int[] result = {x_coord,y_coord};
    	
    	return result;
     	
    }
    
    public int[] getCellFromPoint(Point p){
    	int X = (int) Math.floor(((p.x - minX)/cellWidth) + 1);
    	int Y = (int) Math.floor(((p.y - minY)/cellWidth) + 1);  	
    	int[] location = {X,Y};
    	 
    	return location;
    }
    
    public long getKeyFromLocationInGrid(int x_coord, int y_coord) {
    	long ans = ((long)x_coord * (long)(nCols + 1) + (long)y_coord);
    	return ans;
    }
    
    public List<Long> getNeighbourKeys(long key){
        List<Long> neighbourKeys = new ArrayList<Long>();

        int[] location = getLocationInGridFromKey(key);
        int x = location[0];
        int y = location[1];

        for(int i = -2; i<=2; i++){
            for(int j = -2; j<=2; j++){
                if(i==0 && j==0) continue; 
                if(i*j == 4 || i*j == -4) continue;

                int xn = x + i;
                int yn = y + j;
                if((xn >= 1) && (xn <= nRows ) && (yn >= 1) && (yn <= nCols))
                    neighbourKeys.add(getKeyFromLocationInGrid(xn,yn));
            }		   
        }
        return neighbourKeys;	   
    }

    public boolean isCellClassified(long key) {
        List<Point> list = grid.get(key);
        Point p = list.get(0);

        return (p.clusterId != 0);
    }

    public boolean cellHasCorePoint(long key) {
        List<Point> list = grid.get(key);

        for(Point p: list){
            if(p.Classifier == "CORE")
              return true;
        }
        return false;
    }

    public boolean areCellsNeighbour(long cell_1, long cell_2){

        List<Point> pointList1 = grid.get(cell_1);
        List<Point> pointList2 = grid.get(cell_2);

        //if any pair of core points from either cells are neighbours
        for(Point p1 : pointList1){
            if(p1.Classifier == "CORE"){
                for(Point p2 : pointList2){
                    if(p2.Classifier == "CORE"){
                            if((p1.findDistanceFrom(p2)) <= epsilon)
                                    return true;
                    }
                }
            }					
        }
        return false;
    }
    
    
 }