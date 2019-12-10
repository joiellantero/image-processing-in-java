//based on code found on https://l.facebook.com/l.php?u=https%3A%2F%2Fpastebin.com%2FzuD9JTxn%3Ffbclid%3DIwAR0X99GVR40fP6y0wubSRjNfKeiyPnsreXWlJZ_9yW2NS2VT7FTJ9Pt3YY0&h=AT2fMs7VHjlspC2LaZeQB_HkRRdCcC6OS9vRFu-9GUotwfOvNXNQ4-dAfBwXbMlVQk8EkOIlnW4cMjuYbwfq40HRJz6hxzInMDT8KRmH-dwXtNmyRCK0gsX7tVMSRb4-64v8skehN2-3h_LvHymyIfuEvFQ; and https://pastebin.com/FBZzsG2i?fbclid=IwAR3jR9Ap1yDj67IEDE-BJgFLpEiVa3tkVvP9HNpaHvrlz2XAQwSVhVTiros

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
import javax.imageio.ImageIO;
 
 class rgb_to_gray extends Thread{
       
        private int x;
        private int y;
        private int x_end;
        private int y_end;
        BufferedImage image;
		private int rgb;
        
        public void run() {
        	for (int i=x; i<x_end; i++){
            	for (int j=0; j<image.getHeight(); j++){
		            int rgb = image.getRGB(i,j);
                    int a = (rgb>>24)&0xff; 
                	int r = (rgb>>16)&0xff;
					rgb = (a<<24) | (r<<16) | (0<<8) | 0;
  
                    image.setRGB(i, j, rgb); 
                }
             }
        }
        rgb_to_gray(BufferedImage image, int x, int x_end)
        {
                this.x = x;
                this.x_end = x_end;
                this.image = image;
                 
               
        }       
}
public class Red_mt
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
                        System.out.println("Error in opening image");
             }
                 
             long start=System.currentTimeMillis();
             
             rgb_to_gray t1 = new rgb_to_gray(image, 0, image.getWidth()/4);
             rgb_to_gray t2 = new rgb_to_gray(image, image.getWidth()/4, image.getWidth()/2);
             rgb_to_gray t3 = new rgb_to_gray(image, image.getWidth()/2, image.getWidth()-(image.getWidth()/4));
             rgb_to_gray t4 = new rgb_to_gray(image, image.getWidth()-(image.getWidth()/4), image.getWidth());
             
             /* threads*/
             t1.start();
             t2.start();
             t3.start();
             t4.start();
             t1.join();t2.join();t3.join();t4.join();
             
             
 
             long stop=System.currentTimeMillis();
             try{
             	ImageIO.write(image, "jpg", new File("../Processed_Images/lena_red_mt.jpg"));
             }
             catch (IOException e){
                System.out.println("Error while saving");
             }
             
             System.out.println("Total time: " + (stop-start)/1000F);
    }
   
}
