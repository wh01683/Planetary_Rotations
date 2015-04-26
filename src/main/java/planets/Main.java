package planets;

import com.jogamp.opengl.GLCapabilities;

import javax.swing.*;

/**
 * Created by robert on 4/25/2015.
 */
public class Main {

    public static void main(String[] args){

        Planet planets = new Planet(1200, 800, initCapabilities());
        JFrame frame = new JFrame("Planets Demo");
        frame.getContentPane().add(planets);
        frame.setSize(planets.getSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        planets.requestFocus();


    }

    public static GLCapabilities initCapabilities(){
        GLCapabilities glCapabilities = new GLCapabilities(null);
        glCapabilities.setRedBits(8);
        glCapabilities.setAlphaBits(8);
        glCapabilities.setBlueBits(8);
        glCapabilities.setGreenBits(8);

        return glCapabilities;
    }
}
