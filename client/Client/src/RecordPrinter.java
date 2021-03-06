import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JLabel;


public class RecordPrinter implements Printable {
	private UneditableTableModel tableModel;
	private JLabel lbl;
	RecordPrinter(UneditableTableModel utm,JLabel lbl){
		this.tableModel=utm;
		this.lbl=lbl;
	}
    @Override  
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {  
  
        if (page > 0) {  
            return NO_SUCH_PAGE;  
        }  
  
        Graphics2D g2d = (Graphics2D) g;  
        g2d.setFont(new Font("宋体", Font.BOLD, 16));  
        g2d.drawString("交易记录表", 100, 15);
        g2d.setFont(new Font("宋体", Font.PLAIN, 14)); 
        g2d.drawString("-------------------------------------", 7, 25);
        g2d.drawString("交易时间          折扣    销售额",7,35);
        for(int i=0;i<this.tableModel.getRowCount();i++){
        	String info=(String) this.tableModel.getValueAt(i,0)+"    "+this.tableModel.getValueAt(i,1)+"    "+this.tableModel.getValueAt(i,2);
        	g2d.drawString(info,7,35+15*(i+1));
        }
        g2d.drawString("-------------------------------------", 7, 35+15*(this.tableModel.getRowCount()+1));
        g2d.drawString(lbl.getText(), 7, 35+15*(this.tableModel.getRowCount()+2));
        return PAGE_EXISTS;  
    }  
	public void makePrinting() {  
		  
        int height = 40 + (this.tableModel.getRowCount()+1) * 15 + 20;  
  
        // 通俗理解就是书、文档  
        Book book = new Book();  
  
        // 打印格式  
        PageFormat pf = new PageFormat();  
        pf.setOrientation(PageFormat.PORTRAIT);  
  
        // 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。  
        Paper p = new Paper();  
        p.setSize(280, height);  
        p.setImageableArea(5, -20, 280, height + 20);  
        pf.setPaper(p);  
  
        // 把 PageFormat 和 Printable 添加到书中，组成一个页面  
        book.append(new RecordPrinter(this.tableModel,this.lbl), pf);  
  
        // 获取打印服务对象  
        PrinterJob job = PrinterJob.getPrinterJob();  
        job.setPageable(book);  
        try {  
            job.print();  
        } catch (PrinterException e) {  
        	//e.printStackTrace();  
        }  
  
    }  
}
