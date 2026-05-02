package comp20050.qssboard;
import java.util.ArrayList;
public class Position {
    private String raw_position;
    private int row;
    private int col;
    String regex = "[,_\\s]";
    ArrayList<Integer> position = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(row, col);
    }

    public Position(String raw_position) {
        this.raw_position = raw_position;
        position = extractPosition();
    }

    public ArrayList<Integer> extractPosition() {
        String[] pos_str = raw_position.split(regex);
        if (pos_str[0].equals("R") || pos_str[0].equals("O")) {

            row = Integer.parseInt(pos_str[1]);
            col = Integer.parseInt(pos_str[2]);

            position.clear();
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

