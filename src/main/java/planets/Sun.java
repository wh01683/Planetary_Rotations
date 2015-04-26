package planets;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;

import java.util.Random;

/**
 * Created by robert on 4/26/15.
 */
public class Sun {

    private static GLU Glu;
    Random r = new Random();

    private static final float[] WHITE = {1f, 1f, 1f};


    /**
     * creates a new sun spherical object with a specified number of planets orbiting it
     * @param planets number of planets orbiting the sphere
     * @param radius radius of the sphere
     * @param lats number of horizontal layers in the sphere
     * @param longs number of vertical layers in the sphere
     * @param gl gl object
     * @param glu
     */

    public Sun(float planets, float radius, float angle, float orbitRadius,
               int lats, int longs, float rotationFactor, GL2 gl, GLU glu) {

        Texture texture = SolarSystemSimulator.getSunTexture();

        Glu = glu;

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

        GLUquadric sun = Sun.Glu.gluNewQuadric();
        Sun.Glu.gluQuadricDrawStyle(sun, GLU.GLU_FILL);
        Sun.Glu.gluQuadricNormals(sun, GLU.GLU_FLAT);
        Sun.Glu.gluQuadricOrientation(sun, GLU.GLU_OUTSIDE);
        Sun.Glu.gluSphere(sun, radius, longs, lats);
        Sun.Glu.gluDeleteQuadric(sun);

        for(int i = 0; i<planets; i++){
            addPlanet(r.nextFloat()*3, radius/3, 10, radius*2, 100, 100, 3, gl, glu);
        }

        gl.glPopMatrix();


    }

    public void addPlanet(float moons, float radius, float angle, float orbitRadius, int lats,
                          int longs, float rotationFactor, GL2 gl, GLU glu){
        Planet planet = new Planet(moons, radius, angle, orbitRadius, lats, longs, rotationFactor, gl, glu);
    }
}
