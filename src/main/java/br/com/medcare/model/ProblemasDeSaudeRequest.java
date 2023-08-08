package br.com.medcare.model;

import java.util.List;

import lombok.Data;

@Data
public class ProblemasDeSaudeRequest {
    private List<String> doencasCronicas;
    private List<String> doencasCongenitas;

}
