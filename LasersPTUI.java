package ptui;

import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

import model.LasersModel;

/**
 * This class represents the view portion of the plain text UI.  It
 * is initialized first, followed by the controller (ControllerPTUI).
 * You should create the model here, and then implement the update method.
 *
 * @author Sean Strout @ RIT CS
 * @author YOUR NAME HERE
 */
public class LasersPTUI implements Observer {
    /** The UI's connection to the model */
    private LasersModel model;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     * @param filename the safe file name
     * @throws FileNotFoundException if file not found
     */
    public LasersPTUI(String filename) throws FileNotFoundException {
        try {
            this.model = new LasersModel(filename);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
    }

    public LasersModel getModel() { return this.model; }

    @Override
    public void update(Observable o, Object arg) {
        display(model);
        System.out.println(model.getMessage());
    }

    /**
     * displays model
     * @param model model to display
     */
    public static void display(LasersModel model)
    {
        char[][] grid = model.getGrid();
        int col = model.getCol();
        int row = model.getRow();

        String result = "  ";
        for(int i=0;i<col;i++)
        {
            result += i%10 + " ";
        }
        result += "\n  ";
        for(int i=0;i<col*2-1;i++)
        {
            result += "-";
        }
        result += "\n";
        for(int i=0; i<row; i++)
        {
            result += i%10 + "|";
            for(int j=0; j<col; j++)
            {
                result += grid[i][j] + " ";
            }
            if (i == row-1)
            {
                break;
            }
            result += "\n";
        }
        System.out.println(result);
    }

    /**
     * displays help menu
     */
    public static void help()
    {
        System.out.println("a|add r c: Add laser to (r,c)\n"+
                "d|display: Display safe\n"+
                "h|help: Print this help message\n"+
                "q|quit: Exit program\n"+
                "r|remove r c: Remove laser from (r,c)\n"+
                "v|verify: Verify safe correctness");
    }

}
