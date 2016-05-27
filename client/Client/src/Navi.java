import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;


public class Navi extends JFrame {

    private JPanel panel = new JPanel();
    private JButton buttonInventory = new JButton("库存管理");
    private JButton buttonProduct = new JButton("前台管理");
    private JButton buttonPrice = new JButton("定价");
    private JButton buttonRecord = new JButton("交易查询");

    Navi() {
        super("商场管理系统: 选择一个操作");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().add(panel);
        panel.setLayout(null);
        
        buttonProduct.setFont(new Font("宋体", Font.PLAIN, 12));
        buttonProduct.setBounds(155, 77, 105, 23);
        
        buttonInventory.setFont(new Font("宋体", Font.PLAIN, 12));
        buttonInventory.setBounds(27, 77, 105, 23);
        
        buttonPrice.setFont(new Font("宋体", Font.PLAIN, 12));
        buttonPrice.setBounds(27, 163, 105, 23);
        
        buttonRecord.setFont(new Font("宋体", Font.PLAIN, 12));
        buttonRecord.setBounds(155, 163, 105, 23);
        
        buttonInventory.addActionListener(e->{
        	Storage storage=new Storage();
        	storage.setVisible(true);
        });
        buttonPrice.addActionListener(e->{
        	PriceSetting priceSetting=new PriceSetting();
        	priceSetting.setVisible(true);
        });
        buttonRecord.addActionListener(e->{
        	SaleRecords saleRecords=new SaleRecords();
        	saleRecords.setVisible(true);
        });
        buttonProduct.addActionListener(e->{
        	SaleManage saleManage=new SaleManage();
        	saleManage.setVisible(true);
        });
        panel.add(buttonProduct);
        panel.add(buttonInventory);
        panel.add(buttonPrice);
        panel.add(buttonRecord);
        this.setSize(300,300);

        new Login(this);
    }

}
