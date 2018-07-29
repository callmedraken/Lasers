package gui;

import backtracking.Backtracker;
import backtracking.Configuration;
import backtracking.SafeConfig;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.nio.file.Paths;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.List;

import model.*;

import javax.swing.*;

/**
 * The main class that implements the JavaFX UI.   This class represents
 * the view/controller portion of the UI.  It is connected to the model
 * and receives updates from it.
 *
 * @author Sean Strout @ RIT CS
 * @author Michael Jones
 * @author John Hsiao
 */
public class LasersGUI extends Application implements Observer {
    /** The UI's connection to the model */
    private LasersModel model;

    /** The main pane of the gui **/
    private BorderPane border;

    /** the list of buttons**/
    private ArrayList<LaserButton> buttons = new ArrayList<>();

    /** Message at top of display **/
    Label message = new Label();

    // stage reference for resizing
    private Stage sizeStage;

    // name of current file
    String loadName;

    // absolute path of current file
    private String fileName;

    @Override
    public void init() throws Exception {
        // the init method is run before start.  the file name is extracted
        // here and then the model is created.
        try
        {
            Parameters params = getParameters();
            String filename = params.getRaw().get(0);
            this.fileName = filename;
            this.loadName = filename;
            this.model = new LasersModel(filename);
        }
        catch (FileNotFoundException fnfe)
        {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
    }

    /**
     * A private utility function for setting the background of a button to
     * an image in the resources subdirectory.
     *
     * @param button the button control
     * @param bgImgName the name of the image file
     */
    private void setButtonBackground(Button button, String bgImgName)
    {
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image( getClass().getResource("resources/" + bgImgName).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        button.setBackground(background);
    }

    /**
     * makes a LaserButton
     * @param c - character in model
     * @param row - row of button
     * @param col - column of button
     * @return LaserButton object
     */
    private LaserButton makeButton(char c, int row, int col)
    {
        LaserButton button = new LaserButton(row, col);
        switch(c){
            // makes blank button, ability to toggle laser
            case '.':
                makeBlank(button);
                button.setOnAction(e -> toggleLaser(button));
                break;
            // all below are pillars, sets image
            case 'X':
                Image pillarX = new Image(getClass().getResourceAsStream("resources/pillarX.png"));
                ImageView XIcon = new ImageView(pillarX);
                button.setGraphic(XIcon);
                button.pillarPresent = true;
                setButtonBackground(button, "white.png");
                break;
            case '0':
                Image pillarZero = new Image(getClass().getResourceAsStream("resources/pillar0.png"));
                ImageView ZeroIcon = new ImageView(pillarZero);
                button.setGraphic(ZeroIcon);
                button.pillarPresent = true;
                setButtonBackground(button, "white.png");
                break;
            case '1':
                Image pillarOne = new Image(getClass().getResourceAsStream("resources/pillar1.png"));
                ImageView OneIcon = new ImageView(pillarOne);
                button.setGraphic(OneIcon);
                button.pillarPresent = true;
                setButtonBackground(button, "white.png");
                break;
            case '2':
                Image pillarTwo = new Image(getClass().getResourceAsStream("resources/pillar2.png"));
                ImageView TwoIcon = new ImageView(pillarTwo);
                button.setGraphic(TwoIcon);
                button.pillarPresent = true;
                setButtonBackground(button, "white.png");
                break;
            case '3':
                Image pillarThree = new Image(getClass().getResourceAsStream("resources/pillar3.png"));
                ImageView ThreeIcon = new ImageView(pillarThree);
                button.setGraphic(ThreeIcon);
                button.pillarPresent = true;
                setButtonBackground(button, "white.png");
                break;
            case '4':
                Image pillarFour = new Image(getClass().getResourceAsStream("resources/pillar4.png"));
                ImageView FourIcon = new ImageView(pillarFour);
                button.setGraphic(FourIcon);
                button.pillarPresent = true;
                setButtonBackground(button, "white.png");
                break;
            default:
                System.out.println("ERROR IN MAKEBUTTON");
        }
        return button;
    }

    /**
     * makes space into laser or vice versa
     * @param b - button to toggle
     */
    private void toggleLaser(LaserButton b)
    {
        if(b.laserPresent)
        {
            model.remove(b.row, b.col);
            b.laserPresent = false;
            update(model, this);
        }
        else
        {
            model.add(b.row, b.col);
            b.laserPresent = true;
            update(model, this);
        }
    }

    /**
     *
     * @param stage the stage to add UI components into
     */
    private void init(Stage stage)
    {
        border = new BorderPane();  // create a new borderpane
        GridPane grid;
        updateButtons();
        grid = buildGrid();
        border.setCenter(grid);
        message.setPadding(new Insets(10));
        message.setTextFill(Color.web("magenta"));  // change the top message font color
        message.setFont(new Font("Comic Sans MS", model.getCol()*3));  // change the top message font
        border.setTop(message);
        border.setRight(makeButtons());
        // ANIME BACKGROUND
        border.setStyle("-fx-background-size: stretch; -fx-background-image: url('http://i.imgur.com/2PT3CHL.png')");
        //border.setStyle("-fx-background-color: lightblue");  // BORING BACKGROUND
        Scene scene = new Scene(border);
        stage.setScene(scene);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        this.sizeStage = primaryStage;  // reference to stage for resizing
        init(primaryStage);  // do all your UI initialization here
        primaryStage.setTitle("Lasers");
        message.setTextFill(Color.web("magenta"));
        message.setFont(new Font("Comic Sans MS", model.getCol()*3));
        String loadFileName = Paths.get(loadName).getFileName().toString();
        model.setMessage(loadFileName + " loaded!");
        primaryStage.show();
    }

    @Override
    public void update(Observable o, Object arg)
    {
        /**
         * loop through the grid
         * check all update cases
         */
        for (int i = 0; i < model.getRow(); i++) {
            for (int j = 0; j < model.getCol(); j++) {
                switch (model.getVal(i, j)) {
                    case 'L':
                        makeLaser(buttons.get((i * model.getCol()) + j));
                        buttons.get((i * model.getCol()) + j).laserPresent = true;
                        break;
                    case '.':
                        makeBlank(buttons.get((i * model.getCol()) + j));
                        buttons.get((i * model.getCol()) + j).laserPresent = false;
                        break;
                    case 'h':
                        makeHBeam(buttons.get((i * model.getCol()) + j));
                        buttons.get((i * model.getCol()) + j).laserPresent = false;
                        break;
                    case 'v':
                        makeVBeam(buttons.get((i * model.getCol()) + j));
                        buttons.get((i * model.getCol()) + j).laserPresent = false;
                        break;
                    case 'c':
                        makeCBeam(buttons.get((i * model.getCol()) + j));
                        buttons.get((i * model.getCol()) + j).laserPresent = false;
                        break;

                }
                if((buttons.get((i * model.getCol()) + j)).pillarPresent){
                    setButtonBackground(buttons.get((i * model.getCol()) + j), "white.png");
                }
            }
        }
        if(!model.getError().isEmpty()){
            int r = (int) model.getError().get(0);
            int c = (int) model.getError().get(1);
            setButtonBackground(buttons.get((r * model.getCol()) + c), "red.png");
            if(model.getVal(r,c) == '.'){
                Image red = new Image(getClass().getResourceAsStream("resources/red.png"));
                ImageView redIcon = new ImageView(red);
                (buttons.get((r * model.getCol()) + c)).setGraphic(redIcon);
            }
            model.removeError();
        }

        border.setCenter(buildGrid());
        message.setText(model.getMessage());
        sizeStage.sizeToScene();
    }

    /**
     * helper - makes a button into a laser
     * @param button - button to make into laser
     */
    public void makeLaser(LaserButton button){
        Image image = new Image(getClass().getResourceAsStream("resources/laser2.png"));
        ImageView LaserIcon = new ImageView(image);
        button.setGraphic(LaserIcon);
        setButtonBackground(button, "yellow.png");
    }

    /**
     * helper - makes a button into a blank space
     * @param button - button to make into blank
     */
    public void makeBlank(LaserButton button){
        Image image = new Image(getClass().getResourceAsStream("resources/white.png"));
        ImageView BlankIcon = new ImageView(image);
        button.setGraphic(BlankIcon);
        setButtonBackground(button, "white.png");
    }

    /**
     * helper - makes a button into a beam
     * @param button - button to make into beam
     */
    public void makeHBeam(LaserButton button)
    {
        Image image = new Image(getClass().getResourceAsStream("resources/beamH.png"));
        ImageView BeamIcon = new ImageView(image);
        button.setGraphic(BeamIcon);
        setButtonBackground(button, "white.png");
    }

    /**
     * helper - makes a button into a beam
     * @param button - button to make into beam
     */
    public void makeVBeam(LaserButton button)
    {
        Image image = new Image(getClass().getResourceAsStream("resources/beamV.png"));
        ImageView BeamIcon = new ImageView(image);
        button.setGraphic(BeamIcon);
        setButtonBackground(button, "white.png");
    }

    /**
     * helper - makes a button into a beam
     * @param button - button to make into beam
     */
    public void makeCBeam(LaserButton button)
    {
        Image image = new Image(getClass().getResourceAsStream("resources/beamC.png"));
        ImageView BeamIcon = new ImageView(image);
        button.setGraphic(BeamIcon);
        setButtonBackground(button, "white.png");
    }


    /**
     * button extension, represents buttons in grid
     */
    public class LaserButton extends Button
    {
        public int row;
        public int col;
        public boolean laserPresent;
        public boolean pillarPresent;
        public LaserButton(int r, int c)
        {
            this.row = r;
            this.col = c;
            this.laserPresent = false;
            this.pillarPresent = false;
        }
    }

    /**
     * builds side menu of buttons
     * @return - VBox of butons
     */
    private Node makeButtons()
    {
        // all the css edits for the button styles
        final String BUTTON_STYLE = "-fx-background-color: \n" +
                "        #3c7fb1,\n" +
                "        linear-gradient(#fafdfe, #e8f5fc),\n" +
                "        linear-gradient(#eaf6fd 0%, #d9f0fc 49%, #bee6fd 50%, #a7d9f5 100%);\n" +
                "    -fx-background-insets: 0,1,2;\n" +
                "    -fx-background-radius: 3,2,1;\n" +
                "    -fx-text-fill: black;";

        VBox bList = new VBox();  // create a new VBOX
        bList.setPadding(new Insets(10));
        bList.setSpacing(2);
        Button check = new Button("Check");  // create the check button
        // change the check button styles
        check.setStyle(BUTTON_STYLE);
        check.setOnMousePressed(e-> check.setStyle("-fx-text-fill: white"));
        check.setOnMouseReleased(e-> check.setStyle(BUTTON_STYLE));
        Button hint = new Button("Hint");  // create the hint button
        // change the hint button style
        hint.setOnMousePressed(e-> hint.setStyle("-fx-text-fill: white"));
        hint.setOnMouseReleased(e-> hint.setStyle(BUTTON_STYLE));
        hint.setStyle(BUTTON_STYLE);
        Button reset = new Button("Reset");  // create the reset button
        // change the reset button style
        reset.setOnMousePressed(e-> reset.setStyle("-fx-text-fill: white"));
        reset.setOnMouseReleased(e-> reset.setStyle(BUTTON_STYLE));
        reset.setStyle(BUTTON_STYLE);
        Button load = new Button("Load");  // create the load button
        // change the load button style
        load.setOnMousePressed(e-> load.setStyle("-fx-text-fill: white"));
        load.setOnMouseReleased(e-> load.setStyle(BUTTON_STYLE));
        load.setStyle(BUTTON_STYLE);
        Button solve = new Button("Solve");  // create the solve button
        // change the solve button style
        solve.setOnMousePressed(e-> solve.setStyle("-fx-text-fill: white"));
        solve.setOnMouseReleased(e-> solve.setStyle(BUTTON_STYLE));
        solve.setStyle(BUTTON_STYLE);

        // set the size of the buttons
        check.setPrefWidth(50);
        hint.setPrefWidth(50);
        reset.setPrefWidth(50);
        load.setPrefWidth(50);
        solve.setPrefWidth(50);

        // reset button action
        reset.setOnAction(e -> {
            for (LaserButton b : buttons) {
                if(b.laserPresent) {toggleLaser(b);}
            }
            model.removeError();
            model.setMessage("Safe was reset.");
            model.announceChange();
        });

        // check button action
        check.setOnAction(e -> {
            model.verify();
        });

        // load button action
        load.setOnAction(e ->{
                try {
                    load();
                    model.announceChange();
                }
                catch (FileNotFoundException f){
                    System.out.println("FILE NOT FOUND");
                }
                catch (IOException i){
                    System.out.println("IO EXCEPTION");
                }
            });

        // solve button action
        solve.setOnAction(e -> {
            try {
                SafeConfig config = new SafeConfig(fileName);
                Backtracker backtracker = new Backtracker(false);
                Optional<Configuration> sol = backtracker.solve(config);
                if(sol.isPresent()){
                    SafeConfig solConfig = (SafeConfig) sol.get();
                    this.model = new LasersModel(solConfig.getModel());
                    model.addObserver(this);
                    model.announceChange();

                }
                else{
                    model.setMessage("No solution exists");
                }
            }
            catch(FileNotFoundException f){
                model.setMessage( loadName + " NOT FOUND");
            }
            finally {
                update(model, this);
            }
        });

        // hint button action
        hint.setOnAction(e ->{
            if(model.verify()){
                model.setMessage("Hint: try clicking check!");
            }
            else {
                SafeConfig config = new SafeConfig(this.model);
                Backtracker backtracker = new Backtracker(false);
                List<Configuration> path = backtracker.solveWithPath(config);
                if (path.size() != 0) {
                    int[] coords = SafeConfig.findNewLaser(config, (SafeConfig) path.get(0));
                    toggleLaser(buttons.get(coords[0] * model.getCol() + coords[1]));
                    model.setMessage("Hint: added laser to (" + coords[0] + ", " + coords[1] + ")");
                } else {
                    model.setMessage("Hint: no solution from here!");
                }
            }
        });
        bList.getChildren().addAll(check, hint, solve, reset, load);  // add all the buttons to the parent layout
        return bList;
    }

    /**
     * rebuilds grid based on buttons data
     * @return grid of buttons
     */
    private GridPane buildGrid()
    {
        GridPane grid = new GridPane();
        for (int i = 0; i < model.getRow(); i++) {
            for (int j = 0; j < model.getCol(); j++) {
                grid.add(buttons.get((i * model.getCol()) + j),j,i);
            }
        }
        return grid;
    }

    /**
     * loads new safe file
     * @throws FileNotFoundException
     */
    private void load() throws IOException
    {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(new Stage());
        if (file != null)
        {
            loadName = Paths.get(file.getAbsolutePath()).getFileName().toString();
            fileName = Paths.get(file.getAbsolutePath()).toString();
            this.model = new LasersModel(file.getAbsolutePath());
            updateButtons();
            message.setTextFill(Color.web("magenta"));
            message.setFont(new Font("Comic Sans MS", model.getCol()*3));
            model.setMessage(loadName + " loaded!");
            model.addObserver(this);
            update(model, this);
        }
    }

    /**
     * uses model to update grid of buttons
     * @return - grid of buttons
     */
    private ArrayList<LaserButton> updateButtons()
    {
        buttons.clear();  // clear the buttonlist
        /**
         * loop through the grid and create a new grid of buttons
         * used for reset
         */
        for (int i = 0; i < model.getRow(); i++)
        {
            for (int j = 0; j < model.getCol(); j++)
            {
                char c = model.getVal(i,j);
                LaserButton b = makeButton(c,i,j);
                buttons.add(b);
            }
        }
        return buttons;
    }
}
