package vn.mobileid.selfcare.rest;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppCtrl {

    @RequestMapping("/")
    public String home(ModelMap modal, HttpServletResponse response, HttpServletRequest request) {
        modal.addAttribute("title","CRUD Example");
        return "index.html";
    }

}
