// Author: Liam Hough
// Student No: 20062376

package com.example.guessmaster;

import android.os.Bundle; //Import all necessary libraries and widgets
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import android.content.DialogInterface;
import java.util.Random;

public class GuessMaster extends AppCompatActivity { //Main GuessMaster Function

    private TextView entityName; //initializing variables for functions
    private TextView ticketSum;
    private Button guessButton;
    private EditText userIn;
    private Button btnclearContent;
    //private String user_input;
    private ImageView entityImage;
    String answer;

    private int numOfEntities;
    private Entity[] entities;
    private int[] tickets;
    private int totalTicketNum;
    //Stores Entity Name
    String entName;
    int entityId = 0;
    int currentTicketWon = 0;

    //GUESSMASTER CODE FROM ASSN2
    public GuessMaster() {
        numOfEntities = 0;
        entities = new Entity[10];
    }

    public void addEntity(Entity entity) {
        entities[numOfEntities++] = entity.clone();
    }

    public void playGame(int entityId) { //play game with EntityId provided
        Entity entity = entities[entityId];
        playGame(entity);
    }

    public void playGame(Entity entity) { //Main Play game engine
//  Name of entity to be guessed in the entityName textview
        entityName.setText(entity.getName());
            //Get Input from the EditText
        answer = userIn.getText().toString();
        answer = answer.replace("\n", "").replace("\r", "");

        if (answer.equals("quit")) {
            System.exit(0);
        }

        Date date = new Date(answer);
//		System.out.println("you guess is: " + date);

        if (date.precedes(entity.getBorn())) { //if the date is too early, delivers pop-up
              AlertDialog.Builder early = new AlertDialog.Builder(GuessMaster.this);
              early.setTitle("Incorrect");
              early.setMessage("Try a later date!");
              early.setCancelable(false);
              early.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog1, int which){
                      dialog1.cancel(); //puts away the message alert
                  }
            });
            AlertDialog dialog1 = early.create();
            dialog1.show();

        } else if (entity.getBorn().precedes(date)) { //if the answer is too kate, delivers pop-up
            AlertDialog.Builder late = new AlertDialog.Builder(GuessMaster.this);
            late.setTitle("Incorrect");
            late.setMessage("Try an earlier date");
            late.setCancelable(false);
            late.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog2, int id) {
                    dialog2.cancel(); //after ok is pressed, gets rid of pop-up message
                }
            });
            AlertDialog dialog2 = late.create();
            dialog2.show();
//          System.out.println("Incorrect. Try an earlier date.");
        } else { //This is the winning Case

            //WINNING ALERT
            totalTicketNum = totalTicketNum + entity.getAwardedTicketNumber();
            AlertDialog.Builder correct = new AlertDialog.Builder(GuessMaster.this);
            correct.setTitle("You won");
            correct.setMessage("BINGO! " + entity.closingMessage());
            correct.setCancelable(false);
            ticketSum.setText("Number of Tickets: " + totalTicketNum); //Outputs total tickets

            correct.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog3, int which){
                    Entity entity = entities[entityId];
                    currentTicketWon = entity.getAwardedTicketNumber();
                    ContinueGame(); //Initializes a new entity to use and rotates through
                    Toast.makeText(getBaseContext(), "You earned " + currentTicketWon+ " tickets", Toast.LENGTH_SHORT).show(); //outputs quick message of current tix
                }
            });
            AlertDialog dialog3 = correct.create();
            dialog3.show();
        }

    }

    //Continue Game Method - This method gets another Entity randomly
    //Updates the Image View and the Entity Name field
    public void ContinueGame(){ //In order to rotate and get a new Entity
        entityId = genRandomEntityId();
        Entity entity = entities[entityId];
        entName = entity.getName();
        //Call the ImageSetter Method
            ImageSetter(entity);
        //Print the name of th entity to be guessed in the entityName textview
            entityName.setText(entName);
        //Clear Previous entry
            userIn.getText().clear();
    }

    public void playGame() { //not used but randomly generated playGame
            int entityId = genRandomEntityId();
            playGame(entityId);
    }

    public int genRandomEntityId() { //used to create random entity
        Random randomNumber = new Random();
        return randomNumber.nextInt(numOfEntities);
    }


    public void changeEntity(){ //change the entity
        int entityId2 = genRandomEntityId();
        Entity entity = entities[entityId2];
        userIn.setText("");
        entityName.setText(entity.getName());
        ImageSetter(entity);
    }

    public void ImageSetter(Entity entity1){ //chooses the image to place in screen based on entity
        if(entity1.getName() == "Justin Trudeau"){
            entityImage.setImageResource(R.drawable.justint);
        }
        else if(entity1.getName() == "Celine Dion"){
            entityImage.setImageResource(R.drawable.celidion);
        }
        else if(entity1.getName() == "Liam Hough"){
            entityImage.setImageResource(R.drawable.creator);
        }
        else if(entity1.getName() == "United States"){
            entityImage.setImageResource(R.drawable.usaflag);
        }
    }

    public void welcomeToGame(Entity entity){ //Intial pop-up when user hits the game
        AlertDialog.Builder welcomealert = new AlertDialog.Builder(GuessMaster.this);
        welcomealert.setTitle("GuessMaster_Game_v3");
        welcomealert.setMessage(entity.welcomeMessage());
        welcomealert.setCancelable(false);
        ImageSetter(entity);

        welcomealert.setNegativeButton("START_GAME", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                ContinueGame();
                Toast.makeText(getBaseContext(), "Game is Starting... Enjoy!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = welcomealert.create();
        dialog.show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) { //intiializes the whole game (ie. main function!)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_activity);
        //Specify the button in the view
        guessButton = findViewById(R.id.btnGuess);
        //Clear button in view
        btnclearContent = findViewById(R.id.btnClear);
        //EditText for user input
        userIn = findViewById(R.id.guessinput);
        //TextView for total tickets
        ticketSum = findViewById(R.id.ticket);
        //Image of Entity
        entityImage = findViewById(R.id.entityImage);
        //Name of Entity
        entityName = findViewById(R.id.entityName);
        ticketSum.setText("Total Tickets:" + totalTicketNum);
        entityName.setText(entName);

        //Entities to be created/used
        Politician jTrudeau = new Politician("Justin Trudeau", new Date("December", 25, 1971), "Male", "Liberal", 0.25);////
        Singer cDion = new Singer("Celine Dion", new Date("March", 30, 1961), "Female", "La voix du bon Dieu", new Date("November", 6, 1981), 0.5);////
        Person lHough = new Person("Liam Hough", new Date("September", 25, 1999),"Male", 1);////
        Country usa = new Country("United States", new Date("July", 4, 1776), "Washinton D.C.", 0.1);////

        GuessMaster gm = this;

        gm.addEntity(usa);
        gm.addEntity(lHough);
        gm.addEntity(cDion);
        gm.addEntity(jTrudeau);
        entityId = genRandomEntityId();
        welcomeToGame(entities[entityId]);



        //OnClick Listener action for clear button
        btnclearContent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeEntity();
            }
        });
        //OnClick Listener action for submit button
        guessButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //playing game
                playGame(entities[entityId]);
            }
        });
    }

}
