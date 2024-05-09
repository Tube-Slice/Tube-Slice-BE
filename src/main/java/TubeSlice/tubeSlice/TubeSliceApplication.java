package TubeSlice.tubeSlice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@OpenAPIDefinition(servers = {@Server(url = "https://tubeslice.site", description = "tubeslice")})
@EnableJpaAuditing
public class TubeSliceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TubeSliceApplication.class, args);
	}

}
