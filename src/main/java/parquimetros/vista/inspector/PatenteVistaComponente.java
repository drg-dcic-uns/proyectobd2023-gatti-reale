package parquimetros.vista.inspector;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class PatenteVistaComponente extends PlainDocument {

	private static final long serialVersionUID = 1L;
	private int limit;

    PatenteVistaComponente(int limit) {
        super();
        this.limit = limit;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }
        if ((getLength() + str.length()) <= limit) {

        	String combinedText = getText(0, getLength()) + str;

        	if (combinedText.length() <= 3) {
        		//if (combinedText.matches("[A-Z]{0,3}")) { Solo mayúsculas
            	if (combinedText.matches("[A-Za-z]{0,3}")) {        			
        			super.insertString(offset, str, attr);
        		}
        	} else {
        		//if (combinedText.matches("[A-Z]{3}[0-9]{0,3}")) { Solo mayúsculas
        		if (combinedText.matches("[A-Za-z]{3}[0-9]{0,3}")) {
        			super.insertString(offset, str, attr);
        		}
        	}
        }        
    }

}