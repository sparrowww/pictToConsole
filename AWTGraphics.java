import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.*;       // Using AWT's Graphics and Color
import java.awt.event.*; // Using AWT event classes and listener interfaces
import java.awt.image.Raster;
import javax.swing.*;    // Using Swing's components and containers
 
/** Custom Drawing Code Template */
// A Swing application extends javax.swing.JFrame
public class AWTGraphics extends JFrame {
   // Define constants

   private final Raster raster;
   private final int width;
 
   // Constructor to set up the GUI components and event handlers
   public AWTGraphics(String pathName, Raster raster, int height, int width) {
      this.width = width;
      this.raster = raster;

      // Declare an instance of the drawing canvas,
      // which is an inner class called DrawCanvas extending javax.swing.JPanel.
      DrawCanvas canvas = new DrawCanvas();    // Construct the drawing canvas
      canvas.setPreferredSize(new Dimension(width, height));
 
      // Set the Drawing JPanel as the JFrame's content-pane
      Container cp = getContentPane();
      cp.add(canvas);
      // or "setContentPane(canvas);"
 
      setDefaultCloseOperation(EXIT_ON_CLOSE);   // Handle the CLOSE button
      pack();              // Either pack() the components; or setSize()
      String title = pathName + " (" + width + "x" + height + ")";
      setTitle(title);  // "super" JFrame sets the title
      setVisible(true);    // "super" JFrame show
   }
 
   /**
    * Define inner class DrawCanvas, which is a JPanel used for custom drawing.
    */
   private class DrawCanvas extends JPanel {
      // Override paintComponent to perform your own painting
      @Override
      public void paintComponent(Graphics g) {
         super.paintComponent(g);     // paint parent's background
         setBackground(Color.BLACK);  // set background color for this JPanel
 
         // Your custom painting codes. For example,
         // Drawing primitive shapes
         int x = 0;
         int y = 0;
         for(int i = 0; i < raster.getDataBuffer().getSize(); i+=3){
            int b = raster.getDataBuffer().getElem(i);
            int gg = raster.getDataBuffer().getElem(i+1);
            int r = raster.getDataBuffer().getElem(i+2);
            int bitSize = 0xf0;
            int numC = searchColor(r&bitSize,gg&bitSize,b&bitSize);
            g.setColor(arrColor[numC]);       // change the drawing color
            //g.setColor(new Color(r&bitSize,gg&bitSize,b&bitSize));       // change the drawing color

            if ( i != 0 && i % ((width * 3)) == 0) {
               x = 0;
               ++y;
            }
            else {
               g.drawLine(x, y, x+1, y);
               x++;
            }
         }
      }
   }
   private static final Color []arrColor = new Color[]{
           new Color(0, 0, 0),
           new Color(255, 0, 0),
           new Color(0, 255, 0),
           new Color(128, 0, 0),
           new Color(0, 0, 255),
           new Color(255, 0, 255),
           new Color(0, 255, 255),
           new Color(192, 192, 192),
           new Color(128, 128, 128),
           new Color(128, 0, 0),
           new Color(0, 128, 0),
           new Color(255, 255, 0),
           new Color(0, 0, 128),
           new Color(128, 0, 128),
           new Color(0, 128, 128),
           new Color(255, 255, 255)};

   public static int searchColor(int r, int g, int b){
      int min = Integer.MAX_VALUE;
      int numColor = 0;
      for (int i = 0; i < arrColor.length; ++i){
         int tmp = Math.abs(arrColor[i].getRed() - r) +
                 Math.abs(arrColor[i].getGreen() - g) +
                 Math.abs(arrColor[i].getBlue() - b);
         if (tmp < min){
            min = tmp;
            numColor = i;
         }
      }
      //System.out.println("min = " + min);
      return numColor;
   }
}