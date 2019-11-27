//based on code found on https://github.com/yusufshakeel/Java-Image-Processing-Project/blob/master/example/Sepia.java
// Java program to demonstrate colored to red colored image conversion 
import java.io.File; 
import java.io.IOException; 
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO; 

class Singlethread extends Thread{
    public void run(){
        BufferedImage image = null; 
        File f = null; 
  
        // read image 
        try
        { 
            f = new File("lena.jpg"); 
            image = ImageIO.read(f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        } 

        //Define the starting time
        long start = System.currentTimeMillis();
  
        // get image width and height 
        int width = image.getWidth(); 
        int height = image.getHeight(); 
  
        for (int y = 0; y < height; y++){ 
            for (int x = 0; x < width; x++){ 
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
  
        // write image 
        try
        { 
            f = new File("lena_sepia_st.jpg"); 
            ImageIO.write(image, "jpg", f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        } 

        //Define how long it took
        long end = System.currentTimeMillis();
        float time = (end-start)/1000F;
        System.out.println(time + " seconds"); 

    }
}
  
public class Sepia_st_v2
{ 
    public static void main(String args[])throws IOException 
    { 
        // //Define the starting time
        // long start = System.currentTimeMillis();

        Singlethread object = new Singlethread();
        object.start();

        //  //Define how long it took
        //  long end = System.currentTimeMillis();
        //  float time = (end-start)/1000F;
        //  System.out.println(time + " seconds"); 
    } 
} 
