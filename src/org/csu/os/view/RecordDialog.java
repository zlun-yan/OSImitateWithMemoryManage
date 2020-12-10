package org.csu.os.view;

import org.csu.os.domain.pojo.Progress;
import org.csu.os.service.table.RecordTableData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RecordDialog extends JFrame {
    private static JTable dataTable = new JTable();

    private static double aveWaitTime;
    private static JLabel aveTimeLabel = new JLabel("", SwingConstants.RIGHT);

    public RecordDialog() {
        initTablePanel();
        initAveTimePanel();

        refresh();
        pack();
        setTitle("进程运行日志");
        setLocationRelativeTo(null);
    }

    private void initTablePanel() {
        JPanel panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(dataTable);
        panel.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setPreferredSize(new Dimension(450, 300));

        add(panel, BorderLayout.CENTER);
    }

    private void initAveTimePanel() {
        JPanel panel = new JPanel();
        aveTimeLabel.setText("平均等待时间为: " + aveWaitTime);
        panel.add(aveTimeLabel);

        add(panel, BorderLayout.SOUTH);
    }

    public static void refresh() {
        List<Progress> progressList = RecordTableData.getProgressList();
        int count = progressList.size();

        Object[][] tableData = new Object[count][4];
        for (int i = 0; i < count; i++) {
            tableData[i][0] = progressList.get(i).getName();
            if (progressList.get(i).getResponseTime() != -1) tableData[i][1] = progressList.get(i).getResponseTime();
            if (progressList.get(i).getTurnAroundTime() != -1) tableData[i][2] = progressList.get(i).getTurnAroundTime();
            if (progressList.get(i).getWaitTime() != -1) tableData[i][3] = progressList.get(i).getWaitTime();
        }

        dataTable.setModel(new DefaultTableModel(tableData, new String[]{"进程名称", "响应时间",
                "周转时间", "等待时间"}){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        aveWaitTime = RecordTableData.getAveWaitTime();
        aveTimeLabel.setText("平均等待时间为: " + aveWaitTime);
    }
}