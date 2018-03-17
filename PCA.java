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
    
    
    
    //initialize membership
    FCM fcm = new FCM(imageIn,input,cluster,fuzziness);
    membership=fcm.get_FCM_membership();

    while(true){
      max++;
      kCenters=get_Cluster_Center(membership);
      temp_membership = get_membership(kCenters,membership);
      
      if(ImageTools.compareArray(membership,temp_membership,term) || max==100){
        update=get_output(kCenters,temp_membership);
        System.out.println("PCA - Xei And Beni: "+ImageTools.compactnessAndSeparationMetric(input,ImageTools.getNormalize(membership),kCenters,this.fuzziness));
        System.out.println("PCA - iIndex: "+ImageTools.iIndex(input,ImageTools.getNormalize(membership),kCenters,this.fuzziness));
        System.out.println("Outer Loop Ran "+max+" times");
        System.out.println("--- \t --- \t ---");
        break;
        
      }else{
        if(max%10==0){System.out.println("Currently in Loop "+max);}
        membership=temp_membership;    
      }
    }  
    return ImageTools.pixelsArrayToImage(ImageTools.TRGBArrayToPixelsArray(update, imageInDimension), imageInDimension);
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
        if(Double.isNaN(num/den)){
         System.out.println("Invalid center value for cluster"+i+" and dimension"+j); 
         throw new IllegalArgumentException();
        }
        kCenters[i][j]=num/den;
        
      }
    }
    return kCenters;
    
  }

    private double[][][] get_membership(double[][] kCenters,double[][][] membership){ //PCA96
    //PCA96
    double[][][] temp_membership=new double[this.width][this.height][this.cluster];
    double mean = get_dataset_mean();
    double beta = get_beta(mean);
    
    for(int curr=0;curr<kCenters.length;curr++){
      for(int row=0;row<this.imageInDimension.getHeight();row++){
        for(int column=0;column<this.imageInDimension.getWidth();column++){
          double dist=0;
          for(int d=1;d<this.dim;d++){
            dist+=(Math.pow(this.input[d][column][row]-kCenters[curr][d],2));
         }
          double factor = (dist*this.fuzziness*Math.sqrt(kCenters.length)*-1)/beta;
          factor = Math.exp(factor);
          temp_membership[column][row][curr]=factor;
        }
      }
    }
    
    
    
    return temp_membership;
  }
  
  private int[][][] get_output(double[][] kCenters, double[][][] temp_membership){
    int[] count=new int[kCenters.length];
    //int update[][][] = ImageTools.pixelsArrayToTRGBArray(ImageTools.imageToPixelsArray(this.imageIn), this.imageInDimension);
    int update[][][] = new int[input.length][input[0].length][input[0][0].length];
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
        update[0][column][row]=100; //alpha
        for(int i=1;i<4;i++){//RGB
          update[i][column][row]=(int)kCenters[select][i];
          count[select]++;
        }
        
      }
    }
    return update;
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

  
}