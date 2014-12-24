package cn.hm.quickbo.app.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import cn.hm.quickbo.conf.AWSConfigure;
import cn.hm.quickbo.dbtable.util.HttpLogin;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SettingDialog extends JDialog {

  private static final long serialVersionUID = -1847687160851978176L;

  private final JPanel contentPanel = new JPanel();
  private JTextField textField_2;
  private JTextField textField_3;
  private JTextField textField;

  /**
   * 最后一次正确记录.
   */
  private String historyTextField1;
  private String historyTextField2;
  private String historyTextField3;

  private boolean confAvailable = false;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    try {
      SettingDialog dialog = new SettingDialog();
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      dialog.setVisible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Create the dialog.
   */
  public SettingDialog() {
    addWindowListener(new WindowAdapter() {

      @Override
      public void windowDeactivated(WindowEvent e) {
        if (!confAvailable && !SettingDialog.this.isVisible()) {
          recoverdHistoryRecord();
        }
        confAvailable = false;
      }
    });

    setTitle("\u8BBE\u7F6E");
    setResizable(false);
    setBounds(100, 100, 240, 170);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    contentPanel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
        ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
        FormFactory.DEFAULT_ROWSPEC, }));
    {
      JLabel label = new JLabel("后台地址:");
      label.setHorizontalAlignment(SwingConstants.RIGHT);
      contentPanel.add(label, "2, 2, right, default");
    }
    {
      textField = new JTextField();
      textField.setToolTipText("例如: http://localhost:8088/portal/console");
      contentPanel.add(textField, "4, 2, fill, default");
      textField.setColumns(10);
    }
    {
      JLabel label = new JLabel("\u767B\u5F55\u8D26\u6237:");
      contentPanel.add(label, "2, 4, right, default");
    }
    {
      textField_2 = new JTextField();
      textField_2.setColumns(10);
      contentPanel.add(textField_2, "4, 4, fill, default");
    }
    {
      JLabel label = new JLabel("\u767B\u5F55\u5BC6\u7801:");
      contentPanel.add(label, "2, 6, right, default");
    }
    {
      textField_3 = new JTextField();
      textField_3.setColumns(10);
      contentPanel.add(textField_3, "4, 6, fill, default");
    }
    {
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);
      {
        JButton okButton = new JButton("\u5B8C\u6210");
        okButton.addActionListener(new ActionListener() {

          public void actionPerformed(ActionEvent e) {
            // 获取配置
            AWSConfigure conf = AWSConfigure.getInstance();
            conf.setAwsurl(textField.getText());
            conf.setUsername(textField_2.getText());
            conf.setPassword(textField_3.getText());

            // 测试连接.
            if (HttpLogin.testLogin()) {
              // 成功执行.
              SettingDialog.this.setVisible(false);
              confAvailable = true;
              setHistoryRecord();
            } else {
              // 错误提示.
              JOptionPane.showMessageDialog(null, "请检查 IP 和 端口 是否填写正确!", "错误提示", JOptionPane.ERROR_MESSAGE);
              confAvailable = false;
            }
          }
        });
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
      }
    }
  }

  public boolean isConfAvailable() {
    return confAvailable;
  }

  /**
   * 设置正确记录.
   */
  private void setHistoryRecord() {
    historyTextField1 = textField.getText();
    historyTextField2 = textField_2.getText();
    historyTextField3 = textField_3.getText();
  }

  /**
   * 回复正确记录.
   */
  private void recoverdHistoryRecord() {
    textField.setText(historyTextField1);
    textField_2.setText(historyTextField2);
    textField_3.setText(historyTextField3);
  }

}
