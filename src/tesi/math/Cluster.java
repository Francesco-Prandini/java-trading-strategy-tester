/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tesi.math;

import java.util.ArrayList;
import java.math.BigDecimal;

/**
 *
 * @author Francesco
 */
public class Cluster {
            private ArrayList<Point> points;
            private Point mean;
            private int dimension;

            public Cluster(ArrayList<Point> points){
                this.points=points;
                this.dimension=points.get(0).getDimension();
            }

            public Cluster(Point mean){
                this.mean=mean;
                this.points=new ArrayList();
                this.dimension=mean.getDimension();
            }

            public ArrayList<Point> getPoints() {
                return points;
            }

            public void setPoints(ArrayList<Point> points) {
                this.points = points;
            }

            public void addPoint(Point p){
                points.add(p);
            }
            public void flush(){
                this.points=new ArrayList();
            }
            public Point getMean(){
                return mean;
            }
            public void setMean(Point mean){
                this.mean=mean;
            }
            public Point calculateMean(int scale){ //Risolvere: restituisce null se non ci sono punti nel cluster.
                int i,j;

                ArrayList<BigDecimal> meanCoordinates=new ArrayList();
                BigDecimal currentCoordinate;

                if(points.size()>0){
                    for(i=0;i<dimension;i++){
                    currentCoordinate=new BigDecimal(0);
                    for(j=0;j<points.size();j++){
                        currentCoordinate=currentCoordinate.add(points.get(j).getCoordinates().get(i));
                        }
                        meanCoordinates.add(currentCoordinate.divide(new BigDecimal(points.size()),scale,BigDecimal.ROUND_HALF_UP));
                    }

                return new Point(meanCoordinates);
                }
                return mean;
            }
            public boolean isEqual(Cluster c){
                int i;
                boolean equal=true;

                for(i=0;i<points.size();i++){
                    if(!points.get(i).isEqual(c.getPoints().get(i))){
                        equal=false;
                        break;
                    }
                }

                return equal;
            }
}
