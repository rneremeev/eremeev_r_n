/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeev_r_n;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author feels
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        initComponents();
    }

    private Mat source;
    private Mat flippedSrc;
    private Mat destination;
    private Rain rain;
    private Mat[] imgparts;
    
    private int blur = 1;
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSlider1 = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Капли дождя ");

        jSlider1.setMajorTickSpacing(4);
        jSlider1.setMaximum(17);
        jSlider1.setMinimum(1);
        jSlider1.setMinorTickSpacing(2);
        jSlider1.setPaintTicks(true);
        jSlider1.setValue(1);
        jSlider1.setEnabled(false);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel1.setText("Уровень размытия");

        jButton1.setText("Открыть");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Сохранить");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jButton1)
                .addGap(3, 3, 3)
                .addComponent(jButton2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton2)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JFileChooser fileopen = new JFileChooser(new File(System.getProperty("user.dir")));
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            String path = fileopen.getSelectedFile().getPath();
            source = Highgui.imread(path, Highgui.CV_LOAD_IMAGE_COLOR);
            //Конечная картинка
            destination = new Mat(source.rows(), source.cols(), source.type());
            flippedSrc = new Mat(source.rows(), source.cols(), source.type());
            source.copyTo(destination);
             //Перевернуть оригинал
            Core.flip(source, flippedSrc, -1);
            Core.flip(flippedSrc, flippedSrc, 1);

            //Разделить на 2 части
            //imgparts = getImgparts(source);
            //Капли
            rain = RainGenerator.generate(50, destination.width(), destination.height());
            //Отражения
            imgparts = getImgparts(flippedSrc);
            drawReflection(imgparts, destination, rain, blur);
            
            displayImage(mat2BufferedImage(destination), jLabel2);
            jSlider1.setEnabled(true);
            jButton2.setEnabled(true);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        JFileChooser fileopen = new JFileChooser(new File(System.getProperty("user.dir")));
        int ret = fileopen.showDialog(null, "Сохранить файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            String path = fileopen.getSelectedFile().getPath();
            Highgui.imwrite(path, destination);
            JOptionPane.showMessageDialog(null, "Сохранено");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        // TODO add your handling code here:
        JSlider src = (JSlider) evt.getSource();
        if (!src.getValueIsAdjusting()) {
            int val = (int) src.getValue();
            
            blur = val;
            drawReflection(imgparts, destination, rain, blur);
            
            displayImage(mat2BufferedImage(destination), jLabel2);
        }
    }//GEN-LAST:event_jSlider1StateChanged
    
    //Поделить на 2 изображения
    private Mat[] getImgparts(Mat src) {
        Mat[] res = new Mat[2];
        int w = src.width();
        int h = src.height();
        
        for (int i = 0; i < res.length; i++) {
            Rect rectROI;
            switch (i) {
                case 0:
                    rectROI = new Rect(0, 0, w/2, h);
                    break;
                default:
                    rectROI = new Rect(w/2, 0, w/2, h);
                    break;
                    
            }
            res[i] = new Mat(src, rectROI);
        }
        return res;
    }
    
    private void drawReflection(Mat[] src, Mat dst, Rain rain, int blurlvl) {
        try {
            //Перевернутый оригинал на каждую каплю с размытием
            Mat backreflect = new Mat(dst.rows(), dst.cols(), dst.type());
            double alpha = 0.45;
            for (int i = 0; i < rain.size; i++) {
                drawIntoArea(src[rain.imgpart.get(i)], backreflect, (int) rain.topleft.get(i).x,
                        (int) rain.topleft.get(i).y, rain.radius.get(i) * 2, rain.radius.get(i) * 2, blurlvl);
            }
            Core.addWeighted(source/*[rain.imgpart.get(i)]*/, alpha, backreflect, 1 - alpha, 0, backreflect);

            //Контуры капель на маску
            Rect rectROI = new Rect(0, 0, dst.cols(), dst.rows());
            Mat mask = new Mat(dst.rows(), dst.cols(), dst.type());
            for (int i = 0; i < rain.size; i++) {
                Imgproc.drawContours(mask, rain.points, i, new Scalar(255, 255, 255), -1);
            }
            
            Mat srcROI = new Mat(backreflect, rectROI);
            Mat dstROI = new Mat(dst, rectROI);
            Mat dst1 = new Mat();
            Mat dst2 = new Mat();

            srcROI.copyTo(dst1, mask);

            Core.bitwise_not(mask, mask);
            dstROI.copyTo(dst2, mask);

            dstROI.setTo(new Scalar(0, 0, 0));
            //Сложить маску и оригинал
            Core.add(dst1, dst2, dstROI);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawIntoArea(Mat src, Mat dst, int x, int y, int width, int height, int blurlvl) {
        if (x < 0 || y < 0 || width < 0 || height < 0) {
            return;
        }
        try {
            Mat scaledSrc = new Mat();
            Imgproc.resize(src, scaledSrc, new Size(width, height), 1, 1, Imgproc.INTER_AREA);

            Mat ROI = new Mat(dst, (new Rect(x, y, scaledSrc.rows(), scaledSrc.cols())));
            scaledSrc.copyTo(ROI);
            if (blurlvl < 0) {
                blurlvl = 1;
            }
            if (blurlvl % 2 == 0) {
                blurlvl++;
            }
            Imgproc.GaussianBlur(ROI, ROI, new Size(blurlvl, blurlvl), 0);
        } catch (Exception e) {

        }
    }

    private BufferedImage mat2BufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    private void displayImage(BufferedImage img2, JLabel label) {
        ImageIcon icon = new ImageIcon(img2);
        label.setIcon(icon);
        ((JFrame) label.getTopLevelAncestor()).pack();
        label.revalidate();
        label.repaint();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSlider jSlider1;
    // End of variables declaration//GEN-END:variables
}