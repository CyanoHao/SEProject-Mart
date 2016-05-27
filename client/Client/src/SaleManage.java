import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class SaleManage extends JFrame {

	private JPanel contentPane;
	private JTable saleTable;
	private JScrollPane scrollPane;
	private JTable outTable;
	private JScrollPane scrollPane2;
	private JButton btnPrint;
	private JLabel lblGoods;
	private JButton btnImport;
	private JButton btnDiscount;
	private JButton btnSale;
	private JLabel lbnList;
	private JLabel lblTotal;
	private UneditableTableModel tableModel;

	public SaleManage() {
		setTitle("前台管理");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 687, 378);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(28, 44, 276, 232);
		contentPane.add(panel);
		
		String[] colNames={"ID","商品名","售价"};
		Object[][] items={};
		tableModel = new UneditableTableModel(items,colNames);
		saleTable = new JTable(tableModel);
		saleTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		saleTable.setPreferredScrollableViewportSize(new Dimension(panel.getWidth(),panel.getHeight()));
		saleTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		saleTable.getColumnModel().getColumn(0).setPreferredWidth((int)(panel.getWidth()*5.0/10.0));
		saleTable.getColumnModel().getColumn(1).setPreferredWidth((int)(panel.getWidth()*5.0/10.0));
	    panel.setLayout(null);
	    scrollPane=new JScrollPane(saleTable);
	    scrollPane.setBounds(0,0,panel.getWidth(),panel.getHeight());
	    panel.add(scrollPane);
	    
	    btnPrint = new JButton("打印商品表");
	    btnPrint.setFont(new Font("宋体", Font.PLAIN, 12));
	    btnPrint.setBounds(93, 286, 127, 23);
	    contentPane.add(btnPrint);
	    
	    lblGoods = new JLabel("商品表");
	    lblGoods.setFont(new Font("宋体", Font.PLAIN, 12));
	    lblGoods.setBounds(28, 19, 54, 15);
	    contentPane.add(lblGoods);
	    
	    btnImport = new JButton("->");
	    btnImport.setFont(new Font("宋体", Font.PLAIN, 12));
	    btnImport.setBounds(314, 152, 48, 23);
	    contentPane.add(btnImport);
	    
		JPanel outPanel = new JPanel();
		outPanel.setBounds(372, 44, 283, 232);
		contentPane.add(outPanel);
		
		String[] colOutNames={"商品名","数量","总价"};
		Object[][] itemsOut={};
	    outPanel.setLayout(null);
	    outTable = new JTable(itemsOut,colOutNames);
	    outTable.setPreferredScrollableViewportSize(new Dimension(outPanel.getWidth(),outPanel.getHeight()));
	    outTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
	    outTable.getColumnModel().getColumn(0).setPreferredWidth((int)(outPanel.getWidth()*5.0/10.0));
	    outTable.getColumnModel().getColumn(1).setPreferredWidth((int)(outPanel.getWidth()*2.0/10.0));
	    outTable.getColumnModel().getColumn(2).setPreferredWidth((int)(outPanel.getWidth()*3.0/10.0));
	    scrollPane2=new JScrollPane(outTable);
	    scrollPane2.setBounds(0, 0, 283, 232);
	    outPanel.add(scrollPane2);
	    
	    btnDiscount = new JButton("设置折扣");
	    btnDiscount.setFont(new Font("宋体", Font.PLAIN, 12));
	    btnDiscount.setBounds(413, 286, 93, 23);
	    contentPane.add(btnDiscount);
	    
	    btnSale = new JButton("出售");
	    btnSale.setFont(new Font("宋体", Font.PLAIN, 12));
	    btnSale.setBounds(542, 286, 74, 23);
	    contentPane.add(btnSale);
	    
	    lbnList = new JLabel("购物清单");
	    lbnList.setFont(new Font("宋体", Font.PLAIN, 12));
	    lbnList.setBounds(372, 19, 65, 15);
	    contentPane.add(lbnList);
	    
	    lblTotal = new JLabel("总计金额: 0");
	    lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
	    lblTotal.setFont(new Font("宋体", Font.PLAIN, 12));
	    lblTotal.setBounds(542, 19, 113, 15);
	    contentPane.add(lblTotal);
	    
	    btnPrint.addActionListener(e->{
	    	new Thread(()->{
		    	PricePrinter printer=new PricePrinter(tableModel);
		    	printer.makePrinting();
	    	}).start();

	    });
	    
	    RefreshProductThread rpt=new RefreshProductThread(this.tableModel);
	    rpt.start();
	}

}
