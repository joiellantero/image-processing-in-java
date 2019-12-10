// package image_convert.bmp;
package image.multithread; 
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class Blue_mt extends RecursiveAction {
    private int[] mSource;
    private int mStart;
    private int mLength;
    private int[] mDestination;
  
    // Processing window size; should be odd.
    private int mBlueWidth = 15;
  
    public Blue_mt(int[] src, int start, int length, int[] dst) {
        mSource = src;
        mStart = start;
        mLength = length;
        mDestination = dst;
    }

    protected void computeDirectly() {
        int sidePixels = (mBlueWidth - 1) / 2;
        for (int index = mStart; index < mStart + mLength; index++) {
            // Calculate average.
            float rt = 0, gt = 0, bt = 0;
            for (int mi = -sidePixels; mi <= sidePixels; mi++) {
                int mindex = Math.min(Math.max(mi + index, 0),
                                    mSource.length - 1);
                int pixel = mSource[mindex];
                rt += (float)((pixel & 0x00ff0000) >> 16) / mBlueWidth;
                gt += (float)((pixel & 0x0000ff00) >>  8) / mBlueWidth;
                bt += (float)((pixel & 0x000000ff) >>  0) / mBlueWidth;
            }
          
            // Reassemble destination pixel.
            int dpixel = (0xff000000     ) |
                   (((int)rt) << 16) |
                   (((int)gt) <<  8) |
                   (((int)bt) <<  0);
            mDestination[index] = dpixel;
        }
    }

    protected static int sThreshold = 100000;

    @Override 
    protected void compute() {
        if (mLength < sThreshold) {
            computeDirectly();
            return;
        }
    
        int split = mLength / 2;
    
        invokeAll(new Blue_mt(mSource, mStart, split, mDestination),
                  new Blue_mt(mSource, mStart + split, mLength - split, mDestination));
    }
    public static void main(String args[])throws IOException 
    { 
        String srcName = "../Raw_Image/lena.jpg";
        File srcFile = new File(srcName);
        BufferedImage image = ImageIO.read(srcFile);

        BufferedImage blueImage = blue(image);

        String dstName = "../Processed_Images/lena_blue.jpg";
        File dstFile = new File(dstName);
        ImageIO.write(blueImage, "jpg", dstFile);

    }  
    
    public static BufferedImage blue(BufferedImage srcImage) {
        int w = srcImage.getWidth();
        int h = srcImage.getHeight();
 
        int[] src = srcImage.getRGB(0, 0, w, h, null, 0, w);
        int[] dst = new int[src.length];
 
        Blue_mt b = new Blue_mt(src, 0, src.length, dst);
 
        ForkJoinPool pool = new ForkJoinPool();
 
        long start = System.currentTimeMillis();
        pool.invoke(b);
        long end = System.currentTimeMillis();
 
        float time = (end-start)/1000F;
        System.out.println("Elapsed time: " + time + " s"); 
 
        BufferedImage dstImage =
                new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        dstImage.setRGB(0, 0, w, h, dst, 0, w);
 
        return dstImage;
    }
}

