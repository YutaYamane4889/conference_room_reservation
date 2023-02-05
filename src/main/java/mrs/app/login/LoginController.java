package mrs.app.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * ログインコントローラ
 * @author 山根
 *
 */
@Controller
public class LoginController {

	/**
	 * ログイン画面を返す
	 * @param mav ModelAndView
	 * @return
	 */
	@RequestMapping(value = "/loginForm",method = RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("login/loginForm");
		return mav;
	}
}
