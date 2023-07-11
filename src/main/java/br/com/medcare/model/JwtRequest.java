package br.com.medcare.model;

import java.io.Serializable;

import org.antlr.v4.runtime.misc.NotNull;

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
	
    private String email;
     
    private String password;

}
