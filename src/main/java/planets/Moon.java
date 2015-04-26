package planets;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Created by robert on 4/26/15.
 */
public class Moon {


    private static GLU Glu;

    private static final float[] WHITE = {1f, 1f, 1f};


    public Moon(int asteroids, float radius, float angle, float orbitRadius, int lats, int longs,
                float rotationFactor, GL2 gl, GLU glu){

        Texture texture = SolarSystemSimulator.getMoonTexture();
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

        GLUquadric moon = Moon.Glu.gluNewQuadric();
        Moon.Glu.gluQuadricDrawStyle(moon, GLU.GLU_FILL);
        Moon.Glu.gluQuadricNormals(moon, GLU.GLU_FLAT);
        Moon.Glu.gluQuadricOrientation(moon, GLU.GLU_OUTSIDE);
        Moon.Glu.gluSphere(moon, radius, longs, lats);
        Moon.Glu.gluDeleteQuadric(moon);

        gl.glPopMatrix();
    }



}
