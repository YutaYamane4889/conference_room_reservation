package mrs.domain.service.user;

/**
 * 予約済みの場合の例外
 * @author iwamoto
 *
 */
public class AlreadyRegisteredException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public AlreadyRegisteredException(String message) {
		super(message);
	}
}