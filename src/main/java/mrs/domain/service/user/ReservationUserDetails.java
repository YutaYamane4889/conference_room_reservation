package mrs.domain.service.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import mrs.domain.model.User;

/**
 * ユーザー詳細情報
 * @author iwamoto
 *
 */
public class ReservationUserDetails implements UserDetails {

	/**
	 *　シリアライズ
	 */
	private static final long serialVersionUID = 1L;

	// modelのUser情報
	private final User user;
	//ユーザ情報の詳細
	public ReservationUserDetails(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
	//	ユーザのロール権限を生成（認可処理用）
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// GrantedAuthorityに変換「ROLE_」プレフィックス必要
		return AuthorityUtils.createAuthorityList("ROLE_" + this.user.getRoleName().name());
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUserId();
	}

	/*
	 *
	 * アカウント期限切れ（未実装）
	 *
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	/*
	 *
	 * アカウントロック（未実装）
	 *
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	/*
	 *
	 * パスワード有効期限切れ（未実装）
	 *
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	/*
	 *
	 * アカウント無効化（未実装）
	 *
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}