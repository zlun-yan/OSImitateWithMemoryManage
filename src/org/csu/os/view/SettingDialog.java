package org.csu.os.view;

import org.csu.os.view.component.ControllerPanel;

import javax.swing.*;
import java.awt.*;

public class SettingDialog extends JFrame {
    private JSpinner randomTimeMinSpinner;
    private JSpinner randomTimeMaxSpinner;
    private JSpinner randomPriorityMinSpinner;
    private JSpinner randomPriorityMaxSpinner;
    private JSpinner randomMemoryMinSpinner;
    private JSpinner randomMemoryMaxSpinner;

    public SettingDialog() {
        init();
        initButton();

        pack();
        setTitle("设置");
        setLocationRelativeTo(null);
    }

    private void init() {
        randomTimeMinSpinner = new JSpinner(new SpinnerNumberModel(ControllerPanel.getTimeMinn(), 1, 2048, 1));
        randomTimeMaxSpinner = new JSpinner(new SpinnerNumberModel(ControllerPanel.getTimeMaxx(), 1, 2048, 1));
        randomPriorityMinSpinner = new JSpinner(new SpinnerNumberModel(ControllerPanel.getPriorityMinn(), 1, 2048, 1));
        randomPriorityMaxSpinner = new JSpinner(new SpinnerNumberModel(ControllerPanel.getPriorityMaxx(), 1, 2048, 1));
        randomMemoryMinSpinner = new JSpinner(new SpinnerNumberModel(ControllerPanel.getMemoryMinn(), 1, 2048, 1));
        randomMemoryMaxSpinner = new JSpinner(new SpinnerNumberModel(ControllerPanel.getMemoryMaxx(), 1, 2048, 1));

        randomTimeMinSpinner.addChangeListener(event -> {
            int value = (int) randomTimeMinSpinner.getValue();
            int maxx = (int) randomTimeMaxSpinner.getValue();
            if (value > maxx) randomTimeMinSpinner.setValue(maxx);
        });
        randomTimeMaxSpinner.addChangeListener(event -> {
            int value = (int) randomTimeMaxSpinner.getValue();
            int minn = (int) randomTimeMinSpinner.getValue();
            if (value < minn) randomTimeMaxSpinner.setValue(minn);
        });
        randomPriorityMinSpinner.addChangeListener(event -> {
            int value = (int) randomPriorityMinSpinner.getValue();
            int maxx = (int) randomPriorityMaxSpinner.getValue();
            if (value > maxx) randomPriorityMinSpinner.setValue(maxx);
        });
        randomPriorityMaxSpinner.addChangeListener(event -> {
            int value = (int) randomPriorityMaxSpinner.getValue();
            int minn = (int) randomPriorityMinSpinner.getValue();
            if (value < minn) randomPriorityMaxSpinner.setValue(minn);
        });
        randomMemoryMinSpinner.addChangeListener(event -> {
            int value = (int) randomMemoryMinSpinner.getValue();
            int maxx = (int) randomMemoryMaxSpinner.getValue();
            if (value > maxx) randomMemoryMinSpinner.setValue(maxx);
        });
        randomMemoryMaxSpinner.addChangeListener(event -> {
            int value = (int) randomMemoryMaxSpinner.getValue();
            int minn = (int) randomMemoryMinSpinner.getValue();
            if (value < minn) randomMemoryMaxSpinner.setValue(minn);
        });

        JLabel randomTimeMinLabel = new JLabel("随机时间最小值: ");
        randomTimeMinLabel.setPreferredSize(new Dimension(120, 30));
        randomTimeMinSpinner.setPreferredSize(new Dimension(80, 30));
        JPanel randomTimeMinPanel = new JPanel();
        randomTimeMinPanel.add(randomTimeMinLabel);
        randomTimeMinPanel.add(randomTimeMinSpinner);

        JLabel randomTimeMaxLabel = new JLabel("随机时间最大值: ");
        randomTimeMaxLabel.setPreferredSize(new Dimension(120, 30));
        randomTimeMaxSpinner.setPreferredSize(new Dimension(80, 30));
        JPanel randomTimeMaxPanel = new JPanel();
        randomTimeMaxPanel.add(randomTimeMaxLabel);
        randomTimeMaxPanel.add(randomTimeMaxSpinner);

        JLabel randomPriorityMinLabel = new JLabel("随机优先级最小值: ");
        randomPriorityMinLabel.setPreferredSize(new Dimension(120, 30));
        randomPriorityMinSpinner.setPreferredSize(new Dimension(80, 30));
        JPanel randomPriorityMinPanel = new JPanel();
        randomPriorityMinPanel.add(randomPriorityMinLabel);
        randomPriorityMinPanel.add(randomPriorityMinSpinner);

        JLabel randomPriorityMaxLabel = new JLabel("随机优先级最大值: ");
        randomPriorityMaxLabel.setPreferredSize(new Dimension(120, 30));
        randomPriorityMaxSpinner.setPreferredSize(new Dimension(80, 30));
        JPanel randomPriorityMaxPanel = new JPanel();
        randomPriorityMaxPanel.add(randomPriorityMaxLabel);
        randomPriorityMaxPanel.add(randomPriorityMaxSpinner);

        JLabel randomMemoryMinLabel = new JLabel("随机主存大小最小值: ");
        randomMemoryMinLabel.setPreferredSize(new Dimension(120, 30));
        randomMemoryMinSpinner.setPreferredSize(new Dimension(80, 30));
        JPanel randomMemoryMinPanel = new JPanel();
        randomMemoryMinPanel.add(randomMemoryMinLabel);
        randomMemoryMinPanel.add(randomMemoryMinSpinner);

        JLabel randomMemoryMaxLabel = new JLabel("随机主存大小最大值: ");
        randomMemoryMaxLabel.setPreferredSize(new Dimension(120, 30));
        randomMemoryMaxSpinner.setPreferredSize(new Dimension(80, 30));
        JPanel randomMemoryMaxPanel = new JPanel();
        randomMemoryMaxPanel.add(randomMemoryMaxLabel);
        randomMemoryMaxPanel.add(randomMemoryMaxSpinner);

        Box box = Box.createVerticalBox();
        box.add(randomTimeMinPanel);
        box.add(randomTimeMaxPanel);
        box.add(randomPriorityMinPanel);
        box.add(randomPriorityMaxPanel);
        box.add(randomMemoryMinPanel);
        box.add(randomMemoryMaxPanel);

        add(box, BorderLayout.CENTER);
    }

    private void initButton() {
        JButton confirmButton = new JButton("确认");
        JButton cancelButton = new JButton("取消");

        confirmButton.addActionListener(event -> {
            ControllerPanel.setTimeMinn((int) randomTimeMinSpinner.getValue());
            ControllerPanel.setTimeMaxx((int) randomTimeMaxSpinner.getValue());
            ControllerPanel.setPriorityMinn((int) randomPriorityMinSpinner.getValue());
            ControllerPanel.setPriorityMaxx((int) randomPriorityMaxSpinner.getValue());
            ControllerPanel.setMemoryMinn((int) randomMemoryMinSpinner.getValue());
            ControllerPanel.setMemoryMaxx((int) randomMemoryMaxSpinner.getValue());
            dispose();
        });
        cancelButton.addActionListener(event -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}
