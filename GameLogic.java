//Worked with Anna Lieb
import java.awt.*;
import java.awt.event.*;
import java.util.*;


//TODO: Implement throwing the pan
//A Simple version of the scrolling game, featuring Avoids, Gets, and RareGets
//Players must reach a score threshold to win
//If player runs out of HP (via too many Avoid collisions) they lose
public class GameLogic extends ScrollingGameEngine {
    
    //Dimensions of game window
    private static final int DEFAULT_WIDTH = 900;
    private static final int DEFAULT_HEIGHT = 600;  
    
    //Starting PlayerEntity coordinates
    private static final int STARTING_PLAYER_X = 0;
    private static final int STARTING_PLAYER_Y = 100;
    
    //Score needed to win the game
    //private static final int SCORE_TO_WIN = 300;
    
    //Maximum that the game speed can be increased to
    //(a percentage, ex: a value of 300 = 300% speed, or 3x regular speed)
    private static final int MAX_GAME_SPEED = 300;
    //Interval that the speed changes when pressing speed up/down keys
    private static final int SPEED_CHANGE = 20;    
    
    private static final String INTRO_SPLASH_FILE = "assets/FinalSplash.png";  
    private static final String LAWN_IMAGE_FILE = "assets/GameLawn.png";      
    //Key pressed to advance past the splash screen
    public static final int ADVANCE_SPLASH_KEY = KeyEvent.VK_ENTER;
    
    //Interval that Entities get spawned in the game window
    //ie: once every how many ticks does the game attempt to spawn new Entities
    private static final int SPAWN_INTERVAL = 45;
    
    //A Random object for all your random number generation needs!
    public static final Random rand = new Random();
    
    private static final int AVOID_ENT_HEIGHT = 75;
    private static final int GET_ENT_HEIGHT = 50;
    private static final int PAUSE_KEY = 'P';
    private static final int THROW_KEY = 'C';

    
    
    //Player's current score
    private int zombiesKilled = 0;
    private int hp = 1;
    private int tacoKillCount = 0;
    
    //Stores a reference to game's PlayerEntity object for quick reference
    //(This PlayerEntity will also be in the displayList)
    private PlayerEntity player;

    private boolean isPaused;

    

    public GameLogic(){
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public GameLogic(int gameWidth, int gameHeight){
        super(gameWidth, gameHeight);
    }
    
    
    //Performs all of the initialization operations that need to be done before the game starts
    protected void preGame(){
        this.setBackgroundImage(LAWN_IMAGE_FILE);
        this.setSplashImage(INTRO_SPLASH_FILE);
        player = new PlayerEntity(STARTING_PLAYER_X, STARTING_PLAYER_Y, PlayerEntity.PLAYER_WITHOUT_PAN);
        displayList.add(player); 
    }
    
    //Called on each game tick
    protected void updateGame(){
        //scroll all scrollable Entities on the game board
        if (isPaused)
            return;
        scrollEntities();   
        //Spawn new entities only at a certain interval
        if (ticksElapsed % SPAWN_INTERVAL == 0){            
            spawnNewEntities();
            garbageCollectEntities();
        }
        checkCollisions();

        
        //Update the title text on the top of the window
        if(player.getPlayerState().equals(PlayerEntity.PLAYER_AFTER_TACO))
            setTitleText("HP: " + hp + ", Zombies Killed: " + zombiesKilled + ", Taco Uses Left: " + (5 - tacoKillCount));
        else if (player.getPlayerState().equals(PlayerEntity.PLAYER_WITH_PAN)){
            setTitleText("HP: " + hp + ", Zombies Killed: " + zombiesKilled +", 1 Pan use!");
        }
        else{
            setTitleText("HP: " + hp + ", Zombies Killed: " + zombiesKilled +", No Pan or Taco - CAUTION");
        }
                
    }
    
    public void checkCollisions(){
        for (int i = 0; i < displayList.size(); i++){
            Entity e = displayList.get(i);
            if (player.isCollidingWith(e)){
                if (e instanceof Consumable){
                    handlePlayerCollision((Consumable)e);
                }
            }
            if (e instanceof ThrowEntity){
                //System.out.println("This Happened");
                checkOtherEntityCollision(e);
            }
        }
    }
    
    //Scroll all scrollable entities per their respective scroll speeds
    protected void scrollEntities(){
        for (int i = 0; i < displayList.size(); i++){
            Entity e = displayList.get(i);
            if (e instanceof Scrollable){ 
                ((Scrollable)e).scroll();
            }
        }
    }
    
    //Handles "garbage collection" of the displayList
    //Removes entities from the displayList that are no longer relevant
    //(i.e. will no longer need to be drawn in the game window).
    // question: is the ones that arent formed bc of collided when instantiated included in this?
    protected void garbageCollectEntities(){
        for (int i = 0; i < displayList.size() -1; i++){
            Entity e = displayList.get(i);
            if (e.getRightX() <= 0){
                displayList.remove(e);
                //System.out.println("REMOVED");
            }      
            if (e instanceof ThrowEntity && e.getRightX() > getWindowWidth()){
                displayList.remove(e);
                //System.out.println("pan was garbage collected");
            }
        }   
    }
    
    private void changeCharacterState(String newState){
        displayList.remove(player);
        player = new PlayerEntity(player.getX(), player.getY(), newState);
        displayList.add(player);
    }

    //Called whenever it has been determined that the PlayerEntity collided with a consumable
    private void handlePlayerCollision(Consumable collidedWith){
        if (collidedWith instanceof GetEntity){
            changeCharacterState(PlayerEntity.PLAYER_WITH_PAN);
        }
        if (collidedWith instanceof AvoidEntity){
            if (player.getPlayerState().equals(PlayerEntity.PLAYER_WITHOUT_PAN)){
                hp += collidedWith.getDamageValue();
            }
            else if (player.getPlayerState().equals(PlayerEntity.PLAYER_AFTER_TACO)){
                tacoKillCount++;
                zombiesKilled++;
                if (tacoKillCount == 5){
                    changeCharacterState(PlayerEntity.PLAYER_WITHOUT_PAN);
                }
            }
            else if (player.getPlayerState().equals(PlayerEntity.PLAYER_WITH_PAN)){
                zombiesKilled++;
                changeCharacterState(PlayerEntity.PLAYER_WITHOUT_PAN);
            }
        }
        if (collidedWith instanceof RareGetEntity){
            changeCharacterState(PlayerEntity.PLAYER_AFTER_TACO);
            tacoKillCount = 0;
        }
        displayList.remove((Consumable)collidedWith);
        if (isGameOver()){
            postGame();
        }

       
    }
    
    
    //Spawn new Entities on the right edge of the game board
    private void spawnNewEntities(){

        int numAvoidsSpawned = rand.nextInt(3);
        for (int i = 0; i < numAvoidsSpawned; i++){
            generateAvoidEntity();
        }

        int numGetsSpawned = rand.nextInt(3);
        for (int i = 0; i < numGetsSpawned; i++){
            generateGetEntity();
        }

        int chanceHappened = rand.nextInt(10);
        if(chanceHappened == 0){
            generateRareGetEntity();
        }
    }   
            
    

    private void generateAvoidEntity(){
        AvoidEntity aEntity = new AvoidEntity(DEFAULT_WIDTH, rand.nextInt(getWindowHeight() - AVOID_ENT_HEIGHT));
        displayList.add(aEntity); 
        checkOtherEntityCollision(aEntity);
    }

    private void generateGetEntity(){
        GetEntity gEntity = new GetEntity(DEFAULT_WIDTH, rand.nextInt(getWindowHeight() - GET_ENT_HEIGHT));
        displayList.add(gEntity);
        checkOtherEntityCollision(gEntity);
    }

    private void generateRareGetEntity(){
        RareGetEntity rgEntity = new RareGetEntity(DEFAULT_WIDTH, rand.nextInt(getWindowHeight() - GET_ENT_HEIGHT));
        displayList.add(rgEntity);
        checkOtherEntityCollision(rgEntity);
    }

    private void throwPan(){
        ThrowEntity thrownPan = new ThrowEntity(player.getRightX() + player.getWidth(), player.getY());
        changeCharacterState(PlayerEntity.PLAYER_WITHOUT_PAN);
        displayList.add(thrownPan);
    }

    private void checkOtherEntityCollision(Entity checking){
        for (int i = 0; i < displayList.size() - 1; i++){
            Entity e = displayList.get(i);
            if(e == checking){
                continue;
            }
            if (checking.isCollidingWith(e)){ 
                displayList.remove(checking);
                if (checking instanceof ThrowEntity){
                    //.out.println("Pan removed");
                    //System.out.println(e);
                    displayList.remove(e);
                    if (e instanceof AvoidEntity){
                        zombiesKilled++;
                    }
                }
                return; // so it doesnt have to go through entire list
            }
        }
    }
    
    //Called once the game is over, performs any end-of-game operations
    protected void postGame(){
        if (zombiesKilled == 25){
            super.setTitleText("GAME OVER - You Won!");
        }
        else{
            super.setTitleText("GAME OVER - You Lose!");
        }
    }
    
    
    //Determines if the game is over or not
    //Game can be over due to either a win or lose state
    protected boolean isGameOver(){
        if (zombiesKilled >= 25 || hp == 0)
            return true;
        return false;   //****   placeholder... implement me!   ****
       
    }
    
    
    
    //Reacts to a single key press on the keyboard
    protected void handleKeyPress(int key){        
        setDebugText("Key Pressed!: " + KeyEvent.getKeyText(key));
        
        //if a splash screen is active, only react to the "advance splash" key... nothing else!
        if (getSplashImage() != null){
            if (key == ADVANCE_SPLASH_KEY)
                super.setSplashImage(null); 
            return;
        }

        if (isPaused == true){
            if (key == PAUSE_KEY)
                isPaused = false;
            return;
        }
        
        if (key == PAUSE_KEY){
            isPaused = true;
            return;
        }
        

        // FIX: IF KEY IS A MOVEMENT KEY
        if (key == RIGHT_KEY){
            if(player.getRightX()!= getWindowWidth()){
                int newX = Math.min(getWindowWidth() - player.getWidth() , player.getX()+player.getMovementSpeed()); 
                player.setX(newX);
            }
        }
        if (key == LEFT_KEY){
            if(player.getX()!= 0){
                int newX = Math.max(0,player.getX() - player.getMovementSpeed());
                player.setX(newX);
            }
        }
        if (key == UP_KEY){
            if(player.getY()!= 0){
                int newY = Math.max(0,player.getY() - player.getMovementSpeed());
                player.setY(newY);
            }       
        }
        if (key == DOWN_KEY){ 
            if(player.getBottomY()!= getWindowHeight()) {
                int newY = Math.min(getWindowHeight() - player.getHeight() , player.getY() + player.getMovementSpeed()); 
                player.setY(newY);
            }   
        }

        if (key == THROW_KEY && player.getPlayerState().equals(PlayerEntity.PLAYER_WITH_PAN)){
            throwPan();
        }
        


        else if (key == SPEED_UP_KEY){
            if (getGameSpeed() < MAX_GAME_SPEED)
                setGameSpeed(getGameSpeed() + SPEED_CHANGE);
        }
        else if (key == SPEED_DOWN_KEY){ 
            if (getGameSpeed() > SPEED_CHANGE) 
                setGameSpeed(getGameSpeed() - SPEED_CHANGE);
        }
    }    
    
    
    //Handles reacting to a single mouse click in the game window
    //Won't be used in Simple Game... you could use it in Creative Game though!
    protected MouseEvent handleMouseClick(MouseEvent click){
        if (click != null){ //ensure a mouse click occurred
            int clickX = click.getX();
            int clickY = click.getY();
            setDebugText("Click at: " + clickX + ", " + clickY);
        }
        return click; //returns the mouse event for any child classes overriding this method
    }
    
    
    
    
}
