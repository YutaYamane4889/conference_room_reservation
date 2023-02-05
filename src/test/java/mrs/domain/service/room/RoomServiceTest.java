package mrs.domain.service.room;

import static com.ninja_squad.dbsetup.Operations.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.LocalDate;
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

import mrs.domain.model.ReservableRoom;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoomServiceTest {
	@Autowired RoomService service;
	@Autowired DataSource dataSource;
	//セットアップをスキップする
	private static DbSetupTracker TRACKER = new DbSetupTracker();
	//データ削除対象テーブル指定
	public static final Operation DELETE_ALL = Operations.deleteAllFrom("meeting_room","reservable_room");
	//テストデータの準備例
	public static final Operation INSERT =
	        sequenceOf(
	        		insertInto("meeting_room")
	        		.columns("room_id", "room_name")
	        		.values(1,"テスト１")
	        		.values(2,"テスト２")
	        		.values(3,"テスト３")
					.build(),
					insertInto("reservable_room")
	        		.columns("reserved_date", "room_id")
	        		.values("2022-08-10",1)
	        		.values("2022-08-10",2)
	        		.values("2022-08-11",2)
	        		.values("2022-08-12",3)
					.build());
	@Before
	public void before(){
	Destination dest = new DataSourceDestination(dataSource);
	Operation ops = Operations.sequenceOf(DELETE_ALL, INSERT);
	DbSetup dbSetup = new DbSetup(dest, ops);
	dbSetup.launch();
	}

	@Test
	public void _8月10日指定で2件返ってくるか検証() {
		TRACKER.skipNextLaunch();
		List<ReservableRoom> data = service.findReservableRooms(LocalDate.of(2022, 8, 10));
		assertThat(data.size(), is(2));
	}

	@Test
	public void _8月15日指定で0件返ってくるか検証() {
		TRACKER.skipNextLaunch();
		List<ReservableRoom> data = service.findReservableRooms(LocalDate.of(2022, 8, 15));
		assertThat(data.size(), is(0));
	}

	@Test
	public void 全件取得で2件IDが昇順で返ってくるか検証() {
		TRACKER.skipNextLaunch();
		List<ReservableRoom> data = service.findReservableRooms(LocalDate.of(2022, 8, 10));
		assertThat((data.get(0)).getMeetingRoom().getRoomId(), is(1));
		assertThat((data.get(1)).getMeetingRoom().getRoomId(), is(2));
	}

	@Test
	public void 全件取得で会議室名が取得できるか検証() {
		TRACKER.skipNextLaunch();
		List<ReservableRoom> data = service.findReservableRooms(LocalDate.of(2022, 8, 10));
		assertThat((data.get(0)).getMeetingRoom().getRoomName(), is("テスト１"));
		assertThat((data.get(1)).getMeetingRoom().getRoomName(), is("テスト２"));
	}
}

