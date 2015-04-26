package planets;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by robert on 4/26/15.
 */
public class SolarSystemSimulator extends GLCanvas implements GLEventListener {

    private static final String TEXTURE_PATH_DESKTOP = "C:\\Users\\robert\\IdeaProjects\\learning_opengl\\src\\main\\java\\learning_jogl\\";
    private static final String CURRENT_PATH = System.getProperty("user.dir");
    private static final long serialVersionUID = 1L;
    private static final float[] WHITE = {1f, 1f, 1f};


    final int FPS = 60;
    private static FPSAnimator animator;
    private static GLU glu;
    static Random r = new Random();
    private static Texture earthTexture;
    private static Texture moonTexture;
    private static Texture sunTexture;
    private static Texture venusTexture;
    private static Texture jupiterTexture;
    private static Texture jupiterTexture2;

    private static float angle1 = 0;
    private static float angle2 = 50;
    private static float angle3 = 100;
    private static float angle4 = 150;

    public SolarSystemSimulator(int width, int height, GLCapabilities glCapabilities){
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
        setCamera(gl, glu, 1000);

        setLight(gl);

        Sun sun = new Sun(3, 100, 20, 5, 100, 100, 1, gl, glu);

    }

    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {

        final GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
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



        try {
            InputStream earthStream = new FileInputStream(CURRENT_PATH + "/earth.jpg");
            TextureData earthData = TextureIO.newTextureData(getGLProfile(), earthStream, false, "jpg");
            earthTexture = TextureIO.newTexture(earthData);

            InputStream moonStream = new FileInputStream(CURRENT_PATH+ "/moon.jpg");
            TextureData moonData = TextureIO.newTextureData(getGLProfile(), moonStream, false, "jpg");
            moonTexture = TextureIO.newTexture(moonData);

            InputStream sunStream = new FileInputStream(CURRENT_PATH+ "/sun.jpg");
            TextureData sunData = TextureIO.newTextureData(getGLProfile(), sunStream, false, "jpg");
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

    public static Texture getEarthTexture(){
        return earthTexture;
    }

    public static Texture getMoonTexture() {
        return moonTexture;
    }

    public static Texture getSunTexture() {
        return sunTexture;
    }

    public static Texture getVenusTexture() {
        return venusTexture;
    }

    public static Texture getJupiterTexture() {
        return jupiterTexture;
    }

    public static Texture getJupiterTexture2() {
        return jupiterTexture2;
    }
}
