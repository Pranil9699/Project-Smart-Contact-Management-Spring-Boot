package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.smart.dao.ForgotPasswordRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.ForgotPassword;
import com.smart.entities.Location;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.services.EmailService;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private ForgotPasswordRepository forgotPasswordRepository;

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
	
	@PostMapping("/getPlace")
	 public String locationSubmit(@ModelAttribute Location location, BindingResult bindingResult, @RequestParam("name") String name) {
		 
		 
//			RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> topic_body = restTemplate.exchange("https://nominatim.openstreetmap.org/?addressdetails=1&q="+location.getName()+"&format=json&limit=1", 
					HttpMethod.GET, null, String.class );//comsuming openstreetmap api
					 
			String  topics = topic_body.getBody(); 
			System.out.println(topics);
			topics = topics.replace("\"address\":{", "");
			topics = topics.replace("[","");
			topics = topics.replace("]","");
			topics = topics.replace("}}","");
			topics = topics.replace("{","");
			
			List<String> test = new ArrayList<String>();
			
			String[] list = topics.split(",\"");
			Location l = new Location();
			
			for (int i = 0; i < list.length; i++) {
				
				String j =list[i].replace("\"", "");
				list[i] = j;

				 String[] list1 = list[i].split(":");
				 
				
				if (list1[0].equals("lat")  ) {
					test.add(list1[1]);
				}
				if ( list1[0].equals("lon") ) {
								
					test.add(list1[1]);
							}
				if ( list1[0].equals("place_id") ) {
					
					test.add(list1[1]);
				}
				if ( list1[0].equals("country") ) {
					
					test.add(list1[1]);
				}
			}
			System.out.println(test);
			location.setCountry(test.get(3));
			location.setPlace_id(test.get(0));
			location.setLatitude(test.get(1));
			location.setLongitud(test.get(2));
			
			System.out.println(location);
			return "details";
	  }
	 
	 @GetMapping("/map")
	 public String locationSubmit(Model model) {
		 
			model.addAttribute("Location", new Location());
		
			System.out.println("Done");
			return "locationInterface";
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

	// forgot password req.
	@GetMapping("/forgot-password")
	public String forgot_password(Model model) {
		model.addAttribute("title", "Forgot Password(Email)");
		return "forgotpassword";
	}

	@PostMapping("/do_checkemail")
	public String do_checkemail(@RequestParam("email") String email, Model model, HttpSession session) {

		User user = userRepository.getUserByUsername(email);
		if (user != null) {

			// create the OTP
			Random random = new Random();
			StringBuilder otp = new StringBuilder(6);
			for (int i = 0; i < 6; i++) {
				otp.append(random.nextInt(10));
			}
			int OTP = Integer.parseInt(otp.toString());
			System.out.println("OTP :" + OTP);

//	        /sending Email OTP to user email
			boolean check = EmailService.sendEmail(email, OTP);

			if (check == true) {

				ForgotPassword save = forgotPasswordRepository.save(new ForgotPassword(email, OTP));
				System.out.println(save + "From DB");
				session.setAttribute("message", new Message("Email Is Sended to " + email, "alert-success"));

			} else {
				session.setAttribute("message",
						new Message("Pleased Try Again, Email is Not Sended!!", "alert-danger"));
				return "redirect:/forgot-password";

			}

		} else {
			System.out.println("Apsend");
			session.setAttribute("message", new Message("Email-Id is Wrong !!", "alert-danger"));
			return "redirect:/forgot-password";
		}
		model.addAttribute("title", "Forgot Password(Email) OTP page");
		model.addAttribute("user", user);
		return "userOTP";
	}

	@PostMapping("/do_checkemailAndotp")
	public String do_checkemailAndotp(@RequestParam("email") String email, @RequestParam("otp") String Sotp,
			Model model, HttpSession session) {

		System.out.println(email + " : " + Sotp);
		Integer otp = Integer.parseInt(Sotp);
		ForgotPassword first = new ForgotPassword(email, otp);
		ForgotPassword byId = forgotPasswordRepository.getByEmailAndOtp(email,otp);
		System.out.println(byId+" : "+first);
		if (first.getEmail().toLowerCase().equals(byId.getEmail())||first.getOtp()==byId.getOtp()) {
			System.out.println("Matched");
			model.addAttribute("user", userRepository.getUserByUsername(email));
		} else {
			session.setAttribute("message", new Message("OTP is Not Matched", "alert-danger"));
			return "redirect:/forgot-password";
		}

		return "resetpassword";
	}

	@PostMapping("/do_resetpassword")
	public String do_resetpassword(@RequestParam("email") String email, @RequestParam("newpassword") String newPassword,
			Model model, HttpSession session) {

		if (newPassword.length() >= 6) {
			User user = userRepository.getUserByUsername(email);
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			session.setAttribute("message", new Message("Password is Updated", "alert-success"));
			forgotPasswordRepository.deleteById(email);
			return "redirect:/signin";
		} else {
			session.setAttribute("message", new Message("Password Must upTo 6 Characters", "alert-danger"));
			return "resetpassword";
		}

	}

}
