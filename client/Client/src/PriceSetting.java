import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JLabel;


public class PriceSetting extends JFrame {

	private JPanel contentPane;
	private JTable saleTable;
	private JScrollPane scrollPane;
	private JButton btnChangePrice;
	private JButton btnCancel;
	private JLabel saleLabel;
	private JButton btnPrint;

	public PriceSetting() {
		setTitle("定价管理");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 483, 383);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(127, 37, 316, 272);
		contentPane.add(panel);
		
		String[] colNames={"商品名","前台存量","售价"};
		Object[][] items={};
		saleTable = new JTable(items,colNames);
		saleTable.setPreferredScrollableViewportSize(new Dimension(panel.getWidth(),panel.getHeight()));
		saleTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		saleTable.getColumnModel().getColumn(0).setPreferredWidth((int)(panel.getWidth()*5.0/10.0));
		saleTable.getColumnModel().getColumn(1).setPreferredWidth((int)(panel.getWidth()*2.0/10.0));
		saleTable.getColumnModel().getColumn(2).setPreferredWidth((int)(panel.getWidth()*3.0/10.0));
	    panel.setLayout(null);
	    scrollPane=new JScrollPane(saleTable);
	    scrollPane.setBounds(0,0,panel.getWidth(),panel.getHeight());
	    panel.add(scrollPane);
	    
	    btnChangePrice = new JButton("修改定价");
	    btnChangePrice.setFont(new Font("宋体", Font.PLAIN, 11));
	    btnChangePrice.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    	}
	    });
	    btnChangePrice.setBounds(10, 87, 93, 23);
	    contentPane.add(btnChangePrice);
	    
	    btnCancel = new JButton("退出");
	    btnCancel.setFont(new Font("宋体", Font.PLAIN, 11));
	    btnCancel.setBounds(10, 215, 93, 23);
	    contentPane.add(btnCancel);
	    
	    saleLabel = new JLabel("前台商品表");
	    saleLabel.setFont(new Font("宋体", Font.PLAIN, 12));
	    saleLabel.setBounds(127, 12, 74, 15);
	    contentPane.add(saleLabel);
	    
	    btnPrint = new JButton("打印前台表");
	    btnPrint.setFont(new Font("宋体", Font.PLAIN, 11));
	    btnPrint.setBounds(10, 151, 93, 23);
	    contentPane.add(btnPrint);
	}

}
