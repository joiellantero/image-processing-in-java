//based on code found on https://github.com/yusufshakeel/Java-Image-Processing-Project/blob/master/example/RedImage.java
import java.io.File; 
import java.io.IOException; 
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO; 
  
public class Blue
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
  	//Define the starting time
    	long start = System.currentTimeMillis();

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
                int b = (rgb)&0xff; 
  
                // set new RGB (blue still the same and then 0 for red and green)
                rgb = (a<<24) | (0<<16) | (0<<8) | b; 
  
                image.setRGB(x, y, rgb); 
            } 
        } 
  
        // write image 
        try
        { 
            f = new File("lena_blue.jpg"); 
            ImageIO.write(image, "jpg", f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        } 
    } 
  	//Define how long it took
	  long end = System.currentTimeMillis();
		float time = (end-start)/1000F;
	  System.out.println(time + "seconds"); 
} 
