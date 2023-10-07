import database.EntityWriter;
import entity.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class RepositoryTest {
    @Test
    @DisplayName("EntityWriter가 올바르게 텍스트 파일을 작성한다")
    public void entityWriterTest() throws Exception{
        EntityWriter<Movie> writer = new EntityWriter<>("test.txt");
        HashMap<String, Movie> data = new HashMap<>();
        Movie m1 = new Movie("OPHM", "오펜하이머", 180);
        Movie m2 = new Movie("AVGS", "어벤져스", 200);
        data.put(m1.getId(), m1);
        data.put(m2.getId(), m2);
        final boolean result = writer.dump(data);

        BufferedReader bf = new BufferedReader(new FileReader("test.txt"));
        String s1 = bf.readLine();
        String s2 = bf.readLine();

        assertThat(s1).isEqualTo(m1.toString().trim());
        assertThat(s2).isEqualTo(m2.toString().trim());

        assertThat(result).isTrue();
    }
}
