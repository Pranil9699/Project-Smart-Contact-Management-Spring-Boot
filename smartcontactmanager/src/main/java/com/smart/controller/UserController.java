package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository repository;
	@Autowired
	private ContactRepository contactrepository;

	@ModelAttribute
	public void addCommonAttribute(Model model, Principal principal) {
		String userName = principal.getName();
		User user = repository.getUserByUsername(userName);
		System.out.println(user);
		model.addAttribute("user" , user);
	}

	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {

		model.addAttribute("title", "Normal User dashboard");
		return "normal/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}

	@PostMapping("/process-form")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("image") MultipartFile file,
			Principal principal, Model model, HttpSession session) {
		try {
			String userName = principal.getName();
			User user = repository.getUserByUsername(userName);
			contact.setUser(user);

			if (file.isEmpty()) {
				System.out.println("We haven't chosen any file");
				contact.setImageURL("icons8-no-camera.gif");
			} else {
				contact.setImageURL(file.getOriginalFilename());
				System.out.println("Image received");

//				// Save the file to a temporary location
//				Path tempFilePath = Files.createTempFile("upload_", file.getOriginalFilename());
//				Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);
//
//				// Move the file to the desired location
//				File saveFile = new ClassPathResource("static/img").getFile();
//				Path destinationFilePath = Paths
//						.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
//				Files.move(tempFilePath, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("File is successfully saved in the folder");
			}

			System.out.println(contact);
			user.getContacts().add(contact);
			User saveUser = repository.save(user);
			System.out.println(saveUser);
			model.addAttribute("title", "Add Contact");
			session.setAttribute("message", new Message("SuccessFully Contact Added !!", "alert-success"));
		} catch (Exception ex) {
			ex.printStackTrace();
			session.setAttribute("message", new Message("Something Went Wrong !!" + ex.getMessage(), "alert-danger"));
		}
		return "normal/add_contact_form";
	}

//	@GetMapping("/show-contact")
//	public String show_contact(Model model,Principal principal) {
//		String name = principal.getName();
//		User user = repository.getUserByUsername(name);
//		List<Contact> findByUser = contactrepository.findContactsByUser(user.getId());
//		for (Contact contact : findByUser) {
//			System.out.println(" contact name is "+contact.getName());
//		}
//	
//		model.addAttribute("contact", findByUser);
//		model.addAttribute("title", "Show Contacts");
//		return "normal/show_contact";
//	}
	@GetMapping("/show-contact/{page}")
	public String show_contact(@PathVariable("page") Integer page, Model model, Principal principal) {
		String name = principal.getName();
		User user = repository.getUserByUsername(name);

		Pageable pageable = PageRequest.of(page, 5);

		Page<Contact> findContactsByUser = contactrepository.findContactsByUser(user.getId(), pageable);

		model.addAttribute("contact", findContactsByUser);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", findContactsByUser.getTotalPages());

		model.addAttribute("title", "Show Contacts");
		return "normal/show_contact";
	}

	// showing perticular contact details.
	@GetMapping("/{cid}/contact")
	public String showContactDetail(@PathVariable("cid") Integer integer, Model model, Principal principal) {

		Optional<Contact> findById = contactrepository.findById(integer);
		Contact contact = null;
		if (!findById.isEmpty()) {
			contact = findById.get();

			String name = principal.getName();
			User user = repository.getUserByUsername(name);

			if (user.getId() == contact.getUser().getId()) {
				model.addAttribute("Scontact", contact);
				model.addAttribute("title", "Contact-" + contact.getName());
			} else {
				model.addAttribute("title", "Contact-?");
			}
		}
		// System.out.println(integer);

		return "normal/contact_detail";
	}

	// delete the contact handler
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model model, Principal principal,
			HttpSession session) {

		Optional<Contact> findById = contactrepository.findById(cid);
		Contact contact = null;
		if (!findById.isEmpty()) {
			contact = findById.get();

			String name = principal.getName();
			User user = repository.getUserByUsername(name);

			if (user.getId() == contact.getUser().getId()) {

				user.getContacts().remove(contact);

				repository.save(user);

//				
//				contact.setUser(null);
//				Contact save = contactrepository.save(contact);
				// contactrepository.delete(save);

				System.out.println("Contact is Deleted...");
				session.setAttribute("message", new Message("Contact is Deleted...", "alert-success"));
			} else {
				System.out.println("Contact is not Deleted...");
				session.setAttribute("message", new Message("Contact is not Deleted...", "alert-danger"));
			}
		} else {
			session.setAttribute("message", new Message("Contact Not Found To Delete", "alert-danger"));
		}
		return "redirect:/user/show-contact/0";
	}

	// open update form handler
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer integer, Model model) {

		model.addAttribute("title", "Update Contact");
		Contact contact = contactrepository.findById(integer).get();
		model.addAttribute("contact", contact);
		return "normal/update_form";
	}

	// update contact handler
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("image") MultipartFile file, Model m,
			HttpSession session, Principal principal) {

		try {

			// old contact details
			Contact oldcontact = contactrepository.findById(contact.getCid()).get();

			// check the file is empty or not

			if (!file.isEmpty()) {
				// file work
				// rewrite

//				delete the old pic

				File deletefile = new ClassPathResource("static/img").getFile();
				File file2 = new File(deletefile, oldcontact.getImageURL());
				file2.delete();

//				update the new pic

				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImageURL(file.getOriginalFilename());

			} else {

				// if file not select then put old image as it is
				contact.setImageURL(oldcontact.getImageURL());
			}

			String name = principal.getName();
			User user = repository.getUserByUsername(name);
			contact.setUser(user);
			this.contactrepository.save(contact);
			session.setAttribute("message", new Message("Your Contact is Updated...", "alert-success"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		System.out.println(contact.getCid());
		return "redirect:/user/" + contact.getCid() + "/contact";

	}
	
	// showing the user profile handler
	@GetMapping("/profile")
	public String  profileHandler(Model model) {
		
		model.addAttribute("title","User Profile Page");
		return "normal/profile";
	}
}
