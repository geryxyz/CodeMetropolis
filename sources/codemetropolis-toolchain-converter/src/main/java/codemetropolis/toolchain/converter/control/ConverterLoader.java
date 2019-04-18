package codemetropolis.toolchain.converter.control;

import java.util.Map;

import codemetropolis.toolchain.commons.cdf.converter.CdfConverter;
import codemetropolis.toolchain.converter.gitlab.GitLabConverter;
import codemetropolis.toolchain.converter.sonarqube.SonarQubeConverter;
import codemetropolis.toolchain.converter.sourcemeter.GraphConverter;

public class ConverterLoader {
	
	private ConverterLoader() {}
	
	public static CdfConverter load(ConverterType converterType, Map<String, String> params) {
		switch(converterType) {
			case GITLAB:
				return new GitLabConverter(params);
			case SOURCEMETER:
				return new GraphConverter(params);
			case SONARQUBE:
				return new SonarQubeConverter(params);
			default:
				return null;
		}
	}
}
