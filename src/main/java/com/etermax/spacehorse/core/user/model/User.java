package com.etermax.spacehorse.core.user.model;

import com.etermax.spacehorse.core.authenticator.model.Role;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User {

	private String userId;
	private String password;
	private Role role;
	private String sessionToken;
    private Platform platform;
    private Date createdDate;
    private Date lastUpdatedDate;

	public List<String> getPasswords() {
		return passwords;
	}

	private List<String> passwords = new ArrayList<>();

	public User(String userId, String password, Role role, Platform platform) {
		this.userId = userId;
		this.password = SHA256Digester.digest(password);
		this.passwords.add(this.password);
		this.sessionToken = UUID.randomUUID().toString();
		this.role = role;
		this.platform = platform;
	}

	public User(Boolean digestedPassword, String userId, String password, List<String> passwords, Role role, Platform platform) {
		this.userId = userId;
		this.sessionToken = UUID.randomUUID().toString();
		this.role = role;
		this.password = digestedPassword ? password : SHA256Digester.digest(password);
        this.platform = platform;
        if (passwords != null) {
            this.passwords.addAll(passwords);
        }
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public boolean isAdmin() {
		return Role.ADMIN.equals(getRole());
	}

	public boolean isSupport() {
		return Role.SUPPORT.equals(getRole());
	}

	public boolean isTester() {
		return Role.TESTER.equals(getRole());
	}

	public boolean validatePassword(String pass) {
		String digestedPass = SHA256Digester.digest(pass);
		return digestedPass.equals(getPassword()) || getPasswords().contains(digestedPass);
	}

	public boolean isAdminOrTester() {
		return isAdmin() || isTester();
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return new EqualsBuilder().append(userId, ((User) o).userId).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(userId).toHashCode();
    }

	public void addPassword(String newPassword) {
		String password = SHA256Digester.digest(newPassword);
		this.passwords.add(password);
	}

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
