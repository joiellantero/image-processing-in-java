//based on code found on https://l.facebook.com/l.php?u=https%3A%2F%2Fpastebin.com%2FzuD9JTxn%3Ffbclid%3DIwAR0X99GVR40fP6y0wubSRjNfKeiyPnsreXWlJZ_9yW2NS2VT7FTJ9Pt3YY0&h=AT2fMs7VHjlspC2LaZeQB_HkRRdCcC6OS9vRFu-9GUotwfOvNXNQ4-dAfBwXbMlVQk8EkOIlnW4cMjuYbwfq40HRJz6hxzInMDT8KRmH-dwXtNmyRCK0gsX7tVMSRb4-64v8skehN2-3h_LvHymyIfuEvFQ; and https://pastebin.com/FBZzsG2i?fbclid=IwAR3jR9Ap1yDj67IEDE-BJgFLpEiVa3tkVvP9HNpaHvrlz2XAQwSVhVTiros

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
import javax.imageio.ImageIO;
 
class Singlethread extends Thread{
       
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
        Singlethread(BufferedImage image, int x, int x_end)
        {
                this.x = x;
                this.x_end = x_end;
                this.image = image;
        }       
}

public class Blue_st
{      
    static int w_total = 0;
    static int h_total = 0;
    static BufferedImage image = null;
    static int totalTime = 0;
    static String[] s = new String[100];
    static String[] name = new String[20];
    static int i = 0;
   
    public static void main( String[] args ) throws InterruptedException
    {

        final File folder = new File("../Raw_Image");
        Blue_st listFiles = new Blue_st();
        listFiles.listFilesForFolder(folder);

        //read raw images
        try
        {
            image = ImageIO.read(new File(s[i]));
        }

        
        catch (IOException e)
        {
            System.out.println(e);
        }
            
        long start=System.currentTimeMillis();
        
        Singlethread t1 = new Singlethread(image, 0, image.getWidth());

        t1.start();
        t1.join();

        long stop=System.currentTimeMillis();

        //save processed images
        try
        {
            ImageIO.write(image, "jpg", new File("../Processed_Images/" + name[i]));
            System.out.println("End, saved");
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        
        System.out.println("Total time: " + (stop-start));
    }
   
    public void listFilesForFolder(final File folder){
        for (final File fileEntry : folder.listFiles()){
            if (fileEntry.isDirectory()){
                listFilesForFolder(fileEntry);
            }

            else{
                s[i] = fileEntry.getPath();
                name[i] = fileEntry.getName();
                System.out.println(fileEntry.getName());
            }
        }
        i++;
    }
}
