package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.services.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;

@UserInsertValid
public class UserInsertDTO extends UserDTO {
	@Serial
	private static final long serialVersionUID = 1L;

	//@Size(message = "Deve ter no mínimo 8 caracteres.")
	@NotBlank
	@Size(min = 6, message = "Deve ter no mínimo 6 caracteres.")
	private String password;

	UserInsertDTO() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
