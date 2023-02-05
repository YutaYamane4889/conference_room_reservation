package mrs.app.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mrs.domain.model.User;
import mrs.domain.service.user.DeleteUserService;
import mrs.domain.service.user.EditUserService;

/**
 * ユーザ編集画面制御
 * @author 山根
 *
 */
@Controller
public class EditUserController {
	@Autowired EditUserService service;
	@Autowired DeleteUserService DeleteUserService;

	/**
	 * Formオブジェクトを初期化するModelAttributeメソッド
	 * @return
	 */
	@ModelAttribute
	public EditUserForm setUpEditUserForm() {
		EditUserForm editUserForm = new EditUserForm();
		return editUserForm;
	}
	/**
	 * 編集画面初期表示処理
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/edit/{userId}",method = RequestMethod.GET)
	public String inputScreen(Model model,@PathVariable("userId") String userId,EditUserForm editForm) {
		User userData = service.serchUser(userId);
		editForm.setUserId(userData.getUserId());
		editForm.setLastName(userData.getLastName());
		editForm.setFirstName(userData.getFirstName());
		editForm.setRoleName(userData.getRoleName());
		model.addAttribute("editUserForm",editForm);
		return "management/edit";
	}

	@RequestMapping(value = "/edit/{userId}",method = RequestMethod.POST,params = "edit")
	public String editUser(@Validated @ModelAttribute ("editUserForm") EditUserForm editForm,BindingResult result,Model model,@PathVariable("userId") String userId) {
		if (result.hasErrors()) {
			System.out.println("バリデーションエラーあり");
			return inputScreen(model,userId,editForm);
		}else {
			User userData = new User();
			userData.setUserId(editForm.getUserId());
			userData.setLastName(editForm.getLastName());
			userData.setFirstName(editForm.getFirstName());
			if(!editForm.getPassword().isEmpty()) {
				if(editForm.getPassword().length() > 7 && editForm.getPassword().length() < 21) {
					userData.setPassword(editForm.getPassword());
				}else {
					model.addAttribute("passwordError","パスワードは8文字以上20文字以内で入力してください。");
					return inputScreen(model,userId,editForm);
				}
			}
			userData.setRoleName(editForm.getRoleName());
			service.editUser(userData);
			return inputScreen(model,userId,editForm);
		}
	}
	@RequestMapping(value = "/edit/{userId}",method = RequestMethod.POST,params = "back")
	public String back(Model model) {
		return "redirect:/manage";
	}

	@RequestMapping(value = "/edit/{userId}",method = RequestMethod.POST,params = "delete")
	public String deleteUser(Model model,@PathVariable("userId") String userId) {
		DeleteUserService.deleteUser(userId);
		return "redirect:/manage";
	}
}
