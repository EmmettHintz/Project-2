//The entity that the human player controls in the game window
//The player moves in reaction to player input
public class PlayerEntity extends Entity {
    
    //Location of image file to be drawn for a PlayerEntity
    public static final String PLAYER_WITHOUT_PAN = "assets/playerWithoutPan.png";
    public static final String PLAYER_WITH_PAN = "assets/PlayerWithPan.png";
    public static final String PLAYER_AFTER_TACO = "assets/CrazyDaveWithTaco.png";

    //Dimensions of the PlayerEntity
    private static final int PLAYER_WIDTH = 50;
    private static final int PLAYER_HEIGHT = 125;
    //Default speed that the PlayerEntity moves (in pixels) each time the user moves it
    private static final int DEFAULT_MOVEMENT_SPEED = 7;
    //Starting hit points
    private static final int STARTING_HP = 1;
    
    
    //Current movement speed
    private int movementSpeed;
    //Remaining Hit Points (HP) -- indicates the number of "hits" (ie collisions
    //with AvoidEntities) that the player can take before the game is over
    private int hp;
    
    
    public PlayerEntity(){
        this(0, 0, PLAYER_WITHOUT_PAN);        
    }
    
    public PlayerEntity(int x, int y, String playerIcon){
        super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT, playerIcon);  
        this.hp = STARTING_HP;
        this.movementSpeed = DEFAULT_MOVEMENT_SPEED;
    }
    
    public String getPlayerState(){
        return super.imageName;
    }

    //Retrieve and set the PlayerEntity's current movement speed 
    public int getMovementSpeed(){
        return this.movementSpeed;
    }
    
    public void setMovementSpeed(int newSpeed){
        this.movementSpeed = newSpeed;
    }  
    
    /* public void setCharacterModel(boolean hasWeapon){
        if(hasWeapon){
            //super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_IMAGE_FILE_With_Pan);  
        }
    } */
    
    //Retrieve the PlayerEntity's current HP
    public int getHP(){
        return hp;
    }
    
    
    //Set the player's HP to a specific value.
    //Returns an boolean indicating if PlayerEntity still has HP remaining
    public boolean setHP(int newHP){
        this.hp = newHP;
        return (this.hp > 0);
    }
    
    //Set the player's HP to a specific value.
    //Returns an boolean indicating if PlayerEntity still has HP remaining
    public boolean modifyHP(int delta){
        this.hp += delta;
        return (this.hp > 0);
    }

    
    
    
    
    
    
    
    
}
