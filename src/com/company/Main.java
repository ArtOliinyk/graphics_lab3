package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    static int getDistance(Point dot1, Point dot2) {
        return (dot2.x - dot1.x) * (dot2.x - dot1.x) + (dot2.y - dot1.y) * (dot2.y - dot1.y);
    }

    static long getVectorMultiplication(Point dot1, Point dot2, Point dot3) {
        return (long) (dot2.x - dot1.x) * (dot3.y - dot1.y) - (long) (dot3.x - dot1.x) * (dot2.y - dot1.y);
    }

    static void FindConvexHull() throws IOException{
        int j = 0;
        Point point = new Point(0, 0);
        for(int i = 0; i < points.length; i++){
            if(points[i].x > point.x)
                point.x = points[i].x;
        }
        for(int i = 0; i < points.length; i++){
            if(points[i].y > point.y && points[i].x == point.x){
                point.y = points[i].y;
                j = i;
            }
        }
        convexHullPoints[0] = point;
        points[j] = points[0];
        points[0] = convexHullPoints[0];
        j = 0;
        int min = 0;
        do{
            for(int i = 0; i < points.length; i++){
                if(getVectorMultiplication(convexHullPoints[j], points[min], points[i]) < 0 ||
                        getVectorMultiplication(convexHullPoints[j], points[min],  points[i]) == 0
                                && getDistance(convexHullPoints[j], points[min]) < getDistance(convexHullPoints[j], points[i])){
                    min = i;
                }
            }
            j++;
            convexHullPoints[j] = points[min];
            min = 0;
        }
        while (!(convexHullPoints[j].x == convexHullPoints[0].x && convexHullPoints[j].y == convexHullPoints[0].y));
    }
    private static final String FILE = "DS3.txt";
    private static Point[] points;
    private static Point[] convexHullPoints;

    public static void main(final String[] args) throws IOException {
        try{
            final BufferedReader br = new BufferedReader(new FileReader(new File(FILE)));
            points = new Point[22159];
            int i = 0;
            while(br.ready()){
                final String[] split = br.readLine().split(" ");
                final int x = Integer.parseInt(split[0]);
                final int y = Integer.parseInt(split[1]);
                points[i++] = new Point(x, y);
            }
        } catch (final Exception e){
            e.printStackTrace();
        }
        convexHullPoints = new Point[22160];
        FindConvexHull();
        BufferedImage image = new BufferedImage(540, 960, ColorSpace.TYPE_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setPaint (new Color(255, 255, 255) );
        for(int i = 0; i < points.length; i++)
        {
            graphics.fillRect(points[i].x, points[i].y, 1, 1);
        }
        graphics.setPaint(new Color(0, 0, 255));
        for(int i = 0; i < points.length; i++)
        {
            if(convexHullPoints[i+1] != null)
                graphics.drawLine(convexHullPoints[i].x, convexHullPoints[i].y, convexHullPoints[i + 1].x, convexHullPoints[i + 1].y);
        }
        File outFile = new File("lab3_image.png");
        ImageIO.write(image, "png", outFile);
    }
}
