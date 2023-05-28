import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/* ------------------------------------------------------------
               Topic : Dynamic and Greedy Approach
            Experiment : Weighted Interval Scheduling
   ------------------------------------------------------------- */

public class Main
{
    public static void main(String[] args) throws IOException
    {
        String inputFile = args[0];   // <inputFile>.json will be taken as input from command line.
        Assignment assignmentArray[] = readFile(inputFile);
        Scheduler scheduler = new Scheduler(assignmentArray);
        //In addition to the console outputs, we will output our result as json files.
        writeOutput("solution_dynamic.json", scheduler.scheduleDynamic());
        writeOutput("solution_greedy.json", scheduler.scheduleGreedy());

    }
    private static Assignment[] readFile(String filename) throws FileNotFoundException
    {
        try
        {
            // Gson instance
            Gson gson = new Gson();

            // Creating a reader
            Reader reader = Files.newBufferedReader(Paths.get(filename));
            // Creating our objects automatically
            Assignment[] assignmentArray= new Gson().fromJson(reader, new TypeToken<Assignment[]>() {}.getType());
            return assignmentArray;

        }
        catch (Exception ex)
        {
            throw new FileNotFoundException();
        }
    }

    private static void writeOutput(String filename, ArrayList<Assignment> arrayList) throws IOException
    {
        // Gson instance
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        // Creating json file and writing the content of our arraylist inside it.
        Writer writer = Files.newBufferedWriter(Paths.get(filename));
        gson.toJson(arrayList, writer);
        writer.close();

    }

}

