import java.awt.BorderLayout;
import java.awt.Dimension;




import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;




import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.net.URI;
import java.util.regex.Pattern;




import javax.swing.JLabel;




import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


public class PriceSetting extends JFrame {

	private JPanel contentPane;
	private JTable saleTable;
	private JScrollPane scrollPane;
	private JButton btnChangePrice;
	private UneditableTableModel tableModel;
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
		
		String[] colNames={"ID","商品名","售价"};
		Object[][] items={};
		tableModel = new UneditableTableModel(items,colNames);
		saleTable = new JTable(tableModel);
		saleTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		saleTable.setPreferredScrollableViewportSize(new Dimension(panel.getWidth(),panel.getHeight()));
		saleTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		saleTable.getColumnModel().getColumn(0).setPreferredWidth((int)(panel.getWidth()*3.0/10.0));
		saleTable.getColumnModel().getColumn(1).setPreferredWidth((int)(panel.getWidth()*5.0/10.0));
		saleTable.getColumnModel().getColumn(2).setPreferredWidth((int)(panel.getWidth()*2.0/10.0));
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
	    btnCancel.addActionListener(e->{
	    	this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING) );
	    });
	    
	    saleLabel = new JLabel("前台商品表");
	    saleLabel.setFont(new Font("宋体", Font.PLAIN, 12));
	    saleLabel.setBounds(127, 12, 74, 15);
	    contentPane.add(saleLabel);
	    
	    btnPrint = new JButton("打印商品表");
	    btnPrint.setFont(new Font("宋体", Font.PLAIN, 11));
	    btnPrint.setBounds(10, 151, 93, 23);
	    contentPane.add(btnPrint);
	    
	    RefreshProductThread rpt=new RefreshProductThread(this.tableModel);
	    rpt.start();
	    
	    btnChangePrice.addActionListener(e->{
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
	    	NewPriceFrame npf=new NewPriceFrame(rowNum,this.saleTable);
	    	npf.setVisible(true);
	    });
	    
	    btnPrint.addActionListener(e->{
	    	new Thread(()->{
		    	PricePrinter printer=new PricePrinter(tableModel);
		    	printer.makePrinting();
	    	}).start();

	    });
	}
	
	static public String modifyPrice(Double d){
		if(d<=0) return "未定价";
		return d.toString();
	}
}

class UneditableTableModel extends DefaultTableModel{
	UneditableTableModel(Object[][] data, Object[] columnNames){
		super(data, columnNames);
	}
	@Override
	public boolean isCellEditable(int row, int column){
		return false;
	}
}

class RefreshProductThread extends Thread{
	private UneditableTableModel tableModel;
	RefreshProductThread(UneditableTableModel utm){
		super();
		this.tableModel=utm;
	}
	public void run(){
    	try {
            URI uri = new URIBuilder()
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
			
			jsonStringBuilder.deleteCharAt(jsonStringBuilder.length()-1);
			jsonStringBuilder.deleteCharAt(0);
			Pattern jsonEditer=Pattern.compile("  \"(.*?)\": \\{");
			String jsonString='['+jsonEditer.matcher(jsonStringBuilder.toString()).replaceAll("  {\n    \"id\": \"$1\", ")+']';
			JSONTokener jsonTokener = new JSONTokener(jsonString);
			JSONArray productJSONArray=(JSONArray) jsonTokener.nextValue(); 
			for(int i=0;i<productJSONArray.length();i++){
				JSONObject jo=productJSONArray.getJSONObject(i);
				Object[] newItem={jo.getString("id"),jo.getString("name"),PriceSetting.modifyPrice(new Double(jo.getDouble("price")))};
				tableModel.addRow(newItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
