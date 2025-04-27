package com.rmasci13.github.templates;

import com.rmasci13.github.user.User;
import com.rmasci13.github.user.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping("login")
    public String getLoginView() {
        return "login";
    }

    @GetMapping("index")
    public String getIndexView() {
        return "index";
    }

    @GetMapping("signup")
    public String getSignupView() {
        return "signup";
    }



//    @GetMapping("index")
//    public String getIndexView(Model model, Authentication auth) {
//        User loggedUser = (User) auth.getPrincipal();
//        Integer userID = loggedUser.getId();
//        model.addAttribute("userID", userID);
//        return "index";
//    }
}
