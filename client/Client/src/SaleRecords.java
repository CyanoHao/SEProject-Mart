import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JButton;


public class SaleRecords extends JFrame {

	private JPanel contentPane;
	private JTextField beginField;
	private JTextField endField;
	private JTable recordTable;
	private JScrollPane scrollPane;
	private JButton btnDetail;
	private JButton btnPrint;
	private JLabel lblSum;

	public SaleRecords() {
		setTitle("交易记录查询");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 527, 421);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		beginField = new JTextField();
		beginField.setFont(new Font("宋体", Font.PLAIN, 12));
		beginField.setBounds(88, 96, 79, 21);
		contentPane.add(beginField);
		beginField.setColumns(10);
		
		JLabel lblBegin = new JLabel("起始时间");
		lblBegin.setFont(new Font("宋体", Font.PLAIN, 12));
		lblBegin.setBounds(24, 99, 54, 15);
		contentPane.add(lblBegin);
		
		endField = new JTextField();
		endField.setFont(new Font("宋体", Font.PLAIN, 12));
		endField.setBounds(88, 158, 79, 21);
		contentPane.add(endField);
		endField.setColumns(10);
		
		JLabel lblEnd = new JLabel("终止时间");
		lblEnd.setFont(new Font("宋体", Font.PLAIN, 12));
		lblEnd.setBounds(24, 161, 54, 15);
		contentPane.add(lblEnd);
		
		JButton btnSearching = new JButton("确定");
		btnSearching.setFont(new Font("宋体", Font.PLAIN, 12));
		btnSearching.setBounds(49, 229, 93, 23);
		contentPane.add(btnSearching);
		
		JPanel panel = new JPanel();
		panel.setBounds(177, 34, 316, 272);
		contentPane.add(panel);
		
		String[] colNames={"交易时间","折扣","销售额"};
		Object[][] items={};
		recordTable = new JTable(items,colNames);
		recordTable.setPreferredScrollableViewportSize(new Dimension(panel.getWidth(),panel.getHeight()));
		recordTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		recordTable.getColumnModel().getColumn(0).setPreferredWidth((int)(panel.getWidth()*5.0/10.0));
		recordTable.getColumnModel().getColumn(1).setPreferredWidth((int)(panel.getWidth()*2.0/10.0));
		recordTable.getColumnModel().getColumn(2).setPreferredWidth((int)(panel.getWidth()*3.0/10.0));
	    panel.setLayout(null);
	    scrollPane=new JScrollPane(recordTable);
	    scrollPane.setBounds(0,0,panel.getWidth(),panel.getHeight());
	    panel.add(scrollPane);
	    
	    btnDetail = new JButton("详情");
	    btnDetail.setFont(new Font("宋体", Font.PLAIN, 12));
	    btnDetail.setBounds(199, 333, 93, 23);
	    contentPane.add(btnDetail);
	    
	    btnPrint = new JButton("打印记录表");
	    btnPrint.setFont(new Font("宋体", Font.PLAIN, 12));
	    btnPrint.setBounds(346, 333, 114, 23);
	    contentPane.add(btnPrint);
	    
	    lblSum = new JLabel("总销售额: 0");
	    lblSum.setFont(new Font("宋体", Font.PLAIN, 12));
	    lblSum.setBounds(24, 337, 114, 15);
	    contentPane.add(lblSum);
	}
}
