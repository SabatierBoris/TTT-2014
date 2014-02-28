package TTT.PC.views;

import java.util.List;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import TTT.PC.models.communication.ConnexionListener;
import TTT.PC.models.communication.StatutChangedEvent;
import TTT.PC.models.config.Config;
import TTT.PC.models.config.ConfigListener;

import TTT.PC.controllers.ConnexionController;
import TTT.PC.controllers.ConfigController;


public class ConfigTableModel extends AbstractTableModel implements ConnexionListener, ConfigListener {
	static final long serialVersionUID = -9091945182909139391L;
	private final List<Config> data = new ArrayList<Config>();
	private final String[] headers = {"Key","Value"};
	private ConfigController controller = null;

	public ConfigTableModel(ConnexionController mcontroller, ConfigController ccontroller){
		super();
		this.controller = ccontroller;

		this.controller.addView(this);
		mcontroller.addView(this);
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

	private void empty(){
		this.data.clear();
	}

	private void load(){
		this.controller.getConfigList();
	}

	@Override
	public void newConfig(Config conf){
		this.data.add(conf);
		this.fireTableDataChanged();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex){
		return (columnIndex == 1);
	}

	@Override
	public void statutChanged(StatutChangedEvent event){
		if(event.getNewStatut()){
			this.load();
		} else {
			this.empty();
		}
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex){
		if(value != null && columnIndex == 1){
			this.controller.updateConfig(this.data.get(rowIndex),(String)value);
		}
	}
}
