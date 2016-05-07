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
public class Point {
    private ArrayList<BigDecimal> coordinates;
    private int dimension;
    private boolean flagUp;

    public Point(ArrayList<BigDecimal> coordinates){
        this.coordinates=coordinates;
        this.dimension=coordinates.size();
    }

    public ArrayList<BigDecimal> getCoordinates(){
        return this.coordinates;
    }
    public int getDimension(){
        return dimension;
    }
    public void setFlagUp(Boolean flag){
        this.flagUp=flag;
    }
    public boolean getFlag(){
        return flagUp;
    }

    //restituisce il quadrato della distanza euclidea del punto dal punto p
    public BigDecimal squaredEuclideanDistance(Point p){ //BUG: controllo sul numero di coordinate dei due punti...
        int i;
        BigDecimal b=new BigDecimal(0);

        for(i=0;i<coordinates.size();i++){
            b=b.add((p.getCoordinates().get(i).subtract(coordinates.get(i))).pow(2));
        }
        return b;
    }
    public Double euclideanDistance(Point p){ //BUG: controllo sul numero di coordinate dei due punti...
        int i;
        BigDecimal b=new BigDecimal(0);

        for(i=0;i<coordinates.size();i++){
            b=b.add((p.getCoordinates().get(i).subtract(coordinates.get(i))).pow(2));
        }
        return Math.sqrt(b.doubleValue());
    }
    public Double euclideanNorm(){
        int i;
        BigDecimal b=new BigDecimal(0);

        for(i=0;i<coordinates.size();i++){
            b=b.add(coordinates.get(i).pow(2));
        }
        return Math.sqrt(b.doubleValue());
    }
     /*
      * norma euclidea per le prime n coordinate del punto
      */
    public Double euclideanNorm(int n){
        int i;
        BigDecimal b=new BigDecimal(0);

        if(!(n<=coordinates.size())) return null;

        for(i=0;i<n;i++){
            b=b.add(coordinates.get(i).pow(2));
        }
        return Math.sqrt(b.doubleValue());
    }

     /*
      * Normalizza le prime n coordinate del punto
      */
    public void normalize(int n,int scale){
        int i;
        Double euclideanNorm;
        BigDecimal b;
        if((euclideanNorm=euclideanNorm(n))!=null){
            for(i=0;i<coordinates.size();i++){
                b=coordinates.get(i);
                if(euclideanNorm!=0) b=b.divide(new BigDecimal(euclideanNorm),scale,BigDecimal.ROUND_HALF_UP);
                coordinates.set(i, b);
            }
        }
    }
    public void normalize(int scale){
        int i;
        Double euclideanNorm;
        BigDecimal b;
        if((euclideanNorm=euclideanNorm())!=null){
            for(i=0;i<coordinates.size();i++){
                b=coordinates.get(i);
                if(euclideanNorm!=0) b=b.divide(new BigDecimal(euclideanNorm),scale,BigDecimal.ROUND_HALF_UP);
                coordinates.set(i, b);
            }
        }
    }
    public Point getNormalized(int n,int scale){
        int i;
        Double euclideanNorm;
        BigDecimal b;
        ArrayList<BigDecimal> normalizedCoordinates=new ArrayList();

        if((euclideanNorm=euclideanNorm(n))!=null){
            for(i=0;i<coordinates.size();i++){
                b=coordinates.get(i);
                if(euclideanNorm!=0) b=b.divide(new BigDecimal(euclideanNorm),scale,BigDecimal.ROUND_HALF_UP);
                normalizedCoordinates.add(b);
            }
        }
        return new Point(normalizedCoordinates);
    }
    public Point getNormalized(int scale){
        int i;
        Double euclideanNorm;
        BigDecimal b;
        ArrayList<BigDecimal> normalizedCoordinates=new ArrayList();

        if((euclideanNorm=euclideanNorm())!=null){
            for(i=0;i<coordinates.size();i++){
                b=coordinates.get(i);
                if(euclideanNorm!=0) b=b.divide(new BigDecimal(euclideanNorm),scale,BigDecimal.ROUND_HALF_UP);
                normalizedCoordinates.add(b);
            }
        }
        return new Point(normalizedCoordinates);
    }
    public Point findClosestPoint(ArrayList<Point> means){
        int i;
        Point closestMean,currentMean;
        BigDecimal closestDistance,currentDistance;

        closestMean=means.get(0);
        closestDistance=this.squaredEuclideanDistance(closestMean);

        for(i=0;i<means.size();i++){
            currentMean=means.get(i);
            if((currentDistance=this.squaredEuclideanDistance(currentMean)).compareTo(closestDistance)==-1){
                closestDistance=currentDistance;
                closestMean=currentMean;
            }
        }
        return closestMean;
   }


    public boolean isEqual(Point p){
        int i;
        boolean equal=true;

        for(i=0;i<coordinates.size();i++){
            if(!(coordinates.get(i).compareTo(p.getCoordinates().get(i))==0)){
                equal=false;
                break;
            }
        }

        return equal;
    }

    public String toString(){
        int i;
        String s="";

        for(i=0;i<coordinates.size();i++){
            s=s+" "+coordinates.get(i);
        }
        return s;
    }
}
