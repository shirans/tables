import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by boaz.y on 11/01/2017.
 */
@RestController
@EnableAutoConfiguration
public class TablesController {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TablesController.class, args);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello Tables!";
    }


    @RequestMapping("/getappointment")
    public Appointment greeting(@RequestParam(value="userName", defaultValue="Boaz") String name) {
        return new Appointment(1, name,"At Taboola Kitchen!!!!",System.currentTimeMillis());
    }
}
