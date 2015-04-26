package planets;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;

import java.util.Random;


/**
 * Created by robert on 4/25/2015.
 */
public class Planet {

    private static GLU Glu;

    private static final float[] WHITE = {1f, 1f, 1f};
    Random r = new Random();

    public Planet(float moons, float radius, float angle, float orbitRadius, int lats, int longs,
                 float rotationFactor, GL2 gl, GLU glu){
        Glu = glu;

        Texture texture = SolarSystemSimulator.getEarthTexture();

        gl.glPushMatrix();

        angle = (angle + rotationFactor)%360f;

        final float x = (float) Math.sin(Math.toRadians(angle))*orbitRadius;
        final float y = (float) Math.cos(Math.toRadians(angle))*orbitRadius;
        final float z = 0;
        gl.glTranslatef(x, y, z);
        gl.glRotatef(angle, 0, 0, -1);
        gl.glRotatef(45f, 0, 1, 0);

        gl.glDisable(GL.GL_TEXTURE_2D);

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, WHITE, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, WHITE, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, .5f);

        texture.enable(gl);
        texture.bind(gl);

        GLUquadric planet = glu.gluNewQuadric();
        glu.gluQuadricTexture(planet, true);
        glu.gluQuadricDrawStyle(planet, GLU.GLU_FILL);
        glu.gluQuadricNormals(planet, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(planet, GLU.GLU_OUTSIDE);
        glu.gluSphere(planet, radius, longs, lats);
        glu.gluDeleteQuadric(planet);

        for(int i = 0; i<moons; i++){
            addMoon(r.nextInt() * 3, radius / 3, (r.nextFloat() * 200 + 150), radius * 2, 100, 100, 3, gl, glu);
        }

        gl.glPopMatrix();
    }

    public void addMoon(int asteroids, float radius, float angle, float orbitRadius, int lats,
                          int longs, float rotationFactor, GL2 gl, GLU glu){
        Moon moon = new Moon(asteroids, radius, angle, orbitRadius, lats, longs, rotationFactor, gl, glu);
    }





}
