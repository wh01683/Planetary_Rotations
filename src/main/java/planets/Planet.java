package planets;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by robert on 4/25/2015.
 */
public class Planet extends GLCanvas implements GLEventListener{

    private static final long serialVersionUID = 1L;
    final int FPS = 60;
    private static FPSAnimator animator;
    private static GLU glu;
    static Random r = new Random();
    private static Texture earthTexture;
    private static Texture moonTexture;
    private static Texture sunTexture;

    private static float angle1 = 0;
    private static float angle2 = 50;
    private static float angle3 = 100;
    private static float angle4 = 150;

    public Planet(int width, int height, GLCapabilities glCapabilities){
        super(glCapabilities);
        setSize(width, height);
        addGLEventListener(this);
    }

    public void init(GLAutoDrawable glAutoDrawable) {
        glAutoDrawable.setGL(new DebugGL2(glAutoDrawable.getGL().getGL2()));
        final GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);

        loadTextures();
        animator = new FPSAnimator(this, FPS);

        gl.glShadeModel(GL2.GL_SMOOTH);

        gl.glClearColor(0f, 0f, 0f, 1f);
        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
        glu = new GLU();

        animator.start();

    }

    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    public void display(GLAutoDrawable glAutoDrawable) {
        while(!animator.isAnimating()){
            return;
        }
        final GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        //initiate camera
        setCamera(gl, glu, 200);

        setLight(gl);

        sun(8, 100, 100, 2, gl);
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {

        final GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }

    /**
     * creates a new sun spherical object with a specified number of planets orbiting it
     *
     * @param radius radius of the sphere
     * @param lats number of horizontal layers in the sphere
     * @param longs number of vertical layers in the sphere
     * @param planets number of planets orbiting the sphere
     * @param gl gl object
     */
    private static void sun(float radius, int lats, int longs, int planets, GL2 gl){
        gl.glPushMatrix();

        float[] rgba = {1f, 1f, 1f};
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, .5f);

        sunTexture.enable(gl);
        sunTexture.bind(gl);

        GLUquadric sun = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(sun, GLU.GLU_FILL);
        glu.gluQuadricNormals(sun, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(sun, GLU.GLU_OUTSIDE);
        glu.gluSphere(sun, radius, longs, lats);
        glu.gluDeleteQuadric(sun);

        planet(radius / 3f, radius * 2, longs, 2, gl, lats);
        planet(radius / 1.5f, radius * 5, longs, 2, gl, lats);

        gl.glPopMatrix();
    }
    private static void planet(float radius, float orbitRadius, int longs, int moons, GL2 gl, int lats){
        gl.glPushMatrix();

        angle2 = (angle2 +.5f)%360f;

        final float x = (float) Math.sin(Math.toRadians(angle2))*orbitRadius;
        final float y = (float) Math.cos(Math.toRadians(angle2))*orbitRadius;
        final float z = 0;
        gl.glTranslatef(x, y, z);
        gl.glRotatef(angle2, 0, 0, -1);
        gl.glRotatef(45f, 0, 1, 0);

        gl.glDisable(GL.GL_TEXTURE_2D);
        float[] rgba = {1f, 1f, 1f};
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, .5f);

        earthTexture.enable(gl);
        earthTexture.bind(gl);

        GLUquadric planet = glu.gluNewQuadric();
        glu.gluQuadricTexture(planet, true);
        glu.gluQuadricDrawStyle(planet, GLU.GLU_FILL);
        glu.gluQuadricNormals(planet, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(planet, GLU.GLU_OUTSIDE);
        glu.gluSphere(planet, radius, longs, lats);
        glu.gluDeleteQuadric(planet);

        moon(radius / 2f, radius * 1.6f, longs, lats, 0, gl);
        moon(radius / 3f, radius * 2f, longs, lats, 0, gl);
        moon(radius / 2.5f, radius * 2.5f, longs, lats, 0, gl);

        gl.glPopMatrix();
    }
    private static void moon(float radius, float orbitRadius, int longs, int lats, int asteroids, GL2 gl){
        gl.glPushMatrix();

        angle3 = (angle3 +1f)%360f;

        final float x = (float) Math.sin(Math.toRadians(angle3))*orbitRadius;
        final float y = (float) Math.cos(Math.toRadians(angle3))*orbitRadius;
        final float z = 0;
        gl.glTranslatef(x, y, z);
        gl.glRotatef(angle3, 0, 0, -1);
        gl.glRotatef(45f, 0, 1, 0);

        gl.glDisable(GL.GL_TEXTURE_2D);

        float[] rgba = {1f, 1f, 1f};
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, .5f);

        moonTexture.enable(gl);
        moonTexture.bind(gl);

        GLUquadric moon = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(moon, GLU.GLU_FILL);
        glu.gluQuadricNormals(moon, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(moon, GLU.GLU_OUTSIDE);
        glu.gluSphere(moon, radius, longs, lats);
        glu.gluDeleteQuadric(moon);

        gl.glPopMatrix();
    }

    private static void setLight(GL2 gl){

        float SHINE_ALL_DIRECTIONS = 1;
        //TODO: replace coordinates with coordinates of the sun to simulate light coming from the sun
        float[] lightPos = {0, 0, 0, SHINE_ALL_DIRECTIONS};
        float[] lightColorAmbient = {.2f, .2f, .2f, 1f};
        float[] lightColorSpecular = {0.8f, 0.8f, 0.8f, 1f};

        // Set light parameters.
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);

        // Enable lighting in GL.
        gl.glEnable(GL2.GL_LIGHT1);
        gl.glEnable(GL2.GL_LIGHTING);
/*
        float[] rgba = {.3f, .5f, 1f};

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, .5f);*/


    }

    private void loadTextures(){

        String path = "C:\\Users\\robert\\IdeaProjects\\learning_opengl\\src\\main\\java\\learning_jogl\\";

        try {
            InputStream earthStream = new FileInputStream(path + "earth.png");
            TextureData earthData = TextureIO.newTextureData(getGLProfile(), earthStream, false, ".png");
            earthTexture = TextureIO.newTexture(earthData);

            InputStream moonStream = new FileInputStream(path + "moon.png");
            TextureData moonData = TextureIO.newTextureData(getGLProfile(), moonStream, false, ".png");
            moonTexture = TextureIO.newTexture(moonData);

            InputStream sunStream = new FileInputStream(path + "sun.png");
            TextureData sunData = TextureIO.newTextureData(getGLProfile(), sunStream, false, ".png");
            sunTexture = TextureIO.newTexture(sunData);


        }
        catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }

    }


    private void setCamera(GL2 gl, GLU glu, float distance){

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        float widthHeightRatio = (float) getWidth() / (float) getHeight();
        glu.gluPerspective(45, widthHeightRatio, 1, 1000);
        glu.gluLookAt(0, 0, distance, 0, 0, 0, 0, 1, 0);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

}
