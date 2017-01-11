import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by boaz.y on 11/01/2017.
 */
@Controller
@EnableAutoConfiguration
public class TablesController {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello Tables!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TablesController.class, args);
    }
}
