package mrs.domain.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mrs.domain.model.User;

/**
 * ユーザ編集リポジトリ
 * @author 山根
 *
 */
@Repository
public interface EditUserRepository extends JpaRepository<User, String> {
}
