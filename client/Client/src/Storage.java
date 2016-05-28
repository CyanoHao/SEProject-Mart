import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;

import java.awt.Font;
import javax.swing.JLabel;


public class Storage extends JFrame {

	private JPanel contentPane;
	private JTable storeTable;
	private JTable storeOutTable;
	private JScrollPane scrollPane;
	private JScrollPane outScrollPane;
	private JButton btnImport;
	private JButton btnExport;
	private JButton btnNewButton;
	private JButton btnPrintStore;

	public Storage() {
		setTitle("库存管理");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 679, 356);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton addStorage = new JButton("添加库存");
		addStorage.setFont(new Font("宋体", Font.PLAIN, 12));
		addStorage.setBounds(133, 261, 93, 23);
		contentPane.add(addStorage);
		
		JPanel panel = new JPanel();
		panel.setBounds(60, 45, 244, 194);
		contentPane.add(panel);
		
		String[] colNames={"货品名","库存量","平均进价"};
		Object[][] items={};
		storeTable = new JTable(items,colNames);
		storeTable.setPreferredScrollableViewportSize(new Dimension(panel.getWidth(),panel.getHeight()));
		storeTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		storeTable.getColumnModel().getColumn(0).setPreferredWidth((int)(panel.getWidth()*5.0/10.0));
		storeTable.getColumnModel().getColumn(1).setPreferredWidth((int)(panel.getWidth()*2.0/10.0));
		storeTable.getColumnModel().getColumn(2).setPreferredWidth((int)(panel.getWidth()*3.0/10.0));
	    panel.setLayout(null);
	    scrollPane=new JScrollPane(storeTable);
	    scrollPane.setBounds(0,0,panel.getWidth(),panel.getHeight());
	    panel.add(scrollPane);
	    
	    btnImport = new JButton("->");
	    btnImport.setFont(new Font("宋体", Font.PLAIN, 12));
	    btnImport.setBounds(314, 84, 56, 23);
	    contentPane.add(btnImport);
	    
	    btnExport = new JButton("<-");
	    btnExport.setFont(new Font("宋体", Font.PLAIN, 12));
	    btnExport.setBounds(314, 147, 56, 23);
	    contentPane.add(btnExport);
	    
	    JLabel storeLabel = new JLabel("库存列表");
	    storeLabel.setFont(new Font("宋体", Font.PLAIN, 12));
	    storeLabel.setBounds(60, 24, 54, 15);
	    contentPane.add(storeLabel);
	    
		JPanel outputPanel = new JPanel();
		outputPanel.setBounds(380, 45, 244, 194);
		contentPane.add(outputPanel);
		
		String[] colOutNames={"货品名","取出量"};
		Object[][] outItems={};
		storeOutTable = new JTable(outItems,colOutNames);
		storeOutTable.setPreferredScrollableViewportSize(new Dimension(outputPanel.getWidth(),outputPanel.getHeight()));
		storeOutTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		storeOutTable.getColumnModel().getColumn(0).setPreferredWidth((int)(outputPanel.getWidth()*5.0/10.0));
		storeOutTable.getColumnModel().getColumn(1).setPreferredWidth((int)(outputPanel.getWidth()*5.0/10.0));
		outputPanel.setLayout(null);
	    outScrollPane=new JScrollPane(storeOutTable);
	    outScrollPane.setBounds(0,0,outputPanel.getWidth(),outputPanel.getHeight());
	    outputPanel.add(outScrollPane);
	    
	    JLabel outLabel = new JLabel("出货列表");
	    outLabel.setFont(new Font("宋体", Font.PLAIN, 12));
	    outLabel.setBounds(380, 24, 54, 15);
	    contentPane.add(outLabel);
	    
	    btnNewButton = new JButton("交付前台");
	    btnNewButton.setFont(new Font("宋体", Font.PLAIN, 12));
	    btnNewButton.setBounds(460, 261, 93, 23);
	    contentPane.add(btnNewButton);
	    
	    btnPrintStore = new JButton("打印库存表");
	    btnPrintStore.setFont(new Font("宋体", Font.PLAIN, 12));
	    btnPrintStore.setBounds(290, 261, 111, 23);
	    contentPane.add(btnPrintStore);
	}
}
