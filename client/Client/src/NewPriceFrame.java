import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.Font;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
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


public class NewPriceFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	public double newPrice;
	private JTable table;
	private int rowNum;

	public NewPriceFrame(int rowNum,JTable table) {
		this.rowNum=rowNum;
		this.table=table;
		setTitle("输入单价");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 477, 149);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnConfirm = new JButton("确定");
		btnConfirm.setFont(new Font("宋体", Font.PLAIN, 12));
		btnConfirm.setBounds(90, 78, 93, 23);
		contentPane.add(btnConfirm);
		
		JButton btnCancel = new JButton("取消");
		btnCancel.setFont(new Font("宋体", Font.PLAIN, 12));
		btnCancel.setBounds(253, 78, 93, 23);
		contentPane.add(btnCancel);
		
		JLabel lblNewPrice = new JLabel("请输入该商品新的单位价格(0<price<=9999.99)");
		lblNewPrice.setFont(new Font("宋体", Font.PLAIN, 12));
		lblNewPrice.setBounds(22, 24, 262, 37);
		contentPane.add(lblNewPrice);
		
		textField = new JTextField();
		textField.setBounds(294, 32, 66, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
	    btnCancel.addActionListener(e->{
	    	this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING) );
	    });
	    btnConfirm.addActionListener(e->{
	    	Pattern pattern=Pattern.compile("^(?:[1-9][0-9]*(?:\\.[0-9]+)?|0\\.(?!0+$)[0-9]+)$");
	    	Matcher matcher=pattern.matcher(textField.getText());
	    	if(matcher.find()){
	    		newPrice=Double.parseDouble(textField.getText());
	    		if(newPrice>9999.99){
	    			newPrice=9999.99;
		    		JOptionPane.showMessageDialog(
	                        null,
	                        "价格溢出，自动定价为9999.99",
	                        "提示",
	                        JOptionPane.INFORMATION_MESSAGE
	                );
	    		}
	    		new Thread(()->{
	                URI getUri,postUri;
					try {
						getUri = new URIBuilder()
							.setScheme("http")
							.setUserInfo(Main.username, Main.passwd)
							.setHost("devel.cyano.cn")
							.setPort(10001)
							.setPath("/Mart/v1.0/product/get")
							.build();
						HttpGet getProducts=new HttpGet(getUri);
						CloseableHttpClient http = HttpClients.createDefault();
						CloseableHttpResponse getResponse=http.execute(getProducts);
						JSONTokener getJsonTokener = new JSONTokener(EntityUtils.toString(getResponse.getEntity()));
						String id=(String) this.table.getValueAt(rowNum, 0);
						JSONObject jo=(JSONObject)getJsonTokener.nextValue();
						jo.getJSONObject(id).put("price",newPrice);
						postUri = new URIBuilder()
							.setScheme("http")
							.setUserInfo(Main.username, Main.passwd)
							.setHost("devel.cyano.cn")
							.setPort(10001)
							.setPath("/Mart/v1.0/product/update")
							.build();
						HttpPost postProducts=new HttpPost(postUri);
						StringEntity se = new StringEntity(jo.toString(),"utf-8");
						se.setContentEncoding("UTF-8");
						se.setContentType("application/json");
						postProducts.setEntity(se);
						HttpResponse res = http.execute(postProducts);
						table.getModel().setValueAt(newPrice,rowNum,2);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
	    		}).start();
	    		JOptionPane.showMessageDialog(
                        null,
                        "价格已更新!",
                        "操作成功",
                        JOptionPane.INFORMATION_MESSAGE
                );
	    		this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING) );
	    		return;
	    	}
	    	else{
                JOptionPane.showMessageDialog(
                        null,
                        "非法的单价金额(price>0)!",
                        "操作失败",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
	    	}
	    });
	}

}
