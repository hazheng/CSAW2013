import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class stitch {

    public static void main(String[] args) {
	    ArrayList<String> files = count();
        int start=0;
        for(int i=0;i<files.size()&&!(files.get(i).equals(new String("333797")));i++){
            start++;
        }
        int[] data = getData(files.get(start));
        files.remove(start);
        int[] t1;
        int[][] avg = genAvg();
        int temp =0;
        int min = Integer.MAX_VALUE;
        int index = -1;
        for(int l=0;l<3;l++){
            System.out.printf("Line: %d",(avg.length/64)*l);
            index = -1;
            min = Integer.MAX_VALUE;
            for(int i=0;i<files.size();i++){

                t1 = getData(files.get(i));
                t1 = cat(data,t1);
                temp = eval(genImg(t1),avg,l+1);
                System.out.printf("(%d: %d)",i,temp);
                if(temp<min)
                {
                    min = temp;
                    index = i;
                }
            }
            if(index!=-1){
                System.out.printf("\nFile %s Val %d\n",files.get(index),min);
                data = cat(data,getData(files.get(index)));
                files.remove(index);
            }
        }
        System.out.printf("DONE");
        fwrite("test.jpg",data);

    }

    public static void fwrite(String fname,int[] data){
        FileOutputStream fout = null;
        try{
            fout = new FileOutputStream(new File(fname));
        }
        catch (IOException e){
            System.out.printf("FUCK\n");
        }
        for(int i=0;i<data.length;i++){
            try{
                fout.write(data[i]);
            }
            catch (IOException e){
                System.out.printf("FUCK\n");
            }
        }
        try{
            fout.close();
        }
        catch (IOException e){
            System.out.printf("FUCKER\n");
        }
    }

    public static int[] getData(String fname){
        File myFile = new File(fname);
        FileInputStream fin = null;
        ArrayList<Integer> data = new ArrayList<Integer>();
        int temp =0;
        try{
            fin = new FileInputStream(myFile);
        }
        catch (IOException e){
            return null;
        }
        while(temp!=-1){
            try{
                temp = fin.read();
            }
            catch (IOException e){
                return null;
            }
            if(temp!=-1)
                data.add(temp);
        }
        try{
            fin.close();
        }
        catch (IOException e){

        }
        int[] result = new int[data.size()];
        for(int i=0;i<data.size();i++){
            result[i] = data.get(i);
        }
        return result;
    }

    public static BufferedImage genImg(int[] data){
        fwrite("temppic.jpg",data);
        BufferedImage img = null;
        try{
            img = ImageIO.read(new File("temppic.jpg"));
        }
        catch (IOException e){
            return null;
        }
        return img;
    }

    public static ArrayList<String> count(){
        String temp;
        ArrayList<String> files = new ArrayList<String>();
        FileInputStream fin = null;
        File myfile = null;

        for(int i=107076;i<497217;i++){
            temp = Integer.toString(i);
            try{
                myfile = new File(temp);
                fin = new FileInputStream(myfile);
                files.add(temp);
            }
            catch (FileNotFoundException e){

            }
        }
        System.out.printf("Program has loaded %d files\n",files.size());
        return files;
    }

    public static int[][] genAvg(){
        BufferedImage img = null;
        Color pix = null;
        int height,width,r,g,b;
        try{
            img = ImageIO.read(new File("tumb.jpg"));
        }
        catch (IOException e){
        }
        height = img.getHeight();
        width = img.getWidth();
        int[][] avg = new int[height][3];
        for(int i=0;i<height;i++){
            r=0;g=0;b=0;
            for(int j=0;j<width;j++){
                pix = new Color(img.getRGB(j,i));
                r+= pix.getRed();
                g+= pix.getGreen();
                b+= pix.getBlue();
            }
            r/=width;
            g/=width;
            b/=width;
            avg[i][0]=r;
            avg[i][1]=g;
            avg[i][2]=b;
        }
        return  avg;
    }

    public static int eval(BufferedImage img, int[][] avg,int num){
        int height = img.getHeight();
        int width = img.getWidth();
        int val = 0;
        Color pix = null;
        int[] diff = new int[3];
        int x, y;
        int interval = height/avg.length;
        for(int i=(avg.length/64)*(num-1);i<(avg.length/64)*num;i++){
            diff[0] = 0; diff[1]=0; diff[2]=0;
            x=0;y=0;
            for (int j=0;j<interval&&i*interval+j<img.getHeight();j++){
                for (int k=0;k<img.getWidth();k++){
                    pix = new Color(img.getRGB(k,i*interval+j));
                    diff[0] += pix.getRed();
                    diff[1] += pix.getGreen();
                    diff[2] += pix.getBlue();
                    x++;
                }
                y++;
            }
            for (int j=0;j<avg[1].length;j++){
                diff[j] = diff[j]/(img.getWidth()*interval);
                val += Math.abs(diff[j]-avg[i][j]);
            }
        }
        return val;
    }

    public static int[] cat(int[] a, int[] b){
        int[] c = new int[a.length+b.length];
        for(int i=0;i<a.length;i++){
            c[i]=a[i];
        }
        for (int i=0;i<b.length;i++){
            c[a.length+i]=b[i];
        }
        return c;
    }
}
