import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Main demo = new Main();
        demo.simulate();
    }

    /*  Takes an input file input.txt read, write, and display cache commands, and executes them to simulate the cache  */
    private void simulate() throws FileNotFoundException {
        // initializes memory and cache
        Memory myMemory = new Memory();
        Cache myCache = new Cache();
        myMemory.initialize();
        myCache.initEmptyCache();
        myCache.setMemory(myMemory);

        // scans a file holding commands and data - acts accordingly
        File file = new File("src/input.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String hexAddress;
            switch (scanner.nextLine()) {
                case "R": // read
                    hexAddress = "0x" + scanner.nextLine();
                    myCache.read(Integer.decode(hexAddress));
                    break;
                case "W": // write
                    hexAddress = "0x" + scanner.nextLine();
                    short data = Short.decode("0x" + scanner.nextLine());
                    myCache.write(data, Integer.decode(hexAddress));
                    break;
                case "D": // display
                    myCache.printCache();
                    break;
                default:
                    System.out.println("How did we get to this branch?");
                    break;
            }
        }
    }
}

