//based on code found on https://l.facebook.com/l.php?u=https%3A%2F%2Fpastebin.com%2FzuD9JTxn%3Ffbclid%3DIwAR0X99GVR40fP6y0wubSRjNfKeiyPnsreXWlJZ_9yW2NS2VT7FTJ9Pt3YY0&h=AT2fMs7VHjlspC2LaZeQB_HkRRdCcC6OS9vRFu-9GUotwfOvNXNQ4-dAfBwXbMlVQk8EkOIlnW4cMjuYbwfq40HRJz6hxzInMDT8KRmH-dwXtNmyRCK0gsX7tVMSRb4-64v8skehN2-3h_LvHymyIfuEvFQ; and https://pastebin.com/FBZzsG2i?fbclid=IwAR3jR9Ap1yDj67IEDE-BJgFLpEiVa3tkVvP9HNpaHvrlz2XAQwSVhVTiros

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
 
import javax.imageio.ImageIO;

class Photo {
        public static int num = 5;
}
 
class rgb_to_blue extends Thread{
       
        private int x;
        private int x_end;
        BufferedImage image;
        Color bw_color;
 
        public void run() {
               
                 for (int i=x; i<x_end; i++){
                        for (int j=0; j<image.getHeight(); j++){
                   
                            int rgb = image.getRGB(i,j); 
  
                            int a = (rgb>>24)&0xff; 
                            int b = (rgb)&0xff; 
              
                            // set new RGB (blue still the same and then 0 for red and green)
                            rgb = (a<<24) | (0<<16) | (0<<8) | b; 
              
                            image.setRGB(i, j, rgb); 

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
        static BufferedImage[] image = new BufferedImage[Photo.num];
        static int totalTime = 0;
        static String[] s = new String[100];
        static String[] name = new String[20];
        static int i = 0;

        public void listFilesForFolder(final File folder){
                for (final File fileEntry : folder.listFiles()){
                    if (fileEntry.isDirectory()){
                        listFilesForFolder(fileEntry);
                    }
        
                    else{
                        s[i] = fileEntry.getPath();
                        name[i] = fileEntry.getName();
                    }
                    i++;
                }
        }
        public static void main( String[] args ) throws InterruptedException
        {
     
                final File folder = new File("../Raw_Image");
                Grayscale_st listFiles = new Grayscale_st();
                listFiles.listFilesForFolder(folder);
     
                try
                {
                        System.out.println("Reading Images...");
                        for (int i = 0; i < Photo.num; i++){
                                image[i] = ImageIO.read(new File(s[i]));
                        }
                        System.out.println("Read successful.");
                }
                catch (IOException e)
                {
                        System.out.println(e);
                }

                long duration[] = new long[Photo.num];

                System.out.println("Processing images...");
                for(int i = 0; i < Photo.num; i++){
                        long start=System.currentTimeMillis();
                
                        rgb_to_blue t1 = new rgb_to_blue(image[i], 0, image[i].getWidth()/4);
                        rgb_to_blue t2 = new rgb_to_blue(image[i], image[i].getWidth()/4, image[i].getWidth()/2);
                        rgb_to_blue t3 = new rgb_to_blue(image[i], image[i].getWidth()/2, image[i].getWidth()/4);
                        rgb_to_blue t4 = new rgb_to_blue(image[i], image[i].getWidth()-(image[i].getWidth()/4), image[i].getWidth());
                        
                        /* threads*/
                        t1.start();
                        t2.start();
                        t3.start();
                        t4.start();
                        t1.join();
                        t2.join();
                        t3.join();
                        t4.join();
        
                        long stop=System.currentTimeMillis();
                        duration[i] = stop-start;
                }
                System.out.println("Process successful");

                //save processed images
                try
                {
                        System.out.println("Saving processed images...");
                        for(int i = 0; i < Photo.num; i++){
                                ImageIO.write(image[i], "jpg", new File("../Processed_Images/Grayscale_ST_" + name[i]));
                                // System.out.println("End, saved " + name[i]); 
                        }
                        System.out.println("Save successful");
                }
                catch (IOException e)
                {
                        System.out.println(e);
                }
        }
   
}
