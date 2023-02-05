package mrs.domain.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mrs.domain.model.User;

/**
 *
 * ユーザー名からReservationUserDetailsを作成するインターフェイス
 * Userクラス、主キークラスのString指定
 * @author iwamoto
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

	public List<User> findByUserIdLikeOrLastNameLikeOrFirstNameLikeOrderByUserIdAsc(String user_id,String last_name,String first_name);
}
