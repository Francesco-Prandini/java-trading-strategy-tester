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
public class KMeans {
       public static ArrayList<Point> oldMeans=new ArrayList();
        public static ArrayList<Point> newMeans=new ArrayList();
        public static ArrayList<Cluster> clusters;

        public static void initialize(ArrayList<Point> points,int meansNumber){
            int i=0,j=0;
            clusters=new ArrayList();
            newMeans=new ArrayList();
            oldMeans=new ArrayList();

            while(j<meansNumber){
                newMeans.add(points.get(i));
                clusters.add(new Cluster(points.get(i)));
                j++;
            }

        }
        /*
         * Aggiunge il punto p al cluster la cui media è più vicina a p.
         */
        public static void addToClosestCluster(Point p,ArrayList<Cluster> clusters){
            int i;
            Cluster closestCluster,currentCluster;
            BigDecimal closestDistance,currentDistance;

            closestCluster=clusters.get(0);
            closestDistance=p.squaredEuclideanDistance(closestCluster.getMean());

            for(i=0;i<clusters.size();i++){
                currentCluster=clusters.get(i);
                if((currentDistance=p.squaredEuclideanDistance(currentCluster.getMean())).compareTo(closestDistance)==-1){
                    closestDistance=currentDistance;
                    closestCluster=currentCluster;
                }
            }
            closestCluster.addPoint(p);
        }
        public static boolean equalMeans(int meansNumber){
            int i;
            boolean equal=true;

            for(i=0;i<meansNumber;i++){
                if(!(oldMeans.get(i).isEqual(newMeans.get(i)))){
                    equal=false;
                    break;
                }
            }

            return equal;
        }

	public static ArrayList<Point> run(ArrayList<Point> points,int meansNumber,int scale){

            int i,j,k=0;
            ArrayList<Point> foundMeans=new ArrayList();
            initialize(points,meansNumber);

            do{
                for(i=0;i<points.size();i++){
                    addToClosestCluster(points.get(i),clusters);
                }
                oldMeans=newMeans;
                newMeans=new ArrayList();
                for(i=0;i<clusters.size();i++){
                    Point newMean=clusters.get(i).calculateMean(scale);
                    newMeans.add(newMean);
                    clusters.get(i).setMean(newMean);
                }
                for(i=0;i<clusters.size();i++){
                    clusters.get(i).flush();
                }
                k++;
            }while(!equalMeans(meansNumber));

            for(i=0;i<clusters.size();i++){
                foundMeans.add(clusters.get(i).getMean());
            }
            return foundMeans;
        }
}
