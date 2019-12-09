// package image_convert.bmp;
package image.multithread; 

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
import javax.imageio.ImageIO;
 
class Multithread extends Thread{
    public void run(){
        BufferedImage image = null; 
        File f = null; 
  
        // read image 
        try
        { 
            f = new File("../Raw_Image/lena.jpg"); 
            image = ImageIO.read(f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        } 

        int width = image.getWidth()/4; 
        int height = image.getHeight()/4; 

        Multithread (start, end){
            return ();
        }
  
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
    }
}

public class try_mt
{      
    static int w_total = 0;
    static int h_total = 0;
    static BufferedImage image = null;
    static int totalTime = 0;
   
    public static void main( String[] args ) throws InterruptedException
    {
        try
        {
            image = ImageIO.read(new File("lena.jpg"));
        }
        
            
        catch (IOException e)
        {
            System.out.println(e);
        }
                
        long start=System.currentTimeMillis();

        // ImageMask mask1 = new ImageMask {
        //     imageIn.getWidth(),         // width
        //     imageIn.getHeight(),        // height
        //     0,                          // x-start
        //     0,                          // y-start
        //     imageIn.getWidth(),         // region´s width
        //     imageIn.getHeight()/2 
        // }

        // ImageMask mask2 = new ImageMask {
        //     imageIn.getWidth(),         // width
        //     imageIn.getHeight(),        // height
        //     0,                          // x-start
        //     imageIn.getHeight()/2,      // y-start
        //     imageIn.getWidth(),         // region´s width
        //     imageIn.getHeight()/2 
        // }

        int height = image.getHeight()/4; 

        
        Multithread object1 = new Multithread(0, height);
        Multithread object2 = new Multithread(height, height*2);
        Multithread object3 = new Multithread(height*2, height*3);
        Multithread object4 = new Multithread(height*3, height*4);
            
        /* threads*/
        object1.start();
        object2.start();
        object3.start();
        object4.start();

        object1.join();
        object2.join();
        object3.join();
        object4.join();
        
        long stop=System.currentTimeMillis();

            try
            {
                ImageIO.write(image, "jpg", new File("lena_new.jpg"));
                System.out.println("End, saved");
            }

            catch (IOException e)
            {
                System.out.println(e);
            }
        
        System.out.println("Total time: " + (stop-start));
    }
   
    public static void setobject(int i, int j, Color bw_color)
    {
        image.setRGB(i, j, bw_color.getRGB());
    }
   
}