package mrs.domain.repository.room;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;

/**
 * ReservableRoomエンティティ用のRepository
 * @author 山根
 */
@Repository
public interface ReservableRoomRepository extends JpaRepository<ReservableRoom, ReservableRoomId> {
	/**
	 * ログイン日に予約可能なReservableRoomのリストを会議室ID順に取得するメソッド
	 * @return ReservableRoomのリスト
	 */
	public List<ReservableRoom> findByReservableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc(LocalDate date);


	/**
	 * 排他処理のため指定の予約可能会議室情報をロックする
	 * @param id
	 * @return ReservableRoom
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	public ReservableRoom findOneForUpdateByReservableRoomId(ReservableRoomId id);
}
