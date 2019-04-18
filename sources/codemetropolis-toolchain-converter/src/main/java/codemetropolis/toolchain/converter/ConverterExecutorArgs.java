package codemetropolis.toolchain.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import codemetropolis.toolchain.commons.executor.ExecutorArgs;
import codemetropolis.toolchain.converter.control.ConverterType;

public class ConverterExecutorArgs extends ExecutorArgs {
	
	private ConverterType type;
	private String source;
	private String outputFile;
	private Map<String, String> params;
	
	public ConverterExecutorArgs(ConverterType type, String source, String outputFile) {
		this(type, source, outputFile, new HashMap<String, String>());
	}
	
	public ConverterExecutorArgs(ConverterType type, String source, String outputFile, Map<String, String> params) {
		super();
		this.type = type;
		this.source = source;
		this.outputFile = outputFile;
		this.params = params;
	}

	public ConverterType getType() {
		return type;
	}
	
	public String getSource() {
		return source;
	}

	public String getOutputFile(){
		return outputFile;
	}
	
	public Map<String, String> getParams() {
		return Collections.unmodifiableMap(params);
	}
	
	public String getParameter(String key) {
		return params.get(key);
	}

}
