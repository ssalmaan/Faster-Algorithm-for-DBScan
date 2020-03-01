package dbscan;

class Point {

   double x;
   double y;

   String Classifier;

   int clusterId;

   public Point(double x, double y) {
       this.x = x;
       this.y = y;
       this.Classifier = "UNCLASSIFIED";
   }

   public double findDistanceFrom(Point p) {
        double x_diff = x - p.x;
        double y_diff = y - p.y;
        return Math.sqrt(x_diff*x_diff + y_diff*y_diff);
    }

   public String toString() {
       return "(" + x + ", " + y + ")";
   }
}
