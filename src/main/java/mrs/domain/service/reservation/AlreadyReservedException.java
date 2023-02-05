package mrs.domain.service.reservation;

/**
 * 予約済みの場合の例外
 * @author iwamoto
 *
 */
public class AlreadyReservedException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public AlreadyReservedException(String message) {
		super(message);
	}
}
