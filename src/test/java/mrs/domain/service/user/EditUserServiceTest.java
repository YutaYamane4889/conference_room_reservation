package mrs.domain.service.user;

import static com.ninja_squad.dbsetup.Operations.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

import mrs.domain.model.RoleName;
import mrs.domain.model.User;
import mrs.domain.repository.user.UserRepository;
@RunWith(SpringRunner.class)
@SpringBootTest
public class EditUserServiceTest {
	@Autowired EditUserService service;
	@Autowired DataSource dataSource;
	@Autowired UserRepository repository;
	@Autowired PasswordEncoder passwordEncoder;

	//セットアップをスキップする
	private static DbSetupTracker TRACKER = new DbSetupTracker();
	//データ削除対象テーブル指定
	public static final Operation DELETE_ALL = Operations.deleteAllFrom("usr");
	//テストデータの準備例
	public static final Operation INSERT =
	        sequenceOf(
	        		insertInto("usr")
	        		.columns("user_id", "last_name","first_name","password","role_name")
	        		.values("aaaa","テスト1","テスト1","pass","USER")
	        		.values("bbbb","テスト2","テスト2","pass","ADMIN")
	        		.values("cccc","テスト3","テスト3","pass","USER")
					.build());
	@Before
	public void before(){
	Destination dest = new DataSourceDestination(dataSource);
	Operation ops = Operations.sequenceOf(DELETE_ALL, INSERT);
	DbSetup dbSetup = new DbSetup(dest, ops);
	dbSetup.launch();
	}

	@Test
	public void パスワードが空だったらテーブルのが返されるか検証() {
		User user = new User();
		user.setUserId("aaaa");
		user.setPassword(null);
		assertThat(passwordEncoder.matches("pass",service.checkPasswordEmpty(user)),is(true));
	}

	@Test
	public void パスワードが入力されてたらそのまま返されるか検証() {
		User user = new User();
		user.setUserId("aaaa");
		user.setPassword("test");
		assertThat(passwordEncoder.matches("test",service.checkPasswordEmpty(user)),is(true));
	}

	public void 正常に変更されるか検証() {
		User info = new User();
		info.setUserId("aaaa");
		info.setFirstName("テスト4");
		info.setLastName("テスト4");
		info.setPassword("passpass");
		service.editUser(info);
		List<User> data = repository.findAll();
		assertThat(data.get(0).getUserId(),is("aaaa"));
		assertThat(data.get(0).getFirstName(),is("テスト4"));
		assertThat(data.get(0).getLastName(),is("テスト4"));
		assertThat(data.get(0).getPassword(),is("passpass"));
	}

	public void 他のデータに影響が出ないか検証() {
		User info = new User();
		info.setUserId("aaaa");
		info.setFirstName("テスト4");
		info.setLastName("テスト4");
		info.setPassword("passpass");
		service.editUser(info);
		List<User> data = repository.findAll();
		assertThat(data.get(0).getUserId(),is("aaaa"));
		assertThat(data.get(0).getFirstName(),is("テスト4"));
		assertThat(data.get(0).getLastName(),is("テスト4"));
		assertThat(data.get(0).getPassword(),is("passpass"));

		assertThat(data.get(1).getUserId(),is("bbbb"));
		assertThat(data.get(1).getFirstName(),is("テスト2"));
		assertThat(data.get(1).getLastName(),is("テスト2"));
		assertThat(data.get(1).getPassword(),is("pass"));
		assertThat(data.get(1).getRoleName(),is(RoleName.ADMIN));

		assertThat(data.get(2).getUserId(),is("cccc"));
		assertThat(data.get(2).getFirstName(),is("テスト3"));
		assertThat(data.get(2).getLastName(),is("テスト3"));
		assertThat(data.get(2).getPassword(),is("pass"));
		assertThat(data.get(2).getRoleName(),is(RoleName.USER));
	}
}
