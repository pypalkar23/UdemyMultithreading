package com.latency;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;


public class Main {
    public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./resources/many-flowers-out.jpg";

    public static void main(String[] args) throws IOException,InterruptedException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        //recolorSingleThreaded(originalImage,resultImage);
        long startTime = System.currentTimeMillis();
        recolorMultiThreaded(originalImage,resultImage,6);
        long endTime = System.currentTimeMillis();
        System.out.println("Runtime "+ (endTime-startTime));
        File outputFile = new File(DESTINATION_FILE);
        ImageIO.write(resultImage,"jpg",outputFile);
    }

    public static void recolorMultiThreaded(BufferedImage originalImage, BufferedImage resultImage, int noOfThreads) throws InterruptedException{
        List<Thread> threads = new ArrayList<>();
        int width= originalImage.getWidth();
        int height = originalImage.getHeight()/noOfThreads;

        for (int i=0;i<noOfThreads;i++){
            final int threadMultiplier = i;

            Thread thread = new Thread(()->{
                int leftCorner = 0;
                int topCorner = height * threadMultiplier;

                recolorImage(originalImage,resultImage,leftCorner,topCorner,width,height);
            });

            threads.add(thread);
        }

        for(Thread thread:threads)
            thread.start();

        for(Thread thread:threads)
            thread.join();
    }
    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage,resultImage,0,0,originalImage.getWidth(),originalImage.getHeight());
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner, int width, int height) {
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++)
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage,resultImage,x,y);
            }
    }

    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed;
        int newGreen;
        int newBlue;

        if (isShadeOfGrey(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(resultImage, x, y, newRGB);
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }


    public static int getBlue(int color) {
        return color & 0x000000FF;
    }

    public static int getGreen(int color) {
        return (color & 0x0000FF00) >> 8;
    }

    public static int getRed(int color) {
        return (color & 0xFF0000) >> 16;
    }

    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        rgb |= 0xFF000000;

        return rgb;
    }


    public static boolean isShadeOfGrey(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(green - blue) < 30 && Math.abs(blue - red) < 30;
    }

}
