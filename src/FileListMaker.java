import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import static java.nio.file.StandardOpenOption.CREATE;

public class FileListMaker {
    static ArrayList<String>list=new ArrayList<>();
    static boolean newUnsavedFile = false;
    static boolean sure = false;
    static JFileChooser chooser = new JFileChooser();

    public static void main(String[] args) {


//        ArrayList<String> list = new ArrayList<>();
        Scanner in = new Scanner(System.in);


        String prompt = "What do you want to do to the list? A-Add an item D-Delete an item V- To view list O-Open S- Save C- Clear Q- Quit";
        boolean done = false;



        do {
//            display(list);
            String userInput = SafeInput.getRegExString(in, prompt, "[AaDdVvOoSsCcQq]");
//            System.out.println();
            userInput = userInput.toUpperCase();

            switch (userInput) {
                case "A":
                    display(list);
                    add(list);
                    newUnsavedFile=true;
                    break;
                case "D":
                    display(list);
                    if (list.size() == 0) {
                        System.out.println("There are no items in the list to delete. Try to add items first.");
                        break;
                    }
                    delete(list);
                    newUnsavedFile=true;
                    break;
                case "V":
                    display(list);
                    if (list.size() == 0) {
                        System.out.println("The list is empty please add some lines");
                    }
                    break;
                case "O":
                    if (newUnsavedFile == false) {
                        openFile();
                    }else{
                        System.out.println("Please save your file first.");
                    }
                    break;
                case "S":
                    save(list);
                    newUnsavedFile=true;
                    break;
                case "C":
                    sure = SafeInput.getYNConfirm(in,"Are you sure you want to clear your list?[Y/N]");
                    if (sure) {
                        list.clear();
                        newUnsavedFile = true;
                    }else{
                        System.out.println("Please choose another option");
                        break;
                    }
                    break;
                case "Q":
                    if (newUnsavedFile == true){
                    done = SafeInput.getYNConfirm(in, "Are you sure you want to quit? [Y/N]");
                    }
                    else{
                        System.out.println("Please save the file first");
                    }
                    break;
                default:
                    System.out.println("Please try again");
            }
        } while (!done);
        System.out.println("Thank you for using this list maker.");

    }

    private static void display(ArrayList<String> list) {
        System.out.println("Your current list:");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%n %d %s ", i, list.get(i));
        }
    }

    private static void add(ArrayList<String> list) {
        Scanner in = new Scanner(System.in);
        System.out.println("\nWhat do you want to add to the list?");
        String userString = "";
        if (in.hasNext()) {
            userString = in.nextLine();
        } else {
            System.out.println("You entered an invalid string: " + userString + " Please try again");
        }
        list.add(userString);

    }

    private static void delete(ArrayList<String> list) {
        Scanner in = new Scanner(System.in);
        int deleteInput = SafeInput.getRangedInt(in, "\nWhat is the index number that you want to delete?", 0, (list.size() - 1));
        if (list.size() == 0) {

        }
        list.remove(deleteInput);
    }
    private static void openFile(){

        try{
            File workingDirectory = new File(System.getProperty("user.dir"));

            chooser.setCurrentDirectory(workingDirectory);

            // Opens the file chooser
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                //If users selects a file
                File selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();

                Path path = Paths.get("");


                InputStream in = new BufferedInputStream(Files.newInputStream(file,CREATE));
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));


                //int lineNumber=0;
                while(reader.ready()){
                    String line = reader.readLine();
                    FileListMaker.list.add(line);
                    // list.addAll(Arrays.asList(line.split(" ")));
                    //lineNumber++;
                    newUnsavedFile = true;
                }


                reader.close();
                System.out.println("File loaded successfully!");
            }else{
                //If an error occurred or user canceled
                System.out.println("Failed to choose a file to process");
                System.out.println("Run the program again");
                System.exit(0);
            }

        }catch (IOException ex) {
            ex.printStackTrace();

        }
    }
    public static void save(ArrayList<String> list){

        File workingDirectory = new File(System.getProperty("user.dir"));
        Path file = Paths.get(workingDirectory.getPath() + "\\src\\sampledata.txt");

        try{
            OutputStream out = new BufferedOutputStream(Files.newOutputStream(file,CREATE));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

            for (String rec: list){
                writer.write(rec,0,rec.length());
                writer.newLine();
            }
            writer.close();
            System.out.println("Data has been successfully written to sampledata.txt");

        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

}
