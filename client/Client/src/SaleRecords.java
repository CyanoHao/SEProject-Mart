import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextPane;

import java.awt.Color;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.SystemColor;
import java.awt.event.WindowEvent;import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SaleRecords extends JFrame {

	private JPanel contentPane;
	private JTextField beginField;
	private JTextField endField;
	private JTable recordTable;
	private JScrollPane scrollPane;
	private JButton btnDetail;
	private JButton btnPrint;
	private JLabel lblSum;
	private JSONArray ja;
	private UneditableTableModel tableModel;
	private HashSet<Integer> set;
	private HashMap<Integer,Integer> rowIdMap;
	private double totalFlow;
	private JLabel lblNewLabel;

	public SaleRecords() {
		totalFlow=0.0;
		set=new HashSet<Integer>();
		rowIdMap=new HashMap<Integer,Integer>();
		setTitle("交易记录查询");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 587, 421);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		beginField = new JTextField();
		beginField.setFont(new Font("宋体", Font.PLAIN, 12));
		beginField.setBounds(109, 58, 79, 21);
		contentPane.add(beginField);
		beginField.setColumns(10);
		
		JLabel lblBegin = new JLabel("起始日期");
		lblBegin.setFont(new Font("宋体", Font.PLAIN, 12));
		lblBegin.setBounds(10, 61, 54, 15);
		contentPane.add(lblBegin);
		
		endField = new JTextField();
		endField.setFont(new Font("宋体", Font.PLAIN, 12));
		endField.setBounds(109, 117, 79, 21);
		contentPane.add(endField);
		endField.setColumns(10);
		
		JLabel lblEnd = new JLabel("终止日期(可选)");
		lblEnd.setFont(new Font("宋体", Font.PLAIN, 12));
		lblEnd.setBounds(10, 120, 89, 15);
		contentPane.add(lblEnd);
		
		JButton btnSearching = new JButton("确定");
		btnSearching.setFont(new Font("宋体", Font.PLAIN, 12));
		btnSearching.setBounds(54, 181, 93, 23);
		contentPane.add(btnSearching);
		
		JPanel panel = new JPanel();
		panel.setBounds(216, 32, 316, 272);
		contentPane.add(panel);
		
		String[] colNames={"交易时间","折扣","销售额"};
		Object[][] items={};
		tableModel = new UneditableTableModel(items,colNames);
		recordTable = new JTable(tableModel);
		recordTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
	    btnPrint.setBounds(319, 333, 114, 23);
	    contentPane.add(btnPrint);
	    
	    lblSum = new JLabel("总销售额: 0.00");
	    lblSum.setFont(new Font("宋体", Font.PLAIN, 12));
	    lblSum.setBounds(22, 333, 114, 15);
	    contentPane.add(lblSum);
	    
	    JTextPane guide4Date = new JTextPane();
	    guide4Date.setForeground(Color.BLUE);
	    guide4Date.setBackground(SystemColor.menu);
	    guide4Date.setEditable(false);
	    guide4Date.setFont(new Font("宋体", Font.PLAIN, 12));
	    guide4Date.setText("日期格式: YYYY-MM-DD\r\n年月日位数不足的需要补0");
	    guide4Date.setBounds(25, 240, 150, 57);
	    contentPane.add(guide4Date);
	    
	    JButton btnCancel = new JButton("退出");
	    btnCancel.setFont(new Font("宋体", Font.PLAIN, 12));
	    btnCancel.setBounds(455, 333, 93, 23);
	    contentPane.add(btnCancel);
	    
	    lblNewLabel = new JLabel("查询结果");
	    lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 12));
	    lblNewLabel.setBounds(216, 10, 54, 15);
	    contentPane.add(lblNewLabel);
	    
	    btnCancel.addActionListener(e->{
	    	this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING) );
	    });
	    
	    btnSearching.addActionListener(e->{
	    	String beginDateString=this.beginField.getText();
	    	if("".equals(beginDateString)){
                JOptionPane.showMessageDialog(
                        null,
                        "请输入日期!",
                        "输入日期",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
	    	}
	    	String datePatternString="(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)"; 
	    	Pattern datePattern=Pattern.compile(datePatternString);
	    	Matcher matcher=datePattern.matcher(beginDateString);
	    	if(matcher.find()){
	    		//flag 1: no end; flag 2: correct date; flag 0: error date 
	    		int flag=0;
	    		String endDateString=this.endField.getText();
	    		if("".equals(endDateString)) flag=1;
	    		else{
	    			Matcher endMatcher=datePattern.matcher(endDateString);
	    			if(endMatcher.find()) flag=2;
	    		}
	    		if(flag==0){
	                JOptionPane.showMessageDialog(
	                        null,
	                        "非法的非空终止日期: 格式错误或日期不存在!",
	                        "日期错误",
	                        JOptionPane.ERROR_MESSAGE
	                );
	                return;
	    		}
	    		JSONObject jo=new JSONObject();
	    		jo.put("start",beginDateString.replace('-','/'));
	    		//jo.put("orderby_date","inc");
	    		if(flag==2){
	    			jo.put("end",endDateString.replace('-','/'));
	    		}
				URI postUri;
				try {
					postUri = new URIBuilder()
						.setScheme("http")
						.setUserInfo(Main.username, Main.passwd)
						.setHost("devel.cyano.cn")
						.setPort(10001)
						.setPath("/Mart/v1.0/sale/get")
						.build();
					CloseableHttpClient http = HttpClients.createDefault();
					HttpPost postProducts=new HttpPost(postUri);
					StringEntity se = new StringEntity(jo.toString());
					se.setContentEncoding("UTF-8");
					se.setContentType("application/json");
					postProducts.setEntity(se);
					HttpResponse res = http.execute(postProducts);
					if(res.getStatusLine().getStatusCode()==200){
						rowIdMap.clear();
						totalFlow=0.0;
						set.clear();
						HttpEntity entity=res.getEntity();
						StringBuilder jsonStringBuilder=new StringBuilder(EntityUtils.toString(entity));
						this.ja=(JSONArray) ((JSONObject)new JSONTokener(jsonStringBuilder.toString()).nextValue()).get("sale_list");
						for(int i=recordTable.getRowCount()-1;i>=0;i--){
							tableModel.removeRow(i);
						}
						if(ja.length()==0){
							lblSum.setText(String.format("总销售额: %.2f",totalFlow));
			                JOptionPane.showMessageDialog(
			                        null,
			                        "查询结果为空!",
			                        "提示",
			                        JOptionPane.INFORMATION_MESSAGE
			                );
			                return;
						}
						for(int i=ja.length()-1;i>=0;i--){
							Integer id=Integer.parseInt(ja.getJSONObject(i).get("id").toString());
							//only if id is new
							if(set.contains(id)==false){
								set.add(id);
								String dateTime=ja.getJSONObject(i).get("date").toString();
								String discount=ja.getJSONObject(i).get("discount").toString();
								JSONArray detail=(JSONArray) ja.getJSONObject(i).get("detail");
								Double totalPrice=0.0;
								for(int j=0;j<detail.length();j++){
									totalPrice+=detail.getJSONObject(j).getDouble("price")*(double)detail.getJSONObject(j).getDouble("num");
								}
								this.totalFlow+=totalPrice;
								Object[] newItem={dateTime,discount,String.format("%.2f", totalPrice)};
								tableModel.addRow(newItem);
								rowIdMap.put(new Integer(recordTable.getRowCount()-1), new Integer(id));
							}
						}
						lblSum.setText(String.format("总销售额: %.2f",totalFlow));
					}
					else{
		                JOptionPane.showMessageDialog(
		                        null,
		                        //"查询交易记录出错，请重试!",
		                        res.getStatusLine().toString(),
		                        "错误",
		                        JOptionPane.ERROR_MESSAGE
		                );
		                return;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
	    	}
	    	else{
                JOptionPane.showMessageDialog(
                        null,
                        "非法的起始日期: 格式错误或日期不存在!",
                        "日期错误",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
	    	}
	    });
	    
	    btnDetail.addActionListener(e->{
	    	if(recordTable.getRowCount()==0){
                JOptionPane.showMessageDialog(
                        null,
                        "记录列表为空!",
                        "提示",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
	    	}
	    	if(recordTable.getSelectedRow()==-1){
                JOptionPane.showMessageDialog(
                        null,
                        "请选择一项记录!",
                        "提示",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
	    	}
	    	
	    	Integer id=recordTable.getSelectedRow();
	    	DetailFrame detailView=new DetailFrame(id);
	    	detailView.setVisible(true);
	    });
	    
	    btnPrint.addActionListener(e->{
	    	if(recordTable.getRowCount()==0){
                JOptionPane.showMessageDialog(
                        null,
                        "记录列表为空!",
                        "提示",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
	    	}
	    	RecordPrinter rp=new RecordPrinter(tableModel, lblSum);
	    	rp.makePrinting();
	    });
	}
	
	public class DetailFrame extends JFrame {

		private JPanel contentPane;
		private UneditableTableModel model;
		private JTable table;
		private JScrollPane scrollPane;
		private Integer id;

		public DetailFrame(Integer id) {
			this.id=id;
			setTitle("记录详情");
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			setBounds(100, 100, 550, 234);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));
			setContentPane(contentPane);
			
			String[] colNames={"商品ID","商品名","单价","数量"};
			Object[][] items={};
		    model = new UneditableTableModel(items,colNames);
		    table = new JTable(model);
		    table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		    table.setPreferredScrollableViewportSize(new Dimension(contentPane.getWidth(),contentPane.getHeight()));
		    table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		    table.getColumnModel().getColumn(0).setPreferredWidth((int)(contentPane.getWidth()*3.0/10.0));
		    table.getColumnModel().getColumn(1).setPreferredWidth((int)(contentPane.getWidth()*3.0/10.0));
		    table.getColumnModel().getColumn(2).setPreferredWidth((int)(contentPane.getWidth()*2.0/10.0));
		    table.getColumnModel().getColumn(3).setPreferredWidth((int)(contentPane.getWidth()*2.0/10.0));
		    scrollPane=new JScrollPane(table);
		    scrollPane.setBounds(0, 0, 283, 232);
		    contentPane.add(scrollPane);
		    
            URI uri;
			try {
				uri = new URIBuilder()
					.setScheme("http")
					.setUserInfo(Main.username, Main.passwd)
					.setHost("devel.cyano.cn")
					.setPort(10001)
					.setPath("/Mart/v1.0/product/get")
					.build();
	            HttpGet getProducts=new HttpGet(uri);
	            CloseableHttpClient http = HttpClients.createDefault();
	            CloseableHttpResponse response=http.execute(getProducts);
	            HttpEntity entity=response.getEntity();
	            StringBuilder jsonStringBuilder=new StringBuilder(EntityUtils.toString(entity));
	            JSONTokener jsonTokener = new JSONTokener(jsonStringBuilder.toString());
	            JSONObject productListObj=(JSONObject) jsonTokener.nextValue();
	            
			    JSONObject thisJo=null;
			    for(int i=0;i<ja.length();i++){
			    	if(ja.getJSONObject(i).get("id").equals(rowIdMap.get(id))){
			    		thisJo=ja.getJSONObject(i);
			    	}
			    }
			    JSONArray detailArray=thisJo.getJSONArray("detail");
			    for(int i=0;i<detailArray.length();i++){
			    	String productId=detailArray.getJSONObject(i).getString("prod_id");
			    	Double price=detailArray.getJSONObject(i).getDouble("price");
			    	Double amount=detailArray.getJSONObject(i).getDouble("num");
			    	String name=productListObj.getJSONObject(productId).getString("name");
			    	Object[] newItem={productId,name,price,amount};
			    	model.addRow(newItem);
			    }
			} catch (URISyntaxException | IOException e) {
				e.printStackTrace();
			}
		
		}
		
	}
}
