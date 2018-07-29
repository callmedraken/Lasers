package ptui;

import model.LasersModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class represents the controller portion of the plain text UI.
 * It takes the model from the view (LasersPTUI) so that it can perform
 * the operations that are input in the run method.
 *
 * @author Sean Strout @ RIT CS
 * @author YOUR NAME HERE
 */
public class ControllerPTUI  {
    /** The UI's connection to the model */
    private LasersModel model;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     * @param model The laser model
     */
    public ControllerPTUI(LasersModel model) {
        this.model = model;
    }

    /**
     * Run the main loop.  This is the entry point for the controller
     * @param inputFile The name of the input command file, if specified
     */
    public void run(String inputFile) throws FileNotFoundException{
        try {
            Scanner input = new Scanner(new File(inputFile));
            while (input.hasNextLine()) {
                String s = input.nextLine();
                System.out.println("> " + s);
                this.execute(s);
            }
        }
        catch (FileNotFoundException e){}
        catch (NullPointerException n){}

        LasersPTUI.display(model);
        Scanner input1 = new Scanner(System.in);
        while (true)
        {
            System.out.print("> ");
            String line = input1.nextLine();
            this.execute(line);
        }
    }


    public void execute(String s)
    {
        String params[] = s.split(" ");
        if(!s.equals(""))
        {
            char c = params[0].charAt(0);
            if (c == 'q')
            {
                model.quit();
            }
            else if (c == 'h')
            {
                LasersPTUI.help();
            }
            else if (c == 'a')
            {
                if(params.length == 3)
                {
                    model.add(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                }
                else
                {
                    model.setMessage("Incorrect coordinates");
                }
            }
            else if (c == 'd')
            {
                LasersPTUI.display(model);
            }
            else if (c == 'r')
            {
                if(params.length == 3)
                {
                    model.remove(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                }
                else
                {
                    model.setMessage("Incorrect coordinates");
                }
            }
            else if (c == 'v')
            {
                model.verify();
            }
            else
            {
                model.setMessage("Unrecognized command: " + params[0]);
            }
        }
    }
}
