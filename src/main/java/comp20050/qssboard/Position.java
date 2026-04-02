package comp20050.qssboard;
import java.util.ArrayList;
public class Position {
    private String raw_position;
    private int row;
    private int col;
    String regex = "[,_\\s]";
    ArrayList<Integer> position = new ArrayList<>(); // first index holds row, second index holds colun

    public Position(String raw_position) {
        this.raw_position = raw_position;
    }

    public ArrayList<Integer> extractPosition() {
        String[] pos_str = raw_position.split(regex);
        if (pos_str[0].equals("R") || pos_str[0].equals("O")) {

            // O_7_2
            row = Integer.parseInt(pos_str[1]);
            col = Integer.parseInt(pos_str[2]);

            position.clear(); // for clarity
            position.add(row);
            position.add(col);
        }
        else {
            throw new IllegalArgumentException("Error in extractPosition() - invalid format of cellID given!");
        }

        return position;
    }

    public ArrayList<Integer> getPosition() {
        return position;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getRawPosition() {
        return raw_position;
    }

}

