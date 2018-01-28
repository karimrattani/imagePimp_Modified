import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
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
  setMaximizable(true);
  setResizable(true);
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
  setMaximizable(true);
  setResizable(true);
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
 public boolean saveImage(File imageFile, int[] pixels, int col, int row)
 {
   try{
     BufferedImage image = new BufferedImage(col, row, BufferedImage.TYPE_3BYTE_BGR);
     image.setRGB(0,0,col,row,pixels,0,col);
     ImageIO.write(image,"jpg",imageFile);
//     OutputStream os = new FileOutputStream(imageFile);
//     JPEGEncodeParam param = new JPEGEncodeParam();
//     ImageEncoder enc = ImageCodec.createImageEncoder("JPEG",os,param);
//     
//     enc.encode(image);
//     os.close();
     
     /*FileOutputStream os = new FileOutputStream(imageFile);
      JAI.create("encode",imagePanel.getImageIcon().getImage(),os,JPG,null);
      JAI.create("filestore",imagePanel.getImageIcon().getImage(),imageFile.getName(),JPG,null);
      os.close();*/
   }catch(Exception e){return false;}
   return true;
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