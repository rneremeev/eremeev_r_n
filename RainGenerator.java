
package eremeev_r_n;

import java.util.LinkedList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

/**
 *
 * @author feels
 */
public class RainGenerator {

    private static Node setLinePoints(int iterations) {
        Node first = new Node();
        first.point = new Point(0, 1);
        Node next = new Node();
        next.point = new Point(1, 1);
        first.next = next;

        double minY = 1;
        double maxY = 1;
        Node point;
        Node nextPoint;
        double dx, newX, newY;

        for (int i = 0; i < iterations; i++) {
            point = first;
            while (point.next != null) {
                nextPoint = point.next;

                dx = nextPoint.point.x - point.point.x;
                newX = 0.5 * (point.point.x + nextPoint.point.x);
                newY = 0.5 * (point.point.y + nextPoint.point.y);
                newY += dx * (Math.random() * 2 - 1);

                Node newPoint = new Node();
                newPoint.point = new Point(newX, newY);
     
                if (newY < minY) {
                    minY = newY;
                } else if (newY > maxY) {
                    maxY = newY;
                }

                //put between points
                newPoint.next = nextPoint;
                point.next = newPoint;

                point = nextPoint;
            }
        }

        //normalize to values between 0 and 1
        if (maxY != minY) {
        double normalizeRate = 1 / (maxY - minY);
        point = first;
        while (point.next != null) {
            point.point.y = normalizeRate * (point.point.y - minY);
            point = point.next;
        }

        }
        return first;
    }

    public static Rain generate(int numCircles, int w, int h) {
        Rain res = new Rain(numCircles);
        int w_half = w/2;
        int h_half = h/2;
        for (int i = 0; i < numCircles; i++) {
            int maxRad = (int)(Math.random()*20);
            int minRad = (int)(0.5*maxRad);
            
            int centerX = (int) (maxRad + Math.random() * (w - 2 * maxRad));
            int centerY = (int) (maxRad + Math.random() * (h - 2 * maxRad));

            double phase = Math.random() * Math.PI * 2;

            res.add(drawCircle(centerX, centerY, minRad, maxRad, phase, w_half, h_half));
        }
        return res;
    }

    private static Drop drawCircle(int centerX, int centerY, int minRad, int maxRad, double phase, int w, int h) {
        List<Point> res = new LinkedList<Point>();
        double rad, theta;
        double twoPi = 2 * Math.PI;

        Node pointList = setLinePoints(9);

        theta = phase;
        rad = minRad + pointList.point.y * (maxRad - minRad);
        int x0 = (int) (centerX + rad * Math.cos(theta));
        int y0 = (int) (centerY + rad * Math.sin(theta));
        //Point prevpt = new Point(x0, y0);
        Point startpt = new Point(x0, y0);
        res.add(startpt);
        
        int xtl = Integer.MAX_VALUE;
        int ytl = Integer.MAX_VALUE;
        double maxradius = -1;
        int imgpart = 0;
        while (pointList.next != null) {
            theta = twoPi * pointList.point.x + phase;
            rad = minRad + pointList.point.y * (maxRad - minRad);
            
            x0 = (int)(centerX + rad * Math.cos(theta));
            y0 = (int)(centerY + rad * Math.sin(theta));
            if (x0 < xtl) xtl = x0;
            if (y0 < ytl) ytl = y0;
            double radius = Math.sqrt((x0-centerX)*(x0-centerX) + (y0-centerY)*(y0-centerY));
            if (radius > maxradius) maxradius = radius;
           
            Point nextpt = new Point(x0, y0);
            res.add(nextpt);
            //Core.line(img, prevpt, nextpt, new Scalar(0, 0, 0), 2);
            pointList = pointList.next;
            //prevpt = nextpt;
        }
        //Core.line(img, prevpt, startpt, new Scalar(0, 0, 0), 2);
        MatOfPoint m = new MatOfPoint();
        m.fromList(res);
        
        imgpart = centerX < w ? 0 : 1;
     
        Drop matofpt = new Drop(m, new Point(xtl, ytl), (int)maxradius, imgpart);
        return matofpt;
    }
}
