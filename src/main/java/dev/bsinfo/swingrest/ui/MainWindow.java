

package dev.bsinfo.swingrest.ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class MainWindow extends JFrame {

	private JPanel lowerPanel;
	private JPanel upperPanel;
	private JTable table;

	private TableModel tableModel;

	private JTextField textField1;
	private JTextField textField2;
	private JTextField textField3;
	private JTextField textField4;
	private JTextField textField5;
	private JTextField textField6;

	private JTextField textField7;

	public MainWindow(String title) {
		super(title);
		initModel();
		initLayout();
	}

	private void initModel() {
		tableModel = new TableModel();
	}

	public static void showFrame() {
		JFrame window = new MainWindow("Test window");
		window.pack();
		window.setVisible(true);
	}

	private void initLayout() {
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initPanels();
		initTextFields();
		initTable();
	}

	private void initPanels() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2, 1));
		upperPanel = new JPanel();
		mainPanel.add(upperPanel);
		lowerPanel = new JPanel();
		mainPanel.add(lowerPanel);
		upperPanel.setLayout(new GridLayout(0, 2));
		lowerPanel.setLayout(new GridLayout(0, 1));
		add(mainPanel);
	}

	private void initTextFields() {
		JLabel label1 = new JLabel("Kundennummer");
		textField1 = new JTextField("");
		JLabel label2 = new JLabel("Hausnummer");
		textField2 = new JTextField("");
		JLabel label3 = new JLabel("Wohnungsnummer");
		textField3 = new JTextField("");
		JLabel label4 = new JLabel("Zählerart");
		textField4 = new JTextField("");
		JLabel label5 = new JLabel("Zählernummer");
		textField5 = new JTextField("");
		JLabel label6 = new JLabel("Ablesedatum");
		textField6 = new JTextField("");
		JLabel label7 = new JLabel("Messwerte");
		textField7 = new JTextField("");
		// Initialize Buttons and append action listeners
		JButton save = new JButton("Save");
		save.addActionListener(e -> {
			// Sets the current row with the data from textfields or appends a new row if none is selected
			tableModel.updateRow(new String[]{textField1.getText(), textField2.getText(), textField3.getText(),
					textField4.getText(), textField5.getText(), textField6.getText()});
			// Reset current selection and fields
			tableModel.selectedRowIndex = -1;
			table.clearSelection();
			clearTextFields();
		});
		JButton clear = new JButton("Clear");
		clear.addActionListener(e -> {
			// Reset current selection and fields
			clearTextFields();
			tableModel.selectedRowIndex = -1;
			table.clearSelection();
		});

		upperPanel.add(label1);
		upperPanel.add(textField1);
		upperPanel.add(label2);
		upperPanel.add(textField2);
		upperPanel.add(label3);
		upperPanel.add(textField3);
		upperPanel.add(label4);
		upperPanel.add(textField4);
		upperPanel.add(label5);
		upperPanel.add(textField5);
		upperPanel.add(label6);
		upperPanel.add(textField6);
		upperPanel.add(label7);
		upperPanel.add(textField7);
		upperPanel.add(clear);
		upperPanel.add(save);
	}

	private void clearTextFields() {
		textField1.setText("");
		textField2.setText("");
		textField3.setText("");
		textField4.setText("");
		textField5.setText("");
		textField6.setText("");
	}

	/**
	 * Sets all text fields with the current selection from the JTable
	 * @param data
	 */
	private void setTextFields(Vector data) {
		textField1.setText(String.valueOf(data.get(0)));
		textField2.setText(String.valueOf(data.get(1)));
		textField3.setText(String.valueOf(data.get(2)));
		textField4.setText(String.valueOf(data.get(3)));
		textField5.setText(String.valueOf(data.get(4)));
		textField6.setText(String.valueOf(data.get(5)));
	}

	private void initTable() {
		table = new JTable(tableModel);
		lowerPanel.add(new JScrollPane(table));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		ListSelectionModel selectionModel = table.getSelectionModel();

		selectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (table.getSelectedRow() >= 0) {
					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					tableModel.selectedRowIndex = table.getSelectedRow();
					Vector data = tableModel.getDataVector().elementAt(tableModel.selectedRowIndex);
					setTextFields(data);
				}
			}
		});
		JButton exportButton = new JButton("Export");
		lowerPanel.add(exportButton);
		exportButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			File selectedFile = fileChooser.getSelectedFile();
			try (FileWriter fw = new FileWriter(selectedFile)) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < tableModel.getColumnCount(); i++) {
					fw.write(tableModel.getColumnName(i) + ";");
				}
				for (Vector row : tableModel.getDataVector()) {
					String s = String.join(";", row) + "\n";
					sb.append(s);
				}
				fw.write(sb.toString());
				fw.flush();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}

		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(MainWindow::showFrame);
	}

	public static class TableModel extends DefaultTableModel {

		public int selectedRowIndex = -1;

		TableModel() {
			super();
			initData();
		}

		public void updateRow(String[] data){
			if(this.selectedRowIndex >= 0){
				for(int index = 0; index < data.length; index++)
					setValueAt(data[index], selectedRowIndex, index);
			} else {
				this.addRow(data);
			}
		}

		private void initData(){
			this.addColumn("Kundennummer");
			this.addColumn("Hausnummer");
			this.addColumn("Wohnungsnummer");
			this.addColumn("Zählerart");
			this.addColumn("Zählernummer");
			this.addColumn("Ablesedatum");
			this.addColumn("Messwerte");

		}
	}
}
