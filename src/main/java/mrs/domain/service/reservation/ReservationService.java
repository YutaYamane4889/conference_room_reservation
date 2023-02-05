package mrs.domain.service.reservation;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.repository.reservation.ReservationRepository;
import mrs.domain.repository.room.ReservableRoomRepository;

/**
 * ReservationService
 * @author 山根
 *
 */
@Service
@Transactional
public class ReservationService {
	@Autowired ReservationRepository reservationRepository;
	@Autowired ReservableRoomRepository reservableRoomRepository;

	/**
	 * 予約可能貸会議室IDに紐づく予約情報一覧を取得するメソッド。
	 * @param ReservableRoomId
	 * @return Reservationリスト
	 */
	public List<Reservation> findReservations(ReservableRoomId id){
		return reservationRepository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(id);
	}

	/**
	 * 予約処理実行前に排他処理を実行しておき、複数のユーザによる登録実行されないようロックする。
	   予約が可能かどうかチェックし、予約が未登録の場合に登録実行する。
	 *
	 */
	public void reserve(Reservation info) {
		ReservableRoom room = reservableRoomRepository.findOneForUpdateByReservableRoomId(info.getReservableRoom().getReservableRoomId());
		if (room == null) {
			throw new UnavailableReservationException("指定の日付・会議室の組み合わせは予約できません。");
		}
		List<Reservation> checkRoom = reservationRepository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(info.getReservableRoom().getReservableRoomId());

		for(int i=0;i<checkRoom.size();i++) {
			if(checkRoom.get(i).overlap(info)) {
				throw new AlreadyReservedException("入力の時間帯は既に予約済みです。");
			}
		}
		reservationRepository.save(info);
	}

	/**
	 * 予約IDから予約情報を１件取得
	 * @param id 予約ID
	 * @return
	 */
	public Reservation findOne(int id) {
		return reservationRepository.findById(id).get();
	}

	/**
	 * 指定の予約をキャンセルする。
	 * @param info Reservation 予約情報
	 */
	@PreAuthorize("hasRole('ADMIN') or #reservation.user.userId == principal.user.userId")
	public void cancel(@P(value="reservation") Reservation reservation) {
		reservationRepository.delete(reservation);
	}
}
