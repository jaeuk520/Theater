import entity.Room;
import entity.Seat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomTest {

    @DisplayName("영화관 좌석이 정확하게 출력된다.")
    @ParameterizedTest
    @CsvSource({"10,15", "3,3", "10,5", "2,10"})
    public void toStringTest(int row, int col){
        Seat[][] seats = new Seat[row][col];
        for(int i = 0; i < row; i++){
            seats[i] = new Seat[col];
            for(int j = 0; j < col; j++){
                seats[i][j] = new Seat(('A' + i) + Integer.toString(j), j%2 == 0, false);
            }
        }
        long roomNumber = 1;
        Room room = new Room(Long.toString(roomNumber), seats);
        String[] actual = room.toString().split("\n");
        System.out.println(Arrays.toString(actual));
        assertThat(actual).hasSize(row + 1);
        assertThat(actual[0]).isEqualTo(row + " " + col);
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                assertThat(actual[i+1].charAt(j)).isEqualTo(seats[i][j].isAvailable() ? 'O' : 'X');
            }
        }
    }
}
