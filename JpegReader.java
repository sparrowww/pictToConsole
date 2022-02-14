import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

public class JpegReader {
    static class RGBColor{
        public int r;
        public int g;
        public int b;
        public RGBColor(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
    RGBColor [] RGBColors = new RGBColor[]{
        new RGBColor(0, 0, 0),        // RGBBlack
        new RGBColor(255, 0, 0),      // RGBRed
        new RGBColor(0, 255, 0),      // RGBGreen
        new RGBColor(128, 0, 0),      // RGBOrange
        new RGBColor(0, 0, 255),      // RGBBlue
        new RGBColor(255, 0, 255),    // RGBPurple
        new RGBColor(0, 255, 255),    // RGBCyan
        new RGBColor(192, 192, 192),  // RGBLGray
        new RGBColor(128, 128, 128),  // RGBDGray
        new RGBColor(128, 0, 0),      // RGBLRed
        new RGBColor(0, 128, 0),      // RGBLGreen
        new RGBColor(255, 255, 0),    // RGBYellow
        new RGBColor(0, 0, 128),      // RGBLBlue
        new RGBColor(128, 0, 128),    // RGBLPurple
        new RGBColor(0, 128, 128),    // RGBLCyan
        new RGBColor(255, 255, 255)}; // RGBWhite

    String [] colorArr = new String[]{
            "\033[0;40m", // BLACK
            "\033[0;41m", // RED
            "\033[0;42m", // GREEN
            "\033[0;43m", // BROWN/ORANGE
            "\033[0;44m", // BLUE
            "\033[0;45m", // PURPLE
            "\033[0;46m", // CYAN
            "\033[0;47m", // LIGHT GRAY
            "\033[1;40m", // DARK GRAY
            "\033[1;41m", // LIGHT RED
            "\033[1;42m", // LIGHT GREEN
            "\033[1;43m", // YELLOW
            "\033[1;44m", // LIGHT BLUE
            "\033[1;45m", // LIGHT PURPLE
            "\033[1;46m", // LIGHT CYAN
            "\033[1;47m"}; // WHITE
    public JpegReader(){
        try {
            readJpeg();
        }catch (IOException e){
            System.out.println("readJpeg e = " + e.toString());
        }

    }
    private int searchColor (int r, int g, int b){
        int min = Integer.MAX_VALUE;
        int numColor = 0;
        for (int i = 0; i < 16; ++i){
            int tmp = Math.abs(RGBColors[i].r - r) +
                        Math.abs(RGBColors[i].g - g) +
                        Math.abs(RGBColors[i].b - b);
            if (tmp < min){
                min = tmp;
                numColor = i;
            }
        }
        return numColor;
    }
    private void readJpeg() throws IOException {
        String pathName = System.getProperty("path");
        File fileToRead = new File(pathName);
        BufferedImage bufferedImageInput = ImageIO.read(fileToRead);

        BufferedImage bufferedImageOutput = new BufferedImage(320,
                240, bufferedImageInput.getType());

        Graphics2D g2d = bufferedImageOutput.createGraphics();
        g2d.drawImage(bufferedImageInput, 0, 0, 320, 240, null);
        g2d.dispose();

        int height = bufferedImageOutput.getHeight();
        int width = bufferedImageOutput.getWidth();
        Raster raster = bufferedImageOutput.getData();
        for(int i = 0; i < raster.getDataBuffer().getSize(); i+=3){
            int b = raster.getDataBuffer().getElem(i);
            int g = raster.getDataBuffer().getElem(i+1);
            int r = raster.getDataBuffer().getElem(i+2);
            int numC = searchColor(r,g,b);
            System.out.printf(".%s", colorArr[numC]);
            if ( i != 0 && i % (width * 3) == 0) {
                System.out.println("\033[0m");
            }
        }
    }
}
