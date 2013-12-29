package TTT.commons.communication;

public abstract class Message{
	abstract public String toString();
	abstract public void parse(String data) throws ParsingFailException;
}
