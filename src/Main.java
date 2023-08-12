import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final String start = "S";
    private static final String end = "E";
    private static int lineCalculation = 0;
    private static String fElement;
    private static int startRow;
    private static int startCol;
    //Linked list to store lines of the file
    private static LinkedList lines = new LinkedList();
    //Array list to mark the visited cells
    static ArrayList<String> visited = new ArrayList<>();
    //2D array to get the maze
    private static String[][] maze;
    private static int pathUnits = -1;

    public static void main(String[] args) {
        System.out.print("\u001B[34m[I]\u001B[39m Enter the file name: ");
        Scanner scanner = new Scanner(System.in);
        String filename = scanner.nextLine();
        long startTime = System.nanoTime();
        readMazeLayout(filename);
        if (lines.head != null) {
            fElement = lines.head.data;
        } else {
            System.out.println("\u001B[31m[E]\u001B[39m Linked list is empty");
            return;
        }
        //Getting the maze to a 2D array
        maze = new String[lineCalculation][fElement.length()];
        for (int i = 0; i < lineCalculation; i++) {
            String lineElement = lines.head.data;
            maze[i] = lineElement.split("");
            lines.removeFromFront();
        }
        //Capturing the starting point
        outer:for (int i = 0; i < lineCalculation; i++) {
            for (int j = 0; j < fElement.length(); j++) {
                if (maze[i][j].equals(start)) {
                    startRow = i;
                    startCol = j;
                    break outer;
                }
            }
        }
        //Giving the starting point to dfsAlgo function
        boolean path = dfsAlgo(startRow, startCol);

        //Printing the maze and path
        System.out.println("\n\u001B[31mPath -->\u001B[39m");
        printMaze(visited);
        if (path) {
            System.out.println("Travel --> \u001B[31m" + pathUnits +" Units\u001B[39m");
        } else {
            System.out.println("No path found");
        }
        long endTime = System.nanoTime();
        long executingTime = endTime - startTime;
        System.out.println("Executing time --> \u001B[34m" + executingTime + " nanoseconds \u001B[34m");
    }

    //Getting the maze to linked list line by line
    public static void readMazeLayout(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.insert(line);
                //lineCalculation will help to get maze to 2D array
                lineCalculation++;
            }
            //If file is not found printing an errors message and exit
        } catch (IOException e) {
            System.out.println("\u001B[31m[E]\u001B[39m File not found");
            System.exit(0);
        }
    }

    public static boolean dfsAlgo(int row, int col) {
        //If found end return true
        if (maze[row][col].equals(end)) {
            return true;
        }
        //Adding visited cells to visited array list
        visited.add(row + "," + col);
        //right,down,left,up
        int[] nextrow = {0, 1, 0, -1};
        int[] nextcol = {1, 0, -1, 0};
        for (int i = 0; i < 4; i++) {
            int newRow = row + nextrow[i];
            int newCol = col + nextcol[i];

            //If end is found count the path
            boolean isPathVisited = false;
            for (int j = 0; j < visited.size(); j++) {
                String pathCell = visited.get(j);
                String[] visitedCoordinates = pathCell.split(",");
                if (newRow == Integer.parseInt(visitedCoordinates[0]) && newCol == Integer.parseInt(visitedCoordinates[1])) {
                    isPathVisited = true;
                    break;
                }
            }
            if (validMove(newRow, newCol) && !isPathVisited) {
                if (dfsAlgo(newRow, newCol)) {
                    pathUnits++;
                    return true;
                }
            }
        }
        //Back track
        visited.remove(visited.size() - 1);
        return false;
    }

    //Ckeck the next visited cordinates valid in the maze
    public static boolean validMove(int row, int col) {
        return (row >= 0 && row < lineCalculation && col >= 0 && col < fElement.length() && !maze[row][col].equals("#"));
    }

    // Print the solved maze with path in red color & starting point and ending point in blue color
    public static void printMaze(ArrayList<String> path) {
        for (int i = 0; i < lineCalculation; i++) {
            for (int j = 0; j < fElement.length(); j++) {
                String cell = maze[i][j];
                boolean isPathCell = false;

                // Check if the current cell is in the path
                for (int k = 0; k < path.size(); k++) {
                    String pathCell = path.get(k);
                    String[] visitedCoordinates = pathCell.split(",");
                    if (i == Integer.parseInt(visitedCoordinates[0]) && j == Integer.parseInt(visitedCoordinates[1])) {
                        isPathCell = true;
                        break;
                    }
                }
                if (cell.equals(start)) {
                    System.out.print("\u001B[34m" + cell + "\u001B[39m");
                } else if (cell.equals(end)) {
                    System.out.print("\u001B[34m" + cell + "\u001B[39m");
                } else if (isPathCell) {
                    System.out.print("\u001B[31m" + cell + "\u001B[39m");
                } else {
                    System.out.print(cell);
                }
            }
            System.out.println();
        }
    }
}