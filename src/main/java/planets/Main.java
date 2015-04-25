package planets;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by robert on 4/25/15.
 */
public class Main extends GLJPanel{

    public static void main(String[] args){

        JFrame window = new JFrame("Planets");
        GLCapabilities capabilities = new GLCapabilities(null);
        Planet planets = new Planet(capabilities);
        window.getContentPane().add(planets, BorderLayout.CENTER);

        window.pack();
        window.setLocation(50, 50);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        planets.requestFocusInWindow();
   }


}
