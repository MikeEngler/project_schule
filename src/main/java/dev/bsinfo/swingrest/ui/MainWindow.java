
package dev.bsinfo.swingrest.ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import com.formdev.flatlaf.*;

@SuppressWarnings("serial")
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
		JFrame window = new MainWindow("Zählerstand-Ableser");
		window.pack();
		window.setVisible(true);
	}

	public static void popUp(String message) {
		JFrame pop = new JFrame();
		JOptionPane.showMessageDialog(pop, message);
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
			// Sets the current row with the data from textfields or appends a new row if
			// none is selected
			tableModel.updateRow(new String[] { textField1.getText(), textField2.getText(), textField3.getText(),
					textField4.getText(), textField5.getText(), textField6.getText(), textField7.getText() });
			// Reset current selection and fields
			tableModel.selectedRowIndex = -1;
			table.clearSelection();
			clearTextFields();
			popUp("Data saved!");
		});
		JButton clear = new JButton("Clear");
		clear.addActionListener(e -> {
			// Reset current selection and fields
			clearTextFields();
			tableModel.selectedRowIndex = -1;
			table.clearSelection();
			popUp("Data cleared");
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
		textField7.setText("");
	}

	// loads text from file
	public void loadData(String filePath) {

		ArrayList<String> data = new ArrayList<>();

		if (checkFile(filePath) == true) {
			try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
				String line;
				while ((line = br.readLine()) != null) {
					data.add(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			textField1.setText(String.valueOf(data.get(0)));
			textField2.setText(String.valueOf(data.get(1)));
			textField3.setText(String.valueOf(data.get(2)));
			textField4.setText(String.valueOf(data.get(3)));
			textField5.setText(String.valueOf(data.get(4)));
			textField6.setText(String.valueOf(data.get(5)));
			textField7.setText(String.valueOf(data.get(6)));
		} else {
			popUp("No file to load from!");
		}

	}

	/**
	 * Sets all text fields with the current selection from the JTable
	 * 
	 * @param data
	 * @return
	 */
	private void setTextFields(@SuppressWarnings("rawtypes") Vector data) {
		textField1.setText(String.valueOf(data.get(0)));
		textField2.setText(String.valueOf(data.get(1)));
		textField3.setText(String.valueOf(data.get(2)));
		textField4.setText(String.valueOf(data.get(3)));
		textField5.setText(String.valueOf(data.get(4)));
		textField6.setText(String.valueOf(data.get(5)));
		textField7.setText(String.valueOf(data.get(6)));
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
					// ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					tableModel.selectedRowIndex = table.getSelectedRow();
					@SuppressWarnings("rawtypes")
					Vector data = tableModel.getDataVector().elementAt(tableModel.selectedRowIndex);
					setTextFields(data);
				}
			}
		});

		// button for exporting data to file
		JButton exportButton = new JButton("Export");
		lowerPanel.add(exportButton);
		exportButton.addActionListener(e -> {

			try {
				@SuppressWarnings("rawtypes")
				Vector data = tableModel.getDataVector().elementAt(tableModel.selectedRowIndex);

				ArrayList<String> dataStrings = new ArrayList<String>();
				dataStrings.add(String.valueOf(data.get(0)));
				dataStrings.add(String.valueOf(data.get(1)));
				dataStrings.add(String.valueOf(data.get(2)));
				dataStrings.add(String.valueOf(data.get(3)));
				dataStrings.add(String.valueOf(data.get(4)));
				dataStrings.add(String.valueOf(data.get(5)));
				dataStrings.add(String.valueOf(data.get(6)));

				writeToFile(dataStrings, "data.csv");
			}catch(Exception ex) {
				popUp("No data to export!");
			}
		});

		// delete button for deleting locally saved data from file
		JButton deleteButton = new JButton("Delete savefile");
		lowerPanel.add(deleteButton);
		deleteButton.addActionListener(e -> {
			if (checkFile("data.csv")) {
				deleteFile("data.csv");
			} else {
				popUp("No file to delete");
			}
		});

		// button for loading
		JButton loadButton = new JButton("Load data from savefile");
		lowerPanel.add(loadButton);
		loadButton.addActionListener(e -> {
			loadData("data.csv");

			if (checkFile("data.csv")) {
				popUp("Data loaded successfully!");
			}
		});
	}

	// takes an arraylist of strings an writes it to a txt file
	public static void writeToFile(ArrayList<String> strings, String filename) {
		try {
			FileWriter writer = new FileWriter(filename);
			for (String s : strings) {
				writer.write(s + "\n");
			}
			writer.close();
			System.out.println("Successfully wrote data to file " + filename);
			popUp("Successfully wrote data to file " + filename);
		} catch (IOException e) {
			System.out.println("An error occurred while writing to the file " + filename + ": " + e.getMessage());
		}
	}

	// deletes the save file
	public static void deleteFile(String fileName) {
		File data = new File(fileName);
		data.delete();
		System.out.println("the file " + fileName + " has been deleted!");
		popUp("the file " + fileName + " has been deleted!");
	}

	// checks if a given file exists
	public boolean checkFile(String filePath) {
		boolean exists = false;
		File f = new File(filePath);
		if (f.exists() && !f.isDirectory()) {
			exists = true;
		}
		return exists;
	}

	// main method -> app starting point
	public static void main(String[] args) {

		// theming
		// establish theming via flatlaf
		FlatLightLaf.setup();

		// setting theme to dracula if possible else throw exeption
		try {
			UIManager.setLookAndFeel(new FlatDarculaLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}

		// shows gui
		SwingUtilities.invokeLater(MainWindow::showFrame);
	}

	public static class TableModel extends DefaultTableModel {

		public int selectedRowIndex = -1;

		TableModel() {
			super();
			initData();
		}

		public void updateRow(String[] data) {
			if (this.selectedRowIndex >= 0) {
				for (int index = 0; index < data.length; index++)
					setValueAt(data[index], selectedRowIndex, index);
			} else {
				this.addRow(data);
			}
		}

		private void initData() {
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