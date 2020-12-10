package org.csu.os.view.component;

import org.csu.os.domain.pojo.Progress;
import org.csu.os.service.Static;
import org.csu.os.service.controller.PCBController;
import org.csu.os.view.MainFrame;
import org.csu.os.view.SettingDialog;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

import static org.csu.os.service.Static.mode;

public class ControllerPanel extends JPanel {
    private SettingDialog settingDialog = null;

    private MainFrame parentFrame;
    private static boolean queueOrder = true;  // true为前台

    private static int timeMinn = 1;
    private static int timeMaxx = 10;
    private static int priorityMinn = 1;
    private static int priorityMaxx = 50;
    private static int memoryMinn = 1;
    private static int memoryMaxx = 100;

    private JTextField nameField;
    private JTextField timeField;
    private JTextField priorityField;
    private JTextField memoryField;

    private JLabel tipLabel;  // 错误输入提示

    private JPanel radioButtonPanel;

    private Box box = Box.createVerticalBox();
    private static int count = 0;  // 随机添加计数

    public ControllerPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        init();
        initButton();
        refresh();

        add(box, BorderLayout.CENTER);
    }

    private void init() {
        JLabel nameLabel = new JLabel("进程名称:");
        JLabel timeLabel = new JLabel("预计运行时间:");
        JLabel priorityLabel = new JLabel("优先级:");
        JLabel memoryLabel = new JLabel("所需主存大小");
        nameLabel.setPreferredSize(new Dimension(80, 30));
        timeLabel.setPreferredSize(new Dimension(80, 30));
        priorityLabel.setPreferredSize(new Dimension(80, 30));
        memoryLabel.setPreferredSize(new Dimension(80, 30));

        nameField = new JTextField();
        timeField = new JTextField();
        priorityField = new JTextField();
        memoryField = new JTextField();
        nameField.setPreferredSize(new Dimension(120, 30));
        timeField.setPreferredSize(new Dimension(120, 30));
        priorityField.setPreferredSize(new Dimension(120, 30));
        memoryField.setPreferredSize(new Dimension(120, 30));

        JRadioButton receptionButton = new JRadioButton("前台", true);
        JRadioButton backgroundButton = new JRadioButton("后台");

        receptionButton.addActionListener(event -> {
            if (receptionButton.isSelected()) queueOrder = true;
        });
        backgroundButton.addActionListener(event -> {
            if (backgroundButton.isSelected()) queueOrder = false;
        });

        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(receptionButton);
        radioButtonGroup.add(backgroundButton);

        radioButtonPanel = new JPanel();
        radioButtonPanel.add(receptionButton);
        radioButtonPanel.add(backgroundButton);

        tipLabel = new JLabel();
        tipLabel.setPreferredSize(new Dimension(200, 30));
        tipLabel.setVisible(false);
        JPanel tipPanel = new JPanel();
        tipPanel.add(tipLabel);

        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        JPanel timePanel = new JPanel();
        timePanel.add(timeLabel);
        timePanel.add(timeField);
        JPanel priorityPanel = new JPanel();
        priorityPanel.add(priorityLabel);
        priorityPanel.add(priorityField);
        JPanel memoryPanel = new JPanel();
        memoryPanel.add(memoryLabel);
        memoryPanel.add(memoryField);

        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("操作", SwingConstants.CENTER);
        headerPanel.add(headerLabel);

        box.add(headerPanel);
        box.add(namePanel);
        box.add(timePanel);
        box.add(memoryPanel);
        box.add(priorityPanel);
        box.add(radioButtonPanel);
        box.add(tipPanel);
    }

    private void initButton() {
        JButton confirmButton = new JButton("增加进程");
        JButton cancelButton = new JButton("清除");
        JButton randomAddButton = new JButton("随机添加");
        JButton multiRandomButton = new JButton("随机添加十个");
        JButton settingButton = new JButton("设置");

        multiRandomButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));

        confirmButton.addActionListener(event -> {
            tipLabel.setVisible(false);
            String name = nameField.getText();
            String time = timeField.getText();
            String priority = priorityField.getText();
            String memory = memoryField.getText();

            int timeToInt, memoryToInt, priorityToInt = -1;
            if (name == null || name.equals("")) {
                tipLabel.setText("请输入进程名称");
                tipLabel.setVisible(true);
                return;
            }

            if (time == null || time.equals("")) {
                tipLabel.setText("请输入进程预计运行时间");
                tipLabel.setVisible(true);
                return;
            }
            try {
                timeToInt = Integer.parseInt(time);
            } catch (NumberFormatException e) {
                tipLabel.setText("请输入整数预计运行时间");
                tipLabel.setVisible(true);
                return;
            }

            if (memory == null || memory.equals("")) {
                tipLabel.setText("请输入所需主存大小");
                tipLabel.setVisible(true);
                return;
            }
            try {
                memoryToInt = Integer.parseInt(memory);
            } catch (NumberFormatException e) {
                tipLabel.setText("请输入整数所需主存大小");
                tipLabel.setVisible(true);
                return;
            }

            Progress progress;
            if ((mode == Static.DispatchMode.MQ && queueOrder) || mode == Static.DispatchMode.PSA) {
                if (priority == null || priority.equals("")) {
                    tipLabel.setText("请输入进程优先级");
                    tipLabel.setVisible(true);
                    return;
                }
                try {
                    priorityToInt = Integer.parseInt(priority);
                } catch (NumberFormatException e) {
                    tipLabel.setText("请输入整数优先级");
                    tipLabel.setVisible(true);
                    return;
                }
            }

            progress = new Progress(name, priorityToInt, timeToInt, memoryToInt, queueOrder ? 1 : 2);
            PCBController.addProgress(progress);

            nameField.setText("");
            timeField.setText("");
            priorityField.setText("");
            memoryField.setText("");
        });
        cancelButton.addActionListener(event -> {
            tipLabel.setVisible(false);
            nameField.setText("");
            timeField.setText("");
            priorityField.setText("");
            memoryField.setText("");
        });
        randomAddButton.addActionListener(event -> {
            tipLabel.setVisible(false);
            randomAdd();
        });
        multiRandomButton.addActionListener(event -> {
            tipLabel.setVisible(false);
            for (int i = 0; i < 10; i++) {
                randomAdd();
            }
        });
        settingButton.addActionListener(event -> {
            if (settingDialog == null) settingDialog = new SettingDialog();
            settingDialog.setVisible(true);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(settingButton);

        JPanel buttonPanelLine2 = new JPanel();
        buttonPanelLine2.add(randomAddButton);
        buttonPanelLine2.add(multiRandomButton);

//        JPanel buttonPanelLine3 = new JPanel();
//        buttonPanelLine3.add(settingButton);

        box.add(buttonPanel);
        box.add(buttonPanelLine2);
//        box.add(buttonPanelLine3);
    }

    private void randomAdd() {
        String name = "Random_" + ++count;
        int time = new Random().nextInt(timeMaxx - timeMinn) + timeMinn;
        int priority = new Random().nextInt(priorityMaxx - priorityMinn) + priorityMinn;
        int memory = new Random().nextInt(memoryMaxx - memoryMinn) + memoryMinn;
//        RecordDialog.refresh();

        Progress progress;
        if (mode == Static.DispatchMode.MQ) {
            int randomQueueOrder = new Random().nextInt(2) + 1;
            if (randomQueueOrder == 1) progress = new Progress(name, priority, time, memory, randomQueueOrder);
            else progress = new Progress(name, -1, time, memory, randomQueueOrder);  // 如果是后台的话
        }
        else progress = new Progress(name, priority, time, memory, 1);

        PCBController.addProgress(progress);
    }

    public static int getTimeMinn() {
        return timeMinn;
    }

    public static void setTimeMinn(int timeMinn) {
        ControllerPanel.timeMinn = timeMinn;
    }

    public static int getTimeMaxx() {
        return timeMaxx;
    }

    public static void setTimeMaxx(int timeMaxx) {
        ControllerPanel.timeMaxx = timeMaxx;
    }

    public static int getPriorityMinn() {
        return priorityMinn;
    }

    public static void setPriorityMinn(int priorityMinn) {
        ControllerPanel.priorityMinn = priorityMinn;
    }

    public static int getPriorityMaxx() {
        return priorityMaxx;
    }

    public static void setPriorityMaxx(int priorityMaxx) {
        ControllerPanel.priorityMaxx = priorityMaxx;
    }

    public static int getMemoryMinn() {
        return memoryMinn;
    }

    public static void setMemoryMinn(int memoryMinn) {
        ControllerPanel.memoryMinn = memoryMinn;
    }

    public static int getMemoryMaxx() {
        return memoryMaxx;
    }

    public static void setMemoryMaxx(int memoryMaxx) {
        ControllerPanel.memoryMaxx = memoryMaxx;
    }

    public static void clear() {
        count = 0;
        timeMinn = 1;
        timeMaxx = 10;
        priorityMinn = 1;
        priorityMaxx = 50;
    }

    public void refresh() {
        if (mode != Static.DispatchMode.MQ) radioButtonPanel.setVisible(false);
        else radioButtonPanel.setVisible(true);
    }
}
