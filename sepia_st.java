import java.io.File; 
import java.io.IOException; 
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO; 

class sepia{
	void rgbtosepia(BufferedImage image, int height, int width){
		File f = null;
		//Define the starting time
    	long start = System.currentTimeMillis();
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
            f = new File("lena_sepia_multi.jpg"); 
            ImageIO.write(image, "jpg", f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }
		//Define how long it took
		long end = System.currentTimeMillis();
		float time = (end-start)/1000F;
    	System.out.println(time + "seconds"); 
		//System.out.println("Done.");

	}
}

class MyThread extends Thread{
	sepia ref;
	BufferedImage image;
	int height;
	int width;
	MyThread(sepia p, BufferedImage image_in, int height_in, int width_in){
		ref = p;
		image = image_in;
		height = height_in;
		width = width_in;
	}
	@Override
	public void run(){
		ref.rgbtosepia(image, height, width);
	}
}

public class sepia_st
{ 
    public static void main(String args[])throws IOException 
    { 
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
  
		// get image width and height 
        int width = image.getWidth(); 
        int height = image.getHeight(); 
  
        sepia new_img = new sepia();
		MyThread nRef = new MyThread(new_img, image, height, width);
		nRef.start();
  
    } 
} 
