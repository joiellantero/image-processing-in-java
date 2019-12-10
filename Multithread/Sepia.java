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
                   
                            int rgb = image.getRGB(x,y); 
  
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
                            // set new RGB (blue still the same and then 0 for red and green) 
                            rgb = (a<<24) | (r<<16) | (g<<8) | b; 
              
                            image.setRGB(x, y, rgb); 
                   
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
