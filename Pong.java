import com.badlogic.gdx.ApplicationAdapter; 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer; 
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle; 
import com.badlogic.gdx.math.Circle; 
import com.badlogic.gdx.Input.Keys; 
import com.badlogic.gdx.math.Vector2; 
import com.badlogic.gdx.math.MathUtils; 
import com.badlogic.gdx.math.Intersector; 
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.*;

public class Pong extends ApplicationAdapter//A Pong object ___________ ApplicationAda
{
    private OrthographicCamera camera; //the camera to our world
    private Viewport viewport; //maintains the ratios of your world
    private ShapeRenderer renderer; //used to draw textures and fonts 
    private BitmapFont font; //used to draw fonts (text)
    private SpriteBatch batch; //also needed to draw fonts (text)

    private Rectangle leftPaddle;//Rectangle object to represent the left paddle
    private Rectangle rightPaddle; //same, Rectangle is a class from libGDX
    //private Circle ball; //Circle object to represent the ball (Circle is a class form libGDX)
    //private float ballAngle; //holds the angle the ball is traveling
    private boolean started = false; //has the game started yet
    private int player1Score = 0; //keep track of scores
    private int player2Score = 0; 

    //WORLD_WIDTH and WORLD_HEIGHT proportional to config.width and config.height in GameLauncher class
    public static final float WORLD_WIDTH = 800; 
    public static final float WORLD_HEIGHT = 480;

    //other constance we will need
    public static final float PADDLE_WIDTH = 20; 
    public static final float PADDLE_HEIGHT = 480;
    public static final float RADIUS = 15;
    public static final float PADDLE_SPEED = 10;
    public static final float BALL_SPEED = 10;
    public static final int DELAY = 120;

    private ArrayList<Ball> balls;
    private Stopwatch timer;
    private int order;
    private int roundNumber;

    @Override//called once when the game is started (kind of like our constructor)
    public void create(){
        camera = new OrthographicCamera(); //camera for our world, it is not moving
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera); //maintains world units from screen units
        renderer = new ShapeRenderer(); 
        font = new BitmapFont(); 
        batch = new SpriteBatch(); 

        balls = new ArrayList<Ball>();

        balls.add(new Ball(WORLD_WIDTH / 2 - RADIUS, WORLD_HEIGHT / 2 - RADIUS, RADIUS, 0, false)); 
        //balls.add(new Ball(WORLD_WIDTH / 2 - RADIUS, WORLD_HEIGHT / 2 - RADIUS, RADIUS, 0, false));
        //balls.add(new Ball(WORLD_WIDTH / 2 - RADIUS, WORLD_HEIGHT / 2 - RADIUS, RADIUS, 0, false));
        
        leftPaddle = new Rectangle(0, 0, PADDLE_WIDTH, PADDLE_HEIGHT); 
        rightPaddle = new Rectangle(WORLD_WIDTH - PADDLE_WIDTH, WORLD_HEIGHT / 2 - PADDLE_HEIGHT / 2,
            PADDLE_WIDTH, PADDLE_HEIGHT);
        order = 0;
        timer = new Stopwatch();
        roundNumber = -1;
    }

    @Override//this is called 60 times a second, all the drawing is in here, or helper
    //methods that are called from here
    public void render(){
        viewport.apply(); 
        //these two lines wipe and reset the screen so when something action had happened
        //the screen won't have overlapping images
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        
        for(int i = 0; i < balls.size()-1; i++){
            for(int n = i+1; n < balls.size(); n++){
                if(Intersector.overlaps(balls.get(i), balls.get(n)) && balls.get(i).getInteract() && balls.get(n).getInteract()){
                    float yDiff = balls.get(i).y - balls.get(n).y;
                    float xDiff = balls.get(i).x - balls.get(n).x;
                    float percentOfBall = yDiff / RADIUS;
                    if(xDiff>0){
                        balls.get(i).setAngle(-45 + (percentOfBall * 90));
                        balls.get(n).setAngle(225 - (percentOfBall * 90));
                    }else{
                        balls.get(n).setAngle(-45 + (percentOfBall * 90));
                        balls.get(i).setAngle(225 - (percentOfBall * 90));
                    }
                }
            }
        }
        
        //if the game has started adjust the position
        //of the ball based on the ball angle
        if(started)
        {
            for(Ball ball: balls){
                if(order*DELAY>timer.getTime())
                    break;
                ball.x += BALL_SPEED * MathUtils.cosDeg(ball.getAngle());//cosine gets the change in x distance
                ball.y += BALL_SPEED * MathUtils.sinDeg(ball.getAngle()); //sine gets the change in y distance
                ball.setInteract(true);
                order++;
            }
            order = 0;
        }

        //check for input: These are the controls for the game.
        //Example; the up key moves the right paddle up and the W key
        //modify the y coordinate of the appropriate paddle depending on what key is pressed
        if(Gdx.input.isKeyPressed(Keys.UP))
        {
            rightPaddle.y += PADDLE_SPEED;           
        }
        if(Gdx.input.isKeyPressed(Keys.DOWN))
        {
            rightPaddle.y -= PADDLE_SPEED;           
        }

        if(Gdx.input.isKeyPressed(Keys.W))
        {
            leftPaddle.y += PADDLE_SPEED;           
        }
        if(Gdx.input.isKeyPressed(Keys.S))
        {
            leftPaddle.y -= PADDLE_SPEED; 
        }
        //start the game
        if(Gdx.input.isKeyJustPressed(Keys.SPACE) && !started)
        {
            roundNumber++;
            started = true;  
            if(roundNumber!=0 && roundNumber%3==0)
                balls.add(new Ball(WORLD_WIDTH / 2 - RADIUS, WORLD_HEIGHT / 2 - RADIUS, RADIUS, 0, false));
            timer.start();
        }

        //TODO add a total 4 if statements to not let the paddles
        //move off the screen. You can access the
        //bottom left coordinate of the paddles like this:
        //leftPaddle.x and leftPaddle.y 
        //You will need the constants PADDLE_HEIGHT
        //WORLD_HEIGHT
        if(leftPaddle.y>WORLD_HEIGHT-PADDLE_HEIGHT){
            leftPaddle.y = WORLD_HEIGHT - PADDLE_HEIGHT;
        }
        if(leftPaddle.y<0){
            leftPaddle.y = 0;
        }
        if(rightPaddle.y>WORLD_HEIGHT-PADDLE_HEIGHT){
            rightPaddle.y = WORLD_HEIGHT - PADDLE_HEIGHT;
        }
        if(rightPaddle.y<0){
            rightPaddle.y = 0;
        }

        //make the ball bounce of the top and bottom walls
        for(Ball ball: balls){
            if(ball.y + RADIUS > WORLD_HEIGHT)
                ball.multiplyAngle(-1); 
            if(ball.y - RADIUS < 0)
                ball.multiplyAngle(-1); 

            //check for collision

            //TODO: Replace if false. Check the Intersector class for a method that checks if 
            //a Circle object and Rectangle object overlap

            if(Intersector.overlaps(ball, rightPaddle))
            {
                //determine what angle the ball will bounce off the paddle

                //ball.y - rightPaddle.y determines the distance bettween the bottom left of the paddle
                //and where the ball is when it hits the paddle
                //example if the ball's y coordinate is 10 and the rightPaddle.y is 4, the ball hit 6 units
                //above the bottom left of the paddle
                //Then dividing by the PADDLE_HEIGHT gets a percentage from 0 to 1, 
                //example if the PADDLE_HEIGHT is 20 then the 6 from before would result in 6/20 = .3
                //in other words the ball hit 3/10 of the way up the paddle
                float percentOfPaddle = (ball.y - rightPaddle.y) / PADDLE_HEIGHT;

                //percentOfPaddle will be between 0 and 1, thus the ball angle will be between 225 and 135
                ball.setAngle(225 - (percentOfPaddle * 90)); //place for constants possibly? 

            }

            //TODO: Replace if false. Check the Intersector class for a method that checks if 
            //a Circle object and Rectangle object overlap
            if(Intersector.overlaps(ball, leftPaddle))
            {
                //similar logic to above
                float percentOfPaddle = (ball.y - leftPaddle.y) / PADDLE_HEIGHT;

                ball.setAngle(-45 + (percentOfPaddle * 90)); 

            }

            //TODO:check if each player has scored based on the 
            //position of the ball, and reset the ball.x and ball.y 
            //to the center of the screen, ballAngle back to 0, started back to false
            //and increment player1Score or player2Score

            if(ball.x<0){
                for(Ball ballReset: balls){
                    ballReset.setPosition(WORLD_WIDTH / 2 - RADIUS, WORLD_HEIGHT / 2 - RADIUS);
                    ballReset.setInteract(false);
                }
                started = false;
                timer.reset();
                player2Score++;
                ball.setAngle(0);
                break;
            }

            if(ball.x>WORLD_WIDTH){
                for(Ball ballReset: balls){
                    ballReset.setPosition(WORLD_WIDTH / 2 - RADIUS, WORLD_HEIGHT / 2 - RADIUS);
                    ballReset.setInteract(false);
                }
                started = false;
                timer.reset();
                player1Score++;
                ball.setAngle(0);    
                break;
            }
        }
        
        //draw everything on the screen with our renderer
        //draw each object based on its attributes
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.setColor(Color.WHITE); 
        renderer.begin(ShapeType.Filled);

        //draw each rectangle based on the attributes of the Rectangle objects - leftPaddle
        //and rightPaddle
        //draw the circle based on the attributes of the Circle object - ball

        renderer.rect(leftPaddle.x, leftPaddle.y, leftPaddle.width, leftPaddle.height);
        renderer.rect(rightPaddle.x, rightPaddle.y, rightPaddle.width, rightPaddle.height);
        for(Ball ball: balls){
            renderer.circle(ball.x, ball.y, ball.radius);
        }
        renderer.end();

        GlyphLayout layout = new GlyphLayout(font, "Press SPACE_BAR to start");
        batch.begin();
        if(!started)
        {

            font.draw(batch, layout, 
                WORLD_WIDTH / 2 - layout.width / 2, 
                WORLD_HEIGHT/2 + layout.height / 2 + 20);

        }

        font.draw(batch, player1Score + ":" + player2Score, WORLD_WIDTH / 2 - 20, 440); 
        batch.end(); 
        timer.update();
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true); 
    }

    @Override
    public void dispose(){
        renderer.dispose(); 
        batch.dispose(); 
    }

}
