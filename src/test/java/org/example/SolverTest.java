package org.example;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SolverTest extends TestCase {

    Board board;
    Solver solver;
    private String desiredAnswer;
    int integralValueOfDesiredAnswer;
    public void setUp() throws Exception {
        super.setUp();
        File inputDirctory = new File("C:\\Users\\shazd\\IdeaProjects\\MavenProj\\src\\test\\java\\org\\example\\");
//        for(File file: inputDirctory.listFiles()) {
//            readFileAndInitializeBoard(file);
//        }
        File [] listOfFiles = inputDirctory.listFiles();
        File file;
        for (int i = 0; i < 50; i++) {
            file = listOfFiles[i];
            System.out.printf("Testing file %s\n", file);
            readFileAndInitializeBoard(file);
        }
    }
    private void readFileAndInitializeBoard(File file){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // Read the size of the array from the first line
            int size = Integer.parseInt(br.readLine().trim());
            // Read the desired answer from the second line
            String answerLine = br.readLine().trim();
            if (answerLine.equals("|")) {
                desiredAnswer = "|"; // Set it to the string '|'
            } else {
                desiredAnswer = answerLine; // Keep it as a string representation of the integer
            }

            int[][] tiles = new int[size][size]; // Create the array based on the size
            int row = 0;

            // Read the array input
            while ((line = br.readLine()) != null && row < size) {
                String[] values = line.trim().split("\\s+"); // Split by space
                for (int col = 0; col < values.length; col++) {
                    tiles[row][col] = Integer.parseInt(values[col]);
                }
                row++;
            }
            // Initialize the Board and Solver with the read tiles
            board = new Board(tiles);
            solver = new Solver(board);
            integralValueOfDesiredAnswer = Integer.parseInt(desiredAnswer);

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as needed
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number from file: " + file.getName());
            e.printStackTrace();
        }
    }
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testIsSolvable() {
        if (solver.isSolvable()) {
            assertEquals(integralValueOfDesiredAnswer,solver.moves());
        } else {
            assertEquals('|', solver.moves());
        }
    }
}