import java.awt.*;
import java.awt.Image.*;
import javax.swing.ImageIcon;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.Random;
public class ImageTools{
  //**************************************************************************
//**************************************************************************
  protected static int[][] getClustersColor(int cluster){//get Cluster Color to differentiate
    Random rnd=new Random();
    int[][] arr=new int[cluster][3];
    for(int i=0;i<arr.length;i++){
      for(int j=0;j<arr[0].length;j++){
        arr[i][j]=rnd.nextInt(255);
      }
      
    }
    return arr;   
  }
  
  protected static boolean compareArray(double[][][]arr1,double[][][]arr2,double term){
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
//**************************************************************************
  protected static Dimension getImageDimension(Image imageIn)
  {
    
    // Convert image to an Image Icon to get the dimensions
    ImageIcon imageIconIn = new ImageIcon(imageIn);
    return new Dimension(imageIconIn.getIconWidth(), imageIconIn.getIconHeight());
    
  }
  
  
  protected static Image pixelsArrayToImage(int pixels[], Dimension imageInDimension)
  {
    
    // Generate an image object from the pixels array
    return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource((int) imageInDimension.getWidth(), (int) imageInDimension.getHeight(), pixels, 0, (int) imageInDimension.getWidth()));
    
  }
  
  protected static int[] imageToPixelsArray(Image imageIn)
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
  
  protected static int[] TRGBArrayToPixelsArray(int[][][] TRGB, Dimension imageInDimension)
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
  
  protected static int[][][] pixelsArrayToTRGBArray(int[] pixels, Dimension imageInDimension)
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
  protected static final int getTransparencyComponent(int pixel)
  {
    return (pixel >> 24) & 0xff; // Return Transparency
  }
  
  protected static final int getRedComponent(int pixel)
  {
    return (pixel >> 16) & 0xff; // Return Red
  }
  
  protected static final int getGreenComponent(int pixel)
  {
    return (pixel >> 8) & 0xff; // Return Green
  }
  
  protected static final int getBlueComponent(int pixel)
  {
    return pixel & 0xff; // Return Blue
  }
  
  protected static final int getTRGB(int transparency, int red, int green, int blue)
  {
    return (transparency << 24) | (red << 16) | (green << 8) | (blue); // Return TRGB
  }
  
  // Public
  
  
  // Inner Class(es) /////////////////////////////////////////////////
  // Private
  
  // Protected
  
  // Public
  
}