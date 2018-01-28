import java.awt.*;
import javax.swing.JOptionPane;
import java.util.Random;

public class GPCA{
  Image imageIn;
  int[][][] input;
  int cluster;
  double fuzziness;
  Dimension imageInDimension;
  int width;
  int height;
  int dim;
  int[][] clusterColor;
  GPCA(Image imageIn,int[][][] input,int cluster, double fuzziness){
    this.imageIn=imageIn;
    this.input=input;
    this.cluster=cluster;
    this.fuzziness=fuzziness;
    imageInDimension = ImageTools.getImageDimension(this.imageIn);
    width=(int)imageInDimension.getWidth();//column
    height=(int)imageInDimension.getHeight();//row
    dim=input.length;
    clusterColor=ImageTools.getClustersColor(cluster);
  } 
  
  public Image get_GPCA(){
    int update[][][] = new int[dim][this.width][this.height];
    double term=.00001;
    double membership[][][] = new double[width][height][cluster];
    double temp_membership[][][]= new double[width][height][cluster];
    double poss[][][] = new double[width][height][cluster];
    int[][] clusterColor=ImageTools.getClustersColor(this.cluster);
    double kCenters[][]=new double[this.cluster][dim];
    int max=0;
    
    membership=initialize_membership();
    
    //calculate possibility
    
    kCenters=get_Cluster_Center(membership);
    
    
    while(true){
       max++;
       if(max%10==0){System.out.println("Inside Loop for "+max);}
       temp_membership=get_membership(kCenters,membership);
       
       if(ImageTools.compareArray(membership,temp_membership,term) || max==1000){
         
         System.out.println("Outer Loop Ran "+max+" times");
         break;
         
       }else{
         membership=temp_membership;
         kCenters=get_Cluster_Center(membership);
       }
       

    }
     update=get_output(kCenters,temp_membership);
     return ImageTools.pixelsArrayToImage(ImageTools.TRGBArrayToPixelsArray(update, imageInDimension), imageInDimension);
  }
  private double[][][] initialize_membership(){
    double[][][] membership = new double[this.width][this.height][this.cluster];
    Random rand=new Random();
    for (int row = 0; row < this.imageInDimension.getHeight(); row++){
      for (int column = 0; column < this.imageInDimension.getWidth(); column++)
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
    return membership;

  }
    
  private double[][] get_Cluster_Center(double[][][] memb){
    double[][] kCenters = new double[this.cluster][dim];
    
    for(int i=0;i<kCenters.length;i++){//cluster
      for(int j=1;j<kCenters[i].length;j++){//RGB
        double sum=0;
        double den=0;
        for (int row = 0; row < imageInDimension.getHeight(); row++){
          for (int column = 0; column < imageInDimension.getWidth(); column++)
          {
            sum+=input[j][column][row]*(Math.pow(memb[column][row][i],fuzziness));//equation #25
            den+=Math.pow(memb[column][row][i],fuzziness);
          }
        }
        // System.out.println(sum/den);
        kCenters[i][j]=sum/den;
        
      }
    }
    
    return kCenters;
    
  }
  
  private double get_intra_cluster_distance(double[][] kCenters, int column, int row,double[][][] membership){
    double dist=0;
    double neu=0;
    double den=0;
    double sum=0;
    double all_clus_dist;
    for(int j=0;j<kCenters.length;j++){//all cluster
      all_clus_dist=0;
      for(int d=1;d<dim;d++){
        all_clus_dist+=Math.pow(input[d][column][row]-kCenters[j][d],2);
      }
      all_clus_dist=Math.sqrt(all_clus_dist);
      neu+=Math.pow(membership[column][row][j],fuzziness)*Math.pow(all_clus_dist,2);
      den+=Math.pow(membership[column][row][j],fuzziness);
    }
    sum=Math.sqrt(neu/den); 
    
    return sum;
    
  }
  
  private double calculate_membership(double[][] kCenters, int column, int row, double intra_clust, int curr){
    double dist=0;
    for(int d=1;d<dim;d++){
      dist+=Math.pow(input[d][column][row]-kCenters[curr][d],2);
    }
    dist=Math.sqrt(dist);
    //calculate membership
    double memb=0;
    if(dist==0){
      memb=1;
    }else if(dist==1){
      memb=0;
    }else{
      memb = Math.pow(1+(Math.pow(fuzziness*cluster,3)*Math.pow(dist/intra_clust,2)),-1);
    }
    
    return memb;
    
  }
  
  private double[][][] get_membership(double[][] kCenters, double[][][] membership){
    double temp_membership[][][]= new double[width][height][cluster];
    for(int curr=0;curr<kCenters.length;curr++){//specific cluster for sum
      for (int row = 0; row < imageInDimension.getHeight(); row++){
        for (int column = 0; column < imageInDimension.getWidth(); column++)
        {
          //get intracluster distance - #14
          double intra_clus_distance = get_intra_cluster_distance(kCenters,column,row,membership);
          temp_membership[column][row][curr]= calculate_membership(kCenters,column,row,intra_clus_distance,curr);       
          
          
        }
      }
    }
    return temp_membership;

  }
  
  private int[][][] get_output(double[][] kCenters, double[][][] membership){
    int update[][][] = ImageTools.pixelsArrayToTRGBArray(ImageTools.imageToPixelsArray(this.imageIn), this.imageInDimension);
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
        for(int i=1;i<4;i++){//RGB
          update[i][column][row]=clusterColor[select][i-1];
        }
        
        
      }
    } 
    return update;
  }
  
  
  
}