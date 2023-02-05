package mrs.app.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mrs.domain.model.User;
import mrs.domain.service.user.UserService;

@Controller
public class UserController {

	@Autowired UserService service;
	/**
	 * ユーザ一覧画面初期表示処理
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/manage",method = RequestMethod.GET)
	public String listUsers(Model model) {
		List<User> userList = service.findAllUsers();
		model.addAttribute("userList",userList);
		return "management/user";
	}

	/**
	 * ユーザ一覧画面データ検索表示処理
	 * @param user_id
	 * @param first_name
	 * @param last_name
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/manage",method = RequestMethod.POST)
	public String listUsers(@RequestParam("userId") String userId,@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,Model model) {
		List<User> userList = service.findUsers(userId, lastName, firstName);
		model.addAttribute(userList);
		return "management/user";
	}
}
