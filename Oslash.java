package main.oslash;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Oslash {

    private static String TEMP_LOC = "java.io.tmpdir";
    private static String TEMP_FILE = "tempFile";
    private static String TXT = ".txt";
    private static String FULL_STOP = ". ";
    private static String NEXT_LINE = ". \n";

    public static void main(String[] args) throws IOException {
        Oslash oslash = new Oslash();
        oslash.findMatches(args);
    }

    public void findMatches(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Invalid input");
            return;
        }

        String path = args[0];
        String keyword =  args[1];

        File file = new File(path);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found, provide correct file path");
        }
        File tempFile = getTempFile();
        writeDataInTempFile(scanner, tempFile);
        try {
            scanner = new Scanner(tempFile);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }
        List<String> matchedLines = findWordInFile(keyword, scanner, tempFile);
        printMatchedlines(matchedLines);
    }

    private static void printMatchedlines(List<String> matchedLines) {
        if (matchedLines.isEmpty()) {
            System.out.println("Word was not found in the file");
            return;
        }
        for (String matchedLine : matchedLines) {
            System.out.println(matchedLine);
        }
    }

    private static List<String> findWordInFile(String keyword, Scanner scanner, File tempFile) {
        List<String> matchedLines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(keyword)) {
                matchedLines.add(line);
            }
        }
        scanner.close();
        tempFile.delete();
        return matchedLines;
    }

    private static void writeDataInTempFile(Scanner scanner, File tempFile) throws IOException {
        FileWriter fileWriter = new FileWriter(tempFile);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String sFinal = line.replace(FULL_STOP, NEXT_LINE);
            fileWriter.write(sFinal);
        }
        scanner.close();
        fileWriter.close();
    }

    private static File getTempFile() throws IOException {
        String prefix = TEMP_FILE;
        String suffix = TXT;
        String tmpdir = System.getProperty(TEMP_LOC);
        File directoryPath = new File(tmpdir);
        File tempFile = File.createTempFile(prefix, suffix, directoryPath);
        return tempFile;
    }
}
