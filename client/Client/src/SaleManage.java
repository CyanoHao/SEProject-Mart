import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.Font;
import java.net.URI;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;


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
	private UneditableTableModel outTableModel;
	private HashMap<String,Integer> productMap;
	private HashMap<Integer,String> productMapRev;
	private double rawTotalPrice;
	private double discount;

	public SaleManage() {
		discount=1.00;
		rawTotalPrice=0;
		productMap=new HashMap<String,Integer>();
		productMapRev=new HashMap<Integer,String>();
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
	    outTableModel = new UneditableTableModel(itemsOut,colOutNames);
	    outTable = new JTable(outTableModel);
	    outTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
	    btnDiscount.setBounds(478, 286, 93, 23);
	    contentPane.add(btnDiscount);
	    
	    btnSale = new JButton("出售");
	    btnSale.setFont(new Font("宋体", Font.PLAIN, 12));
	    btnSale.setBounds(581, 286, 74, 23);
	    contentPane.add(btnSale);
	    
	    lbnList = new JLabel("购物清单");
	    lbnList.setFont(new Font("宋体", Font.PLAIN, 12));
	    lbnList.setBounds(372, 19, 65, 15);
	    contentPane.add(lbnList);
	    
	    lblTotal = new JLabel("折扣后金额: 0.00");
	    lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
	    lblTotal.setFont(new Font("宋体", Font.PLAIN, 12));
	    lblTotal.setBounds(478, 19, 177, 15);
	    contentPane.add(lblTotal);
	    
	    JLabel lblDisCount = new JLabel("折扣比例: 1.00");
	    lblDisCount.setFont(new Font("宋体", Font.PLAIN, 12));
	    lblDisCount.setBounds(366, 290, 102, 15);
	    contentPane.add(lblDisCount);
	    
	    btnPrint.addActionListener(e->{
	    	new Thread(()->{
		    	PricePrinter printer=new PricePrinter(tableModel);
		    	printer.makePrinting();
	    	}).start();

	    });
	    
	    RefreshProductThread rpt=new RefreshProductThread(this.tableModel);
	    rpt.start();
	    
	    btnImport.addActionListener(e->{
	    	int rowNum=saleTable.getSelectedRow();
	    	if(rowNum==-1){
                JOptionPane.showMessageDialog(
                        null,
                        "请选择商品",
                        "错误",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
	    	}
	    	if("未定价".equals((String)saleTable.getModel().getValueAt(rowNum,2))){
                JOptionPane.showMessageDialog(
                        null,
                        "未定价的商品不能出售，请联系店长",
                        "错误",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
	    	}
	    	String amount=JOptionPane.showInputDialog(
	    				null, 
	    				"请输入商品销售数量(>0)", 
	    				"输入数量", 
	    				JOptionPane.QUESTION_MESSAGE
	    	);
	    	Pattern pattern=Pattern.compile("^(?:[1-9][0-9]*(?:\\.[0-9]+)?|0\\.(?!0+$)[0-9]+)$");
	    	Matcher matcher=pattern.matcher(amount);
	    	if(!matcher.find()){
                JOptionPane.showMessageDialog(
                        null,
                        "非法的销售数量(amount>0),请重试!",
                        "错误",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
	    	}
	    	Integer rowid=productMap.get((String)saleTable.getModel().getValueAt(rowNum,0));
	    	Double price=Double.parseDouble((String)saleTable.getModel().getValueAt(rowNum,2));
	    	if(rowid!=null){
	    		//update new amount and total price in right table
	    		Double oriAmount=Double.parseDouble((String) outTable.getModel().getValueAt(rowid, 1));
	    		outTable.getModel().setValueAt(new Double(oriAmount+Double.parseDouble(amount)).toString(), rowid, 1);
	    		outTable.getModel().setValueAt(new Double((oriAmount+Double.parseDouble(amount))*price).toString(), rowid, 2);
	    		rawTotalPrice+=(Double.parseDouble(amount))*price;
	    		lblTotal.setText(String.format("折扣后金额: %.2f",rawTotalPrice*discount));
	    	}
	    	else{
	    		//add a row in right table
	    		rowid=outTable.getModel().getRowCount();
	    		productMap.put((String)saleTable.getModel().getValueAt(rowNum,0), rowid);
	    		productMapRev.put(rowid, (String)saleTable.getModel().getValueAt(rowNum,0));
	    		Object[] newItem={(String)saleTable.getModel().getValueAt(rowNum,1),amount,price*Double.parseDouble(amount)};
	    		outTableModel.addRow(newItem);
	    		rawTotalPrice+=price*Double.parseDouble(amount);
	    		lblTotal.setText(String.format("折扣后金额: %.2f",rawTotalPrice*discount));
	    	}
	    });
	    
	    //implement the button to configure discount rate
	    btnDiscount.addActionListener(e->{
	    	String disCountString=JOptionPane.showInputDialog(
    				null, 
    				"请输入折扣比例(0~1)", 
    				"输入折扣", 
    				JOptionPane.QUESTION_MESSAGE
	    	);
	    	Pattern pattern=Pattern.compile("^(?:[1-9][0-9]*(?:\\.[0-9]+)?|0\\.(?!0+$)[0-9]+)$");
	    	Matcher matcher=pattern.matcher(disCountString);
	    	boolean flag=false;
	    	if(matcher.find()){
	    		Double inputDc=Double.parseDouble(disCountString);
	    		java.text.DecimalFormat df=new java.text.DecimalFormat("#.00");
	    		double inputDcDf=Double.parseDouble(df.format(inputDc));
	    		if(inputDcDf>0 && inputDcDf<=1){
	    			this.discount=inputDcDf;
	    			lblDisCount.setText(String.format("折扣比例: %.2f",discount));
	    			lblTotal.setText(String.format("折扣后金额: %.2f",rawTotalPrice*discount));
	    			flag=true;
	    		}
	    	}
	    	if(flag==false){
                JOptionPane.showMessageDialog(
                        null,
                        "非法的折扣比例(保留两位后>0且<=1),请重试!",
                        "错误",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
	    	}
	    });
	    
		//implement the button to record sale
	    btnSale.addActionListener(e->{
	    	int listLength=outTable.getModel().getRowCount();
	    	if(listLength==0){
                JOptionPane.showMessageDialog(
                        null,
                        "购物列表为空!",
                        "错误",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
	    	}
	    	JSONObject jo=new JSONObject();
	    	jo.put("discount", discount);
	    	JSONArray ja=new JSONArray();
	    	for(int i=0;i<listLength;i++){
	    		JSONObject innerJo=new JSONObject();
	    		innerJo.put("prod_id", productMapRev.get(i));
	    		double sumPrice=Double.parseDouble(outTable.getModel().getValueAt(i, 2).toString());
	    		double amount=Double.parseDouble(outTable.getModel().getValueAt(i, 1).toString());
	    		innerJo.put("price", sumPrice/amount);
	    		innerJo.put("num",amount);
	    		ja.put(innerJo);
	    	}
	    	jo.put("detail", ja);
	    	try {
				URI postUri = new URIBuilder()
					.setScheme("http")
					.setUserInfo(Main.username, Main.passwd)
					.setHost("devel.cyano.cn")
					.setPort(10001)
					.setPath("/Mart/v1.0/sale/add")
					.build();
				CloseableHttpClient http = HttpClients.createDefault();
				HttpPost postProducts=new HttpPost(postUri);
				StringEntity se = new StringEntity(jo.toString());
				se.setContentEncoding("UTF-8");
				se.setContentType("application/json");
				postProducts.setEntity(se);
				HttpResponse res = http.execute(postProducts);
				if(res.getStatusLine().getStatusCode()==200){
					JOptionPane.showMessageDialog(
							null,
							"交易成功，记录已提交!",
							"交易成功",
							JOptionPane.INFORMATION_MESSAGE
							);
				}
				else{
	                JOptionPane.showMessageDialog(
	                        null,
	                        "交易失败,请重试!",
	                        "错误",
	                        JOptionPane.ERROR_MESSAGE
	                );
	                return;
				}
				productMap.clear();
				productMapRev.clear();
				rawTotalPrice=0;
				lblTotal.setText(String.format("折扣后金额: %.2f",rawTotalPrice*discount));
				for(int i=listLength-1;i>=0;i--){
					outTableModel.removeRow(i);
				}
                return;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
	    });
	}
	
}
