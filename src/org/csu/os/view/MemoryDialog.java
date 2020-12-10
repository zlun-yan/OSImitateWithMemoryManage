package org.csu.os.view;

import org.csu.os.domain.pojo.MemoryParam;
import org.csu.os.service.table.MemoryTableData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MemoryDialog extends JFrame {
    private static JTable memoryTable = new JTable();

    public MemoryDialog() {
        JPanel tablePanel = initTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        refresh();
        pack();
        setTitle("未分分区表");
        setLocationRelativeTo(null);
    }

    private JPanel initTablePanel() {
        JPanel panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(memoryTable);
        panel.add(memoryTable.getTableHeader(), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setPreferredSize(new Dimension(450, 300));

//        add(panel, BorderLayout.CENTER);
        return panel;
    }

    public static void refresh() {
        List<MemoryParam> memoryParamList = MemoryTableData.getRemainMemoryList();
        int count = memoryParamList.size();

        Object[][] tableData = new Object[count][3];
        for (int i = 0; i < count; i++) {
            MemoryParam memoryParam = memoryParamList.get(i);

            tableData[i][0] = memoryParam.getStart();
//            tableData[i][1] = memoryParam.getEnd();
            tableData[i][1] = memoryParam.getLength();
        }

        memoryTable.setModel(new DefaultTableModel(tableData, new String[]{"起始地址", "长度"}){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
}
