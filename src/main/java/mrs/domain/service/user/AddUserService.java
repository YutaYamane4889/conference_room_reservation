package mrs.domain.service.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mrs.domain.model.User;
import mrs.domain.repository.user.AddUserRepository;

@Service
public class AddUserService {
	@Autowired AddUserRepository repository;
	@Autowired PasswordEncoder passwordEncoder;


	public void addUser(User info) {
		Optional<User> user = repository.findById(info.getUserId());
		if (!user.isPresent()) {
			String tmp = info.getPassword();
			info.setPassword(passwordEncoder.encode(tmp));
			repository.save(info);
		}else {
			throw new AlreadyRegisteredException("入力されたユーザIDは既に登録されています。");
		}
	}
}
