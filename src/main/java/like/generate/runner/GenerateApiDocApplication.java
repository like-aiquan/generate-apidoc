package like.generate.runner;


import like.generate.runner.generator.APIParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GenerateApiDocApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GenerateApiDocApplication.class, args);
//        new APIParser(GenerateApiDocApplication.class).parse("like.generate.runner");
    }

}
