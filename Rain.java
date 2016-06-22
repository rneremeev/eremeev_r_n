/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eremeev_r_n;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

/**
 *
 * @author feels
 */
public class Rain {
    public List<MatOfPoint> points;
    public List<Point> topleft;
    public List<Integer> radius;
    public List<Integer> imgpart; 
    public int size;
    
    public Rain(int n) {
        points = new ArrayList<MatOfPoint>(n);
        topleft = new ArrayList<Point>(n);
        radius = new ArrayList<Integer>(n);
        imgpart = new ArrayList<Integer>(n);
        size = n;
    }
    
    public void add(Drop drop) {
        points.add(drop.points);
        topleft.add(drop.topleft);
        radius.add(drop.radius);
        imgpart.add(drop.imgpart);
    }
}
