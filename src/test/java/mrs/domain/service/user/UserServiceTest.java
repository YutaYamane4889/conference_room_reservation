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
import org.springframework.test.context.junit4.SpringRunner;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

import mrs.domain.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
	@Autowired UserService service;
	@Autowired DataSource dataSource;
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
	public void ユーザー指定で1件返ってくるか検証() {
		List<User> data = service.findUsers("aaaa", "テスト1", "テスト1");
		assertThat(data.size(), is(1));
	}

	@Test
	public void ユーザー指定で1件正しく返ってくるか検証() {
		List<User> data = service.findUsers("aaaa", "テスト1", "テスト1");
		assertThat(data.get(0).getUserId(),is("aaaa"));
		assertThat(data.get(0).getLastName(),is("テスト1"));
		assertThat(data.get(0).getFirstName(),is("テスト1"));
	}

	@Test
	public void ユーザー名のみ指定で1件返ってくるか検証() {
		List<User> data = service.findUsers("", "テスト1", "");
		assertThat(data.size(), is(1));
	}

	@Test
	public void ユーザー名のみ指定で1件正しく返ってくるか検証() {
		List<User> data = service.findUsers("aaaa","","");
		assertThat(data.get(0).getUserId(),is("aaaa"));
		assertThat(data.get(0).getLastName(),is("テスト1"));
		assertThat(data.get(0).getFirstName(),is("テスト1"));
	}

	@Test
	public void ユーザー名あいまい検索のみ指定で1件正しく返ってくるか検証() {
		List<User> data = service.findUsers("a","","");
		assertThat(data.get(0).getUserId(),is("aaaa"));
		assertThat(data.get(0).getLastName(),is("テスト1"));
		assertThat(data.get(0).getFirstName(),is("テスト1"));
	}

	@Test
	public void ユーザーIDが昇順で正しく返ってくるか検証() {
		List<User> data = service.findUsers("","テスト","");
		assertThat(data.get(0).getUserId(),is("aaaa"));
		assertThat(data.get(1).getUserId(),is("bbbb"));
		assertThat(data.get(2).getUserId(),is("cccc"));
	}
}

