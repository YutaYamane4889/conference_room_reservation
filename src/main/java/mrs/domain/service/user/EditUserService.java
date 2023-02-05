package mrs.domain.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mrs.domain.model.User;
import mrs.domain.repository.user.EditUserRepository;

@Service
public class EditUserService {
	@Autowired EditUserRepository repository;
	@Autowired PasswordEncoder passwordEncoder;

	/**
	 * IDからユーザ検索するメソッド
	 * @param UserId
	 * @return User
	 */
	public User serchUser(String UserId) {
		return repository.findById(UserId).get();
	}

	/**
	 * パスワードをハッシュ化して返すメソッド
	 * @param password
	 * @return
	 */
	public String encordPassword(String password) {
		return passwordEncoder.encode(password);
	}

	/**
	 * パスワードが空だったらユーザテーブルのハッシュ化パスワードを返すメソッド
	 * @param info
	 * @return password
	 */
	public String checkPasswordEmpty(User info) {
		if(info.getPassword() == null) {
			User seachUser = serchUser(info.getUserId());
			return encordPassword(seachUser.getPassword());
		}
		return encordPassword(info.getPassword());
	}

	/**
	 * ユーザ編集メソッド
	 * @param info
	 */
	public void editUser(User info) {
		info.setPassword(checkPasswordEmpty(info));
		repository.save(info);
	}

}
