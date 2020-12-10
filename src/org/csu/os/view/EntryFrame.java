package org.csu.os.view;

import org.csu.os.service.Static;
import org.csu.os.service.Init;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import java.awt.*;

import static org.csu.os.service.Static.memoryAllocateMode;
import static org.csu.os.service.Static.mode;

public class EntryFrame extends JFrame {
    private int PCBCount = 6;
    private int timeSlice = 4;
    private static MainFrame mainFrame;

    private JRadioButton FCFSButton = new JRadioButton("先到先服务调度", true);
    private JRadioButton SJFButton = new JRadioButton("最短作业调度");
    private JRadioButton PSAButton = new JRadioButton("优先级调度");
    private JRadioButton RRButton = new JRadioButton("轮转法调度");
    private JRadioButton MQButton = new JRadioButton("多级队列调度");
    private JRadioButton MFQButton = new JRadioButton("多级反馈队列调度");

    private JRadioButton FirstButton = new JRadioButton("首次适应算法", true);
    private JRadioButton FitButton = new JRadioButton("最佳适应算法");
    private JRadioButton BiggestButton = new JRadioButton("最差适应算法");

    private JPanel timeSlicePanel;

    public EntryFrame() {
        JPanel dispatchRadioButtonPanel = initDispatchRadioButton();
        JPanel settingPanel = initPanel();
        JPanel buttonPanel = initButton();
        JPanel memoryRadioButtonPanel = initMemoryRadioButton();

        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new BorderLayout());
        radioButtonPanel.add(dispatchRadioButtonPanel, BorderLayout.NORTH);
        radioButtonPanel.add(memoryRadioButtonPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(radioButtonPanel, BorderLayout.NORTH);
        add(settingPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setTitle("调度算法模拟");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel PCBCountLabel = new JLabel("设定道数:");
        PCBCountLabel.setPreferredSize(new Dimension(100, 30));
        JSpinner PCBCountSpinner = new JSpinner(new SpinnerNumberModel(PCBCount, 1, 2048, 1));
        PCBCountSpinner.setPreferredSize(new Dimension(120, 30));
        PCBCountSpinner.addChangeListener(event -> PCBCount = (int) PCBCountSpinner.getValue());

        JPanel PCBCountPanel = new JPanel();
        PCBCountPanel.add(PCBCountLabel);
        PCBCountPanel.add(PCBCountSpinner);

        JLabel timeSliceLabel = new JLabel("设定时间片长度:");
        timeSliceLabel.setPreferredSize(new Dimension(100, 30));
        JSpinner timeSliceSpinner = new JSpinner(new SpinnerNumberModel(timeSlice, 1, 2048, 1));
        timeSliceSpinner.setPreferredSize(new Dimension(120, 30));
        timeSliceSpinner.addChangeListener(event -> timeSlice = (int) timeSliceSpinner.getValue());

        timeSlicePanel = new JPanel();
        timeSlicePanel.add(timeSliceLabel);
        timeSlicePanel.add(timeSliceSpinner);
        timeSlicePanel.setVisible(false);

        panel.add(PCBCountPanel, BorderLayout.NORTH);
        panel.add(timeSlicePanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel initDispatchRadioButton() {
        JPanel panel = new JPanel();
        ButtonGroup radioButtonGroup = new ButtonGroup();

        radioButtonGroup.add(FCFSButton);
        radioButtonGroup.add(SJFButton);
        radioButtonGroup.add(PSAButton);
        radioButtonGroup.add(RRButton);
        radioButtonGroup.add(MQButton);
        radioButtonGroup.add(MFQButton);

        FCFSButton.addActionListener(event -> {
            if (FCFSButton.isSelected()) {
                mode = Static.DispatchMode.FCFS;
                timeSlicePanel.setVisible(false);
                pack();
            }
        });  // 先到先服务
        SJFButton.addActionListener(event -> {
            if (SJFButton.isSelected()) {
                mode = Static.DispatchMode.SJF;
                timeSlicePanel.setVisible(false);
                pack();
            }
        });  // 最短时间调度
        PSAButton.addActionListener(event -> {
            if (PSAButton.isSelected()) {
                mode =  Static.DispatchMode.PSA;
                timeSlicePanel.setVisible(false);
                pack();
            }
        });  //  优先级调度
        RRButton.addActionListener(event -> {
            if (RRButton.isSelected()) {
                mode = Static.DispatchMode.RR;
                timeSlicePanel.setVisible(true);
                pack();
            }
        });  //  时间片轮转
        MQButton.addActionListener(event -> {
            if (MQButton.isSelected()) {
                mode = Static.DispatchMode.MQ;
                timeSlicePanel.setVisible(true);
                pack();
            }
        });  // 多级队列调度
        MFQButton.addActionListener(event -> {
            if (MFQButton.isSelected()) {
                mode = Static.DispatchMode.MFQ;
                timeSlicePanel.setVisible(true);
                pack();
            }
        });  // 多级反馈队列调度


        panel.add(FCFSButton);
        panel.add(SJFButton);
        panel.add(PSAButton);
        panel.add(RRButton);
        panel.add(MQButton);
        panel.add(MFQButton);

        return panel;
    }

    private JPanel initMemoryRadioButton() {
        JPanel panel = new JPanel();
        ButtonGroup radioButtonGroup = new ButtonGroup();

        radioButtonGroup.add(FirstButton);
        radioButtonGroup.add(FitButton);
        radioButtonGroup.add(BiggestButton);

        FirstButton.addActionListener(event -> {
            if (FirstButton.isSelected()) {
                memoryAllocateMode = Static.MemoryAllocateMode.FIRST;
            }
        });
        FitButton.addActionListener(event -> {
            if (FitButton.isSelected()) {
                memoryAllocateMode = Static.MemoryAllocateMode.FIT;
            }
        });
        BiggestButton.addActionListener(event -> {
            if (BiggestButton.isSelected()) {
                memoryAllocateMode = Static.MemoryAllocateMode.BIGGEST;
            }
        });

        panel.add(FirstButton);
        panel.add(FitButton);
        panel.add(BiggestButton);

        return panel;
    }

    private JPanel initButton() {
        JButton confirmButton = new JButton("开始");
        confirmButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));

        confirmButton.addActionListener(event -> {
            Init.init(PCBCount, timeSlice);
            if (mainFrame == null) mainFrame = new MainFrame(this);
            else mainFrame.refresh();

            switch (mode) {
                case FCFS:
                    mainFrame.setTitle("先到先服务调度");
                    break;
                case SJF:
                    mainFrame.setTitle("最短作业调度");
                    break;
                case PSA:
                    mainFrame.setTitle("优先级调度");
                    break;
                case RR:
                    mainFrame.setTitle("轮转法调度");
                    break;
                case MQ:
                    mainFrame.setTitle("多级队列调度");
                    break;
                case MFQ:
                    mainFrame.setTitle("多级反馈队列调度");
                    break;
                default:
                    break;
            }

            mainFrame.setVisible(true);
            setVisible(false);
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);

        return buttonPanel;
    }

    public static MainFrame getMainFrame() {
        return mainFrame;
    }
}
