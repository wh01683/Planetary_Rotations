package planets;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by robert on 4/25/2015.
 */
public class Planet {

    private static GLU Glu;
    private int lats, longs;
    private float radius, angle, orbitRadius, rotationFactor;


    private static final float[] WHITE = {1f, 1f, 1f};
    Random r = new Random();
    private ArrayList<Moon> moons = new ArrayList<Moon>(3);

    public Planet(float moons, float radius, float angle, float orbitRadius, int lats, int longs,
                  float rotationFactor){

        this.radius = radius;
        this.angle = angle;
        this.orbitRadius = orbitRadius;
        this.lats = lats;
        this.longs = longs;
        this.rotationFactor = rotationFactor;

        while(this.moons.size() < moons){
            addMoon(r.nextInt()*3, radius/(r.nextFloat()*3+2), 10, radius*r.nextFloat()*4+2, 100, 100, r.nextFloat()*10);
        }




    }

    public void addMoon(int asteroids, float radius, float angle, float orbitRadius, int lats,
                          int longs, float rotationFactor){
        Moon moon = new Moon(asteroids, radius, angle, orbitRadius, lats, longs, rotationFactor);
        moons.add(moon);
    }


    public void draw(GL2 gl, GLU glu){

        Glu = glu;

        Texture texture = SolarSystemSimulator.getEarthTexture();

        gl.glPushMatrix();


        gl.glDisable(GL.GL_TEXTURE_2D);

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, WHITE, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, WHITE, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, .5f);

        texture.enable(gl);
        texture.bind(gl);


        angle = (angle + rotationFactor)%360f;

        final float x = (float) Math.sin(Math.toRadians(angle))*orbitRadius;
        final float y = (float) Math.cos(Math.toRadians(angle))*orbitRadius;
        final float z = 0;
        gl.glTranslatef(x, y, z);
        gl.glRotatef(angle, 0, 0, -1);
        gl.glRotatef(45f, 0, 1, 0);


        GLUquadric planet = Glu.gluNewQuadric();
        Glu.gluQuadricTexture(planet, true);
        Glu.gluQuadricDrawStyle(planet, GLU.GLU_FILL);
        Glu.gluQuadricNormals(planet, GLU.GLU_FLAT);
        Glu.gluQuadricOrientation(planet, GLU.GLU_OUTSIDE);
        Glu.gluSphere(planet, radius, longs, lats);
        Glu.gluDeleteQuadric(planet);

        for(Moon m : moons){
            m.draw(gl, Glu);
        }

        gl.glPopMatrix();


    }



}
