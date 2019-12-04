//based on code found on https://github.com/yusufshakeel/Java-Image-Processing-Project/blob/master/example/Sepia.java
// Java program to demonstrate colored to red colored image conversion 
import java.io.File; 
import java.io.IOException; 
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO; 
import java.io.*;

class Singlethread extends Thread{
    public void run(){

        BufferedImage image = null; 
        File f = null; 

        try{
            f = new File("../Raw_Image/lena.jpg"); 
            image = ImageIO.read(f); 

            // System.out.println ("Thread " + 
            //       Thread.currentThread().getId() + 
            //       " is running"); 
        }

        catch(IOException e) 
        { 
            System.out.println(e); 
        } 

        // get width and height 
        int width = image.getWidth(); 
        int height = image.getHeight(); 
  
        // convert to red image 
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
            f = new File("../Processed_Images/lena_grayscale_st.jpg"); 
            ImageIO.write(image, "jpg", f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }
    }
}
  
public class Gray_scale_st
{ 
    public static void main(String args[])throws IOException 
    {

        // //Define the starting time
        long start = System.currentTimeMillis();
        
        Singlethread thread = new Singlethread();
        thread.start();

        try{
           thread.join(); 
        }

        catch(Exception ex) { 
            System.out.println(ex); 
        }

        //  //Define how long it took
         long end = System.currentTimeMillis();
         float time = (end-start)  / 1000F;
         System.out.println("Elapsed time: " + time + " s"); 
    } 
} 
