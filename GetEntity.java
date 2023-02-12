//GetEntities are entities that the player wants to collide with, as they increase
//their score.
public class GetEntity extends Entity implements Consumable, Scrollable {
    
    //Location of image file to be drawn for a GetEntity
    private static final String GET_IMAGE_FILE = "assets/Pan.png";
    //Dimensions of the GetEntity  
    private static final int GET_WIDTH = 50;
    private static final int GET_HEIGHT = 50;
    //Speed that the GetEntity moves (in pixels) each time the game scrolls
    private static final int GET_SCROLL_SPEED = 5;
    //Amount of points received when player collides with a GetEntity
    private static final int GET_POINT_VALUE = 20;
    
    
    public GetEntity(){
        this(0, 0);        
    }
    
    public GetEntity(int x, int y){
        super(x, y, GET_WIDTH, GET_HEIGHT, GET_IMAGE_FILE);  
    }
    
    public GetEntity(int x, int y, String imageFileName){
        super(x, y, GET_WIDTH, GET_HEIGHT, imageFileName);
    }
    
    public int getScrollSpeed(){
        return GET_SCROLL_SPEED;
    }
    
    //Move the GetEntity left by its scroll speed
    public void scroll(){
        setX(getX() - GET_SCROLL_SPEED);
    }
    
    //Colliding with a GetEntity increases the player's score by the specified amount
    public int getPointsValue(){
        return GET_POINT_VALUE;
    }
    
    //Colliding with a GetEntity does not affect the player's HP
    public int getDamageValue(){
        return 0;
    }
    
}
