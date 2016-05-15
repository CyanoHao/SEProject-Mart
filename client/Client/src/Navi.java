import javax.swing.*;
import java.awt.*;


class Navi extends JFrame {

    private JPanel panel = new JPanel(new FlowLayout());
    private JButton buttonStaffing = new JButton("人员管理");
    private JButton buttonProduct = new JButton("商品管理");
    private JButton buttonInventory = new JButton("库存管理");

    Navi() {
        super("商场管理系统: 选择一个操作");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(panel);
        panel.add(buttonStaffing);
        panel.add(buttonProduct);
        panel.add(buttonInventory);
        this.setSize(300,300);

        new Login(this);
    }

}
