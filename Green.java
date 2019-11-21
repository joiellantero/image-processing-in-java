//based on code found on https://github.com/yusufshakeel/Java-Image-Processing-Project/blob/master/example/RedImage.java
// Java program to demonstrate colored to rblue colored image conversion 
import java.io.File; 
import java.io.IOException; 
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO; 
  
public class Green
{ 
    public static void main(String args[])throws IOException 
    { 
        BufferedImage image = null; 
        File f = null; 
  
        // read image 
        try
        { 
            f = new File("lena.jpg"); 
            image = ImageIO.read(f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        } 
  
        // get width and height 
        int width = image.getWidth(); 
        int height = image.getHeight(); 
  
        // convert to red image 
        for (int y = 0; y < height; y++) 
        { 
            for (int x = 0; x < width; x++) 
            { 
                int rgb = image.getRGB(x,y); 
  
                int a = (rgb>>24)&0xff; 
                int g = (rgb>>8)&0xff; 
  
                // set new RGB (blue still the same and then 0 for red and green)
                rgb = (a<<24) | (0<<16) | (g<<8) | 0; 
  
                image.setRGB(x, y, rgb); 
            } 
        } 
  
        // write image 
        try
        { 
            f = new File("lena_green.jpg"); 
            ImageIO.write(image, "jpg", f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        } 
    } 
} 