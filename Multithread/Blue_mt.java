//based on code found on https://l.facebook.com/l.php?u=https%3A%2F%2Fpastebin.com%2FzuD9JTxn%3Ffbclid%3DIwAR0X99GVR40fP6y0wubSRjNfKeiyPnsreXWlJZ_9yW2NS2VT7FTJ9Pt3YY0&h=AT2fMs7VHjlspC2LaZeQB_HkRRdCcC6OS9vRFu-9GUotwfOvNXNQ4-dAfBwXbMlVQk8EkOIlnW4cMjuYbwfq40HRJz6hxzInMDT8KRmH-dwXtNmyRCK0gsX7tVMSRb4-64v8skehN2-3h_LvHymyIfuEvFQ; and https://pastebin.com/FBZzsG2i?fbclid=IwAR3jR9Ap1yDj67IEDE-BJgFLpEiVa3tkVvP9HNpaHvrlz2XAQwSVhVTiros

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
 
import javax.imageio.ImageIO;

class Photo {
    public static int num = 1000;
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
    static BufferedImage image;
    static int totalTime = 0;
    static String[] s = new String[1000];
    static String[] name = new String[1000];
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

        final File folder = new File("./test_images/");
        Blue_mt listFiles = new Blue_mt();
        listFiles.listFilesForFolder(folder);

        long duration[] = new long[Photo.num];

        for(int i = 0; i < Photo.num; i++){
		try 
		{
		    image = ImageIO.read(new File(s[i]));
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
                t1.join();
                t2.join();
                t3.join();
                t4.join();
        
                long stop=System.currentTimeMillis();
                duration[i] = stop-start;

		try
		{
		    ImageIO.write(image, "jpg", new File("./processed_images/Blue_MT_" + name[i]));
		}
		catch (IOException e)
		{
		    System.out.println(e);
		}
        }
        System.out.println("Process successful");
        

        String content[] = new String[Photo.num];

        try {
            File file = new File("./Execution_Time/Blue_MT_Execution_Timelog.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            System.out.println("Saving timelog...");
            for (int i = 0; i < Photo.num; i++){
                content[i] = String.valueOf(duration[i]);//name[i] + ": processed at " + duration[i] + "ms";

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
            // try{
            //     if(br!=null){
            //         br.close();
            //     }
            // }
            // catch(Exception ex){
            //     System.out.println("Error in closing the BufferedWriter"+ex);
            // }
        }  
    }
}
