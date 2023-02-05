/**
 *
 */
package mrs.domain.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mrs.domain.model.User;

/**
 * ユーザデータ登録を行うリポジトリ
 * @author 山根
 */
@Repository
public interface AddUserRepository extends JpaRepository<User, String> {
}
