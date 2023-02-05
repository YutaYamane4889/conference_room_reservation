package mrs.app.User;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import mrs.domain.model.RoleName;

public class AddUserForm {
	@NotEmpty(message = "必須です")
	private String userId;

	@NotEmpty(message = "必須です")
	private String lastName;

	@NotEmpty(message = "必須です")
	private String firstName;

	@Size(min=8,max=20,message = "パスワードは8文字以上20文字以内で入力してください。")
	private String password;

	@NotNull(message = "必須です")
	@Enumerated(EnumType.STRING)
	private RoleName roleName;

	/**
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId セットする userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName セットする lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName セットする firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password セットする password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return roleName
	 */
	public RoleName getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName セットする roleName
	 */
	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}

}
