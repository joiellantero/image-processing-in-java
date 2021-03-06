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
		            rgb = (a<<24) | (r<<16) | (g<<8) | b; 
	  
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

public class Sepia_st
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
        Sepia_st listFiles = new Sepia_st();
        listFiles.listFilesForFolder(folder);

       long duration[] = new long[Photo.num];

        System.out.println("Processing images...");
        for(int i = 0; i < Photo.num; i++){
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
            // System.out.println(name[i] + ": processed at " + (stop-start) + "ms");
            duration[i] = stop-start;
			//save processed images
		    try
		    {
		        ImageIO.write(image, "jpg", new File("./processed_images/Sepia_ST_" + name[i]));
		    }
		    catch (IOException e)
		    {
		        System.out.println(e);
		    }        
        }
        System.out.println("Process successful");

        String content[] = new String[Photo.num];

        try {
            File file = new File("./Execution_Time/Sepia_ST_Execution_Timelog.txt");

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
            
        }  
    }
}
