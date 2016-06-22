/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eremeev_r_n;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

/**
 *
 * @author feels
 */
public class Drop {
    public MatOfPoint points;
    public Point topleft; 
    public int radius;
    public int imgpart;
    
    public Drop(MatOfPoint points, Point topleft, int radius, int imgpart) {
        this.points = points;
        this.topleft = topleft;
        this.radius = radius;
        this.imgpart = imgpart;
    }
}
