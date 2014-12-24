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
import cn.hm.quickbo.app.mess.SetMessage;
import cn.hm.quickbo.conf.AppConfigure;
import cn.hm.quickbo.dbtable.service.FileTableGenerator;
import cn.hm.quickbo.dbtable.service.impl.AWSQuickTableGeneratorImpl;
import cn.hm.quickbo.dbtable.util.HttpLogin;
import cn.hm.quickbo.util.ValidateUtil;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class MainWindow {

  private JFrame frmv;
  private JTextArea recordTextArea;
  private JLabel resultLabel;
  private JTextField excelPathTextField;
  private JButton startBuildBtn;
  private JScrollPane scrollPane;
  private JButton exportRecordBtn;
  private JButton openModelBtn;
  private JMenuBar menuBar;
  private JMenu mnNewMenu;
  private JMenuItem confMenuItem;
  private JMenuItem exitMenuItem;
  private JSeparator separator;
  private SettingDialog settingDialog;
  private JButton openExcelBtn;

  private FileTableGenerator generator;

  /**
   * Launch the application.
   * 
   * @wbp.parser.entryPoint
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {

      public void run() {
        MainWindow window = new MainWindow();
        window.frmv.setVisible(true);
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
   * 
   * @wbp.parser.entryPoint
   */
  public MainWindow() {
    initialize();
    initializeEvent();

    AWSQuickTableGeneratorImpl tableGenerator = new AWSQuickTableGeneratorImpl();
    setTableGenerator(tableGenerator);
    setSetMessage(tableGenerator);
    setAppConfig(AppConfigure.getInstance());
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {

    frmv = new JFrame();

    frmv.setBounds(100, 100, 500, 490);
    frmv.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frmv.setIconImage(new BufferedImage(1, 1, BufferedImage.BITMASK));
    frmv.getContentPane().setLayout(
            new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.DEFAULT_COLSPEC,
                FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] { FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("max(25dlu;default)"), FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("max(161dlu;default):grow"),
                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

    openExcelBtn = new JButton("\u6253\u5F00\u6587\u4EF6");

    excelPathTextField = new JTextField();
    frmv.getContentPane().add(excelPathTextField, "2, 3, fill, default");
    excelPathTextField.setColumns(10);
    frmv.getContentPane().add(openExcelBtn, "3, 3, left, default");
    settingDialog = new SettingDialog();

    startBuildBtn = new JButton("\u5F00\u59CB\u5EFA\u8868");

    frmv.getContentPane().add(startBuildBtn, "4, 3");

    resultLabel = new JLabel("\u7ED3\u679C\u8BB0\u5F55:");
    frmv.getContentPane().add(resultLabel, "2, 4");

    scrollPane = new JScrollPane();
    frmv.getContentPane().add(scrollPane, "2, 5, 3, 1, fill, fill");

    recordTextArea = new JTextArea();
    recordTextArea.setEditable(false);
    scrollPane.setViewportView(recordTextArea);
    recordTextArea.setBorder(UIManager.getBorder("TextArea.border"));

    openModelBtn = new JButton("打开模板");

    frmv.getContentPane().add(openModelBtn, "3, 7");

    exportRecordBtn = new JButton("\u5BFC\u51FA\u8BB0\u5F55");

    frmv.getContentPane().add(exportRecordBtn, "4, 7");

    menuBar = new JMenuBar();
    menuBar.setMargin(new Insets(5, 0, 5, 0));
    frmv.setJMenuBar(menuBar);

    mnNewMenu = new JMenu("\u5F00\u59CB");
    mnNewMenu.setHorizontalTextPosition(SwingConstants.CENTER);
    mnNewMenu.setHorizontalAlignment(SwingConstants.CENTER);
    mnNewMenu.setPreferredSize(new Dimension(35, 23));
    menuBar.add(mnNewMenu);

    confMenuItem = new JMenuItem("\u8BBE\u7F6E");
    mnNewMenu.add(confMenuItem);

    separator = new JSeparator();
    mnNewMenu.add(separator);

    exitMenuItem = new JMenuItem("\u9000\u51FA");
    mnNewMenu.add(exitMenuItem);
  }

  /**
   * @wbp.parser.entryPoint
   */
  public void initializeEvent() {

    /**
     * 退出提示.
     */
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

    /**
     * 按钮导出记录.
     */
    exportRecordBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        // 导出记录
        FileWriter writer = null;
        try {
          writer = new FileWriter("temp.txt");
          writer.write(recordTextArea.getText());
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

    /**
     * 退出菜单.
     */
    exitMenuItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    /**
     * 设置菜单.
     */
    confMenuItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        settingDialog.setModal(true);
        settingDialog.setVisible(true);
      }
    });

    /**
     * 打开模板.
     */
    openModelBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        String path = System.getProperty("user.dir");
        // 导出模板
        try {
          Runtime.getRuntime().exec("cmd /c start " + path + "\\example.xlsx");
        } catch (IOException e1) {
          e1.printStackTrace();
          JOptionPane.showMessageDialog(frmv, "模板打开失败!!");
        }
      }
    });

    /**
     * 开始建表.
     */
    startBuildBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        // 获取文件名
        final String filepath = excelPathTextField.getText();
        if (ValidateUtil.validateNullOrEmtpy(filepath)) {
          JOptionPane.showMessageDialog(frmv, "请填写文件路径!");
          return;
        }

        recordTextArea.setText("");
        recordTextArea.append("开始检测配置...\n");
        if (!HttpLogin.testLogin()) {
          JOptionPane.showMessageDialog(frmv, "请先配置!");
          settingDialog.setModal(true);
          settingDialog.setVisible(true);
          if (!settingDialog.isConfAvailable()) {
            return;
          }
        }

        recordTextArea.append("配置检测成功\n");
        // 开始创建表
        new Thread() {

          public void run() {
            try {
              generator.startCreate(filepath);
              JOptionPane.showMessageDialog(frmv, "创建完成!");
            } catch (RuntimeException e) {
              JOptionPane.showMessageDialog(frmv, "创建失败!");
              recordTextArea.append("创建失败!\n");
              recordTextArea.append("原因:" + e.getMessage());
              e.printStackTrace();
            }
          }
        }.start();
      }
    });

    /**
     * 打开Excel文件.
     */
    openExcelBtn.addActionListener(new ActionListener() {

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
        if (selectedFile == null || selectedFile.getAbsolutePath().equals(excelPathTextField.getText())) {
          return;
        } else {
          excelPathTextField.setText(selectedFile.getAbsolutePath());
        }
      }
    });
  }

  /**
   * 注入表生成器.
   * 
   * @param generator
   */
  public void setTableGenerator(FileTableGenerator generator) {
    this.generator = generator;
  }

  /**
   * 注入配置.
   * 
   * @param appConfig
   */
  public void setAppConfig(AppConfigure appConfig) {
    frmv.setTitle(appConfig.getAppName() + " " + appConfig.getVersion());
  }

  /**
   * 设置消息传输.
   * 
   * @param setMessage
   */
  public void setSetMessage(SetMessage setMessage) {
    setMessage.setPutMessage(new PutMessage() {

      @Override
      public void putMessage(String message) {
        recordTextArea.append(message);
        recordTextArea.append("\n");
        recordTextArea.select(recordTextArea.getText().length(), recordTextArea.getText().length());
      }
      @Override
      public void clearMessage() {
        recordTextArea.setText("");
      }
    });
  }

}
