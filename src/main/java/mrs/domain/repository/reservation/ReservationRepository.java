package mrs.domain.repository.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;

/**
 * Reservationエンティティ用のRepository
 * @author 山根
 *
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	/**
	 * 予約リストを予約開始時間順に取得するメソッド。
	 * @param ReservableRoomId
	 * @return Reservationリスト
	 */
	public List<Reservation> findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(ReservableRoomId id);
}
