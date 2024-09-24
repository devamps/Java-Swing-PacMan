package frames;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;

public class HighScoreFrame extends JFrame {

    private JTable scoresTable;
    private DefaultTableModel tableModel;
    private static HighScoreFrame instance;

    public HighScoreFrame() {
        super("High-Scores");

        String[] columnNames = {"User", "Score"};
        tableModel = new DefaultTableModel(columnNames,0);
        scoresTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(scoresTable);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        scoresTable.setRowSorter(sorter);
        sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(1, SortOrder.DESCENDING)));

        add(scrollPane);
        setSize(500,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                new StartingFrame();
            }
        });

    }

    public void addScore(String username, int score) {
        Object[] newRow = {username, score};
        tableModel.addRow(newRow);
    }

    public static HighScoreFrame getInstance() {
        if (instance == null) {
            instance = new HighScoreFrame();
        }
        return instance;
    }
}
