import java.io.*;
import java.util.Scanner;

public class FileWriter {
    /** writeFile() method - writes the file of the object
     *
     * @param users To print the tree object
     * @throws IOException whenever an input or output failed
     */
    public void writeFile(DiaryTree users) throws IOException{
        //TreeNode tn = new TreeNode();
        Scanner s1 = new Scanner( System.in );

        System.out.println( "Enter the file name you want to save to (end with .bin) > " );
        FileOutputStream o = new FileOutputStream( s1.nextLine() );
        ObjectOutputStream oo = new ObjectOutputStream(o); //Outputs object
        oo.writeObject(users);

    }

    /** readFile() method - reads the file
     *
     * @throws IOException Whenever an input or output failed
     * @throws ClassNotFoundException If a class is not found it throws an exception
     */
    public void readFile(String input) throws IOException, ClassNotFoundException{

        FileInputStream oRead = new FileInputStream(input);
        ObjectInputStream ooRead = new ObjectInputStream(oRead);
        DiaryTree saveTree = (DiaryTree) ooRead.readObject(); //Reads the tree object

        System.out.println(saveTree.print()); //Prints the saved tree
    }
}
