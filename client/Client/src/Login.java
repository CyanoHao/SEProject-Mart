import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


class Login extends JFrame {

    private JPanel panel = new JPanel();
    private JTextField inputUsername = new JTextField();
    private JPasswordField inputPasswd = new JPasswordField();
    private JButton buttonLogin = new JButton();
    private JLabel labelPasswd = new JLabel();
    private JLabel labelUsername = new JLabel();

    private Navi navi;

    Login(Navi navi) {
        super("商场管理系统: 请登录");
        this.navi = navi;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = inputUsername.getText();
                String passwd = new String(inputPasswd.getPassword());
                try {
                    URI uri = new URIBuilder()
                            .setScheme("http")
                            .setUserInfo(username, passwd)
                            .setHost("devel.cyano.cn")
                            .setPort(10001)
                            .setPath("/Mart/v1.0/auth")
                            .build();
                    HttpGet get = new HttpGet(uri);
                    CloseableHttpResponse response = Main.http.execute(get);

                    int responseCode = response.getStatusLine().getStatusCode();
                    switch (responseCode) {
                        case 200:
                            JSONObject res = new JSONObject(EntityUtils.toString(response.getEntity()));
                            Main.username = username;
                            Main.passwd = passwd;
                            Main.priority = res.getInt("priority");

                            Login login = (Login)((JButton)e.getSource()).getRootPane().getParent();
                            login.setVisible(false);
                            login.navi.setVisible(true);
                            
                            break;
                        case 401:
                            JOptionPane.showMessageDialog(
                                    null,
                                    "用户名或密码错误",
                                    "登录失败",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            break;
                        default:
                            JOptionPane.showMessageDialog(
                                    null,
                                    String.format(
                                            "服务器错误\n错误代码: %d\n错误信息: %s",
                                            responseCode,
                                            response.getStatusLine().getReasonPhrase()
                                    ),
                                    "服务器错误",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            break;
                    }
                } catch (URISyntaxException ex) {
                    /* do nothing */
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "无法连接服务器", "无法连接服务器", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.getContentPane().add(panel);
        panel.setLayout(new GridBagLayout());

        Insets zero = new Insets(0, 0, 0, 0);

        labelUsername.setText("用户名");
        panel.add(labelUsername, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, zero, 0, 0));
        panel.add(inputUsername, new GridBagConstraints(1, 0, 1, 1, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, zero, 0, 0));

        labelPasswd.setText("密码");
        panel.add(labelPasswd, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, zero, 0, 0));
        panel.add(inputPasswd, new GridBagConstraints(1, 1, 1, 1, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, zero, 0, 0));

        buttonLogin.setText("登录");
        panel.add(buttonLogin, new GridBagConstraints(0, 2, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, zero, 0, 0));

        setSize(300, 300);
        setVisible(true);
    }
}
