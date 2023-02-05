package mrs.app.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mrs.domain.model.User;
import mrs.domain.service.user.AddUserService;
import mrs.domain.service.user.AlreadyRegisteredException;

/**
 * ユーザ新規登録コントローラ
 * @author 山根
 */
@Controller
public class AddUserController {
	@Autowired AddUserService service;
	/**
	 * Formオブジェクトを初期化するModelAttributeメソッド
	 * @return
	 */
	@ModelAttribute
	public AddUserForm setUpAddUserForm() {
		AddUserForm form = new AddUserForm();
		return form;
	}
	/**
	 * 入力画面表示制御
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/register",method = RequestMethod.GET)
	public String listUsers(Model model) {
		return "management/add";
	}

	@RequestMapping(value = "/register",method = RequestMethod.POST,params = "sinki")
	public String addUser(@Validated AddUserForm form,BindingResult result,Model model) {
		User user = new User();
		user.setUserId(form.getUserId());
		user.setLastName(form.getLastName());
		user.setFirstName(form.getFirstName());
		user.setPassword(form.getPassword());
		user.setRoleName(form.getRoleName());
		if (result.hasErrors()) {
			return listUsers(model);
		}
		try {
			service.addUser(user);
		} catch (AlreadyRegisteredException e) {
			model.addAttribute("err",e.getMessage());
			return "management/add";
		}
		return "management/add";
	}

	@RequestMapping(value = "/register",method = RequestMethod.POST,params = "back")
	public String back(Model model) {
		return "redirect:/manage";
	}

}
