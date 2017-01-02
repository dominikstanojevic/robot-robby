package hr.fer.zemris.projekt.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JCreateMapDialog extends JDialog {

    private static final long serialVersionUID = 3671122922403916985L;

    private int rows;
    private int columns;

    public JCreateMapDialog() {

        initGUI();
        setTitle("Pick map size");
        pack();

    }

    private void initGUI() {

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        add(p);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));

        JSlider slMapRows = new JSlider(2, 15, 10);
        slMapRows.createStandardLabels(5);
        slMapRows.setMajorTickSpacing(5);
        slMapRows.setPaintTicks(true);
        slMapRows.setPaintLabels(true);

        JLabel lMapRows = new JLabel("Number of rows: " + slMapRows.getValue());
        slMapRows.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                lMapRows.setText("Number of rows: " + slMapRows.getValue());

            }
        });
        p.add(lMapRows);
        p.add(slMapRows);
        slMapRows.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JSlider slMapColumns = new JSlider(2, 15, 10);
        slMapColumns.createStandardLabels(5);
        slMapColumns.setMajorTickSpacing(5);
        slMapColumns.setPaintTicks(true);
        slMapColumns.setPaintLabels(true);

        JLabel lMapColumns = new JLabel("Number of columns: " + slMapColumns.getValue());
        slMapColumns.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                lMapColumns.setText("Number of columns: " + slMapColumns.getValue());

            }
        });
        p.add(lMapColumns);
        p.add(slMapColumns);
        slMapColumns.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JButton btnOK = new JButton("OK");
        p.add(btnOK);

        btnOK.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                rows = slMapRows.getValue();
                columns = slMapColumns.getValue();

                dispose();
            }
        });

    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
