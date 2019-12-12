//based on code found on https://l.facebook.com/l.php?u=https%3A%2F%2Fpastebin.com%2FzuD9JTxn%3Ffbclid%3DIwAR0X99GVR40fP6y0wubSRjNfKeiyPnsreXWlJZ_9yW2NS2VT7FTJ9Pt3YY0&h=AT2fMs7VHjlspC2LaZeQB_HkRRdCcC6OS9vRFu-9GUotwfOvNXNQ4-dAfBwXbMlVQk8EkOIlnW4cMjuYbwfq40HRJz6hxzInMDT8KRmH-dwXtNmyRCK0gsX7tVMSRb4-64v8skehN2-3h_LvHymyIfuEvFQ; and https://pastebin.com/FBZzsG2i?fbclid=IwAR3jR9Ap1yDj67IEDE-BJgFLpEiVa3tkVvP9HNpaHvrlz2XAQwSVhVTiros

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
import javax.imageio.ImageIO;
 
class rgb_to_sepia extends Thread{
       
        private int x;
        private int y;
        private int x_end;
        private int y_end;
        BufferedImage image;
	    private int rgb;
        Color bw_color;
 
        public void run() {
               
                 for (int i=x; i<x_end; i++){
                        for (int j=0; j<image.getHeight(); j++){
                   
                            int rgb = image.getRGB(i,j); 
  
                            int a = (rgb>>24)&0xff; 
                            int r = (rgb>>16)&0xff;
                            int g = (rgb>>8)&0xff; 
                            int b = rgb&0xff;  
                            int tr = (int)(r*0.393 + g*0.769 + b*0.189);
                               int tg = (int)(r*0.349 + g*0.686 + b*0.168);
                            int tb = (int)(r*0.272 + g*0.534 + b*0.131);
                              
                            if (tr > 255){
                                r = 255;
                            }else{
                                r = tr;
                            }
                            if (tg > 255){
                                g = 255;
                            }else{
                                g = tg;
                            }	
                            if (tb > 255){
                                b = 255;
                            }else{
                                b = tb;
                            }
                            
                            rgb = (a<<24) | (r<<16) | (g<<8) | b; 
              
                            image.setRGB(i, j, rgb); 
                   
                        }
                    }
               
        }
        rgb_to_sepia(BufferedImage image, int x, int x_end)
        {
                this.x = x;
                this.x_end = x_end;
                this.image = image;
                 
               
        }       
}

public class Sepia
{      
    static int w_total = 0;
    static int h_total = 0;
    static BufferedImage image = null;
    static int totalTime = 0;
   
    public static void main( String[] args ) throws InterruptedException
    {
     
             try
             {
                        image = ImageIO.read(new File("../Raw_Image/lena.jpg"));
             }
           
             
             catch (IOException e)
             {
                        System.out.println(e);
             }
                 
             long start=System.currentTimeMillis();
             
             rgb_to_sepia t1 = new rgb_to_sepia(image, 0, image.getWidth()/4);
             rgb_to_sepia t2 = new rgb_to_sepia(image, image.getWidth()/4, image.getWidth()/2);
             rgb_to_sepia t3 = new rgb_to_sepia(image, image.getWidth()/2, image.getWidth()-(image.getWidth()/4));
             rgb_to_sepia t4 = new rgb_to_sepia(image, image.getWidth()-(image.getWidth()/4), image.getWidth());
             
             /* threads*/
             t1.start();
             t2.start();
             t3.start();
             t4.start();
             t1.join();t2.join();t3.join();t4.join();
             
             
 
             long stop=System.currentTimeMillis();
 
                    try
                    {
                                ImageIO.write(image, "jpg", new File("../Processed_Images/lena_sepia_mt.jpg"));
                                System.out.println("End, saved");
                        }
                    catch (IOException e)
                    {
                                System.out.println("Error while saving");
                        }
             
             System.out.println("Total time: " + (stop-start));
    }
   
}
