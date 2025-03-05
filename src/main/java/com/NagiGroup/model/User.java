package com.NagiGroup.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
	@NotNull(message = "login_user_id cannot be null")
	@NotBlank(message = "login_user_id cannot be empty")
	private String login_user_id;

	@NotNull(message = "Password cannot be null")
	@NotBlank(message = "Password cannot be empty")
	private String password;
}
