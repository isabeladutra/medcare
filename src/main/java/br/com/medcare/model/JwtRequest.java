package br.com.medcare.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4156956013439001950L;
	private String username;
	private String password;


}
