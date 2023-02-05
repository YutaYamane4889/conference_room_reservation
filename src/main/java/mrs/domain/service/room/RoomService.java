package mrs.domain.service.room;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mrs.domain.model.MeetingRoom;
import mrs.domain.model.ReservableRoom;
import mrs.domain.repository.room.MeetingRoomRepository;
import mrs.domain.repository.room.ReservableRoomRepository;

/**
 * Roomサービスクラス
 * @author 山根
 *
 */
@Service
public class RoomService {
	@Autowired ReservableRoomRepository reservableRoomRepository;
	@Autowired MeetingRoomRepository meetingRoomRepository;

	/** 日付から予約可能な会議室をリストで返すメソッド
	 *
	 * @param date
	 * @return リストReservableRoom
	 */
	public List<ReservableRoom> findReservableRooms(LocalDate date){
		return reservableRoomRepository.findByReservableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc(date);
	}

	/**
	 * 指定の会議室IDに紐づく貸会議室情報を１件取得するメソッド。
	 * @param id
	 * @return MeetingRoom
	 */
	public MeetingRoom findMeetingRoom(int id) {
		return meetingRoomRepository.findById(id).get();
	}
}
