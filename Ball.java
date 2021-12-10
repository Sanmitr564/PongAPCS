import com.badlogic.gdx.math.Circle; 
import java.util.*;
import com.badlogic.gdx.math.Vector2;
class Ball extends Circle{
    private float angle;
    private boolean canInteract;
    public Ball(){
        super();
        angle = 0;
        canInteract = false;
    }
    
    public Ball(Circle circle, float angle, boolean canInteract){
        super(circle);
        this.angle = angle;
        this.canInteract = canInteract;
    }
    
    public Ball(float x, float y, float radius, float angle, boolean canInteract){
        super(x,y,radius);
        this.angle = angle;
        this.canInteract = canInteract;
    }
    
    public Ball(Vector2 position, float radius, float angle, boolean canInteract){
        super(position, radius);
        this.angle = angle;
        this.canInteract = canInteract;
    }
    
    public Ball(Vector2 center, Vector2 edge, float angle, boolean canInteract){
        super(center, edge);
        this.angle = angle;
        this.canInteract = canInteract;
    }
    
    public float getAngle(){
        return angle;
    }
    
    public void setAngle(float n){
        angle = n;
    }
    
    public void addAngle(float n){
        angle += n;
    }
    
    public void multiplyAngle(float n){
        angle *= n;
    }
    
    public void setInteract(boolean b){
        canInteract = b;
    }
    
    public boolean getInteract(){
        return canInteract;
    }
}