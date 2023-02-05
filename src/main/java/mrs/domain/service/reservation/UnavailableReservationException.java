package mrs.domain.service.reservation;

/**
 * 予約不可の組み合わせ例外
 * @author iwamoto
 *
 */
public class UnavailableReservationException extends RuntimeException {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public UnavailableReservationException(String message) {
		super(message);
	}
}
