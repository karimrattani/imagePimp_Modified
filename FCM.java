import java.awt.*;
import javax.swing.JOptionPane;
import java.util.Random;

public class FCM{
  Image imageIn;
  int[][][] input;
  int cluster;
  double fuzziness;
  Dimension imageInDimension;
  int width;
  int height;
  int dim;
  int[][] clusterColor;
  FCM(Image imageIn,int[][][] input,int cluster, double fuzziness){
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
  public Image get_FCM(){
    int update[][][] = new int[dim][this.width][this.height];
    double term=0.00001;
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
      temp_membership=get_membership(kCenters);
      update=get_output(kCenters,temp_membership);
      //System.out.println("I'm at iteration "+max);
      if(ImageTools.compareArray(membership,temp_membership,term) || max==1000){
        System.out.println("FCM - Xei And Beni: "+ImageTools.compactnessAndSeparationMetric(input,ImageTools.getNormalize(membership),kCenters,this.fuzziness));
        System.out.println("FCM - iIndex: "+ImageTools.iIndex(input,ImageTools.getNormalize(membership),kCenters,this.fuzziness));
        System.out.println("Outer Loop Ran "+max+" times");
        System.out.println("--- \t --- \t ---");
        break;
        
      }else{
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
        double num=0.0;
        double den=0.0;
        for (int row = 0; row < this.imageInDimension.getHeight(); row++){
          for (int column = 0; column < this.imageInDimension.getWidth(); column++)
          {
            
            num+=this.input[j][column][row]*(Math.pow(membership[column][row][i],this.fuzziness));
            den+=Math.pow(membership[column][row][i],this.fuzziness);
          }
        }
        Double ans = num/den;
         if(Double.isNaN(ans)){
           System.err.println("Invalid center value for cluster"+i+" and dimension"+j); 
           ans=0.0;
           throw new IllegalArgumentException();
           
         }
        //System.out.println(sum/den);
        kCenters[i][j]=ans;
        
      }
    }
    return kCenters;
    
  }
  
  private double[][][] get_membership(double[][] kCenters){
    double[][][] temp_membership=new double[this.width][this.height][this.cluster];
    
    for(int curr=0;curr<kCenters.length;curr++){//specific cluster for sum
      for (int row = 0; row < this.imageInDimension.getHeight(); row++){
        for (int column = 0; column < this.imageInDimension.getWidth(); column++)
        {
          double diff=0;
          double coff=2/(this.fuzziness-1);
          double neu=0;
          double den=0;
          double sum=0;
          for(int j=0;j<kCenters.length;j++){//all cluster
            for(int d=1;d<this.dim;d++){
              den+=(Math.pow(this.input[d][column][row]-kCenters[j][d],2));
              neu+=(Math.pow(this.input[d][column][row]-kCenters[curr][d],2));
            }
            den=Math.sqrt(den);
            neu=Math.sqrt(neu);
            double div=Math.pow((neu/den),coff);
            sum+=div;
            
          }
          temp_membership[column][row][curr]=1/sum;
          
        }//column end
      }//row end
    }//cluster end
    
    return temp_membership;

  }
  
  
  private int[][][] get_output(double[][] kCenters, double[][][] temp_membership){
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
          update[i][column][row]=(int)kCenters[select][i];
        }
        
      }
    }
    return update;
  }
  
    public double[][][] get_FCM_membership(){
    int update[][][] = new int[dim][this.width][this.height];
    double term=0.00001;
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
      temp_membership=get_membership(kCenters);
      update=get_output(kCenters,temp_membership);
      
      if(ImageTools.compareArray(membership,temp_membership,term) || max==1000){
        System.out.println("Outer Loop Ran "+max+" times");
        System.out.println("iIndex: "+ImageTools.iIndex(input,ImageTools.getNormalize(membership),kCenters,this.fuzziness));
        System.out.println("Xei And Beni: "+ImageTools.compactnessAndSeparationMetric(input,ImageTools.getNormalize(membership),kCenters,this.fuzziness));
        break;
        
      }else{
        //update membership
        membership=temp_membership;
        kCenters=get_Cluster_Center(membership);        
      }
    }  
    return membership;
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
}