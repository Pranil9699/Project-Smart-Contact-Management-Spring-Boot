package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;
//	@GetMapping("/test")
//	@ResponseBody
//	public String test() {
//		User user = new User();
//		user.setName("Pranil Takawane");
//		user.setEmail("takawanepranil22@gmail.com");
//		userRepository.save(user);
//		return "Working";
//	}

	@GetMapping("/")
	public String Home(Model m) {
		m.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}

	@GetMapping("/about")
	public String About(Model m) {
		m.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}

	@GetMapping("/signup")
	public String SignUp(Model m) {
		m.addAttribute("title", "Register - Smart Contact Manager");
		m.addAttribute("user", new User());
		return "signup";
	}

	// this handler for register user
	@PostMapping("/do_register")
	public String registerUser(@javax.validation.Valid @ModelAttribute("user") User user, BindingResult bindingResult,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model m,
			HttpSession session) {
		try {
			System.out.println("here come");
			if (!agreement) {
				System.out.println("You have not agreed Terms And Conditions");
				throw new Exception("You have not agreed Terms And Conditions");
			}
			System.out.println("here come");

			if (bindingResult.hasErrors()) {
				System.out.println("here come");

				m.addAttribute("user", user);
				System.out.println("here come");

				System.out.println("there are some error when validate the fields");
				System.out.println(user.getPassword());
				System.out.println(bindingResult.getAllErrors().toString());
				System.out.println(bindingResult);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageURL("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
			System.out.println(agreement + " : " + user);

			User result = userRepository.save(user);
			System.out.println(agreement + " : " + user);
			System.out.println(agreement + " : " + result);
			session.setAttribute("message", new Message("SuccessFully Registered !!", "alert-success"));
			m.addAttribute("user", new User());
			return "signup";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			m.addAttribute("user", user);
			session.setAttribute("message", new Message("Something Went Wrong !!" + e.getMessage(), "alert-danger"));
			return "signup";
		}

	}
   
	@GetMapping("/signin")
    public String loginform(Model model) {
        model.addAttribute("title", "Login Form");
		return "login";
    }
	@GetMapping("/login-error")
	public String loginerror(Model model) {
		model.addAttribute("title", "Login error Page ");
		return "login-error";
	}
	
	


}
