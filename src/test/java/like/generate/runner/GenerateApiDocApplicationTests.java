package like.generate.runner;

import like.generate.runner.generator.APIParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class GenerateApiDocApplicationTests {

    @Test
    void contextLoads() {
        APIParser apiParser = new APIParser(GenerateApiDocApplication.class);
        apiParser.parse("like.generate.runner");
    }

}
