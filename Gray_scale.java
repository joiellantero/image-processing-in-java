//based on code found on https://github.com/yusufshakeel/Java-Image-Processing-Project/blob/master/example/Sepia.java
// Java program to demonstrate colored to red colored image conversion 
import java.io.File; 
import java.io.IOException; 
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO; 
  
public class Gray_scale
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
  
        // convert to grey image 
        for (int y = 0; y < height; y++){ 
            for (int x = 0; x < width; x++){ 
                int rgb = image.getRGB(x,y); 
  
                int a = (rgb>>24)&0xff; 
                int r = (rgb>>16)&0xff;
		int g = (rgb>>8)&0xff; 
                int b = rgb&0xff;  
		int red = (int)(r*0.21 + g*0.71 + b*0.07);
               	int green = (int)(r*0.21 + g*0.71 + b*0.07);
               	int blue = (int)(r*0.21 + g*0.71 + b*0.07);
               	rgb = (a<<24) | (red<<16) | (green<<8) | blue; 
  
                image.setRGB(x, y, rgb); 
            }
        }
  
        // write image 
        try
        { 
            f = new File("lena_grayscale_2.jpg"); 
            ImageIO.write(image, "jpg", f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        } 

         //Define how long it took
         long end = System.currentTimeMillis();
         float time = (end-start)/1000F;
         System.out.println(time + " seconds"); 
    } 
} 
