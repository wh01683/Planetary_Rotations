package planets;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by robert on 4/25/15.
 */

public class Planet extends GLJPanel implements KeyListener{


    static int planetAngle = 0;
    static int moonAngle = 0;

    GLU glu;
    FPSAnimator animator;
    Random r = new Random();

    boolean sequence = true;

    private float rotateX = 13;
    private float rotateY = 10;
    private float rotateZ = 2;

    @Override
    public synchronized void addKeyListener(KeyListener keyListener) {
        super.addKeyListener(keyListener);
    }

    public Planet(GLCapabilities capabilities){
        super(capabilities);
        setPreferredSize(new Dimension(1200, 800));


    }

    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        setCamera(gl, glu, 30);

        float[] lightPos = {-30, 0, 0, 1};
        float[] lightColorAmbient = {.2f, .2f, .2f, 1f};
        float[] lightColorSpecular = {.8f, .8f, .8f, 1f};

        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);

        gl.glEnable(GL2.GL_LIGHT1);
        gl.glEnable(GL2.GL_LIGHTING);

        float[] rgba = {.3f, .5f, 1f};
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, .5f);

        sun(300, 100, 2, r.nextFloat(), gl);

    }

    public void init(GLAutoDrawable drawable) {
        // called when the panel is created
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);

        gl.glShadeModel(GL.GL_LINE_SMOOTH);

        gl.glClearColor(0, 0, 0, 0);

        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);

        glu = new GLU();

        animator = new FPSAnimator(this, 60);
        animator.start();
    }

    /**
     * @param gl The GL context.
     * @param glu The GL unit.
     * @param distance The distance from the screen.
     */
    private void setCamera(GL2 gl, GLU glu, float distance) {
        // Change to projection matrix.
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        // Perspective.
        float widthHeightRatio = (float) getWidth() / (float) getHeight();
        glu.gluPerspective(45, widthHeightRatio, 1, 1000);
        glu.gluLookAt(0, 0, distance, 0, 0, 0, 0, 1, 0);

        // Change back to model view matrix.
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void sun(float radius, float orbitRadius, int numberPlanets, float rotationFactor, GL2 gl){

        gl.glPushMatrix();

        GLUquadric sun = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(sun, GLU.GLU_FILL);
        glu.gluQuadricNormals(sun, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(sun, GLU.GLU_OUTSIDE);
        computePosition(orbitRadius, 1, rotationFactor, gl);
        gl.glColor3d(1.0, .7, 0.0);
        glu.gluSphere(sun, radius, 16, 16);
        glu.gluDeleteQuadric(sun);
        for (int i = 0; i < numberPlanets; i++){
            planet(radius / 4, radius * 6, 2, (r.nextFloat() * 3 + 1), gl);
        }

        gl.glPopMatrix();

    }

    public void planet(float radius, float orbitRadius, int numberMoons, float rotationFactor, GL2 gl){
        planetAngle++;

        gl.glPushMatrix();
        GLUquadric moon1 = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(moon1, GLU.GLU_FILL);
        glu.gluQuadricNormals(moon1, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(moon1, GLU.GLU_OUTSIDE);
        computePosition(orbitRadius, planetAngle, rotationFactor, gl);

        gl.glColor3d(0.0, 0.8, 0.8);
        glu.gluSphere(moon1, radius, 16, 16);
        glu.gluDeleteQuadric(moon1);
        for (int i = 0; i < numberMoons; i ++){
            moon(radius / 6, radius * 2, (r.nextFloat() * 3 + 1), gl);
        }
        gl.glPopMatrix();
    }

    public void moon(float radius, float orbitRadius, float rotationFactor, GL2 gl){

        moonAngle++;
        gl.glPushMatrix();
        GLUquadric moon1 = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(moon1, GLU.GLU_FILL);
        glu.gluQuadricNormals(moon1, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(moon1, GLU.GLU_OUTSIDE);
        computePosition(orbitRadius, moonAngle, rotationFactor, gl);
        gl.glColor3d(10.0, 10.0, 10.0);
        glu.gluSphere(moon1, radius, 16, 16);
        glu.gluDeleteQuadric(moon1);
        gl.glPopMatrix();
    }

    public void computePosition(float orbitRadius, float angle, float rotationFactor, GL2 gl){
        angle = (angle + 1f) % 360f;
        final float x = (float) Math.sin(Math.toRadians(angle)) * orbitRadius;
        final float y = (float) Math.cos(Math.toRadians(angle)) * orbitRadius;
        final float z = 0;
        gl.glTranslatef(x, y, z);

        gl.glRotatef(rotateZ * rotationFactor, 0, 0, 1);
        gl.glRotatef(rotateY * rotationFactor, 0, 1, 0);
        gl.glRotatef(rotateX * rotationFactor, 1, 0, 0);
        gl.glRotatef(angle, 0, 0, -1);

        gl.glRotatef(45f, 0, 1, 0);
    }

    public void keyTyped(KeyEvent keyEvent) {

    }

    public void keyPressed(KeyEvent keyEvent) {

        sequence = !sequence;

        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE && sequence){
            rotateY = 0;
            rotateZ = 0;
            rotateX = 0;
            rotate(sequence);
        }
        else{
            rotateY = 10;
            rotateZ = 2;
            rotateX = 13;
            rotate(sequence);
        }
    }

    public void rotate(boolean rotating){
        try {
            while (rotating) {
                rotateX += 2;
                rotateY += 1;
                rotateZ += .05;
                TimeUnit.MILLISECONDS.sleep(10);
            }

        }catch (InterruptedException i){
            i.printStackTrace();
            System.exit(1);
        }
    }

    public void keyReleased(KeyEvent keyEvent) {

    }
}
