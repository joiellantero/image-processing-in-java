//based on code found on https://l.facebook.com/l.php?u=https%3A%2F%2Fpastebin.com%2FzuD9JTxn%3Ffbclid%3DIwAR0X99GVR40fP6y0wubSRjNfKeiyPnsreXWlJZ_9yW2NS2VT7FTJ9Pt3YY0&h=AT2fMs7VHjlspC2LaZeQB_HkRRdCcC6OS9vRFu-9GUotwfOvNXNQ4-dAfBwXbMlVQk8EkOIlnW4cMjuYbwfq40HRJz6hxzInMDT8KRmH-dwXtNmyRCK0gsX7tVMSRb4-64v8skehN2-3h_LvHymyIfuEvFQ; and https://pastebin.com/FBZzsG2i?fbclid=IwAR3jR9Ap1yDj67IEDE-BJgFLpEiVa3tkVvP9HNpaHvrlz2XAQwSVhVTiros

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
 
import javax.imageio.ImageIO;

class numPhoto {
    public static int num = 5;
}
 
class rgb_to_green extends Thread{
       
    private int x;
    private int x_end;
    BufferedImage image;
    Color bw_color;

    public void run() {
        for (int i=x; i<x_end; i++){
            for (int j=0; j<image.getHeight(); j++){
        
                int rgb = image.getRGB(i,j); 

                int a = (rgb>>24)&0xff; 
                int g = (rgb>>8)&0xff; 

                // set new RGB (blue still the same and then 0 for red and green)
                rgb = (a<<24) | (0<<16) | (g<<8) | 0; 

                image.setRGB(i, j, rgb); 
            }
        }
    }
    rgb_to_green(BufferedImage image, int x, int x_end)
    {
        this.x = x;
        this.x_end = x_end;
        this.image = image;
    }       
}
public class Green_st
{      
    static int w_total = 0;
    static int h_total = 0;
    static BufferedImage[] image = new BufferedImage[numPhoto.num];
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
                // System.out.println(fileEntry.getName());
            }
            i++;
        }
    }
   
    public static void main( String[] args ) throws InterruptedException
    {
        final File folder = new File("../Raw_Image");
        Blue_st listFiles = new Blue_st();
        listFiles.listFilesForFolder(folder);
     
        //read raw images
        try 
        {
            System.out.println("Reading Images...");
            for (int i = 0; i < numPhoto.num; i++){
                image[i] = ImageIO.read(new File(s[i]));
            }
            System.out.println("Read successful.");
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
            
        long duration[] = new long[numPhoto.num];

        System.out.println("Processing images...");
        for(int i = 0; i < numPhoto.num; i++){
            long start=System.currentTimeMillis();
        
            rgb_to_green t1 = new rgb_to_green(image[i], 0, image[i].getWidth());
            
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
            for(int i = 0; i < numPhoto.num; i++){
                ImageIO.write(image[i], "jpg", new File("../Processed_Images/Blue_ST_" + name[i]));
                // System.out.println("End, saved " + name[i]); 
            }
            System.out.println("Save successful");
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        
        

        BufferedWriter bw = null;

        try {
                String content = duration + "ms";
                
                File file = new File("../Execution_Time/Green_ST_Execution_Timelog.txt");

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
