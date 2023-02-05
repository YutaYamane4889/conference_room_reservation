package mrs.domain.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mrs.domain.repository.user.DeleteUserRepository;

/**
 * ユーザ削除サービス
 * @author 山根
 *
 */
@Service
public class DeleteUserService {
	@Autowired DeleteUserRepository repository;

	/**
	 * ユーザ削除メソッド
	 * @param userId
	 */
	public void deleteUser(String userId) {
		try {
			repository.deleteById(userId);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// TODO: handle exception
		}

	}


}
