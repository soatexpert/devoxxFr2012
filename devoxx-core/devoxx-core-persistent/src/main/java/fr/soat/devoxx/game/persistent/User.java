/*
 * Copyright (c) 2011 Khanh Tuong Maudoux <kmx.petals@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fr.soat.devoxx.game.persistent;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;

import fr.soat.devoxx.game.persistent.util.UserUtils;

/**
 * User: khanh
 * Date: 20/12/11
 * Time: 00:30
 */
//@XmlRootElement(name = "user")
@Entity
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	@NotNull
	@Size(min = 4)
	private String urlId;

	// @Id
	// @NotNull
	// @Size(min = 4)
	private String name;

	// @Pattern(regexp = "\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b")
	// @Pattern(regexp = "[\\w-]+@([\\w-]+\\.)+[\\w-]+")
	// @Email // Hibernate
	// @NotNull
	// @Pattern(regexp =
	// "[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*\\.[A-Za-z]{2,}")
	private String mail;

	@NotNull
	private String token = StringUtils.EMPTY;

	@PrePersist
	void onPrePersist() {
		generateUserToken();
		generateUsername();
	}

	@PreUpdate
	void onPreUpdate() {
		generateUsername();
	}

	private void generateUserToken() {
		this.setToken(UserUtils.INSTANCE.generateToken());
	}

	private void generateUsername() {
		if (StringUtils.isEmpty(name))
			this.setName(UserUtils.INSTANCE.generateRandomUsername());
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User() {
	}

	public User(String urlId, String mail) {
		this.mail = mail;
		this.urlId = urlId;
	}
	
	public User(String urlId, String mail, String name) {
		this.mail = mail;
		this.urlId = urlId;
		this.name = name;
	}

	@Override
	public String toString() {
		return "User{" + " id='" + id + "'" + ", urlId='" + urlId + "'" + ", name='" + name + "'" + ", mail='" + mail + "'"
		        + (StringUtils.isEmpty(token) ? ", token=<none>" : (", token='" + token + "'")) + '}';
	}
}
