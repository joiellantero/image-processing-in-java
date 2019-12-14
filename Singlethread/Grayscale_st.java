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
        static BufferedImage[] image = new BufferedImage[5];
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
                        for (int i = 0; i < 5; i++){
                                image[i] = ImageIO.read(new File(s[i]));
                        }
                        System.out.println("Read successful.");
                }
                catch (IOException e)
                {
                        System.out.println(e);
                }
                        
                long duration[] = new long[5];

                System.out.println("Processing images...");
                for(int i = 0; i < 5; i++){
                        long start=System.currentTimeMillis();

                        rgb_to_gray t1 = new rgb_to_gray(image[i], 0, image[i].getWidth());

                        t1.start();
                        t1.join();
                
                        long stop=System.currentTimeMillis();
                        duration[i] = stop-start;
                }
                System.out.println("Process successful");
        
                 //save processed images
                try
                {
                        System.out.println("Saving processed images...");
                        for(int i = 0; i < 5; i++){
                                ImageIO.write(image[i], "jpg", new File("../Processed_Images/Grayscale_ST_" + name[i]));
                                // System.out.println("End, saved " + name[i]); 
                        }
                        System.out.println("Save successful");
                }
                catch (IOException e)
                {
                        System.out.println(e);
                }
             
                String content[] = new String[5];

                try {
                        File file = new File("../Execution_Time/Grayscale_ST_Execution_Timelog.txt");

                        if (!file.exists()) {
                                file.createNewFile();
                        }

                        System.out.println("Saving timelog...");
                        for (int i = 0; i < 5; i++){
                                content[i] = name[i] + ": processed at " + duration[i] + "ms";

                                FileWriter fw = new FileWriter(file, true);
                                BufferedWriter br = new
                                BufferedWriter(fw);
                                br.write(content[i]);
                                br.newLine();
                                // System.out.println("content >> " + content[i]);
                                // System.out.println("File " + name[i] + " written Successfully");
                                br.close();
                                fw.close();
                        }
                        System.out.println("Timelog save successful.");
                } 

                catch (IOException ioe) {
                        ioe.printStackTrace();
                }

                finally
                { 
                }  
        }
}
