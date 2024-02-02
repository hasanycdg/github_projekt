package at.qe.skeleton.internal.ui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for setting the home page.
 * No other way of doing so was found.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "welcome.xhtml"; // change default landing page here
    }
}
