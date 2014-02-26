package TTT.PC.models.config;

import java.util.List;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ConfigTableModel extends AbstractTableModel {
	static final long serialVersionUID = -9091945182909139391L;
	private final List<Config> data = new ArrayList<Config>();
	private final String[] headers = {"Key","Value"};

	public ConfigTableModel(){
		super();

		this.data.add(new Config("asserv.lineP","1"));
		this.data.add(new Config("asserv.lineI","0"));
		this.data.add(new Config("asserv.lineD","0"));
		this.data.add(new Config("asserv.angleP","1"));
		this.data.add(new Config("asserv.angleI","0"));
		this.data.add(new Config("asserv.angleD","0"));
		this.data.add(new Config("odo.wheelSize","10"));
	}

	@Override
	public int getRowCount(){
		return this.data.size();
	}

	@Override
	public int getColumnCount(){
		return this.headers.length;
	}

	@Override
	public String getColumnName(int columnIndex){
		return this.headers[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex){
		switch(columnIndex){
			case 0:
				return this.data.get(rowIndex).getKey();
			case 1:
				return this.data.get(rowIndex).getValue();
			default:
				return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex){
		return (columnIndex != 0);
	}
}
