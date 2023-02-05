package mrs.domain.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mrs.domain.model.User;
import mrs.domain.repository.user.UserRepository;

/**
 * ユーザーサービスクラス
 * @author 山根
 *
 */
@Service
public class UserService {
	@Autowired UserRepository userRepository;
	/**
	 * 全ユーザを返すメソッド
	 * @return List<User>
	 */
	public List<User> findAllUsers(){
		return userRepository.findAll();
	}

	/**
	 * ユーザー一覧データ検索処理メソッド
	 * @param user_id
	 * @param last_name
	 * @param first_name
	 * @return
	 */
	public List<User> findUsers(String user_id,String last_name,String first_name){
		if(!user_id.isEmpty()) {
			user_id = "%" + user_id + "%";
		}
		if(!last_name.isEmpty()) {
			last_name = "%" + last_name + "%";
		}
		if(!first_name.isEmpty()) {
			first_name = "%" + first_name + "%";
		}

		if(user_id.isEmpty()&&last_name.isEmpty()&&first_name.isEmpty()) {
			user_id = "%" + user_id + "%";
			last_name = "%" + last_name + "%";
			first_name = "%" + first_name + "%";
		}


		return userRepository.findByUserIdLikeOrLastNameLikeOrFirstNameLikeOrderByUserIdAsc(user_id,last_name, first_name);
	}
}
