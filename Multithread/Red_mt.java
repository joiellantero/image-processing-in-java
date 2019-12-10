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
