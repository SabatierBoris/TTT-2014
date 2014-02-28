package TTT.PC.models.config;

public class Config {
	private String key;
	private String value;

	public Config(String key, String value){
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey(){
		return this.key;
	}

	public String getValue(){
		return this.value;
	}

	public void setValue(String value){
		this.value = value;
	}
}
