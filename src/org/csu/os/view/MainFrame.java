package org.csu.os.view;

import org.csu.os.domain.pojo.MemoryParam;
import org.csu.os.domain.pojo.PCB;
import org.csu.os.domain.pojo.Progress;
import org.csu.os.service.AutoMoving;
import org.csu.os.service.controller.PCBController;
import org.csu.os.service.table.*;
import org.csu.os.view.component.ControllerPanel;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static org.csu.os.service.Static.*;

public class MainFrame extends JFrame {
    private EntryFrame parentFrame;
    private RecordDialog recordDialog;
    private MemoryDialog memoryDialog;
    private static boolean started = false;

    private JButton startButton;
    private JButton pauseButton;
    private JButton suspendButton;
    private JButton resumeButton;
    private JButton recordButton;
    private JButton memoryButton;
    private JButton backButton;

    private JTable PCBTable;
    private JTable BackUpTable;

    private JTable readyTable;
    private JTable runningTable;
    private JTable suspendTable;

    private DefaultTableModel PCBTableModel;
    private DefaultTableModel BackUpTableModel;

    private DefaultTableModel readyTableModel;
    private DefaultTableModel runningTableModel;
    private DefaultTableModel suspendTableModel;

    private JPanel memoryLinePanel;

    public MainFrame(EntryFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());

        memoryLinePanel = new JPanel();
        initMemoryLinePanel(memoryLinePanel);
        JPanel controllerPanel = initControllerPanel();
        JPanel infoTablePanel = initInfoTable();
        JPanel buttonPanel = initButton();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(controllerPanel, BorderLayout.NORTH);
        mainPanel.add(infoTablePanel, BorderLayout.SOUTH);

        add(memoryLinePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setTitle("调度算法模拟");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initMemoryLinePanel(JPanel panel) {
        List<MemoryParam> memoryParamList = MemoryTableData.getRemainMemoryList();
        int end = 0, totSize = MemoryTableData.getTotSize(), totLength = 1394;

        for (int i = 0; i < memoryParamList.size(); i++) {
            MemoryParam memoryParam = memoryParamList.get(i);
            int length = memoryParam.getStart() - end;
            if (length > 0) {  // 这个是已用的内存
                JLabel lineLabel = new JLabel();
                lineLabel.setOpaque(true);
                lineLabel.setPreferredSize(new Dimension(totSize * length / totLength, 30));
                lineLabel.setBackground(Color.ORANGE);
                panel.add(lineLabel);

//                System.out.println("Used memory: " + totSize * length / totLength);
            }
            end = memoryParam.getEnd();

            // 这个是未分分区
            JLabel lineLabel = new JLabel();
            lineLabel.setOpaque(true);
            lineLabel.setPreferredSize(new Dimension(totSize * memoryParam.getLength() / totLength, 30));
            lineLabel.setBackground(Color.MAGENTA);
            panel.add(lineLabel);

//            System.out.println("Valid memory: " + totSize * memoryParam.getLength() / totLength);
        }

        int length = totSize - end;
        if (length > 0) {  // 这个是已用的内存
            JLabel lineLabel = new JLabel();
            lineLabel.setOpaque(true);
            lineLabel.setPreferredSize(new Dimension(totSize * length / totLength, 30));
            lineLabel.setBackground(Color.ORANGE);
            panel.add(lineLabel);

//            System.out.println("Used memory: " + totSize * length / totLength);
        }

        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setHgap(0);
    }

    private JPanel initControllerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));

        PCBTable = new JTable();
        BackUpTable = new JTable();

        JPanel PCBTablePanel = new JPanel();
        JScrollPane PCBScrollPane = new JScrollPane(PCBTable);
        PCBTablePanel.add(PCBTable.getTableHeader(), BorderLayout.NORTH);
        PCBTablePanel.add(PCBScrollPane, BorderLayout.CENTER);
        showPCBData();
        PCBScrollPane.setPreferredSize(new Dimension(450, 300));

        JPanel BackUpTablePanel = new JPanel();
        JScrollPane BackUpScrollPane = new JScrollPane(BackUpTable);
        BackUpTablePanel.add(BackUpTable.getTableHeader(), BorderLayout.NORTH);
        BackUpTablePanel.add(BackUpScrollPane, BorderLayout.CENTER);
        showBackUpData();
        BackUpScrollPane.setPreferredSize(new Dimension(450, 300));

        JPanel PCBPanel = new JPanel();
        PCBPanel.setLayout(new BorderLayout());
        JLabel PCBLabel = new JLabel("就绪队列", SwingConstants.CENTER);
        PCBPanel.add(PCBLabel, BorderLayout.NORTH);
        PCBPanel.add(PCBTablePanel, BorderLayout.CENTER);

        JPanel BackUpPanel = new JPanel();
        BackUpPanel.setLayout(new BorderLayout());
        JLabel BackUpLabel = new JLabel("后备队列", SwingConstants.CENTER);
        BackUpPanel.add(BackUpLabel, BorderLayout.NORTH);
        BackUpPanel.add(BackUpTablePanel, BorderLayout.CENTER);

        JPanel ControllerPanel = new ControllerPanel(this);

        panel.add(PCBPanel);
        panel.add(BackUpPanel);
        panel.add(ControllerPanel);

        return panel;
    }

    private JPanel initInfoTable() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));

        readyTable = new JTable();
        runningTable = new JTable();
        suspendTable = new JTable();

        JPanel readyTablePanel = new JPanel();
        JScrollPane readyScrollPane = new JScrollPane(readyTable);
        readyTablePanel.add(readyTable.getTableHeader(), BorderLayout.NORTH);
        readyTablePanel.add(readyScrollPane, BorderLayout.CENTER);
        showReadyData();
        readyScrollPane.setPreferredSize(new Dimension(450, 300));

        JPanel runningTablePanel = new JPanel();
        JScrollPane runningScrollPane = new JScrollPane(runningTable);
        runningTablePanel.add(runningTable.getTableHeader(), BorderLayout.NORTH);
        runningTablePanel.add(runningScrollPane, BorderLayout.CENTER);
        showRunningData();
        runningScrollPane.setPreferredSize(new Dimension(450, 300));

        JPanel suspendTablePanel = new JPanel();
        JScrollPane suspendTablePane = new JScrollPane(suspendTable);
        suspendTablePanel.add(suspendTable.getTableHeader(), BorderLayout.NORTH);
        suspendTablePanel.add(suspendTablePane, BorderLayout.CENTER);
        showSuspendData();
        suspendTablePane.setPreferredSize(new Dimension(450, 300));

        JPanel readyPanel = new JPanel();
        readyPanel.setLayout(new BorderLayout());
        JLabel readyLabel = new JLabel("就绪队列", SwingConstants.CENTER);
        readyPanel.add(readyLabel, BorderLayout.NORTH);
        readyPanel.add(readyTablePanel, BorderLayout.CENTER);

        JPanel runningPanel = new JPanel();
        runningPanel.setLayout(new BorderLayout());
        JLabel runningLabel = new JLabel("CPU", SwingConstants.CENTER);
        runningPanel.add(runningLabel, BorderLayout.NORTH);
        runningPanel.add(runningTablePanel, BorderLayout.CENTER);

        JPanel suspendPanel = new JPanel();
        suspendPanel.setLayout(new BorderLayout());
        JLabel suspendLabel = new JLabel("挂起队列", SwingConstants.CENTER);
        suspendPanel.add(suspendLabel, BorderLayout.NORTH);
        suspendPanel.add(suspendTablePanel, BorderLayout.CENTER);

        panel.add(readyPanel);
        panel.add(runningPanel);
        panel.add(suspendPanel);

        return panel;
    }

    private JPanel initButton() {
        JPanel panel = new JPanel();

        startButton = new JButton("开始");
        pauseButton = new JButton("暂停");
        suspendButton = new JButton("挂起");
        resumeButton = new JButton("解挂");
        recordButton = new JButton("显示日志");
        memoryButton = new JButton("显示未分分区");
        backButton = new JButton("返回");

        startButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        pauseButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));

        startButton.addActionListener(event -> {
            if (!started) {  // 这个是为了阻塞 一开始没有点开始的时候 不准就绪队列中的进程进入CPU
//                CPUController.wakeUp();
                started = true;
            }
            AutoMoving.start(this);
        });
        pauseButton.addActionListener(event -> AutoMoving.pause());
        suspendButton.addActionListener(event -> {
            // 如果没有选中那么就是返回的-1
            // 选中了是从0开始
            PCBController.suspend(readyTable.getSelectedRow());
            refresh();
        });
        resumeButton.addActionListener(event -> {
            // 如果没有选中那么就是返回的-1
            // 选中了是从0开始
            PCBController.resume(suspendTable.getSelectedRow());
            refresh();
        });
        recordButton.addActionListener(event -> {
            if (recordDialog == null) recordDialog = new RecordDialog();
            recordDialog.setVisible(true);
        });
        memoryButton.addActionListener(event -> {
            if (memoryDialog == null) memoryDialog = new MemoryDialog();
            memoryDialog.setVisible(true);
        });
        backButton.addActionListener(event -> {
            parentFrame.setVisible(true);
            setVisible(false);
        });

        panel.add(startButton);
        panel.add(pauseButton);
        panel.add(suspendButton);
        panel.add(resumeButton);
        panel.add(recordButton);
        panel.add(memoryButton);
        panel.add(backButton);

        return panel;
    }

    private void showBackUpData() {
        List<Progress> processList = BackUpTableData.getProgressList();
        int count = processList.size();

        Object[][] tableData = null;
        String[] backUpColumnName = null;
        switch (mode) {
            case FCFS:
                backUpColumnName = FCFSHeader[0];
                tableData = new Object[count][3];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = processList.get(i).getName();
                    tableData[i][1] = processList.get(i).getTimeLength();
                    tableData[i][2] = processList.get(i).getMemoryLength();
                }
                break;
            case SJF:
                backUpColumnName = SJFHeader[0];
                tableData = new Object[count][3];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = processList.get(i).getName();
                    tableData[i][1] = processList.get(i).getTimeLength();
                    tableData[i][2] = processList.get(i).getMemoryLength();
                }
                break;
            case PSA:
                backUpColumnName = PSAHeader[0];
                tableData = new Object[count][4];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = processList.get(i).getName();
                    tableData[i][1] = processList.get(i).getPriority();
                    tableData[i][2] = processList.get(i).getTimeLength();
                    tableData[i][3] = processList.get(i).getMemoryLength();
                }
                break;
            case RR:
                backUpColumnName = RRHeader[0];
                tableData = new Object[count][3];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = processList.get(i).getName();
                    tableData[i][1] = processList.get(i).getTimeLength();
                    tableData[i][2] = processList.get(i).getMemoryLength();
                }
                break;
            case MQ:
                backUpColumnName = MQHeader[0];
                tableData = new Object[count][5];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = processList.get(i).getName();
                    if (processList.get(i).getQueueOrder() == 1)
                        tableData[i][1] = processList.get(i).getPriority();
                    tableData[i][2] = processList.get(i).getQueueOrder();
                    tableData[i][3] = processList.get(i).getTimeLength();
                    tableData[i][4] = processList.get(i).getMemoryLength();
                }
                break;
            case MFQ:
                backUpColumnName = MFQHeader[0];
                tableData = new Object[count][4];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = processList.get(i).getName();
                    tableData[i][1] = processList.get(i).getQueueOrder();
                    tableData[i][2] = processList.get(i).getTimeLength();
                    tableData[i][3] = processList.get(i).getMemoryLength();
                }
                break;
        }

        BackUpTableModel = new DefaultTableModel(tableData, backUpColumnName){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        BackUpTable.setModel(BackUpTableModel);
    }

    private void showPCBData() {
        List<PCB> PCBList = PCBTableData.getPCBList();  //修改
        int count = PCBList.size();

        Object[][] tableData = null;
        String[] PCBColumnName = null;  // 修改
        switch (mode) {
            case FCFS:
                PCBColumnName = FCFSHeader[1];
                tableData = new Object[count][7];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = PCBList.get(i).getPid();
                    if (PCBList.get(i).isBusy()) {
                        tableData[i][1] = PCBList.get(i).getMyProgress().getName();
                        tableData[i][2] = "占用";
                        tableData[i][3] = PCBList.get(i).getState();
                        tableData[i][4] = PCBList.get(i).getMyProgress().getTimeLength();
                        tableData[i][5] = PCBList.get(i).getMyProgress().getMemoryLength();
                        tableData[i][6] = PCBList.get(i).getMemoryParam().getStart();
                    }
                    else tableData[i][2] = "空闲";
                }
                break;
            case SJF:
                PCBColumnName = SJFHeader[1];
                tableData = new Object[count][7];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = PCBList.get(i).getPid();
                    if (PCBList.get(i).isBusy()) {
                        tableData[i][1] = PCBList.get(i).getMyProgress().getName();
                        tableData[i][2] = "占用";
                        tableData[i][3] = PCBList.get(i).getState();
                        tableData[i][4] = PCBList.get(i).getMyProgress().getTimeLength();
                        tableData[i][5] = PCBList.get(i).getMyProgress().getMemoryLength();
                        tableData[i][6] = PCBList.get(i).getMemoryParam().getStart();
                    }
                    else tableData[i][2] = "空闲";
                }
                break;
            case PSA:
                PCBColumnName = PSAHeader[1];
                tableData = new Object[count][8];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = PCBList.get(i).getPid();
                    if (PCBList.get(i).isBusy()) {
                        tableData[i][1] = PCBList.get(i).getMyProgress().getName();
                        tableData[i][2] = "占用";
                        tableData[i][3] = PCBList.get(i).getState();
                        tableData[i][4] = PCBList.get(i).getMyProgress().getPriority();
                        tableData[i][5] = PCBList.get(i).getMyProgress().getTimeLength();
                        tableData[i][6] = PCBList.get(i).getMyProgress().getMemoryLength();
                        tableData[i][7] = PCBList.get(i).getMemoryParam().getStart();
                    }
                    else tableData[i][2] = "空闲";
                }
                break;
            case RR:
                PCBColumnName = RRHeader[1];
                tableData = new Object[count][7];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = PCBList.get(i).getPid();
                    if (PCBList.get(i).isBusy()) {
                        tableData[i][1] = PCBList.get(i).getMyProgress().getName();
                        tableData[i][2] = "占用";
                        tableData[i][3] = PCBList.get(i).getState();
                        tableData[i][4] = PCBList.get(i).getMyProgress().getTimeLength();
                        tableData[i][5] = PCBList.get(i).getMyProgress().getMemoryLength();
                        tableData[i][6] = PCBList.get(i).getMemoryParam().getStart();
                    }
                    else tableData[i][2] = "空闲";
                }
                break;
            case MQ:
                PCBColumnName = MQHeader[1];
                tableData = new Object[count][9];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = PCBList.get(i).getPid();
                    if (PCBList.get(i).isBusy()) {
                        tableData[i][1] = PCBList.get(i).getMyProgress().getName();
                        tableData[i][2] = "占用";
                        tableData[i][3] = PCBList.get(i).getState();
                        if (PCBList.get(i).getMyProgress().getQueueOrder() == 1)
                            tableData[i][4] = PCBList.get(i).getMyProgress().getPriority();
                        tableData[i][5] = PCBList.get(i).getMyProgress().getQueueOrder();
                        tableData[i][6] = PCBList.get(i).getMyProgress().getTimeLength();
                        tableData[i][7] = PCBList.get(i).getMyProgress().getMemoryLength();
                        tableData[i][8] = PCBList.get(i).getMemoryParam().getStart();
                    }
                    else tableData[i][2] = "空闲";
                }
                break;
            case MFQ:
                PCBColumnName = MFQHeader[1];
                tableData = new Object[count][8];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = PCBList.get(i).getPid();
                    if (PCBList.get(i).isBusy()) {
                        tableData[i][1] = PCBList.get(i).getMyProgress().getName();
                        tableData[i][2] = "占用";
                        tableData[i][3] = PCBList.get(i).getState();
                        tableData[i][4] = PCBList.get(i).getMyProgress().getQueueOrder();
                        tableData[i][5] = PCBList.get(i).getMyProgress().getTimeLength();
                        tableData[i][6] = PCBList.get(i).getMyProgress().getMemoryLength();
                        tableData[i][7] = PCBList.get(i).getMemoryParam().getStart();
                    }
                    else tableData[i][2] = "空闲";
                }
                break;
        }

        PCBTableModel = new DefaultTableModel(tableData, PCBColumnName){  // 修改
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        PCBTable.setModel(PCBTableModel);  // 修改
    }

    private void showReadyData() {
        List<PCB> readyList = ReadyTableData.getReadyPCBList();  //修改
        int count = readyList.size();

        Object[][] tableData = null;
        String[] readyColumnName = null;  // 修改
        switch (mode) {
            case FCFS:
                readyColumnName = FCFSHeader[2];
                tableData = new Object[count][6];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = readyList.get(i).getPid();
                    tableData[i][1] = readyList.get(i).getMyProgress().getName();
                    tableData[i][2] = readyList.get(i).getMyProgress().getTimeLength();
                    tableData[i][3] = readyList.get(i).getMyProgress().getMemoryLength();
                    tableData[i][4] = readyList.get(i).getMemoryParam().getStart();
                    tableData[i][5] = readyList.get(i).getNext();
                }
                break;
            case SJF:
                readyColumnName = SJFHeader[2];
                tableData = new Object[count][6];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = readyList.get(i).getPid();
                    tableData[i][1] = readyList.get(i).getMyProgress().getName();
                    tableData[i][2] = readyList.get(i).getMyProgress().getTimeLength();
                    tableData[i][3] = readyList.get(i).getMyProgress().getMemoryLength();
                    tableData[i][4] = readyList.get(i).getMemoryParam().getStart();
                    tableData[i][5] = readyList.get(i).getNext();
                }
                break;
            case PSA:
                readyColumnName = PSAHeader[2];
                tableData = new Object[count][7];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = readyList.get(i).getPid();
                    tableData[i][1] = readyList.get(i).getMyProgress().getName();
                    tableData[i][2] = readyList.get(i).getMyProgress().getPriority();
                    tableData[i][3] = readyList.get(i).getMyProgress().getTimeLength();
                    tableData[i][4] = readyList.get(i).getMyProgress().getMemoryLength();
                    tableData[i][5] = readyList.get(i).getMemoryParam().getStart();
                    tableData[i][6] = readyList.get(i).getNext();
                }
                break;
            case RR:
                readyColumnName = RRHeader[2];
                tableData = new Object[count][6];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = readyList.get(i).getPid();
                    tableData[i][1] = readyList.get(i).getMyProgress().getName();
                    tableData[i][2] = readyList.get(i).getMyProgress().getTimeLength();
                    tableData[i][3] = readyList.get(i).getMyProgress().getMemoryLength();
                    tableData[i][4] = readyList.get(i).getMemoryParam().getStart();
                    tableData[i][5] = readyList.get(i).getNext();
                }
                break;
            case MQ:
                readyColumnName = MQHeader[2];
                tableData = new Object[count][8];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = readyList.get(i).getPid();
                    tableData[i][1] = readyList.get(i).getMyProgress().getName();
                    if (readyList.get(i).getMyProgress().getQueueOrder() == 1)
                        tableData[i][2] = readyList.get(i).getMyProgress().getPriority();
                    tableData[i][3] = readyList.get(i).getMyProgress().getQueueOrder();
                    tableData[i][4] = readyList.get(i).getMyProgress().getTimeLength();
                    tableData[i][5] = readyList.get(i).getMyProgress().getMemoryLength();
                    tableData[i][6] = readyList.get(i).getMemoryParam().getStart();
                    tableData[i][7] = readyList.get(i).getNext();
                }
                break;
            case MFQ:
                readyColumnName = MFQHeader[2];
                tableData = new Object[count][7];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = readyList.get(i).getPid();
                    tableData[i][1] = readyList.get(i).getMyProgress().getName();
                    tableData[i][2] = readyList.get(i).getMyProgress().getQueueOrder();
                    tableData[i][3] = readyList.get(i).getMyProgress().getTimeLength();
                    tableData[i][4] = readyList.get(i).getMyProgress().getMemoryLength();
                    tableData[i][5] = readyList.get(i).getMemoryParam().getStart();
                    tableData[i][6] = readyList.get(i).getNext();
                }
                break;
        }

        readyTableModel = new DefaultTableModel(tableData, readyColumnName){  // 修改
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        readyTable.setModel(readyTableModel);  // 修改
    }

    private void showRunningData() {
        PCB runningPCB = CPUTableData.getRunningPCB();

        Object[][] tableData = null;
        String[] runningColumn = null;  // 修改
        switch (mode) {
            case FCFS:
                runningColumn = FCFSHeader[3];
                if (runningPCB == null) {
                    tableData = new Object[0][5];
                    break;
                }

                tableData = new Object[1][5];
                tableData[0][0] = runningPCB.getPid();
                tableData[0][1] = runningPCB.getMyProgress().getName();
                tableData[0][2] = runningPCB.getMyProgress().getTimeLength();
                tableData[0][3] = runningPCB.getMyProgress().getMemoryLength();
                tableData[0][4] = runningPCB.getMemoryParam().getStart();
                break;
            case SJF:
                runningColumn = SJFHeader[3];
                if (runningPCB == null) {
                    tableData = new Object[0][5];
                    break;
                }

                tableData = new Object[1][5];
                tableData[0][0] = runningPCB.getPid();
                tableData[0][1] = runningPCB.getMyProgress().getName();
                tableData[0][2] = runningPCB.getMyProgress().getTimeLength();
                tableData[0][3] = runningPCB.getMyProgress().getMemoryLength();
                tableData[0][4] = runningPCB.getMemoryParam().getStart();
                break;
            case PSA:
                runningColumn = PSAHeader[3];
                if (runningPCB == null) {
                    tableData = new Object[0][6];
                    break;
                }

                tableData = new Object[1][6];
                tableData[0][0] = runningPCB.getPid();
                tableData[0][1] = runningPCB.getMyProgress().getName();
                tableData[0][2] = runningPCB.getMyProgress().getPriority();
                tableData[0][3] = runningPCB.getMyProgress().getTimeLength();
                tableData[0][4] = runningPCB.getMyProgress().getMemoryLength();
                tableData[0][5] = runningPCB.getMemoryParam().getStart();
                break;
            case RR:
                runningColumn = RRHeader[3];
                if (runningPCB == null) {
                    tableData = new Object[0][6];
                    break;
                }

                tableData = new Object[1][6];
                tableData[0][0] = runningPCB.getPid();
                tableData[0][1] = runningPCB.getMyProgress().getName();
                tableData[0][2] = runningPCB.getMyProgress().getTimeLength();
                tableData[0][3] = runningPCB.getMyProgress().getMemoryLength();
                tableData[0][4] = runningPCB.getMemoryParam().getStart();
                tableData[0][5] = CPUTableData.getRemainTimeSlice();
                break;
            case MQ:
                runningColumn = MQHeader[3];
                if (runningPCB == null) {
                    tableData = new Object[0][8];
                    break;
                }

                tableData = new Object[1][8];
                tableData[0][0] = runningPCB.getPid();
                tableData[0][1] = runningPCB.getMyProgress().getName();
                if (runningPCB.getMyProgress().getQueueOrder() == 1)
                    tableData[0][2] = runningPCB.getMyProgress().getPriority();
                tableData[0][3] = runningPCB.getMyProgress().getQueueOrder();
                tableData[0][4] = runningPCB.getMyProgress().getTimeLength();
                tableData[0][5] = runningPCB.getMyProgress().getMemoryLength();
                tableData[0][6] = runningPCB.getMemoryParam().getStart();
                tableData[0][7] = CPUTableData.getRemainTimeSlice();
                break;
            case MFQ:
                runningColumn = MFQHeader[3];
                if (runningPCB == null) {
                    tableData = new Object[0][7];
                    break;
                }

                tableData = new Object[1][7];
                tableData[0][0] = runningPCB.getPid();
                tableData[0][1] = runningPCB.getMyProgress().getName();
                tableData[0][2] = runningPCB.getMyProgress().getQueueOrder();
                tableData[0][3] = runningPCB.getMyProgress().getTimeLength();
                tableData[0][4] = runningPCB.getMyProgress().getMemoryLength();
                tableData[0][5] = runningPCB.getMemoryParam().getStart();
                tableData[0][6] = CPUTableData.getRemainTimeSlice();
                break;
        }

        runningTableModel = new DefaultTableModel(tableData, runningColumn){  // 修改
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        runningTable.setModel(runningTableModel);  // 修改
    }

    private void showSuspendData() {
        List<PCB> suspendList = SuspendTableData.getSuspendPCBList();  //修改
        int count = suspendList.size();

        Object[][] tableData = null;
        String[] suspendColumnName = null;  // 修改
        switch (mode) {
            case FCFS:
                suspendColumnName = FCFSHeader[4];
                tableData = new Object[count][5];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = suspendList.get(i).getPid();
                    tableData[i][1] = suspendList.get(i).getMyProgress().getName();
                    tableData[i][2] = suspendList.get(i).getMyProgress().getTimeLength();
                    tableData[i][3] = suspendList.get(i).getMyProgress().getMemoryLength();
                    tableData[i][4] = suspendList.get(i).getMemoryParam().getStart();
                }
                break;
            case SJF:
                suspendColumnName = SJFHeader[4];
                tableData = new Object[count][5];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = suspendList.get(i).getPid();
                    tableData[i][1] = suspendList.get(i).getMyProgress().getName();
                    tableData[i][2] = suspendList.get(i).getMyProgress().getTimeLength();
                    tableData[i][3] = suspendList.get(i).getMyProgress().getMemoryLength();
                    tableData[i][4] = suspendList.get(i).getMemoryParam().getStart();
                }
                break;
            case PSA:
                suspendColumnName = PSAHeader[4];
                tableData = new Object[count][6];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = suspendList.get(i).getPid();
                    tableData[i][1] = suspendList.get(i).getMyProgress().getName();
                    tableData[i][2] = suspendList.get(i).getMyProgress().getPriority();
                    tableData[i][3] = suspendList.get(i).getMyProgress().getTimeLength();
                    tableData[i][4] = suspendList.get(i).getMyProgress().getMemoryLength();
                    tableData[i][5] = suspendList.get(i).getMemoryParam().getStart();
                }
                break;
            case RR:
                suspendColumnName = RRHeader[4];
                tableData = new Object[count][5];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = suspendList.get(i).getPid();
                    tableData[i][1] = suspendList.get(i).getMyProgress().getName();
                    tableData[i][2] = suspendList.get(i).getMyProgress().getTimeLength();
                    tableData[i][3] = suspendList.get(i).getMyProgress().getMemoryLength();
                    tableData[i][4] = suspendList.get(i).getMemoryParam().getStart();
                }
                break;
            case MQ:
                suspendColumnName = MQHeader[4];
                tableData = new Object[count][7];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = suspendList.get(i).getPid();
                    tableData[i][1] = suspendList.get(i).getMyProgress().getName();
                    if (suspendList.get(i).getMyProgress().getQueueOrder() == 1)
                        tableData[i][2] = suspendList.get(i).getMyProgress().getPriority();
                    tableData[1][3] = suspendList.get(i).getMyProgress().getQueueOrder();
                    tableData[i][4] = suspendList.get(i).getMyProgress().getTimeLength();
                    tableData[i][5] = suspendList.get(i).getMyProgress().getMemoryLength();
                    tableData[i][6] = suspendList.get(i).getMemoryParam().getStart();
                }
                break;
            case MFQ:
                suspendColumnName = MFQHeader[4];
                tableData = new Object[count][6];
                for (int i = 0; i < count; i++) {
                    tableData[i][0] = suspendList.get(i).getPid();
                    tableData[i][1] = suspendList.get(i).getMyProgress().getName();
                    tableData[1][2] = suspendList.get(i).getMyProgress().getQueueOrder();
                    tableData[i][3] = suspendList.get(i).getMyProgress().getTimeLength();
                    tableData[i][4] = suspendList.get(i).getMyProgress().getMemoryLength();
                    tableData[i][5] = suspendList.get(i).getMemoryParam().getStart();
                }
                break;
        }

        suspendTableModel = new DefaultTableModel(tableData, suspendColumnName){  // 修改
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        suspendTable.setModel(suspendTableModel);  // 修改
    }

    public void refresh() {
        showRunningData();
        showReadyData();
        showBackUpData();
        showPCBData();
        showSuspendData();

        recordDialog.refresh();
        memoryDialog.refresh();

        memoryLinePanel.removeAll();
        memoryLinePanel.repaint();
        initMemoryLinePanel(memoryLinePanel);
        memoryLinePanel.revalidate();
    }
}
