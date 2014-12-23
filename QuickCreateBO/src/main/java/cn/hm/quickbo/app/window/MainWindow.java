package cn.hm.quickbo.app.window;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import cn.hm.quickbo.app.dialog.SettingDialog;
import cn.hm.quickbo.app.mess.PutMessage;
import cn.hm.quickbo.conf.AppConfigure;
import cn.hm.quickbo.dbtable.service.impl.AWSQuickTableGeneratorImpl;
import cn.hm.quickbo.dbtable.util.HttpTablePaser;
import cn.hm.quickbo.util.ValidateUtil;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class MainWindow implements PutMessage {

  private JFrame frmv;
  private JTextArea textArea;
  private JLabel lblNewLabel;
  private JTextField textField_1;
  private JButton btnNewButton_1;
  private JScrollPane scrollPane;
  private JButton button;
  private JButton button_1;
  private JMenuBar menuBar;
  private JMenu mnNewMenu;
  private JMenuItem menuItem;
  private JMenuItem menuItem_1;
  private JSeparator separator;
  private SettingDialog settingDialog;

  private AWSQuickTableGeneratorImpl generator = new AWSQuickTableGeneratorImpl();

  private AppConfigure appConfig = AppConfigure.getInstance();

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {

      public void run() {
        try {
          MainWindow window = new MainWindow();
          window.frmv.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * 处理默认的皮肤和字体.
   */
  static {

    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
      e1.printStackTrace();
    }

    Font font = new Font("微软雅黑", Font.PLAIN, 12);
    java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = UIManager.get(key);
      if (value instanceof javax.swing.plaf.FontUIResource) {
        UIManager.put(key, font);
      }
    }

  }

  /**
   * Create the application.
   */
  public MainWindow() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {

    frmv = new JFrame();
    frmv.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent e) {
        int confirm = JOptionPane.showConfirmDialog(frmv, "确定退出?", "提示", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
          System.exit(0);
        } else {
          frmv.setVisible(true);
        }
      }
    });

    frmv.setTitle(appConfig.getAppName() + " " + appConfig.getVersion());
    frmv.setBounds(100, 100, 500, 490);
    frmv.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frmv.setIconImage(new BufferedImage(1, 1, BufferedImage.BITMASK));
    frmv.getContentPane().setLayout(
            new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.DEFAULT_COLSPEC,
                FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] { FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("max(25dlu;default)"), FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("max(161dlu;default):grow"),
                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

    JButton btnNewButton = new JButton("\u6253\u5F00\u6587\u4EF6");
    btnNewButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileFilter() {

          @Override
          public String getDescription() {
            return "(.xlsx)文件";
          }
          @Override
          public boolean accept(File f) {
            return f.getName().endsWith(".xlsx") || f.isDirectory();
          }
        });

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.showOpenDialog(null);

        File selectedFile = chooser.getSelectedFile();
        if (selectedFile == null || selectedFile.getAbsolutePath().equals(textField_1.getText())) {
          return;
        } else {
          textField_1.setText(selectedFile.getAbsolutePath());
        }
      }
    });

    textField_1 = new JTextField();
    frmv.getContentPane().add(textField_1, "2, 3, fill, default");
    textField_1.setColumns(10);
    frmv.getContentPane().add(btnNewButton, "3, 3, left, default");
    settingDialog = new SettingDialog();

    generator.setPutMessage(this);

    btnNewButton_1 = new JButton("\u5F00\u59CB\u5EFA\u8868");
    btnNewButton_1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        // 获取文件名
        final String filepath = textField_1.getText();
        if (ValidateUtil.validateNullOrEmtpy(filepath)) {
          JOptionPane.showMessageDialog(frmv, "请填写文件路径!");
          return;
        }

        textArea.setText("");
        textArea.append("开始检测配置...\n");
        if (!HttpTablePaser.testConnection()) {
          JOptionPane.showMessageDialog(frmv, "请先配置!");
          settingDialog.setModal(true);
          settingDialog.setVisible(true);
          if (!settingDialog.isConfAvailable()) {
            return;
          }
        }

        textArea.append("配置检测成功\n");
        // 开始创建表
        new Thread() {

          public void run() {
            try {
              generator.startCreate(filepath);
              JOptionPane.showMessageDialog(frmv, "创建完成!");
            } catch (RuntimeException e) {
              JOptionPane.showMessageDialog(frmv, "创建失败!");
              textArea.append("创建失败!\n");
              textArea.append("原因:" + e.getMessage());
              e.printStackTrace();
            }
          }
        }.start();
      }
    });
    frmv.getContentPane().add(btnNewButton_1, "4, 3");

    lblNewLabel = new JLabel("\u7ED3\u679C\u8BB0\u5F55:");
    frmv.getContentPane().add(lblNewLabel, "2, 4");

    scrollPane = new JScrollPane();
    frmv.getContentPane().add(scrollPane, "2, 5, 3, 1, fill, fill");

    textArea = new JTextArea();
    textArea.setEditable(false);
    scrollPane.setViewportView(textArea);
    textArea.setBorder(UIManager.getBorder("TextArea.border"));

    button_1 = new JButton("打开模板");
    button_1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        String path = System.getProperty("user.dir");
        // 导出模板
        try {
          Runtime.getRuntime().exec("cmd /c start " + path + "\\样例.xlsx");
        } catch (IOException e1) {
          e1.printStackTrace();
          JOptionPane.showMessageDialog(frmv, "模板打开失败!!");
        }
      }
    });
    frmv.getContentPane().add(button_1, "3, 7");

    button = new JButton("\u5BFC\u51FA\u8BB0\u5F55");
    button.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        // 导出记录
        FileWriter writer = null;
        try {
          writer = new FileWriter("temp.txt");
          writer.write(textArea.getText());
        } catch (IOException e1) {
          e1.printStackTrace();
          JOptionPane.showMessageDialog(frmv, "导出失败!");
          return;
        } finally {
          try {
            if (writer != null) {
              writer.close();
            }
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }
        try {
          Runtime.getRuntime().exec("cmd /c start temp.txt");
        } catch (IOException e1) {
          e1.printStackTrace();
          JOptionPane.showMessageDialog(frmv, "导出记录失败!!");
        }
      }
    });
    frmv.getContentPane().add(button, "4, 7");

    menuBar = new JMenuBar();
    menuBar.setMargin(new Insets(5, 0, 5, 0));
    frmv.setJMenuBar(menuBar);

    mnNewMenu = new JMenu("\u5F00\u59CB");
    mnNewMenu.setHorizontalTextPosition(SwingConstants.CENTER);
    mnNewMenu.setHorizontalAlignment(SwingConstants.CENTER);
    mnNewMenu.setPreferredSize(new Dimension(35, 23));
    menuBar.add(mnNewMenu);

    menuItem = new JMenuItem("\u8BBE\u7F6E");
    menuItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        settingDialog.setModal(true);
        settingDialog.setVisible(true);
      }
    });
    mnNewMenu.add(menuItem);

    separator = new JSeparator();
    mnNewMenu.add(separator);

    menuItem_1 = new JMenuItem("\u9000\u51FA");
    menuItem_1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    mnNewMenu.add(menuItem_1);
  }

  @Override
  public void putMessage(String message) {
    textArea.append(message);
    textArea.append("\n");
    textArea.select(textArea.getText().length(), textArea.getText().length());
  }

  @Override
  public void clearMessage() {
    textArea.setText("");
  }
}
