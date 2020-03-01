package dbscan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class FileOperations {

    public static List<Point> readFile(String filename){
        List<Point> pointList = new ArrayList<Point>();

        try{
                BufferedReader br = new BufferedReader(new FileReader(filename));
                String line;
                String[] coords;
                double x,y;

                while((line = br.readLine()) != null){
                    coords = line.split(",");
                    x = Double.parseDouble(coords[0]);
                    y = Double.parseDouble(coords[1]);
                    pointList.add(new Point(x,y));
                }
                br.close();
        }
        catch (IOException e) {
                System.out.println("File not found: "+ filename);
        }
        return pointList;
    }

    	public static Grid constructGrid(List<Point> pointList, double epsilon) //Construction of grid
	{
            double minX,minY,maxX,maxY;
	    Grid cg = new Grid();

	    minX = 1000000;
       	    maxX = -1000000;
       	    minY = 1000000;
    	    maxY = -1000000;

	    for(Point p: pointList){
                if(p.x < minX)
                    minX = p.x;
                else if(p.x > maxX)
                    maxX = p.x;

                if(p.y < minY)
                    minY = p.y;
                else if(p.y > maxY)
                    maxY = p.y;
            }
            cg.setParams(minX, minY, maxX, maxY, epsilon);

	    for(Point p: pointList){
                cg.addPointToGrid(p);

            }
            return cg;
	}

    public static void writeClustersToFile(Map<Integer, List<Point>> clusters, String output){
    	BufferedWriter br = null;

    	try{
            br = new BufferedWriter(new FileWriter(output));
            for(int key : clusters.keySet()){
               List<Point> pl = clusters.get(key);
               br.write(pl.size() + " points in cluster\n");
                for(Point p: pl){
            	    br.write(p.x + "," + p.y + "\n");
               }
            }
            br.close();
    	}
    	catch (IOException e) {
            System.out.println("Problem in writing file");
        }
    }
}
