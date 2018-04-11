package examples.helloboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/guestbooks")
public class GuestbookController {
    @GetMapping  // http://localhost:8080/guestbooks
    public String list(){
        return "list";
    }

    @GetMapping(value = "/write") // http://localhost:8080/guestbooks/write
    public String write(){
        return "write";
    }
}
