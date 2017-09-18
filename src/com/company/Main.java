package com.company;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.company.dataset.DataChangeListener;
import com.company.dataset.DataSet;
import com.company.dataset.GoldMintDataSet;
import com.company.devicecontroller.DMe03Controller;
import com.company.devicecontroller.ScalesController;

import jssc.SerialPortException;
import jssc.SerialPortList;

public class Main implements DataChangeListener<GoldMintDataSet> {

	private final JLabel lbl_data = new JLabel("Данных нет");
	private final DataSet<GoldMintDataSet> data = new DataSet<>(GoldMintDataSet.class);

	private Main() {
		data.registerListener(this);
	}

	@Override
	public void dataChangeProcessing(DataSet<GoldMintDataSet> data) {
		lbl_data.setText("<html>Проба: " + data.getOrDefault(GoldMintDataSet.GOLD_CONTENT, "нет данных")
				+ "<br>" + "Вес: " + data.getOrDefault(GoldMintDataSet.WEIGHT, "нет данных") + "</html>");
	}

	public static void main(String[] args) throws SerialPortException {
		final Main app = new Main();

		System.out.println(Arrays.toString(SerialPortList.getPortNames()));

		COMListener listener = new COMListener(app.data);

		listener.registerDevice(new DMe03Controller("COM5"));
		listener.registerDevice(new ScalesController("COM4"));

		final JFrame frame = new JFrame();
		final JPanel control_pane = new JPanel(new GridBagLayout());
		final JButton btn_settings = new JButton("Настройки");
		final JButton btn_reset = new JButton("Сбросить данные");
		final JButton btn_send = new JButton("Отправить");

		btn_settings.addActionListener(e -> {

		});
		btn_reset.addActionListener(e -> app.data.clear());
		btn_send.addActionListener(e -> {

			// Какая-то серилизация в контракт
			app.data.get(GoldMintDataSet.GOLD_CONTENT);
			app.data.get(GoldMintDataSet.WEIGHT);

		});

		control_pane.add(app.lbl_data, new GridBagConstraints(0, 0, 1, 4, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 0, 0));

		control_pane.add(btn_settings, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		control_pane.add(btn_reset, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		control_pane.add(btn_send, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		control_pane.add(new JLabel(), new GridBagConstraints(1, 3, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

		frame.add(control_pane);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(400, 150);
		frame.setVisible(true);

	}
}
