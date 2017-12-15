import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.media.jai.*;
import javax.swing.*;
//import javax.media.jai.PlanarImage;
import java.text.DecimalFormat;
import com.sun.media.jai.codec.*;
import javax.swing.ImageIcon;
import java.util.Random;
import java.lang.*;
import java.io.*;
import java.util.Scanner;

public class getMultiSpectral extends ImagePimpMinh{
  Image imageIn;
  int[][][] input;
  int alg;
  public getMultiSpectral(int alg,Image imageIn,int[][][] input){
    this.alg=alg;
    this.imageIn=imageIn;
    this.input=input;


  }
public Image getResult(){
    if(alg==1){
     System.out.println("kMeans MultiSpectral");
     return kMeans_multiSpectral(imageIn,input);
    }else if(alg==2){
      System.out.println("fCM MultiSpectral");
      return fCM_multiSpectral(imageIn,input);
    }else if(alg==3){
      System.out.println("NPCA #1 MultiSpectral");
      return npca_1_m(imageIn,input);
    }else{
      System.out.println("Invalid alg value");
      return imageIn;
    }
  }
private Image npca_1_m(Image imageIn,int[][][] input){
  Dimension imageInDimension = getImageDimension(imageIn);
  //int TRGB[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);
  int update[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);//to store updated pixel values

  String inp = JOptionPane.showInputDialog("Enter Cluster");
  int cluster=Integer.parseInt(inp);//Defind cluster value
  inp = JOptionPane.showInputDialog("Enter Fuzziness");
  double fuzziness=Double.parseDouble(inp);

  Random rand=new Random();
  int width=(int)imageInDimension.getWidth();//column
  int height=(int)imageInDimension.getHeight();//row

  int max=1;
  int dim = input.length;
  System.out.println(dim);
  double kCenters[][]=new double[cluster][dim];
  double term=0;
  double membership[][][] = new double[width][height][cluster];
  double poss[][][]=new double[width][height][cluster];
  double temp_kCenters[][]=new double[cluster][dim];
  int count[]=new int[cluster];
  int[][] clusterColor=getClustersColor(cluster);

  //calculate initial membership of pixel to each cluster
  for (int row = 0; row < imageInDimension.getHeight(); row++){
    for (int column = 0; column < imageInDimension.getWidth(); column++)
    {
      double sum_Prob=0.0;
      double remaining_Prob=100.0;
      for(int i=0;i<cluster-1;i++){
        double ran=rand.nextDouble()*remaining_Prob;
        sum_Prob+=ran/100;
        remaining_Prob-=ran;
        membership[column][row][i]=ran/100;
      }
      membership[column][row][cluster-1]=1-sum_Prob;
    }
  }

//  try{
//    writeToFile(membership,"random_generate");
//  }catch(Exception e){
//    System.out.println(e);
//  }

  //calculate possibility
  poss=getPossibility(membership);

//  try{
//    writeToFile(poss,"initial_poss");
//  }catch(Exception e){
//    System.out.println(e);
//  }

  //calculate center for KClusters
  //#3
  for(int i=0;i<kCenters.length;i++){//cluster
    for(int j=0;j<kCenters[i].length;j++){//RGB
      double sum=0;
      double den=0;
      for (int row = 0; row < imageInDimension.getHeight(); row++){
        for (int column = 0; column < imageInDimension.getWidth(); column++)
        {
          sum+=input[j][column][row]*(Math.pow(poss[column][row][i],fuzziness));//equation #25
          den+=Math.pow(poss[column][row][i],fuzziness);
        }
      }
     // System.out.println(sum/den);
      kCenters[i][j]=sum/den;

    }
  }
  System.out.println("Starting Loop");
  while(true){
    System.out.println("Inside Loop for"+max);
    for(int curr=0;curr<kCenters.length;curr++){//specific cluster for sum
    for (int row = 0; row < imageInDimension.getHeight(); row++){
      for (int column = 0; column < imageInDimension.getWidth(); column++)
      {
        double diff=0;
        double coff=2/(fuzziness-1);
        double dist=0;
        double neu=0;
        double den=0;
        double sum=0;
        double param_f=0;
        double all_clus_dist;
        //Equation 14

        //average fuzzy intracluster distance of cluster

        for(int j=0;j<kCenters.length;j++){//all cluster
          all_clus_dist=0;
          for(int d=0;d<dim;d++){
          all_clus_dist+=Math.pow(input[d][column][row]-kCenters[j][d],2);
          }
          all_clus_dist=Math.sqrt(all_clus_dist);
          neu+=Math.pow(membership[column][row][j],fuzziness)*Math.pow(all_clus_dist,2);
          den+=Math.pow(membership[column][row][j],fuzziness);
          //sum+=neu/den;
        }
          sum=Math.sqrt(neu/den);
          dist=0;
          for(int d=0;d<dim;d++){
          dist+=Math.pow(input[d][column][row]-kCenters[curr][0],2);
          }
          dist=Math.sqrt(dist);
          //calculate membership
          double f=0;
          if(dist==0){
            f=1;
          }else if(dist==1){
            f=0;
          }else{
            f = Math.pow(1+(Math.pow(fuzziness*cluster,3)*Math.pow(dist/sum,2)),-1);
          }

          membership[column][row][curr]=f;



      }//column end
    }//row end
    }//cluster end

    //update possibility
    System.out.println("Getting Possibility");
    poss=getPossibility(membership);

//    try{
//      writeToFile(poss,"final_poss");
//    }catch(Exception e){
//      System.out.println(e);
//    }
    System.out.println("Updating Centers");
    //update centers
    for(int i=0;i<kCenters.length;i++){//cluster
      for(int j=0;j<kCenters[i].length;j++){//RGB
        double sum=0;
        double den=0;
        for (int row = 0; row < imageInDimension.getHeight(); row++){
          for (int column = 0; column < imageInDimension.getWidth(); column++)
          {
            sum+=input[j][column][row]*(Math.pow(poss[column][row][i],fuzziness));//equation #25
            den+=Math.pow(poss[column][row][i],fuzziness);
          }
        }

        temp_kCenters[i][j]=sum/den;

      }
    }

    //update to see output
    for (int row = 0; row < imageInDimension.getHeight(); row++){
      for (int column = 0; column < imageInDimension.getWidth(); column++)
      {
        int select=0;

        double val=membership[column][row][0];
        for(int curr=0;curr<kCenters.length;curr++){
          if(membership[column][row][curr]>val){
            val=membership[column][row][curr];
            select=curr;
          }
        }
        count[select]++;

        for(int i=1;i<4;i++){//RGB
            update[i][column][row]=clusterColor[select][i-1];
        }


      }
    }
    if(checkArray(kCenters,temp_kCenters,term) || max==1000){
       //output count
//      for(int i=0;i<count.length;i++){
//        System.out.println(i+": "+count[i]);
//      }
      System.out.println("Outer Loop Ran "+max+" times");
      break;

    }else{
      //update membership
      kCenters=temp_kCenters;
//      try{
//        writeToFile(membership,"final_membership");
//      }catch(Exception e){
//        System.out.println(e);
//      }

      //update center

      for(int i=0;i<kCenters.length;i++){//cluster
        for(int j=0;j<kCenters[i].length;j++){//RGB
          double sum=0;
          double den=0;
          for (int row = 0; row < imageInDimension.getHeight(); row++){
            for (int column = 0; column < imageInDimension.getWidth(); column++)
            {
              sum+=input[j][column][row]*(Math.pow(membership[column][row][i],fuzziness));
              den+=Math.pow(membership[column][row][i],fuzziness);
            }
          }
          kCenters[i][j]=sum/den;
         // System.out.println(kCenters[i][j]);

        }
      }

      max++;
    }
  }//outer loop end
  return pixelsArrayToImage(TRGBArrayToPixelsArray(update, imageInDimension), imageInDimension);
}
/*---------------
 *
 *
 *
 * */
protected Image fCM_multiSpectral(Image imageIn, int[][][] input){
  Dimension imageInDimension = getImageDimension(imageIn);
  //int TRGB[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);
  int update[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);//to store updated pixel values
  int dim=input.length;
  String inp = JOptionPane.showInputDialog("Enter Cluster");
  int cluster=Integer.parseInt(inp);//Defind cluster value
  inp = JOptionPane.showInputDialog("Enter Fuzziness");
  double fuzziness=Double.parseDouble(inp);

  Random rand=new Random();
  int width=(int)imageInDimension.getWidth();//column
  int height=(int)imageInDimension.getHeight();//row
  int max=1;
  double kCenters[][]=new double[cluster][dim];
  double term=0.000005;
  double membership[][][] = new double[width][height][cluster];
  double temp_membership[][][]= new double[width][height][cluster];
  int[][] clusterColor=getClustersColor(cluster);

  //calculate initial membership of pixel to each cluster
  for (int row = 0; row < imageInDimension.getHeight(); row++){
    for (int column = 0; column < imageInDimension.getWidth(); column++)
    {
      double sum_Prob=0.0;
      double remaining_Prob=100.0;
      for(int i=0;i<cluster-1;i++){
        double ran=rand.nextDouble()*remaining_Prob;
        sum_Prob+=ran/100;
        remaining_Prob-=ran;
        membership[column][row][i]=ran/100;
      }
      membership[column][row][cluster-1]=1-sum_Prob;
    }
  }
//  try{
//    writeToFile(membership,"test");
//  }catch(Exception e){
//    System.out.println(e);
//  }

  //calculate center for KClusters
  //#3
  for(int i=0;i<kCenters.length;i++){//cluster
    for(int j=0;j<kCenters[i].length;j++){//RGB
      double sum=0;
      double den=0;
      for (int row = 0; row < imageInDimension.getHeight(); row++){
        for (int column = 0; column < imageInDimension.getWidth(); column++)
        {
          sum+=input[j][column][row]*(Math.pow(membership[column][row][i],fuzziness));
          den+=Math.pow(membership[column][row][i],fuzziness);
        }
      }
      //System.out.println(sum/den);
      kCenters[i][j]=sum/den;

    }
  }

  while(true){
    for(int curr=0;curr<kCenters.length;curr++){//specific cluster for sum
    for (int row = 0; row < imageInDimension.getHeight(); row++){
      for (int column = 0; column < imageInDimension.getWidth(); column++)
      {
        double diff=0;
        double coff=2/(fuzziness-1);
        double neu=0;
        double den=0;
        double sum=0;
        for(int j=0;j<kCenters.length;j++){//all cluster

          for(int d=0;d<dim;d++){
          den+=(Math.pow(input[d][column][row]-kCenters[j][d],2));
          neu+=(Math.pow(input[d][column][row]-kCenters[curr][d],2));
          }
          den=Math.sqrt(den);
          neu=Math.sqrt(neu);
          double div=Math.pow((neu/den),coff);
          sum+=div;

        }
        temp_membership[column][row][curr]=1/sum;

//        //update in return image
//        for(int j=0;j<kCenters[curr].length;j++){
//          update[j+1][column][row]=(int)(TRGB[j+1][column][row]*temp_membership[column][row][curr])+(50*curr);
//        }


      }//column end
    }//row end
    }//cluster end

    //update to see output
    for (int row = 0; row < imageInDimension.getHeight(); row++){
      for (int column = 0; column < imageInDimension.getWidth(); column++)
      {
        int select=0;

        double val=temp_membership[column][row][0];
        for(int curr=0;curr<kCenters.length;curr++){
          if(temp_membership[column][row][curr]>val){
            val=temp_membership[column][row][curr];
            select=curr;
          }
        }
        for(int i=1;i<4;i++){//RGB
         update[i][column][row]=clusterColor[select][i-1];
        }

      }
    }


    if(super.compareArray(membership,temp_membership,term) || max==1000){
      System.out.println("Outer Loop Ran "+max+" times");
      break;

    }else{
      //update membership
      membership=temp_membership;

//      try{
//        writeToFile(membership,"test1");
//      }catch(Exception e){
//       System.out.println(e);
//      }

      //update center

      for(int i=0;i<kCenters.length;i++){//cluster
        for(int j=0;j<kCenters[i].length;j++){//RGB
          double sum=0;
          double den=0;
          for (int row = 0; row < imageInDimension.getHeight(); row++){
            for (int column = 0; column < imageInDimension.getWidth(); column++)
            {
              sum+=input[j][column][row]*(Math.pow(membership[column][row][i],fuzziness));
              den+=Math.pow(membership[column][row][i],fuzziness);
            }
          }
          kCenters[i][j]=sum/den;
         // System.out.println(kCenters[i][j]);

        }
      }

      max++;
    }
  }//outer loop end



  return pixelsArrayToImage(TRGBArrayToPixelsArray(update, imageInDimension), imageInDimension);
}




 protected Image kMeans_multiSpectral(Image imageIn,int[][][] input)
 {
   // Declare local storage
   Dimension imageInDimension = getImageDimension(imageIn);
   //int TRGB[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);
   int update[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);//to store updated pixel values
   String inp = JOptionPane.showInputDialog("Enter Cluster");
   int k=Integer.parseInt(inp);//Defind cluster value
   int dim=input.length;
   //get random centers for K clusters
   int width=(int)imageInDimension.getWidth();
   int height=(int)imageInDimension.getHeight();
   Random r=new Random();

   int kCenters[][]=new int[k][dim];
   int checkMean[][]=new int[k][dim];
   int sumMean[][]=new int[k][dim];
   int meanCount[]=new int[k];
   int count[]=new int[k];

   int[][] clusterColor=getClustersColor(k);


   // initialize values for K clusters
   for(int i=0;i<kCenters.length;i++){
     meanCount[i]=0;
     for(int j=0;j<kCenters[i].length;j++){
       int rand_w=r.nextInt(width);
       int rand_h=r.nextInt(height);
       kCenters[i][j]=input[j][rand_w][rand_h];
       checkMean[i][j]=0;
       sumMean[i][j]=0;
     }
   }

   int max=0;
   //loop until old mean is equal to new mean
   while(true){
     for (int row = 0; row < imageInDimension.getHeight(); row++){
       for (int column = 0; column < imageInDimension.getWidth(); column++)
       {

         double dist=10000000.0;
         double tempDist=0;
         int clusterIndex=0;
         for(int i=0;i<kCenters.length;i++){
           for(int j=0;j<dim;j++){
             tempDist+=Math.pow(input[j][column][row]-kCenters[i][j],2);
           }
          tempDist=Math.sqrt(tempDist);
           if(tempDist<dist){
             dist=tempDist;
             clusterIndex=i;
           }
         }

         meanCount[clusterIndex]++;//increment counter

         for(int i=0;i<dim;i++){
           sumMean[clusterIndex][i]+=input[i][column][row];//add mean
         }

         for(int i=0;i<3;i++){
            update[i+1][column][row]=clusterColor[clusterIndex][i];
         }


       }//column for loop
     }//row for loop

     //update mean
     for(int i=0;i<k;i++){
       //System.out.print("Mean Count for cluster "+i+" is "+meanCount[i]);
       if(meanCount[i]>0){

         for(int j=0;j<3;j++){
           checkMean[i][j]=sumMean[i][j]/meanCount[i];
         }

       }
     }




     if(checkArray(kCenters,checkMean) || max==1000){//check if old and new means are equal or max=1000
       System.out.println("Outer Loop Ran "+max+" Times");
       break;
     }else{
       // kCenters=checkMean;
       for(int i=0;i<kCenters.length;i++){
         meanCount[i]=0;
         for(int j=0;j<kCenters[i].length;j++){
           kCenters[i][j]=checkMean[i][j];//update centers and set all other to 0
           checkMean[i][j]=0;
           sumMean[i][j]=0;
         }
       }
       max++;
     }//end else


   }//end outer loop

   // Convert updated array back to image for return
   return pixelsArrayToImage(TRGBArrayToPixelsArray(update, imageInDimension), imageInDimension);

 }//end method





}
