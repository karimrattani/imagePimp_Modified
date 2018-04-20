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
import javax.swing.*;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import java.util.Random;
import java.lang.*;
import java.io.*;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.util.Collections;
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
    JMenuItem saveAsFileMenuItem = new JMenuItem("Save As...");

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
       
       fileChooser.setCurrentDirectory(new File("images/results/input"));
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
    saveAsFileMenuItem.addActionListener(
                                         new ActionListener()
                                           {
      public void actionPerformed(ActionEvent ae)
      {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        // Initialize dialog box
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // Show the file dialog box
        if (fileChooser.showSaveDialog(ImagePimpMinh.this)!=JFileChooser.CANCEL_OPTION)
        {
          ImageFrame imageFrame = (ImageFrame) desktopPane.getSelectedFrame();
          Dimension imageInDimension = getImageDimension(imageFrame.getImage());
          int row=(int)imageInDimension.getHeight(), col=(int)imageInDimension.getWidth();
          //System.out.println("Save as row: "+row+ " col: "+col);
          int pixels[]=imageToPixelsArray(imageFrame.getImage());
          imageFrame.saveImage(fileChooser.getSelectedFile(),pixels,col,row);
          try{imageFrame.setSelected(true);}catch(Exception e){System.out.println(e);}
        }
      }
    });
    // Add Item(s) to File Menu
    fileMenu.add(newFileMenuItem);
    fileMenu.add(openFileMenuItem);
    fileMenu.addSeparator();
    fileMenu.add(closeFileMenuItem);
    fileMenu.add(saveAsFileMenuItem);

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
    JMenuItem pcaTransformationsMenuItem = new JMenuItem("PCM 93");
    JMenuItem gpcaTransformationsMenuItem = new JMenuItem("GPCA #1");
    JMenuItem npcaTransformationsMenuItem = new JMenuItem("NPCA #1");
    JMenuItem fcmMultiSpectralTransformationsMenuItem = new JMenuItem("FCM MultiSpectral #1");
    JMenuItem npcaMultiSpectralTransformationsMenuItem = new JMenuItem("NPCA MultiSpectral #1");
    JMenuItem gpcaMultiSpectralTransformationsMenuItem = new JMenuItem("GPCA MultiSpectral #1");
    JMenuItem pcaMultiSpectralTransformationsMenuItem = new JMenuItem("PCA MultiSpectral #1");
    JMenuItem kMeansMultiSpectralTransformationsMenuItem = new JMenuItem("kMeans MultiSpectral #1");
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
        Image imageIn = imageFrame.getImage();
        Dimension imageInDimension = getImageDimension(imageIn);
        int input[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);
        String inp = JOptionPane.showInputDialog("Enter Cluster");
        int cluster=Integer.parseInt(inp);//Defind cluster value
        inp = JOptionPane.showInputDialog("Enter Fuzziness");
        double fuzziness=Double.parseDouble(inp);
        FCM res=new FCM(imageIn,input,cluster,fuzziness);
        Image output=res.get_FCM();
        ImageFrame newImageFrame = new ImageFrame(output);
        //ImageFrame newImageFrame = new ImageFrame(fCM(imageFrame.getImage()));
        desktopPane.add(newImageFrame);
        newImageFrame.toFront();
        try{newImageFrame.setSelected(true);}catch(Exception e){}
      }
    }
    );
    pcaTransformationsMenuItem.addActionListener(
                                                 new ActionListener()
                                                   {
      public void actionPerformed(ActionEvent ae)
      {
        // Call contrast enhancement method
        ImageFrame imageFrame = (ImageFrame) desktopPane.getSelectedFrame();
        Image imageIn = imageFrame.getImage();
        Dimension imageInDimension = getImageDimension(imageIn);
        int input[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);
        String inp = JOptionPane.showInputDialog("Enter Cluster");
        int cluster=Integer.parseInt(inp);//Defind cluster value
        inp = JOptionPane.showInputDialog("Enter Fuzziness");
        double fuzziness=Double.parseDouble(inp);
        PCA res=new PCA(imageIn,input,cluster,fuzziness);
        Image output=res.get_PCA();
        ImageFrame newImageFrame = new ImageFrame(output);
        //ImageFrame newImageFrame = new ImageFrame(fCM(imageFrame.getImage()));
        desktopPane.add(newImageFrame);
        newImageFrame.toFront();
        try{newImageFrame.setSelected(true);}catch(Exception e){}
      }
    }
    );
    npcaTransformationsMenuItem.addActionListener(
                                                 new ActionListener()
                                                   {
      public void actionPerformed(ActionEvent ae)
      {
        ImageFrame imageFrame = (ImageFrame) desktopPane.getSelectedFrame();
        Image imageIn = imageFrame.getImage();
        Dimension imageInDimension = getImageDimension(imageIn);
        int input[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);
        String inp = JOptionPane.showInputDialog("Enter Cluster");
        int cluster=Integer.parseInt(inp);//Defind cluster value
        inp = JOptionPane.showInputDialog("Enter Fuzziness");
        double fuzziness=Double.parseDouble(inp);
        NPCA res=new NPCA(imageIn,input,cluster,fuzziness);
        Image output=res.get_NPCA();
        ImageFrame newImageFrame = new ImageFrame(output);
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
               ImageFrame imageFrame = (ImageFrame) desktopPane.getSelectedFrame();
        Image imageIn = imageFrame.getImage();
        Dimension imageInDimension = getImageDimension(imageIn);
        int input[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageIn), imageInDimension);
        String inp = JOptionPane.showInputDialog("Enter Cluster");
        int cluster=Integer.parseInt(inp);//Defind cluster value
        inp = JOptionPane.showInputDialog("Enter Fuzziness");
        double fuzziness=Double.parseDouble(inp);
        GPCA res=new GPCA(imageIn,input,cluster,fuzziness);
        Image output=res.get_GPCA();
        ImageFrame newImageFrame = new ImageFrame(output);
        desktopPane.add(newImageFrame);
        newImageFrame.toFront();
        try{newImageFrame.setSelected(true);}catch(Exception e){}
      }
    }
    );

    //MULTISPECTRAL
    fcmMultiSpectralTransformationsMenuItem.addActionListener(
                                                 new ActionListener()
                                                   {
      public void actionPerformed(ActionEvent ae)
      {
        // Call contrast enhancement method
        ImageFrame newImageFrame = new ImageFrame(multiSpectral(1));
        desktopPane.add(newImageFrame);
        newImageFrame.toFront();
        try{newImageFrame.setSelected(true);}catch(Exception e){}
      }
    }
    );
    
    npcaMultiSpectralTransformationsMenuItem.addActionListener(
                                                 new ActionListener()
                                                   {
      public void actionPerformed(ActionEvent ae)
      {
        ImageFrame newImageFrame = new ImageFrame(multiSpectral(4));
        desktopPane.add(newImageFrame);
        newImageFrame.toFront();
        try{newImageFrame.setSelected(true);}catch(Exception e){}
      }
    }
    );
    
    pcaMultiSpectralTransformationsMenuItem.addActionListener(
                                                 new ActionListener()
                                                   {
      public void actionPerformed(ActionEvent ae)
      {
        ImageFrame newImageFrame = new ImageFrame(multiSpectral(2));
        desktopPane.add(newImageFrame);
        newImageFrame.toFront();
        try{newImageFrame.setSelected(true);}catch(Exception e){}
      }
    }
    );
    
    gpcaMultiSpectralTransformationsMenuItem.addActionListener(
                                                 new ActionListener()
                                                   {
      public void actionPerformed(ActionEvent ae)
      {
        // Call contrast enhancement method
        ImageFrame newImageFrame = new ImageFrame(multiSpectral(3));
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
    transformationsMenu.add(pcaTransformationsMenuItem);
    transformationsMenu.add(npcaTransformationsMenuItem);
    transformationsMenu.add(gpcaTransformationsMenuItem);
    transformationsMenu.add(npcaMultiSpectralTransformationsMenuItem);
    transformationsMenu.add(fcmMultiSpectralTransformationsMenuItem);
    transformationsMenu.add(gpcaMultiSpectralTransformationsMenuItem);
    transformationsMenu.add(pcaMultiSpectralTransformationsMenuItem);
  
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
 protected int[][] getClustersColor(int cluster){//get Cluster Color to differentiate
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
   String inp = JOptionPane.showInputDialog("Enter Cluster");
   int k=Integer.parseInt(inp);//Defind cluster value
   
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
         for(int i=1;i<kCenters.length;i++){
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
 
protected boolean checkArray(int[][] arr1,int[][] arr2){//helper method to check equality for kMeans
  for(int i=0;i<arr1.length;i++){
  for(int j=0;j<arr1[i].length;j++){
    if(arr1[i][j]!=arr2[i][j]){
      return false; 
    }
  }
  }
  return true;
}
protected boolean checkMembership(double[][][] arr1,double[][][] arr2,double term){//helper method to check equality for NPCA
  for(int i=0;i<arr1.length;i++){
  for(int j=0;j<arr1[i].length;j++){
    for(int k=0;k<arr1[i][j].length;k++){
    if(Math.abs(arr1[i][j][k]-arr2[i][j][k])>term){
      return false; 
    }
    }
  }
  }
  return true;
}
protected boolean checkArray(double[][] arr1,double[][] arr2,double term){//helper method to check equality for GPCA_1
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



//**************************************************************************
protected double[][][] getPossibility(double[][][] memb){
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


//******************************************************************************************************
//******************************************************************************************************

protected double XB_Index(double[][][] membership,double[][] kCenter, int[][][] TRGB, Dimension imageInDimension){
  double sum=0;
  int cluster=kCenter.length;
  
  ArrayList<Double> distArr=new ArrayList<Double>();
  
  for(int i=0;i<cluster-1;i++){//add all but last distance
    double dist;
    dist=Math.sqrt(Math.pow(kCenter[i][0]-kCenter[i+1][0],2)+Math.pow(kCenter[i][1]-kCenter[i+1][1],2)+Math.pow(kCenter[i][2]-kCenter[i+1][2],2));
    System.out.println("Cluster Value: "+i+" Distance: "+dist);
    distArr.add(dist);
  }
  double LastDist=Math.sqrt(Math.pow(kCenter[cluster-1][0]-kCenter[0][0],2)+Math.pow(kCenter[cluster-1][1]-kCenter[0][1],2)+Math.pow(kCenter[cluster-1][2]-kCenter[0][2],2));//add last distance
  distArr.add(LastDist);
  Collections.sort(distArr);
  int width=(int)imageInDimension.getWidth();//column
  int height=(int)imageInDimension.getHeight();//row
  for (int row = 0; row < height; row++){
      for (int column = 0; column < width; column++)
      {
        for(int k=0;k<cluster;k++){
          double x = Math.pow(membership[column][row][k],2);
          double dist=Math.pow(TRGB[1][column][row]-kCenter[k][0],2)+Math.pow(TRGB[2][column][row]-kCenter[k][1],2)+Math.pow(TRGB[3][column][row]-kCenter[k][2],2);
          sum+=x*dist;
        }
      }
  }
  System.out.println(width*height);
  double result=sum/(distArr.get(0)*(90000));
  System.out.println(result);
   
  
  
  return result;
}
//******************************************************************************************************
//******************************************************************************************************
//******************************************************************************************************

//******************************************************************************************************
//******************************************************************************************************
//******************************************************************************************************
//******************************************************************************************************
//******************************************************************************************************

protected int[][][] getImage(ImageFrame[] imageFrame1){
  System.out.println("Merging Image Array");
  Dimension dim=getImageDimension(imageFrame1[0].getImage()); 
  int width=(int)dim.getWidth();//column
  int height=(int)dim.getHeight();//row
  int[][][] out=new int[imageFrame1.length][width][height];
   for(int i=0;i<imageFrame1.length;i++){
      System.out.println("Working on Image "+i);
      int TRGB[][][] = pixelsArrayToTRGBArray(imageToPixelsArray(imageFrame1[i].getImage()), dim);
    for(int row=0;row<height;row++){
    for(int col=0;col<width;col++){
       out[i][col][row]=TRGB[1][col][row];//all space will be the same
      }
    } 
  }
    System.out.println("End Merging Image Array");

      
  return out;
}
protected Image multiSpectral(int alg){
  
//        ImageFrame imageFrame1=new ImageFrame(new File("images/bolivia-band1.gif"));
//        ImageFrame imageFrame2=new ImageFrame(new File("images/bolivia-band2.gif"));
//        ImageFrame imageFrame3=new ImageFrame(new File("images/bolivia-band3.gif"));
//        ImageFrame imageFrame4=new ImageFrame(new File("images/bolivia-band4.gif"));
//        ImageFrame imageFrame5=new ImageFrame(new File("images/bolivia-band5.gif"));
//        ImageFrame imageFrame6=new ImageFrame(new File("images/bolivia-band6.gif"));
//        ImageFrame imageFrame7=new ImageFrame(new File("images/bolivia-band7.gif"));
        
//        ImageFrame imageFrame1=new ImageFrame(new File("images/mono_lake-band1.gif"));
//        ImageFrame imageFrame2=new ImageFrame(new File("images/mono_lake-band2.gif"));
//        ImageFrame imageFrame3=new ImageFrame(new File("images/mono_lake-band3.gif"));
//        ImageFrame imageFrame4=new ImageFrame(new File("images/mono_lake-band4.gif"));
//        ImageFrame imageFrame5=new ImageFrame(new File("images/mono_lake-band5.gif"));
//        ImageFrame imageFrame6=new ImageFrame(new File("images/mono_lake-band6.gif"));
//        ImageFrame imageFrame7=new ImageFrame(new File("images/mono_lake-band7.gif"));
//        
  
        ImageFrame imageFrame1=new ImageFrame(new File("images/h1_enhanced.gif"));
        ImageFrame imageFrame2=new ImageFrame(new File("images/h2_enhanced.gif"));
        ImageFrame imageFrame3=new ImageFrame(new File("images/h3_enhanced.gif"));
        ImageFrame imageFrame4=new ImageFrame(new File("images/h4_enhanced.gif"));
        ImageFrame imageFrame5=new ImageFrame(new File("images/h5_enhanced.gif"));
        int cluster=0;
        double fuzziness=0;
        String inp = JOptionPane.showInputDialog("Enter Cluster");
        cluster=Integer.parseInt(inp);//Defind cluster value
        inp = JOptionPane.showInputDialog("Enter Fuzziness");
        fuzziness=Double.parseDouble(inp);
          
          
        
        ImageFrame[] img={imageFrame1,imageFrame2,imageFrame3,imageFrame4,imageFrame5};
        int[][][] out=getImage(img);
        
        //getMultiSpectral multi=new getMultiSpectral(alg,imageFrame1.getImage(),out,cluster,fuzziness);
        Image res=null;
        if(alg==1){
          FCM fcm=new FCM(imageFrame1.getImage(),out,cluster,fuzziness);
          res=fcm.get_FCM();
        }else if(alg==2){
          PCA pca=new PCA(imageFrame1.getImage(),out,cluster,fuzziness);
          res=pca.get_PCA();
          
        }else if(alg==3){
          GPCA gpca=new GPCA(imageFrame1.getImage(),out,cluster,fuzziness);
          res=gpca.get_GPCA();
        }else if(alg==4){
          NPCA npca=new NPCA(imageFrame1.getImage(),out,cluster,fuzziness);
          res=npca.get_NPCA();
        }else{
            throw new IllegalArgumentException("Invalid parameter for algorithm");
        }
        return res;
  
}

//**************************************************************************
//**************************************************************************
 protected static Dimension getImageDimension(Image imageIn)
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


 // Inner Class(es) /////////////////////////////////////////////////
 // Private

 // Protected

 // Public

}