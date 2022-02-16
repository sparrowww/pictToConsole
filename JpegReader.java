import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

public class JpegReader {
    String [] colorArr = new String[]{
            "\033[0;40m",   // BLACK
            "\033[0;101m",  // LIGHT RED
            "\033[0;42m",   // GREEN
            "\033[0;41m",   // RED
            "\033[0;44m",   // BLUE
            "\033[0;45m",   // PURPLE
            "\033[0;46m",   // CYAN
            "\033[0;47m",   // LIGHT GRAY
            "\033[1;100m",  // DARK GRAY
            "\033[1;41m",   // RED
            "\033[1;42m",   // LIGHT GREEN
            "\033[1;103m",  // LIGHT YELLOW
            "\033[1;44m",   // LIGHT BLUE
            "\033[1;45m",   // LIGHT PURPLE
            "\033[1;46m",   // LIGHT CYAN
            "\033[1;107m"}; // WHITE
    public JpegReader(){
        try {
            readJpeg();
        }catch (IOException e){
            System.out.println("readJpeg e = " + e.toString());
        }

    }

    //java -Dpath=pict/3.jpeg -Dw=320 -Dh=240 -Dconsole=true -jar target/pictParse-1.0.jar
    private void readJpeg() throws IOException {
        String WIDTH = System.getProperty("w");
        if (WIDTH == null) {
            WIDTH = "800";
        }
        String HEIGHT = System.getProperty("h");
        if (HEIGHT == null) {
            HEIGHT = "600";
        }
        String pathName = System.getProperty("path");
        if (pathName == null){
            //pathName = "./pict/1.jpeg";
            //pathName = "./pict/2.jpeg";
            pathName = "./pict/3.jpeg";
        }
        String flagConsole = System.getProperty("console");
        if (flagConsole == null) {
            flagConsole = "false";
        }
        File fileToRead = new File(pathName);
        BufferedImage bufferedImageInput = ImageIO.read(fileToRead);
        int height = bufferedImageInput.getHeight();
        int width = bufferedImageInput.getWidth();
        System.out.println("height = " + height);
        System.out.println("width = " + width);

        BufferedImage bufferedImageOutput = new BufferedImage(Integer.parseInt(WIDTH), Integer.parseInt(HEIGHT), bufferedImageInput.getType());
        Graphics2D g2d = bufferedImageOutput.createGraphics();
        g2d.drawImage(bufferedImageInput, 0, 0, Integer.parseInt(WIDTH), Integer.parseInt(HEIGHT), null);
        g2d.dispose();

        height = bufferedImageOutput.getHeight();
        width = bufferedImageOutput.getWidth();
        Raster raster = bufferedImageOutput.getData();
        System.out.println("height = " + height);
        System.out.println("width = " + width);

        if (Boolean.parseBoolean(flagConsole)) {
            for (int i = 0; i < raster.getDataBuffer().getSize(); i += 3) {
                int b = raster.getDataBuffer().getElem(i);
                int g = raster.getDataBuffer().getElem(i + 1);
                int r = raster.getDataBuffer().getElem(i + 2);
                if (i != 0 && i % (width * 3) == 0) {
                    System.out.println("\033[0m");
                }
                else {
                    int bitSize = 0xf0;
                    int numC = AWTGraphics.searchColor(r&bitSize,g&bitSize,b&bitSize);
                    System.out.printf("   %s", colorArr[numC]);
                }
            }
        }
        else {
            new AWTGraphics(pathName, raster, height, width);
        }
    }
}
