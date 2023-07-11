package br.com.medcare.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class JwtResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4052307786993003146L;
	private String email;
	private String accessToken;
}
