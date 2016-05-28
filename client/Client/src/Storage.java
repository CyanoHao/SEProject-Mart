import java.awt.Dimension;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;

import java.awt.Font;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;

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


public class Storage extends JFrame {

	private JPanel contentPane;
	private JTable storeTable;
	private JScrollPane scrollPane;
	private JButton btnOut;
	private JButton btnPrintStore;
	private UneditableTableModel tableModel;
	private JSONObject productInfo;

	public Storage() {
		setTitle("库存管理");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 462, 356);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnAdd = new JButton("新增入库");
		btnAdd.setFont(new Font("宋体", Font.PLAIN, 12));
		btnAdd.setBounds(186, 261, 93, 23);
		contentPane.add(btnAdd);
		
		JPanel panel = new JPanel();
		panel.setBounds(31, 45, 392, 194);
		contentPane.add(panel);
		
		String[] colNames={"商品ID","货品名","库存量","库存金额"};
		Object[][] items={};
		tableModel=new UneditableTableModel(items,colNames);
		storeTable = new JTable(tableModel);
		storeTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		storeTable.setPreferredScrollableViewportSize(new Dimension(panel.getWidth(),panel.getHeight()));
		storeTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		storeTable.getColumnModel().getColumn(0).setPreferredWidth((int)(panel.getWidth()*3.0/10.0));
		storeTable.getColumnModel().getColumn(1).setPreferredWidth((int)(panel.getWidth()*3.0/10.0));
		storeTable.getColumnModel().getColumn(2).setPreferredWidth((int)(panel.getWidth()*2.0/10.0));
		storeTable.getColumnModel().getColumn(3).setPreferredWidth((int)(panel.getWidth()*2.0/10.0));
	    panel.setLayout(null);
	    scrollPane=new JScrollPane(storeTable);
	    scrollPane.setBounds(0,0,panel.getWidth(),panel.getHeight());
	    panel.add(scrollPane);
	    
	    JLabel storeLabel = new JLabel("库存列表");
	    storeLabel.setFont(new Font("宋体", Font.PLAIN, 12));
	    storeLabel.setBounds(31, 23, 54, 15);
	    contentPane.add(storeLabel);
	    
	    btnOut = new JButton("新增出库");
	    btnOut.setFont(new Font("宋体", Font.PLAIN, 12));
	    btnOut.setBounds(330, 261, 93, 23);
	    contentPane.add(btnOut);
	    
	    btnPrintStore = new JButton("打印库存表");
	    btnPrintStore.setFont(new Font("宋体", Font.PLAIN, 12));
	    btnPrintStore.setBounds(31, 261, 111, 23);
	    contentPane.add(btnPrintStore);
	    
	    btnPrintStore.addActionListener(e->{
	    	StoragePrinter sp=new StoragePrinter(tableModel);
	    	sp.makePrinting();
	    });
	    
        URI uri4product;
		try {
			uri4product = new URIBuilder()
				.setScheme("http")
				.setUserInfo(Main.username, Main.passwd)
				.setHost("devel.cyano.cn")
				.setPort(10001)
				.setPath("/Mart/v1.0/product/get")
				.build();
	        HttpGet getProducts=new HttpGet(uri4product);
	        CloseableHttpClient http4prod = HttpClients.createDefault();
	        CloseableHttpResponse response4prod=http4prod.execute(getProducts);
	        HttpEntity entity4prod=response4prod.getEntity();
	        StringBuilder jsonProdStringBuilder=new StringBuilder(EntityUtils.toString(entity4prod));
	        productInfo=(JSONObject)new JSONTokener(jsonProdStringBuilder.toString()).nextValue();
		} catch (URISyntaxException | IOException e2) {
			// TODO 自动生成的 catch 块
			e2.printStackTrace();
		}

		GetInventoryCountThread gict=new GetInventoryCountThread();
		gict.start();
		
		btnOut.addActionListener(e->{
	    	if(storeTable.getRowCount()==0){
                JOptionPane.showMessageDialog(
                        null,
                        "库存列表为空!",
                        "提示",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
	    	}
	    	if(storeTable.getSelectedRow()==-1){
                JOptionPane.showMessageDialog(
                        null,
                        "请选择一种待出货物!",
                        "提示",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
	    	}
	    	String amountString=JOptionPane.showInputDialog(
                    null,
                    "请输入出货数量(0<出货量<=当前库存数量 且为整数)",
                    "输入",
                    JOptionPane.QUESTION_MESSAGE
            );
	    	Pattern pattern=Pattern.compile("^(?:[1-9][0-9]*)$");
	    	Matcher matcher=pattern.matcher(amountString);
	    	Double amount=Double.parseDouble(tableModel.getValueAt(storeTable.getSelectedRow(),2).toString());
	    	if(matcher.find() && Double.parseDouble(amountString)>0 && Double.parseDouble(amountString)<=amount){
	    		String id=tableModel.getValueAt(storeTable.getSelectedRow(),0).toString();
	    		JSONArray ja=new JSONArray();
	    		JSONObject newjo=new JSONObject();
	    		newjo.put("prod_id", id);
	    		newjo.put("price", 0.00);
	    		newjo.put("num", -1*Integer.parseInt(amountString));
	    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    		newjo.put("date",df.format(new Date()));
	    		ja.put(newjo);
	    		CloseableHttpClient http = HttpClients.createDefault();
	    		URI postUri;
				try {
					postUri = new URIBuilder()
						.setScheme("http")
						.setUserInfo(Main.username, Main.passwd)
						.setHost("devel.cyano.cn")
						.setPort(10001)
						.setPath("/Mart/v1.0/inventory/add")
						.build();
		    		HttpPost postOut=new HttpPost(postUri);
		    		StringEntity se = new StringEntity(ja.toString());
		    		se.setContentEncoding("UTF-8");
		    		se.setContentType("application/json");
		    		postOut.setEntity(se);
					HttpResponse res = http.execute(postOut);
					new GetInventoryCountThread().start();
	                JOptionPane.showMessageDialog(
	                        null,
	                        "出货成功!",
	                        "操作成功",
	                        JOptionPane.INFORMATION_MESSAGE
	                );
					

				} catch (Exception e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
	    	}
	    	else{
                JOptionPane.showMessageDialog(
                        null,
                        "非法的出货数量!",
                        "操作失败",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
	    	}
		});
	}
	class GetInventoryCountThread extends Thread{
		public void run(){
			for(int i=storeTable.getRowCount()-1;i>=0;i--){
				tableModel.removeRow(i);
			}
            URI uri;
			try {
				uri = new URIBuilder()
					.setScheme("http")
					.setUserInfo(Main.username, Main.passwd)
					.setHost("devel.cyano.cn")
					.setPort(10001)
					.setPath("/Mart/v1.0/inventory/count")
					.build();
		    	HttpGet getInventoryCount=new HttpGet(uri);
		    	CloseableHttpClient http = HttpClients.createDefault();
				CloseableHttpResponse response=http.execute(getInventoryCount);
				HttpEntity entity=response.getEntity();
				StringBuilder jsonStringBuilder=new StringBuilder(EntityUtils.toString(entity));
				
				jsonStringBuilder.deleteCharAt(jsonStringBuilder.length()-1);
				jsonStringBuilder.deleteCharAt(0);
				Pattern jsonEditer=Pattern.compile("  \"(.*?)\": \\{");
				String jsonString='['+jsonEditer.matcher(jsonStringBuilder.toString()).replaceAll("  {\n    \"id\": \"$1\", ")+']';
				JSONTokener jsonTokener = new JSONTokener(jsonString);
				JSONArray inventoryCountJSONArray=(JSONArray) jsonTokener.nextValue(); 
				
				for(int i=0;i<inventoryCountJSONArray.length();i++){
					String id=inventoryCountJSONArray.getJSONObject(i).getString("id");
					Double count=inventoryCountJSONArray.getJSONObject(i).getDouble("count");
					String name=productInfo.getJSONObject(id).getString("name");
					Double unitprice=productInfo.getJSONObject(id).getDouble("price");
					String priceNum=null;
					if(unitprice>0){
						Double price=count*unitprice;
						priceNum=String.format("%.2f",price);
					}
					else{
						priceNum="未定价";
					}
					Object[] newItem={id,name,count,priceNum};
					if(count>0){
						tableModel.addRow(newItem);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
