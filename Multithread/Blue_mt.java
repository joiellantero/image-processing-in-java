import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
import javax.imageio.ImageIO;
 
class rgb_to_blue extends Thread{
       
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
                   
                            int rgb = image.getRGB(x,y); 
  
                            int a = (rgb>>24)&0xff; 
                            int b = (rgb)&0xff; 
              
                            // set new RGB (blue still the same and then 0 for red and green)
                            rgb = (a<<24) | (0<<16) | (0<<8) | b; 
              
                            image.setRGB(x, y, rgb); 
                   
                        }
                }
               
        }
        rgb_to_blue(BufferedImage image, int x, int x_end)
        {
                this.x = x;
                this.x_end = x_end;
                this.image = image;
        }       
}
public class Blue_mt
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
             
             rgb_to_blue t1 = new rgb_to_blue(image, 0, image.getWidth()/4);
             rgb_to_blue t2 = new rgb_to_blue(image, image.getWidth()/4, image.getWidth()/2);
             rgb_to_blue t3 = new rgb_to_blue(image, image.getWidth()/2, image.getWidth()-(image.getWidth()/4));
             rgb_to_blue t4 = new rgb_to_blue(image, image.getWidth()-(image.getWidth()/4), image.getWidth());
             
             /* threads*/
             t1.start();
             t2.start();
             t3.start();
             t4.start();
             t1.join();t2.join();t3.join();t4.join();
             
             
 
             long stop=System.currentTimeMillis();
 
                    try
                    {
                                ImageIO.write(image, "jpg", new File("../Processed_Images/lena_blue_mt.jpg"));
                                System.out.println("End, saved");
                        }
                    catch (IOException e)
                    {
                                System.out.println("Error while saving");
                        }
             
             System.out.println("Total time: " + (stop-start));
    }
   
}
