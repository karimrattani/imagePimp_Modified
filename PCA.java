import java.awt.*;
import javax.swing.JOptionPane;
import java.util.Random;
//PCA93
public class PCA{
  Image imageIn;
  int[][][] input;
  int cluster;
  double fuzziness;
  Dimension imageInDimension;
  int width;
  int height;
  int dim;
  int[][] clusterColor;
  PCA(Image imageIn,int[][][] input,int cluster, double fuzziness){
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
  public Image get_PCA(){
    int update[][][] = new int[dim][this.width][this.height];
    double term=0.001;
    double membership[][][] = new double[width][height][cluster];
    double temp_membership[][][]= new double[width][height][cluster];
    int[][] clusterColor=ImageTools.getClustersColor(this.cluster);
    double kCenters[][]=new double[this.cluster][dim];
    int max=0;
    
    
    //Initialize Membership
    membership=initialize_membership();
    
    
    //Cluster Center
    kCenters=get_Cluster_Center(membership);
    
    while(true){
      max++;
      temp_membership=get_membership(kCenters,membership);   
      
      if(ImageTools.compareArray(membership,temp_membership,term) || max==1000){
        update=get_output(kCenters,temp_membership);
        System.out.println("Outer Loop Ran "+max+" times");
        ImageTools.writeToFile(getPossibility(temp_membership),"mem_pca");
        break;
        
      }else{
        System.out.println("Currently in Loop "+max);
        //update membership
        membership=temp_membership;
        kCenters=get_Cluster_Center(membership);        
      }
    }  
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
  
  private double[][] get_Cluster_Center(double[][][] membership){
    double[][] kCenters = new double[this.cluster][dim];
    for(int i=0;i<kCenters.length;i++){//cluster
      for(int j=1;j<kCenters[i].length;j++){//RGB
        double num=0;
        double den=0;
        for (int row = 0; row < this.imageInDimension.getHeight(); row++){
          for (int column = 0; column < this.imageInDimension.getWidth(); column++)
          {
            
            num+=this.input[j][column][row]*(Math.pow(membership[column][row][i],this.fuzziness));
            den+=Math.pow(membership[column][row][i],this.fuzziness);
          }
        }
        
        //System.out.println(sum/den);
        kCenters[i][j]=num/den;
        
      }
    }
    return kCenters;
    
  }
  
  private double[][][] get_membership(double[][] kCenters,double[][][] membership){
    double[][][] temp_membership=new double[this.width][this.height][this.cluster];
    //get mean
    double data_set_mean=get_dataset_mean();
    double beta=get_beta(data_set_mean);
    double neu=0;
    System.out.println("My Beta is "+beta);
//    for(int curr=0;curr<kCenters.length;curr++){//specific cluster for sum
//      
//      double den=get_n_for_membership(curr,kCenters,membership);//to get N, use to determine distance when the membership is 0.5
//      double beta=get_beta(data_set_mean);
//      for (int row = 0; row < this.imageInDimension.getHeight(); row++){
//        for (int column = 0; column < this.imageInDimension.getWidth(); column++)
//        {
//          double diff=0;
//          double coff=1/(this.fuzziness-1);
//          double neu=0;
//          double res=0;
//          for(int d=1;d<this.dim;d++){
//            neu+=(Math.pow(this.input[d][column][row]-kCenters[curr][d],2));
//          }
//          //square and sqrt cancel each other
//          res=-1*(neu/den);
//          res=Math.exp(res);
//          temp_membership[column][row][curr]=res;
////          res=1+(Math.pow((neu/den),coff));
////          temp_membership[column][row][curr]=1/res;
//                    
//        }//column end
//      }//row end
//    }//cluster end
    for(int curr=0;curr<kCenters.length;curr++){//clusters
      for (int row = 0; row < this.imageInDimension.getHeight(); row++){
        for (int column = 0; column < this.imageInDimension.getWidth(); column++){
          
          double dist=0;
          for(int d=1;d<this.dim;d++){
            dist+=(Math.pow(this.input[d][column][row]-kCenters[curr][d],2));
          }
          double res=(dist*Math.sqrt(this.cluster)*this.fuzziness*-1)/beta;
          res=Math.exp(res);
          temp_membership[column][row][curr]=res;
          
        }
      }
    }
    return temp_membership;
   
  }
  
  
  private int[][][] get_output(double[][] kCenters, double[][][] temp_membership){
    int[] count=new int[kCenters.length];
    int update[][][] = ImageTools.pixelsArrayToTRGBArray(ImageTools.imageToPixelsArray(this.imageIn), this.imageInDimension);
    for (int row = 0; row < this.imageInDimension.getHeight(); row++){
      for (int column = 0; column < this.imageInDimension.getWidth(); column++)
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
          update[i][column][row]=this.clusterColor[select][i-1];
          count[select]++;
        }
        
      }
    }
    for(int i=0;i<count.length;i++){
     System.out.println("Cluster:"+i+" Elements:"+count[i]); 
    }
    return update;
  }
  
private double get_n_for_membership(int curr_cluster,double[][] kCenters,double[][][] membership){
    //helper function for membership to get n value
    //summation for all data points
    //membership^m times distance between point and center using specified cluster
    //divide it by sum of membership for specified cluster
    
    double res=0;
    double neu=0;
    double den=0;
    double alpha=0.5;
    int k=1;
    int count=0;
    for (int row = 0; row < this.imageInDimension.getHeight(); row++){
      for (int col = 0; col < this.imageInDimension.getWidth(); col++){
        double memb=Math.pow(membership[col][row][curr_cluster],this.fuzziness);
        double dist=0;
        for(int d=1;d<this.dim;d++){
          dist+=(Math.pow(this.input[d][col][row]-kCenters[curr_cluster][d],2));
        }
        //sqrt and square cancel each other for dist
        neu+=memb*dist;
        den+=memb;
      }
    }
    System.out.println(neu/den);
    return k*(neu/den);  
//    for (int row = 0; row < this.imageInDimension.getHeight(); row++){
//      for (int col = 0; col < this.imageInDimension.getWidth(); col++){
//        if(membership[col][row][curr_cluster]>alpha){
//          count++;
//          double dist=0;
//          for(int d=1;d<dim;d++){
//           dist+=(Math.pow(this.input[d][col][row]-kCenters[curr_cluster][d],2));
//          }
//          neu+=dist;
//        }
//      }
//    }
//    System.out.println(neu/count);
//    return neu/count;
  }
private double get_beta(double dataSetMean){
  double res=0;
  for (int row = 0; row < this.imageInDimension.getHeight(); row++){
      for (int col = 0; col < this.imageInDimension.getWidth(); col++){
        double dist=0;
        for(int d=1;d<this.dim;d++){
          dist+=(Math.pow(this.input[d][col][row]-dataSetMean,2));
        }
        res+=dist;
      }   
  }
  double count=this.imageInDimension.getHeight()*this.imageInDimension.getWidth();
  return res/count;
  
}
  
private double get_dataset_mean(){
  double mean=0;
  double count=this.imageInDimension.getHeight()*this.imageInDimension.getWidth();
  for (int row = 0; row < this.imageInDimension.getHeight(); row++){
      for (int col = 0; col < this.imageInDimension.getWidth(); col++){
        //get mean of dimension of value
        double vectorSum=0;
        for(int d=1;d<this.dim;d++){
          vectorSum+=this.input[d][col][row];
        }
        double vectorMean=vectorSum/(this.dim-1);
        mean+=vectorMean;
      }
  }
  return mean/count;
  
  
}



//normalize
private double[][][] getPossibility(double[][][] memb){
  double[][][] poss=memb;
  int clus_index=-1;
  for(int i=0;i<memb.length;i++){
    for(int j=0;j<memb[i].length;j++){
      double max=0;
      for(int k=0;k<memb[i][j].length;k++){//get max value in cluster set or sup
        if(memb[i][j][k]>max){
          max=memb[i][j][k];
          clus_index=k;
        }
      }
      for(int k=0;k<memb[i][j].length;k++){//update max value in cluster set
        poss[i][j][k]=memb[i][j][k]/memb[i][j][clus_index];
      }
    }
  }

  return poss;
  
  
  
}
}