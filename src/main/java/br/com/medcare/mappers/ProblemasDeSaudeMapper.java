package br.com.medcare.mappers;

import br.com.medcare.model.ProblemasDeSaude;
import br.com.medcare.model.ProblemasDeSaudeRequest;
import lombok.Data;

@Data
public class ProblemasDeSaudeMapper {
	
	public static ProblemasDeSaude mapper(ProblemasDeSaudeRequest request) {
		ProblemasDeSaude problemSaude = new ProblemasDeSaude();
		problemSaude.setDoencasCongenitas(request.getDoencasCongenitas());
		problemSaude.setDoencasCronicas(request.getDoencasCronicas());
		return problemSaude;
	}

}
