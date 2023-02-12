//AvoidEntities are entities the player needs to avoid colliding with.
//If a player collides with an avoid, it reduces the players Hit Points (HP).
public class ThrowEntity extends Entity implements Consumable, Scrollable {
    
    //Location of image file to be drawn for an AvoidEntity
    private static final String THROWN_IMAGE_FILE = "assets/ThrownPan.png";
    //Dimensions of the AvoidEntity    
    protected static final int THROW_WIDTH = 75;
    private static final int THROW_HEIGHT = 75;
    //Speed that the avoid moves each time the game scrolls
    private static final int THROWN_SCROLL_SPEED = 5;
    
    
    public ThrowEntity(int x, int y){
        super(x, y, THROW_WIDTH, THROW_HEIGHT, THROWN_IMAGE_FILE);  
    }
    
    
    public int getScrollSpeed(){
        return THROWN_SCROLL_SPEED;
    }
    
    //Move the avoid left by the scroll speed
    public void scroll(){
        setX(getX() + THROWN_SCROLL_SPEED);
    }
    
    //Throwing the pan does not affect the score
    public int getPointsValue(){
        return 0; //added
    }

    @Override //Thrown Pan doesn't have a damage value
    public int getDamageValue() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    
}
