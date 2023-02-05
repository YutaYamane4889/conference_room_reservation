package mrs.app.room;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mrs.domain.model.ReservableRoom;
import mrs.domain.service.room.RoomService;

@Controller
public class RoomsController {
	@Autowired RoomService service;

	/**
	 * 貸会議室一覧画面制御処理
	 * @param model モデル
	 * @return listRoomsのhtml
	 */
	@RequestMapping(value = "/rooms",method = RequestMethod.GET)
	public String listRooms(Model model) {
		List<ReservableRoom> datalist = service.findReservableRooms(LocalDate.now());
		model.addAttribute("date",LocalDate.now());
		model.addAttribute("datalist",datalist);
		return "room/listRooms";
	}

	/**
	 * 貸会議室一覧画面制御処理
	 * @param date GETで受け取った日付パラメータ
	 * @param model モデル
	 * @return listRoomsのhtml
	 */
	@RequestMapping(value = "/rooms/{date}",method = RequestMethod.GET)
	public String listRooms(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,Model model) {
		List<ReservableRoom> datalist = service.findReservableRooms(date);
		model.addAttribute("date",date);
		model.addAttribute("datalist",datalist);
		return "room/listRooms";
	}
}
