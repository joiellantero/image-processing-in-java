// Java program to demonstrate colored to red colored image conversion 
import java.io.File; 
import java.io.IOException; 
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO; 
  
public class Sepia
{ 
    public static void main(String args[])throws IOException 
    { 
        BufferedImage img = null; 
        File f = null; 
  
        // read image 
        try
        { 
            f = new File("lena.jpg"); 
            img = ImageIO.read(f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        } 
  
        // get width and height 
        int width = img.getWidth(); 
        int height = img.getHeight(); 
  
        // convert to red image 
        for (int y = 0; y < height; y++) 
        { 
            for (int x = 0; x < width; x++) 
            { 
                int p = img.getRGB(x,y); 
  
                int a = (p>>24)&0xff; 
                int r = (p>>16)&0xff;
				int g = (p>>8)&0xff; 
                int b = p&0xff;  
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
                // set new RGB 
                // keeping the r value same as in original 
                // image and setting g and b as 0. 
                p = (a<<24) | (r<<16) | (g<<8) | b; 
  
                img.setRGB(x, y, p); 
            } 
        } 
  
        // write image 
        try
        { 
            f = new File("lena_sepia.jpg"); 
            ImageIO.write(img, "jpg", f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        } 
    } 
} 
