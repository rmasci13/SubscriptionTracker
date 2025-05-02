package com.rmasci13.github.templates;

import com.rmasci13.github.enums.BillingCycle;
import com.rmasci13.github.enums.Category;
import com.rmasci13.github.enums.Status;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping("login")
    public String getLoginView() {
        return "login";
    }

    @GetMapping("index")
    public String getIndexView(Model model) {
        model.addAttribute("categories", Arrays.stream(Category.values())
                .map(Enum::name)
                .toList());
        model.addAttribute("statuses", Arrays.stream(Status.values())
                .map(Enum::name)
                .toList());
        model.addAttribute("billingCycles", Arrays.stream(BillingCycle.values())
                .map(Enum::name)
                .toList());
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
