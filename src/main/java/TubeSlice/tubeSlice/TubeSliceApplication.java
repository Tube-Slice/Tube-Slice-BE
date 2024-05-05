package TubeSlice.tubeSlice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TubeSliceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TubeSliceApplication.class, args);
	}

}
