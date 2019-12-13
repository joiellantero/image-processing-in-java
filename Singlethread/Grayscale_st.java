//based on code found on https://l.facebook.com/l.php?u=https%3A%2F%2Fpastebin.com%2FzuD9JTxn%3Ffbclid%3DIwAR0X99GVR40fP6y0wubSRjNfKeiyPnsreXWlJZ_9yW2NS2VT7FTJ9Pt3YY0&h=AT2fMs7VHjlspC2LaZeQB_HkRRdCcC6OS9vRFu-9GUotwfOvNXNQ4-dAfBwXbMlVQk8EkOIlnW4cMjuYbwfq40HRJz6hxzInMDT8KRmH-dwXtNmyRCK0gsX7tVMSRb4-64v8skehN2-3h_LvHymyIfuEvFQ; and https://pastebin.com/FBZzsG2i?fbclid=IwAR3jR9Ap1yDj67IEDE-BJgFLpEiVa3tkVvP9HNpaHvrlz2XAQwSVhVTiros

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.imageio.ImageIO;
 
class rgb_to_gray extends Thread{
       
        private int x;
        private int x_end;
        BufferedImage image;
        Color bw_color;
 
        public void run() {
               
                for (int i=x; i<x_end; i++){
                        for (int j=0; j<image.getHeight(); j++){
                                //Color color = new Color(image.getRGB(i,j));
                                int rgb = image.getRGB(i,j);
                                int a = (rgb>>24)&0xff; 
                                int r = (rgb>>16)&0xff;
                                int g = (rgb>>8)&0xff; 
                                int b = rgb&0xff;
                                int red = (int)(r*0.21 + g*0.71 + b*0.07);
                                int green = (int)(r*0.21 + g*0.71 + b*0.07);
                                int blue = (int)(r*0.21 + g*0.71 + b*0.07);
                                rgb = (a<<24) | (red<<16) | (green<<8) | blue; 

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
public class Grayscale_st
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
                
                rgb_to_gray t1 = new rgb_to_gray(image, 0, image.getWidth());
                
                t1.start();
                t1.join();
                
                long stop=System.currentTimeMillis();
        
                try
                {
                        ImageIO.write(image, "jpg", new File("../Processed_Images/lena_grayscale_st.jpg"));
                        System.out.println("End, saved");
                }
                catch (IOException e)
                {
                        System.out.println("Error while saving");
                }

                long duration = stop - start;
             
                System.out.println("Total time: " + duration + "ms");

                BufferedWriter bw = null;

                try {
                        String content = duration + "ms";
                        
                        File file = new File("../Execution_Time/Grayscale_ST_Execution_Timelog.txt");

                        if (!file.exists()) {
                        file.createNewFile();
                        }

                        FileWriter fw = new FileWriter(file);
                        bw = new BufferedWriter(fw);
                        bw.write(content);
                        System.out.println("File written Successfully");
                } 

                catch (IOException ioe) {
                        ioe.printStackTrace();
                }

                finally
                { 
                        try{
                                if(bw!=null){
                                        bw.close();
                                }
                        }
                        catch(Exception ex){
                                System.out.println("Error in closing the BufferedWriter"+ex);
                        }
                }  
        }
}