////////////////////////////////////////////////////////////////////////////////
// Image Processing software: ImagePimp
// created: 2001.02.20
// copyright reserved by Dr. Chih-Cheng Hung
////////////////////////////////////////////////////////////////////////////
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
// Class Definition(s) /////////////////////////////////////////////////////
// ImagePimp
public class ImagePimpMinh extends JFrame //implements ActionListener
{
 // Instance Variable(s) ////////////////////////////Minh////////////////
 // Constant(s)

 // Data Member(s)
 // Private

 // Protected
 public Container contentPane;
 public JDesktopPane desktopPane;

 // Public

 // Constructor(s) //////////////////////////////////////////////////
 public ImagePimpMinh()
 {

  // Generate Frame Window
  super("ImagePimp");
  initializeFrameWindow();
 }


 // Finalize ////////////////////////////////////////////////////////

 // Method(s) ///////////////////////////////////////////////////////
 // Private
 private void initializeFrameWindow()
 {
  // Get the Frame Window content pane
  contentPane = getContentPane();

  // Set Layout
  contentPane.setLayout(new BorderLayout());

  // Add a desktop pane to the Frame Window content pane
  desktopPane = new JDesktopPane();
        contentPane.add(desktopPane);

  // Window Control(s)
  // Menu Bar(s)
  JMenuBar menuBar = new JMenuBar();
  menuBar.setSize(100, 100);
   // Menu(s)
   // File Menu
   JMenu fileMenu = new JMenu("File");
   fileMenu.setSize(100, 100);
     // File Menu Item(s)
       JMenuItem newFileMenuItem = new JMenuItem("New");


    JMenuItem openFileMenuItem = new JMenuItem("Open");
    JMenuItem closeFileMenuItem = new JMenuItem("Close");

    // Temporarily Disable Unimplemented Menu Item(s)
    newFileMenuItem.setEnabled(false);
    //saveAsFileMenuItem.setEnabled(false);

    // Bind ActionListener(s) to Menu Item(s)
    openFileMenuItem.addActionListener(
     new ActionListener()
     {
      public void actionPerformed(ActionEvent ae)
      {

       // Open a file dialog box to choose a file
       JFileChooser fileChooser = new JFileChooser();

       // Initialize dialog box
       fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

       // Show the file dialog box
       if (fileChooser.showOpenDialog(ImagePimpMinh.this) != JFileChooser.CANCEL_OPTION)
       {

        // If an image was selected, open that image
        ImageFrame imageFrame=new ImageFrame(fileChooser.getSelectedFile());
        desktopPane.add(imageFrame);
        try{imageFrame.setSelected(true);}catch(Exception e){}
       }

      }
     }
    );
    closeFileMenuItem.addActionListener(
     new ActionListener()
     {
      public void actionPerformed(ActionEvent ae)
      {
       // Exit the application
       System.exit(0);
      }
     }
    );

    // Add Item(s) to File Menu
    fileMenu.add(newFileMenuItem);
    fileMenu.add(openFileMenuItem);
    fileMenu.addSeparator();
    fileMenu.add(closeFileMenuItem);

   // Add File Menu to Menu Bar
   menuBar.add(fileMenu);

   // Transformations Menu
   JMenu transformationsMenu = new JMenu("Transformations");

    // Transformations Menu Item(s)
    JMenuItem mirrorTransformationsMenuItem = new JMenuItem("Mirror");
    JMenuItem rotateTransformationsMenuItem = new JMenuItem("Rotate");
    JMenuItem grayscaleTransformationsMenuItem = new JMenuItem("Grayscale");
    JMenuItem kMeansTransformationsMenuItem = new JMenuItem("K-Means");
    JMenuItem fcmTransformationsMenuItem = new JMenuItem("FCM");
    JMenuItem gpcaTransformationsMenuItem = new JMenuItem("GPCA #1");
    JMenuItem customTransformationsMenuItem = new JMenuItem("Custom...");

    JMenuItem segmentationMenuItem = new JMenuItem("Segmentation");
    
    // Temporarily Disable Unimplemented Menu Item(s)
    mirrorTransformationsMenuItem.setEnabled(false);
    rotateTransformationsMenuItem.setEnabled(false);
    customTransformationsMenuItem.setEnabled(false);
    segmentationMenuItem.setEnabled(false);
    //histogramMenuItem.setEnabled(false);

    // Bind ActionListener(s) to Menu Item(s)



//***************grayScale - Required for conversion
    grayscaleTransformationsMenuItem.addActionListener(
     new ActionListener()
     {
      public void actionPerformed(ActionEvent ae)
      {
       // Call contrast enhancement method
       ImageFrame imageFrame = (ImageFrame) desktopPane.getSelectedFrame();
       ImageFrame newImageFrame = new ImageFrame(colorToGrayscale(imageFrame.getImage()));
       desktopPane.add(newImageFrame);
       newImageFrame.toFront();
       try{newImageFrame.setSelected(true);}catch(Exception e){}
      }
     }
    );
    //***************kmeans
    kMeansTransformationsMenuItem.addActionListener(
     new ActionListener()
     {
      public void actionPerformed(ActionEvent ae)
      {
       // Call contrast enhancement method
       ImageFrame imageFrame = (ImageFrame) desktopPane.getSelectedFrame();
       ImageFrame newImageFrame = new ImageFrame(kMeans(imageFrame.getImage()));
       desktopPane.add(newImageFrame);
       newImageFrame.toFront();
       try{newImageFrame.setSelected(true);}catch(Exception e){}
      }
     }
    );
    //***************FCM
    fcmTransformationsMenuItem.addActionListener(
                                                 new ActionListener()
                                                   {
      public void actionPerformed(ActionEvent ae)
      {
        // Call contrast enhancement method
        ImageFrame imageFrame = (ImageFrame) desktopPane.getSelectedFrame();
        ImageFrame newImageFrame = new ImageFrame(fCM(imageFrame.getImage()));
        desktopPane.add(newImageFrame);
        newImageFrame.toFront();
        try{newImageFrame.setSelected(true);}catch(Exception e){}
      }
    }
    );
    gpcaTransformationsMenuItem.addActionListener(
                                                 new ActionListener()
                                                   {
      public void actionPerformed(ActionEvent ae)
      {
        // Call contrast enhancement method
        ImageFrame imageFrame = (ImageFrame) desktopPane.getSelectedFrame();
        ImageFrame newImageFrame = new ImageFrame(gpca_1(imageFrame.getImage()));
        desktopPane.add(newImageFrame);
        newImageFrame.toFront();
        try{newImageFrame.setSelected(true);}catch(Exception e){}
      }
    }
    );

    // Add Item(s) to Transformations Menu
    transformationsMenu.add(grayscaleTransformationsMenuItem);
    transformationsMenu.add(kMeansTransformationsMenuItem);
    transformationsMenu.add(fcmTransformationsMenuItem);
    transformationsMenu.add(gpcaTransformationsMenuItem);

  
    transformationsMenu.addSeparator();
    transformationsMenu.add(customTransformationsMenuItem);

   // Add Transformations Menu to Menu Bar
   menuBar.add(transformationsMenu);

   // Filters Menu
   JMenu filtersMenu = new JMenu("Filter");

    // Filters Menu Item(s)
    JMenuItem contrastEnhancementFiltersMenuItem = new JMenuItem("Contrast Enhancement");

    // Temporarily Disable Unimplemented Menu Item(s)
    //contrastEnhancementFiltersMenuItem.setEnabled(false);
   

    // Bind ActionListener(s) to Menu Item(s)

    // Add Item(s) to Filters Menu
    filtersMenu.add(contrastEnhancementFiltersMenuItem);
    filtersMenu.addSeparator();
    

   // Add Filters Menu to Menu Bar
   menuBar.add(filtersMenu);

   //********************************
   // Image Feature Menu Item(s)


   //**************
   contrastEnhancementFiltersMenuItem.addActionListener(
      new ActionListener(){
         public void actionPerformed(ActionEvent ae)
          {
       ImageFrame imageFrame = (ImageFrame) desktopPane.getSelectedFrame();
       ImageFrame newImageFrame = new ImageFrame(contrastStretching(imageFrame.getImage()));
       desktopPane.add(newImageFrame);
       newImageFrame.toFront();
       try{newImageFrame.setSelected(true);}catch(Exception e){}
        
         }
      }
      
      
    );



  // Add Menu Bar to Frame Window
  setJMenuBar(menuBar);

  // Register the window listener to terminate the application
  addWindowListener
  (
   new WindowAdapter()
   {
    // Override window closing with application termination
    public void windowClosing(WindowEvent we)
    {
     // Terminate application
     System.exit(0);
    }
   }
  );

  // Display the Frame Window
  setSize(600, 500);
  show();
 }



//************************************************************************
 
//************************************************************************
 protected Image colorToGrayscale(Image imageIn)
 {

  // Declare local storage
  Dimension imageInDimension = getImageDimension(imageIn);
  int TRGB[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);

  // Loop through image, averaging color(RGB) values
  for (int row = 0; row < imageInDimension.getHeight(); row++)
   for (int column = 0; column < imageInDimension.getWidth(); column++)
   {

    // Average values and store back into TRGB array
    int average = (TRGB[1][column][row] + TRGB[2][column][row] + TRGB[3][column][row]) / 3;

    TRGB[1][column][row] = average;
    TRGB[2][column][row] = average;
    TRGB[3][column][row] = average;

   }

  // Convert TRGB array back to image for return
  return pixelsArrayToImage(TRGBArrayToPixelsArray(TRGB, imageInDimension), imageInDimension);

 }
 //************************************************************************
//************************************************************************
 protected Image contrastStretching(Image imageIn)
 {
   // Declare local storage
   imageIn= colorToGrayscale(imageIn);
   Dimension imageInDimension = getImageDimension(imageIn);
   int TRGB[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);
   //find lowest pixel
  // Loop through image, find lowest pixel values
   int low=255;
   int high=0;
   
   for (int row = 0; row < imageInDimension.getHeight(); row++){
     for (int column = 0; column < imageInDimension.getWidth(); column++)
     {
       int val = TRGB[1][column][row];
    if(val<low){
      low=val;
    }
    if(val>high){
      high=val;
    }
  }
   }
   double min=0;
   double max=255;
   
  // Loop through image, averaging color(RGB) values
  for (int row = 0; row < imageInDimension.getHeight(); row++)
   for (int column = 0; column < imageInDimension.getWidth(); column++)
   {

    // Average values and store back into TRGB array
      int val = TRGB[1][column][row];
      
     // int calc = ((average-low)((max-min)/(high-low))+min);
      int calc=(int)(((val-low)*((max-min)/(high-low)))+min);//Formula to calculate 
      if(calc<0){
        calc=0;
      }else if(calc>255){
        calc=255;
      }
      
      TRGB[1][column][row] = calc;
      TRGB[2][column][row] = calc;
      TRGB[3][column][row] = calc;

   }
  // Convert TRGB array back to image for return
  return pixelsArrayToImage(TRGBArrayToPixelsArray(TRGB, imageInDimension), imageInDimension);

 }
 //************************************************************************
//************************************************************************
 private int[][] getClustersColor(int cluster){//get Cluster Color to differentiate
   Random rnd=new Random();
   int[][] arr=new int[cluster][3];
   for(int i=0;i<arr.length;i++){
     for(int j=0;j<arr[0].length;j++){
       arr[i][j]=rnd.nextInt(255);
     }
     
   }
   return arr;   
 }
 
 
 
 protected Image kMeans(Image imageIn)//for k=2
 {
   // Declare local storage
   Dimension imageInDimension = getImageDimension(imageIn);
   int TRGB[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);
   int update[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);//to store updated pixel values
   int k=5;//Defind cluster value
   //get random centers for K clusters
   int width=(int)imageInDimension.getWidth();
   int height=(int)imageInDimension.getHeight();
   Random r=new Random();
   int kCenters[][]=new int[k][3];
   int checkMean[][]=new int[k][3];
   int sumMean[][]=new int[k][3];
   int meanCount[]=new int[k];
   int count[]=new int[k];
   int[][] clusterColor=getClustersColor(k);
   
   
   // initialize values for K clusters
   for(int i=0;i<kCenters.length;i++){
     meanCount[i]=0;
     for(int j=0;j<kCenters[i].length;j++){
       int rand_w=r.nextInt(width);
       int rand_h=r.nextInt(height);
       kCenters[i][j]=TRGB[j+1][rand_w][rand_h];
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
         
         double dist=10000000.0;//assume smallest is in red first cluster
         double tempDist=0;
         // int colorIndex=0;//red
         int clusterIndex=0;
         for(int i=0;i<kCenters.length;i++){
           tempDist=Math.sqrt(Math.pow(TRGB[1][column][row]-kCenters[i][0],2)+Math.pow(TRGB[2][column][row]-kCenters[i][1],2)+Math.pow(TRGB[3][column][row]-kCenters[i][2],2));
           if(tempDist<dist){
             dist=tempDist;
             clusterIndex=i;
           }
         }
         
         meanCount[clusterIndex]++;//increment counter
         for(int i=0;i<3;i++){
           sumMean[clusterIndex][i]+=TRGB[i+1][column][row];//add mean
           //switch values between clusters and differentiate values to see visual difference
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

//**************************************************************************
 
private boolean checkArray(int[][] arr1,int[][] arr2){//helper method to check equality for kMeans
  for(int i=0;i<arr1.length;i++){
  for(int j=0;j<arr1[i].length;j++){
    if(arr1[i][j]!=arr2[i][j]){
      return false; 
    }
  }
  }
  return true;
}
private boolean checkArray(double[][] arr1,double[][] arr2,double term){//helper method to check equality for GPCA_1
  for(int i=0;i<arr1.length;i++){
  for(int j=0;j<arr1[i].length;j++){
    if(Math.abs(arr1[i][j]-arr2[i][j])>term){
      return false; 
    }
  }
  }
  return true;
}
 //************************************************************************
private void writeToFile(double membership[][][],String file){
      
  try{ BufferedWriter writer = new BufferedWriter(new FileWriter(file+".csv"));
    for (int row = 0; row < membership.length; row++){
      for (int column = 0; column < membership[row].length; column++)
      {
        writer.write(row+","+column+",");
        for(int i=0;i<membership[row][column].length;i++){
         // System.out.println(membership[column].length);
          writer.write(membership[row][column][i]+",");
        }
        writer.write("\n");
      }
      
    }
    writer.close();
  }catch(Exception e){
    System.out.println(e); 
  }
}

//************************************************************************
protected Image fCM(Image imageIn){
  Dimension imageInDimension = getImageDimension(imageIn);
  int TRGB[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);
  int update[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);//to store updated pixel values
  int cluster=2;//Defind cluster value
  Random rand=new Random();
  int width=(int)imageInDimension.getWidth();//column
  int height=(int)imageInDimension.getHeight();//row
  double fuzziness=10;
  int max=0;
  double kCenters[][]=new double[cluster][3];
  double term=0;
  double membership[][][] = new double[width][height][cluster];
  double temp_membership[][][]= new double[width][height][cluster];
  
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
  try{
    writeToFile(membership,"test");
  }catch(Exception e){
    System.out.println(e); 
  }

  //calculate center for KClusters
  //#3
  for(int i=0;i<kCenters.length;i++){//cluster
    for(int j=0;j<kCenters[i].length;j++){//RGB
      double sum=0;
      double den=0;
      for (int row = 0; row < imageInDimension.getHeight(); row++){
        for (int column = 0; column < imageInDimension.getWidth(); column++)
        {
          sum+=TRGB[j+1][column][row]*(Math.pow(membership[column][row][i],fuzziness));
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
          den=Math.sqrt(Math.pow(TRGB[1][column][row]-kCenters[j][0],2)+Math.pow(TRGB[2][column][row]-kCenters[j][1],2)+Math.pow(TRGB[3][column][row]-kCenters[j][2],2));
          neu=Math.sqrt(Math.pow(TRGB[1][column][row]-kCenters[curr][0],2)+Math.pow(TRGB[2][column][row]-kCenters[curr][1],2)+Math.pow(TRGB[3][column][row]-kCenters[curr][2],2));
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
          update[i][column][row]=TRGB[i][column][row]*select;
        }
        
      }
    }
   
    
    if(compareArray(membership,temp_membership,term) || max==1000){
      System.out.println("Outer Loop Ran "+max+" times");
      break;
      
    }else{
      //update membership
      membership=temp_membership;
      try{
        writeToFile(membership,"test1");
      }catch(Exception e){
       System.out.println(e); 
      }
      
      //update center
      
      for(int i=0;i<kCenters.length;i++){//cluster
        for(int j=0;j<kCenters[i].length;j++){//RGB
          double sum=0;
          double den=0;
          for (int row = 0; row < imageInDimension.getHeight(); row++){
            for (int column = 0; column < imageInDimension.getWidth(); column++)
            {
              sum+=TRGB[j+1][column][row]*(Math.pow(membership[column][row][i],fuzziness));
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
private boolean compareArray(double[][][]arr1,double[][][]arr2,double term){
  for(int i=0;i<arr1.length;i++){
    for(int j=0;j<arr1[i].length;j++){
      for(int k=0;k<arr1[i][j].length;k++){
        if((arr1[i][j][k]-arr2[i][j][k])>term){
          return false; 
        }
      }
    }
  }
  return true;
  
  
  
}
  
//**************************************************************************
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
//**************************************************************************  
protected Image gpca_1(Image imageIn){
  Dimension imageInDimension = getImageDimension(imageIn);
  int TRGB[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);
  int update[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);//to store updated pixel values
  int cluster=5;//Defind cluster value
  Random rand=new Random();
  int width=(int)imageInDimension.getWidth();//column
  int height=(int)imageInDimension.getHeight();//row
  double fuzziness=50;
  int max=0;
  double kCenters[][]=new double[cluster][3];
  double term=0;
  double membership[][][] = new double[width][height][cluster];
  double poss[][][]=new double[width][height][cluster];
  double temp_kCenters[][]=new double[cluster][3];
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
  try{
    writeToFile(membership,"random_generate");
  }catch(Exception e){
    System.out.println(e); 
  }
  //calculate possibility
  poss=getPossibility(membership);
  try{
    writeToFile(poss,"initial_poss");
  }catch(Exception e){
    System.out.println(e); 
  }

  //calculate center for KClusters
  //#3
  for(int i=0;i<kCenters.length;i++){//cluster
    for(int j=0;j<kCenters[i].length;j++){//RGB
      double sum=0;
      double den=0;
      for (int row = 0; row < imageInDimension.getHeight(); row++){
        for (int column = 0; column < imageInDimension.getWidth(); column++)
        {
          sum+=TRGB[j+1][column][row]*(Math.pow(poss[column][row][i],fuzziness));//equation #25
          den+=Math.pow(poss[column][row][i],fuzziness);
        }
      }
     // System.out.println(sum/den);
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
        double dist=0;
        double neu=0;
        double den=0;
        double sum=0;
        double param_f=0;
        double all_clus_dist;
        //Equation 14
        //average fuzzy intracluster distance of cluster 
        
        for(int j=0;j<kCenters.length;j++){//all cluster
          all_clus_dist=Math.sqrt(Math.pow(TRGB[1][column][row]-kCenters[j][0],2)+Math.pow(TRGB[2][column][row]-kCenters[j][1],2)+Math.pow(TRGB[3][column][row]-kCenters[j][2],2));
          neu=Math.pow(membership[column][row][j],fuzziness)*Math.pow(all_clus_dist,2);
          den=Math.pow(membership[column][row][j],fuzziness);
          sum+=Math.sqrt(neu/den);
        }
          dist=Math.sqrt(Math.pow(TRGB[1][column][row]-kCenters[curr][0],2)+Math.pow(TRGB[2][column][row]-kCenters[curr][1],2)+Math.pow(TRGB[3][column][row]-kCenters[curr][2],2));
          
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
    poss=getPossibility(membership);
    try{
      writeToFile(poss,"final_poss");
    }catch(Exception e){
      System.out.println(e); 
    }
    
    //update centers
    for(int i=0;i<kCenters.length;i++){//cluster
      for(int j=0;j<kCenters[i].length;j++){//RGB
        double sum=0;
        double den=0;
        for (int row = 0; row < imageInDimension.getHeight(); row++){
          for (int column = 0; column < imageInDimension.getWidth(); column++)
          {
            sum+=TRGB[j+1][column][row]*(Math.pow(poss[column][row][i],fuzziness));//equation #25
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
      for(int i=0;i<count.length;i++){
        System.out.println(i+": "+count[i]);
      }
      System.out.println("Outer Loop Ran "+max+" times");
      break;
      
    }else{
      //update membership
      kCenters=temp_kCenters;
      try{
        writeToFile(membership,"final_membership");
      }catch(Exception e){
        System.out.println(e); 
      }
      
      //update center
      
      for(int i=0;i<kCenters.length;i++){//cluster
        for(int j=0;j<kCenters[i].length;j++){//RGB
          double sum=0;
          double den=0;
          for (int row = 0; row < imageInDimension.getHeight(); row++){
            for (int column = 0; column < imageInDimension.getWidth(); column++)
            {
              sum+=TRGB[j+1][column][row]*(Math.pow(membership[column][row][i],fuzziness));
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
//**************************************************************************
//**************************************************************************
 protected Dimension getImageDimension(Image imageIn)
 {

  // Convert image to an Image Icon to get the dimensions
  ImageIcon imageIconIn = new ImageIcon(imageIn);
  return new Dimension(imageIconIn.getIconWidth(), imageIconIn.getIconHeight());

 }


 protected Image pixelsArrayToImage(int pixels[], Dimension imageInDimension)
 {

  // Generate an image object from the pixels array
  return createImage(new MemoryImageSource((int) imageInDimension.getWidth(), (int) imageInDimension.getHeight(), pixels, 0, (int) imageInDimension.getWidth()));

 }

 protected int[] imageToPixelsArray(Image imageIn)
 {

  // Declare local storage
  Dimension imageInDimension = getImageDimension(imageIn);
  int imagePixelLength = (int) (imageInDimension.getWidth() * imageInDimension.getHeight());
  int pixels[] = new int[imagePixelLength];

  // Convert image to array via PixelGrabber
  PixelGrabber pixelGrabber = new PixelGrabber(imageIn, 0, 0, (int) imageInDimension.getWidth(), (int) imageInDimension.getHeight(), pixels, 0, (int) imageInDimension.getWidth());
    try
    {
     pixelGrabber.grabPixels();
  }
  catch(InterruptedException ie)
  {
   System.exit(1);
  }

  // Return the pixels array
  return pixels;

 }

 protected int[] TRGBArrayToPixelsArray(int[][][] TRGB, Dimension imageInDimension)
 {

  // Declare local storage
  int imagePixelLength = (int) (imageInDimension.getWidth() * imageInDimension.getHeight());
  int pixels[] = new int[imagePixelLength];

  // Loop through TRGB array, assembling the TRGB values
  for (int column = 0, row = 0, pixelIndex = 0; pixelIndex < imagePixelLength; pixelIndex++)
  {

   // Retreive TRGB
   pixels[pixelIndex] = getTRGB(TRGB[0][column][row], TRGB[1][column][row], TRGB[2][column][row], TRGB[3][column][row]);

   // Calculate column and row indexes
   if (++column == imageInDimension.getWidth())
   {
    // Reset column and increment row
    column = 0;
    row++;
   }
  }

  // Return the pixels array
  return pixels;

 }

 protected int[][][] pixelsArrayToTRGBArray(int[] pixels, Dimension imageInDimension)
 {

  // Declare local storage
  int imagePixelLength = (int) (imageInDimension.getWidth() * imageInDimension.getHeight());
  int TRGB[][][] = new int[4][(int) imageInDimension.getWidth()][(int) imageInDimension.getHeight()];

  // Convert pixel array to TRGB array
  for (int column = 0, row = 0, pixelIndex = 0; pixelIndex < imagePixelLength; pixelIndex++)
  {

   // Store transparency
   TRGB[0][column][row] = getTransparencyComponent(pixels[pixelIndex]);

   // Store red
   TRGB[1][column][row] = getRedComponent(pixels[pixelIndex]);

   // Store green
   TRGB[2][column][row] = getGreenComponent(pixels[pixelIndex]);

   // Store blue
   TRGB[3][column][row] = getBlueComponent(pixels[pixelIndex]);

   // Calculate column and row indexes
   if (++column == imageInDimension.getWidth())
   {
    // Reset column and increment row
    column = 0;
    row++;
   }

  }

  // Return the newly generated TRGB array
  return TRGB;

 }

 // TRGB Component retrieval functions //////////////////////////////
 protected final int getTransparencyComponent(int pixel)
 {
  return (pixel >> 24) & 0xff; // Return Transparency
 }

 protected final int getRedComponent(int pixel)
 {
  return (pixel >> 16) & 0xff; // Return Red
 }

 protected final int getGreenComponent(int pixel)
 {
  return (pixel >> 8) & 0xff; // Return Green
 }

 protected final int getBlueComponent(int pixel)
 {
  return pixel & 0xff; // Return Blue
 }

 protected final int getTRGB(int transparency, int red, int green, int blue)
 {
  return (transparency << 24) | (red << 16) | (green << 8) | (blue); // Return TRGB
 }

 // Public
 public static void main(String args[])
 {

  // Generate an instance of the application
  ImagePimpMinh app = new ImagePimpMinh();

 }

 // Inner Class(es) /////////////////////////////////////////////////
 // Private

 // Protected

 // Public

}

class ImageFrame extends JInternalFrame
{

 // Instance Variable(s) ////////////////////////////////////////////
 // Constant(s)

 // Data Member(s)
 // Private
 private File imageFile;
 private ImagePanel imagePanel;

 // Protected

 // Public

 // Constructor(s) //////////////////////////////////////////////////
 public ImageFrame(File imageFile)
 {

  // Call super class, JInternalFrame constructor
  super(imageFile.getName(), false, true, false);

  // Attempt to load image
  imagePanel = new ImagePanel(imageFile);
  getContentPane().add(imagePanel);

  // Set the internal image file to the passed arguement
  this.imageFile = imageFile;
  this.setTitle(imageFile.getName());
  // Initialize and show the internal frame window
  setSize(imagePanel.getImageIcon().getIconWidth(), imagePanel.getImageIcon().getIconHeight());
  show();
  toFront();
 }

 public ImageFrame(Image image)
 {

  // Call super class, JInternalFrame constructor
  super("Untitled", false, true, false);

  // Attempt to load image
  imagePanel = new ImagePanel(image);
  getContentPane().add(imagePanel);

  // Set the internal image file to a default name
  imageFile = null;

  // Initialize and show the internal frame window
  setSize(imagePanel.getImageIcon().getIconWidth(), imagePanel.getImageIcon().getIconHeight());
  show();
  toFront();
 }

 // Finalize ////////////////////////////////////////////////////////

 // Method(s) ///////////////////////////////////////////////////////
 // Private

 // Protected

 // Public
 public Image getImage()
 {

  // Return the image icon from the image panel, after conversion to an image
  return imagePanel.getImageIcon().getImage();

 }


 private class ImagePanel extends JPanel
 {

  // Instance Variable(s) ////////////////////////////////////////
  // Constant(s)

  // Data Member(s)
  // Private
  private ImageIcon imageIcon;

  // Protected

  // Public

  // Constructor(s) //////////////////////////////////////////////
  public ImagePanel(File imageFile)
  {

   // Load the image
   imageIcon = new ImageIcon(imageFile.toString());

  }

  public ImagePanel(Image image)
  {

   // Load the image
   imageIcon = new ImageIcon(image);

  }

  // Finalize ////////////////////////////////////////////////////

  // Method(s) ///////////////////////////////////////////////////
  // Private

  // Protected

  // Public
  public ImageIcon getImageIcon()
  {

   // Return the image icon
   return imageIcon;

  }

  public void paintComponent(Graphics g)
  {

   // Paint the icon to the panel
   imageIcon.paintIcon(this, g, 0, 0);

  }}}
