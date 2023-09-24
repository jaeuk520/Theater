import entity.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FileLoaderTest {

    FileLoader fileLoader = new FileLoader();
    @Test
    @DisplayName("영화 텍스트를 정확하게 불러온다.")
    public void movieTest() {
        HashMap<String, Movie> actual = fileLoader.getMovies();
        assertThat(actual).hasSize(1);
        assertThat(actual.get("OPHM").getName()).isEqualTo("오펜하이머");
        assertThat(actual.get("OPHM").getRunningTime()).isEqualTo(300);
        System.out.println(actual);
    }
}
