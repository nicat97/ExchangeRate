package exchangerate;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import java.net.URL;
import java.net.SocketException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.*;

import java.util.Date;
import java.text.ParseException;
import javax.swing.JOptionPane;

/**
 * @date 12.01.2018
 * @author Nijat Mammadov
 */
        
public class Main{
    
    public String Convert(String $currency, String $date, double $amount)
    {
        if(validateDate($date))
        {
            Double value[] = new Double[1];
            String date[] = new String[1];
            
            Object result = getCurrency($date, $currency);   
            System.arraycopy(result, 0, value, 0, 1);
            System.arraycopy(result, 1, date, 0, 1); 
            
            value[0] *= $amount;
            
            return date[0]+" tarixli məzənnə: "+value[0]+" AZN";
        }
        else
        {
            JOptionPane.showMessageDialog(null,
                "Tarix daxil edin!",
                "Xəta",
                JOptionPane.ERROR_MESSAGE);
            
            return "";
        }
    }
    
    public boolean validateDate(String date)
    {
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd.MM.YYYY");
        sdfrmt.setLenient(false);

        try
        {
            Date javaDate = sdfrmt.parse(date);
            return true;
        }
        catch (ParseException ex)
        {
            return false;
        }
    }
    
    public Object[] getCurrency(String date, String code)
    {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

            URL url = new URL("http://cbar.az/currencies/"+date+".xml");
            InputStream stream = url.openStream();
            Document doc = (Document) docBuilder.parse(stream);
 
            NodeList listOfCurrencies = doc.getElementsByTagName("Valute");
            NodeList valCursDate = doc.getElementsByTagName("ValCurs");
            Node $Date = valCursDate.item(0);
            String dateFromCbar = ((Element) $Date).getAttribute("Date");

            for(int s=0; s<listOfCurrencies.getLength() ; s++){
                Node fistCurrencyElement = listOfCurrencies.item(s);
                if(fistCurrencyElement.getNodeType() == Node.ELEMENT_NODE){
                    Element firstPersonElement = (Element)fistCurrencyElement;  
                    
                    NodeList valueNameList = firstPersonElement.getElementsByTagName("Value");
                    Element valuElement = (Element)valueNameList.item(0);
                    NodeList $value = valuElement.getChildNodes();
                    
                    NodeList nominalList = firstPersonElement.getElementsByTagName("Nominal");
                    Element nominalElement = (Element)nominalList.item(0);
                    NodeList $nominal = nominalElement.getChildNodes();
                    
                    if(((Element) fistCurrencyElement).getAttribute("Code").equals(code))
                    {
                        double val, nom;
                        val = Double.parseDouble(((Node)$value.item(0)).getNodeValue().trim());                       double v = Double.parseDouble(((Node)$value.item(0)).getNodeValue().trim());
                        nom = Double.parseDouble(((Node)$nominal.item(0)).getNodeValue().trim());

                        return new Object[] {val/nom, dateFromCbar};
                    }
                }
            }
        }
        catch(SocketException s)
        {
            JOptionPane.showMessageDialog(null,
                "İnternet bağlantısı yoxdur.",
                "Xəta",
                JOptionPane.ERROR_MESSAGE);
        }
        catch(UnknownHostException s)
        {
            JOptionPane.showMessageDialog(null,
                "İnternet bağlantısı yoxdur.",
                "Xəta",
                JOptionPane.ERROR_MESSAGE);
        }
        catch (Throwable t)
        {
            System.out.println(t);
            JOptionPane.showMessageDialog(null,
                "Bilinməyən bir xəta baş verdi :(",
                "Xəta",
                JOptionPane.ERROR_MESSAGE);
            return new Object[]{0};
        }
        
        return new Object[]{0};
    }

    public static void main(String[] args) throws IOException {
        
        // TODO code application logic here
        MainFrame frame = new MainFrame();

        frame.setLocationRelativeTo(null);
        //frame.pack();
        frame.setVisible(true);

    } 
}
