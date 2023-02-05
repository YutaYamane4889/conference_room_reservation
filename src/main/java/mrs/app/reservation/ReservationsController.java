package mrs.app.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.service.reservation.AlreadyReservedException;
import mrs.domain.service.reservation.ReservationService;
import mrs.domain.service.reservation.UnavailableReservationException;
import mrs.domain.service.room.RoomService;
import mrs.domain.service.user.ReservationUserDetails;

/**
 * ReservationsController
 * @author 山根
 */
@Controller
public class ReservationsController {
	@Autowired ReservationService reservationService;
	@Autowired RoomService roomService;

	/**
	 * Formオブジェクトを初期化し、開始時刻と終了時刻を設定するModelAttributeメソッド
	 * @return
	 */
	@ModelAttribute
	public ReservationForm setUpReservationForm() {
		ReservationForm form = new ReservationForm();
		form.setStartTime(LocalTime.of(9, 0));
		form.setEndTime(LocalTime.of(10, 0));
		return form;
	}

	/**
	 * 予約画面の初期表示。
	 * @param date
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/reservations/{date}/{id}",method = RequestMethod.GET)
	public String reserveForm(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,@PathVariable("id") int id,Model model) {
		ArrayList<LocalTime> timeList = new ArrayList<LocalTime>();
		LocalTime time = LocalTime.of(9, 0);
		for(int i = 0;i<=28;i++) {
			timeList.add(time);
			time = time.plusMinutes(30);
		}
		ReservableRoomId roomId = new ReservableRoomId(id,date);
		model.addAttribute("timeList",timeList);
		model.addAttribute("room",roomService.findMeetingRoom(id));
		model.addAttribute("reservations", reservationService.findReservations(roomId));
		return "reservation/reserveForm";
		}

	/**
	 *予約処理の制御。
	 * @param form ReservationForm
	 * @param result BindingResult
	 * @param user ReservationUserDetails
	 * @param date LocalDate
	 * @param id int
	 * @param model Model
	 * @return
	 */
	@RequestMapping(value = "/reservations/{date}/{id}",method = RequestMethod.POST)
	public String reserve(@Validated ReservationForm form,BindingResult result, @AuthenticationPrincipal ReservationUserDetails user, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,@PathVariable("id") int id,Model model){
		Reservation reservation = new Reservation();
		reservation.setUser(user.getUser());
		reservation.setStartTime(form.getStartTime());
		reservation.setEndTime(form.getEndTime());
		ReservableRoomId roomId = new ReservableRoomId(id,date);
		ReservableRoom room = new ReservableRoom(roomId);
		reservation.setReservableRoom(room);
		if (result.hasErrors()) {
			return reserveForm(date,id,model);
		}
		try {
			reservationService.reserve(reservation);
		}catch(UnavailableReservationException | AlreadyReservedException e) {
			model.addAttribute("err",e.getMessage());
			return reserveForm(date,id,model);
		}
		return "redirect:/reservations/{date}/{id}";
	}

	/**
	 * 予約キャンセルの制御
	 * @param yoyakuid 予約ID
	 * @param date 指定日付
	 * @param id 貸会議室ID
	 * @param model モデル
	 * @return
	 */
	@RequestMapping(value = "/reservations/{date}/{id}",method = RequestMethod.POST,params = "cancel")
	public String cancel(@RequestParam int yoyakuid,@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,@PathVariable("id") int id,Model model) {
		try {
			reservationService.cancel(reservationService.findOne(yoyakuid));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return reserveForm(date,id,model);
		}
		return "redirect:/reservations/{date}/{id}";
	}
}
