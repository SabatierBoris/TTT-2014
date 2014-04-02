package TTT.commons.communication;

public abstract class Message{
	public static final String DELIMITER = "|";
	abstract public String toString();
	abstract public void parse(String data) throws ParsingFailException;
	abstract public int getId();
}
