package mrs.domain.service.reservation;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

import mrs.domain.model.MeetingRoom;
import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.model.User;
import mrs.domain.repository.reservation.ReservationRepository;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationServiceTest {
	@Autowired ReservationService service;
	@Autowired DataSource dataSource;
	@Autowired ReservationRepository reservationRepository;
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
	        		.build(),
	        		insertInto("reservation")
	        		.columns("reservation_id", "end_time","start_time","reserved_date","room_id","user_id")
	        		.values(1,"10:00:00","09:00:00","2022-08-23",1,"aaaa")
	        		.values(2,"11:00:00","10:00:00","2022-08-23",1,"aaaa")
	        		.values(3,"11:00:00","10:00:00","2022-08-22",1,"aaaa")
	        		.values(4,"11:00:00","10:00:00","2022-08-23",2,"aaaa")
	        		.values(5,"11:00:00","10:00:00","2022-08-23",2,"bbbb")
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
		List<Reservation> data = service.findReservations(roomId);
		assertThat(data.size(),is(2));
	}

	@Test
	public void 予約開始時間順で返ってくるか検証(){
		ReservableRoomId roomId = new ReservableRoomId(1,LocalDate.of(2022, 8, 23));
		List<Reservation> data = service.findReservations(roomId);
		assertThat(data.get(0).getStartTime(),is(LocalTime.of(9, 0)));
		assertThat(data.get(1).getStartTime(),is(LocalTime.of(10, 0)));
	}

	@Test
	public void 指定日取得で想定の2件返ってくるか検証(){
		ReservableRoomId roomId = new ReservableRoomId(1,LocalDate.of(2022, 8, 23));
		List<Reservation> data = service.findReservations(roomId);
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
		List<Reservation> data = service.findReservations(roomId);
		assertThat(data.size(),is(0));
	}

	@Test
	public void 指定IDで0件返ってくるか検証(){
		ReservableRoomId roomId = new ReservableRoomId(5,LocalDate.of(2022, 8, 23));
		List<Reservation> data = service.findReservations(roomId);
		assertThat(data.size(),is(0));
	}

	@Test
	public void 指定日付と指定IDで0件返ってくるか検証(){
		ReservableRoomId roomId = new ReservableRoomId(5,LocalDate.of(2022, 8, 25));
		List<Reservation> data = service.findReservations(roomId);
		assertThat(data.size(),is(0));
	}

	@Test
	public void _指定日取得で0件返ってくるか検証(){
		ReservableRoomId roomId = new ReservableRoomId(1,LocalDate.of(2022, 8, 24));
		List<Reservation> data = service.findReservations(roomId);
		assertThat(data.size(),is(0));
	}

	@Test
	public void 予約できることの検証() {
		Reservation info = new Reservation();
		info.setStartTime(LocalTime.of(22, 0));
		info.setEndTime(LocalTime.of(23, 0));
		ReservableRoom room = new ReservableRoom();
		ReservableRoomId roomId = new ReservableRoomId(1,LocalDate.of(2022, 8, 23));
		room.setReservableRoomId(roomId);
		MeetingRoom meetingRoom = new MeetingRoom();
		meetingRoom.setRoomId(1);
		meetingRoom.setRoomName("テスト１");
		room.setMeetingRoom(meetingRoom);
		info.setReservableRoom(room);
		info.setReservationId(null);
		User user = new User();
		user.setUserId("aaaa");
		info.setUser(user);
		List<Reservation> data = service.findReservations(roomId);
		//セット前に指定日付で２件返ってくることの検証
		assertThat(data.size(),is(2));
		service.reserve(info);
		//セット後に指定日付で３件返ってくることの検証
		List<Reservation> data2 = service.findReservations(roomId);
		assertThat(data2.size(),is(3));

	}

	@Test
	public void 指定の日付会議室の組み合わせは予約できませんの検証() {
		Reservation info = new Reservation();
		info.setStartTime(LocalTime.of(22, 0));
		info.setEndTime(LocalTime.of(23, 0));
		ReservableRoom room = new ReservableRoom();
		ReservableRoomId roomId = new ReservableRoomId(1,LocalDate.of(2022, 8, 24));
		room.setReservableRoomId(roomId);
		MeetingRoom meetingRoom = new MeetingRoom();
		meetingRoom.setRoomId(1);
		meetingRoom.setRoomName("テスト１");
		room.setMeetingRoom(meetingRoom);
		info.setReservableRoom(room);
		info.setReservationId(null);
		User user = new User();
		user.setUserId("aaaa");
		info.setUser(user);

		try {
			service.reserve(info);
		}catch(UnavailableReservationException e) {
			assertThat(e.getMessage(),is("指定の日付・会議室の組み合わせは予約できません。"));
		}
	}

	@Test
	public void 入力の時間帯は既に予約済みですの検証() {
		Reservation info = new Reservation();
		info.setStartTime(LocalTime.of(9, 0));
		info.setEndTime(LocalTime.of(10, 0));
		ReservableRoom room = new ReservableRoom();
		ReservableRoomId roomId = new ReservableRoomId(1,LocalDate.of(2022, 8, 23));
		room.setReservableRoomId(roomId);
		MeetingRoom meetingRoom = new MeetingRoom();
		meetingRoom.setRoomId(1);
		meetingRoom.setRoomName("テスト１");
		room.setMeetingRoom(meetingRoom);
		info.setReservableRoom(room);
		info.setReservationId(null);
		User user = new User();
		user.setUserId("aaaa");
		info.setUser(user);

		try {
			service.reserve(info);
		}catch(AlreadyReservedException e) {
			assertThat(e.getMessage(),is("入力の時間帯は既に予約済みです。"));
		}
	}

	@Test
	public void 予約IDから予約情報を１件取得できるか検証(){
		Reservation info = service.findOne(1);
		assertThat(info.getStartTime(),is(LocalTime.of(9, 0)));
		assertThat(info.getEndTime(),is(LocalTime.of(10, 0)));
		assertThat(info.getReservableRoom().getReservableRoomId().getReservedDate(),is(LocalDate.of(2022, 8, 23)));
		assertThat(info.getReservableRoom().getReservableRoomId().getRoomId(),is(1));
		assertThat(info.getUser().getUserId(),is("aaaa"));
	}

	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void キャンセルで１件データが削除されるか検証(){
		Reservation info = new Reservation();
		info.setStartTime(LocalTime.of(9, 0));
		info.setEndTime(LocalTime.of(10, 0));
		ReservableRoom room = new ReservableRoom();
		ReservableRoomId roomId = new ReservableRoomId(1,LocalDate.of(2022, 8, 23));
		room.setReservableRoomId(roomId);
		MeetingRoom meetingRoom = new MeetingRoom();
		meetingRoom.setRoomId(1);
		meetingRoom.setRoomName("テスト１");
		room.setMeetingRoom(meetingRoom);
		info.setReservableRoom(room);
		info.setReservationId(1);
		User user = new User();
		user.setUserId("aaaa");
		info.setUser(user);

		//キャンセル前に指定日付で２件返ってくることの検証
		List<Reservation> data = service.findReservations(roomId);
		assertThat(data.size(),is(2));
		service.cancel(info);
		//キャンセル後に指定日付で２件返ってくることの検証
		List<Reservation> data2 = service.findReservations(roomId);
		assertThat(data2.size(),is(1));
	}

	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void 存在しないデータはキャンセルできないこと(){
		Reservation info = new Reservation();
		info.setStartTime(LocalTime.of(9, 0));
		info.setEndTime(LocalTime.of(10, 0));
		ReservableRoom room = new ReservableRoom();
		ReservableRoomId roomId = new ReservableRoomId(1,LocalDate.of(2022, 8, 25));
		room.setReservableRoomId(roomId);
		MeetingRoom meetingRoom = new MeetingRoom();
		meetingRoom.setRoomId(1);
		meetingRoom.setRoomName("テスト１");
		room.setMeetingRoom(meetingRoom);
		info.setReservableRoom(room);
		info.setReservationId(1);
		User user = new User();
		user.setUserId("aaaa");
		info.setUser(user);

		//キャンセル前に全件取得で5件返ってくることの検証
		List<Reservation> data = reservationRepository.findAll();
		assertThat(data.size(),is(5));
		try {
			service.cancel(info);
		} catch (Exception e) {
			// TODO: handle exception
			//キャンセル後に全件取得で5件返ってくることの検証
			List<Reservation> data2 = reservationRepository.findAll();
			assertThat(data2.size(),is(5));
		}

	}
}
