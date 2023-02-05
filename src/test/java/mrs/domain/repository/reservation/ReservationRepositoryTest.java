package mrs.domain.repository.reservation;

import static com.ninja_squad.dbsetup.Operations.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
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

import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationRepositoryTest {
	@Autowired ReservationRepository repository;
	@Autowired DataSource dataSource;
	//セットアップをスキップする
	private static DbSetupTracker TRACKER = new DbSetupTracker();
	//データ削除対象テーブル指定
	public static final Operation DELETE_ALL = Operations.deleteAllFrom("reservation","reservable_room");
	//テストデータの準備例
	public static final Operation INSERT =
	        sequenceOf(
	        		insertInto("reservable_room")
	        		.columns("reserved_date", "room_id")
	        		.values("2022-08-23",1)
	        		.values("2022-08-23",2)
	        		.values("2022-08-22",1)
	        		.values("2022-08-24",1)
	        		.build(),
	        		insertInto("reservation")
	        		.columns("reservation_id", "end_time","start_time","reserved_date","room_id","user_id")
	        		.values(1,"10:00:00","09:00:00","2022-08-23",1,"aaaa")
	        		.values(2,"11:00:00","10:00:00","2022-08-23",1,"aaaa")
	        		.values(3,"11:00:00","10:00:00","2022-08-22",1,"aaaa")
	        		.values(4,"11:00:00","10:00:00","2022-08-23",2,"aaaa")
	        		.build());
	@Before
	public void before(){
	Destination dest = new DataSourceDestination(dataSource);
	Operation ops = Operations.sequenceOf(DELETE_ALL, INSERT);
	DbSetup dbSetup = new DbSetup(dest, ops);
	dbSetup.launch();
	}

	@Test
	public void 指定日取得で2件返ってくるか検証(){
		ReservableRoomId roomId = new ReservableRoomId(1,LocalDate.of(2022, 8, 23));
		List<Reservation> data = repository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(roomId);
		assertThat(data.size(),is(2));
	}

	@Test
	public void 予約開始時間順で返ってくるか検証(){
		ReservableRoomId roomId = new ReservableRoomId(1,LocalDate.of(2022, 8, 23));
		List<Reservation> data = repository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(roomId);
		assertThat(data.get(0).getStartTime(),is(LocalTime.of(9, 0)));
		assertThat(data.get(1).getStartTime(),is(LocalTime.of(10, 0)));
	}

	@Test
	public void 指定日取得で想定の2件返ってくるか検証(){
		ReservableRoomId roomId = new ReservableRoomId(1,LocalDate.of(2022, 8, 23));
		List<Reservation> data = repository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(roomId);
		assertThat(data.get(0).getStartTime(),is(LocalTime.of(9, 0)));
		assertThat(data.get(0).getEndTime(),is(LocalTime.of(10, 0)));
		assertThat(data.get(0).getReservableRoom().getReservableRoomId().getReservedDate(),is(LocalDate.of(2022, 8, 23)));
		assertThat(data.get(0).getReservationId(),is(1));
		assertThat(data.get(0).getUser().getUserId(),is("aaaa"));
		assertThat(data.get(1).getStartTime(),is(LocalTime.of(10, 0)));
		assertThat(data.get(1).getEndTime(),is(LocalTime.of(11, 0)));
		assertThat(data.get(1).getReservableRoom().getReservableRoomId().getReservedDate(),is(LocalDate.of(2022, 8, 23)));
		assertThat(data.get(1).getReservationId(),is(2));
		assertThat(data.get(1).getUser().getUserId(),is("aaaa"));
	}

	@Test
	public void 指定日取得で0件返ってくるか検証(){
		ReservableRoomId roomId = new ReservableRoomId(1,LocalDate.of(2022, 8, 25));
		List<Reservation> data = repository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(roomId);
		assertThat(data.size(),is(0));
	}

	@Test
	public void 指定IDで0件返ってくるか検証(){
		ReservableRoomId roomId = new ReservableRoomId(5,LocalDate.of(2022, 8, 23));
		List<Reservation> data = repository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(roomId);
		assertThat(data.size(),is(0));
	}

	@Test
	public void 指定日付と指定IDで0件返ってくるか検証(){
		ReservableRoomId roomId = new ReservableRoomId(5,LocalDate.of(2022, 8, 25));
		List<Reservation> data = repository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(roomId);
		assertThat(data.size(),is(0));
	}

	@Test
	public void _指定日取得で0件返ってくるか検証(){
		ReservableRoomId roomId = new ReservableRoomId(1,LocalDate.of(2022, 8, 24));
		List<Reservation> data = repository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(roomId);
		assertThat(data.size(),is(0));
	}
}
