package TTT.libNXT.task;

import java.util.ArrayList;

public interface USMonitor {
	public ArrayList<String> getNamesSensors();
	public Integer getData(String name);
}
