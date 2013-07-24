package aux;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class BioSaver {
	public static File saveObjectAs(Object obj) {
		File resultFile = null;
		
		try {
			BioFileChooser chooser = BioFileChooser.getFileChooser(obj);
			int returnVal = chooser.showSaveDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				int canWrite = JOptionPane.YES_OPTION;
				File f = chooser.getSelectedFile();
				String filename = f.getName();
				String ext = (filename.lastIndexOf(".")==-1)?"":filename.substring(filename.lastIndexOf(".")+1,filename.length());
				if (ext.equals("")) {
					f = new File(f.getAbsolutePath()+"."+chooser.getExtension());
					chooser.setSelectedFile(f);
				}
				if (f.exists()) {
					canWrite = JOptionPane.showConfirmDialog(null,Messages.getInstance().getString("T_CONFIRM_FILE_OVERRIDE"), //$NON-NLS-1$
						Messages.getInstance().getString("T_FILE_EXISTS"),JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
				}
				if (canWrite == JOptionPane.YES_OPTION) {
					if (obj instanceof Writeable) {
						Writeable writeable = (Writeable) obj;
						if (writeable.write(f))
							resultFile = f;
					} else
						if (saveObject(obj, f))
							resultFile = f;
				}
				if (resultFile != null)
					chooser.setLastFile(resultFile);
			}
		} catch (SecurityException ex) {
			System.err.println(ex.getMessage());
			JOptionPane.showMessageDialog(null,Messages.getInstance().getString("T_PERMISSION_DENIED"),Messages.getInstance().getString("T_PERMISSION_DENIED"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return resultFile;
	}
	
	public static boolean saveObject(Object obj, File f) {
		ObjectOutputStream outputStream;
		try {
			FileOutputStream fileStream = new FileOutputStream(f);
			outputStream = new ObjectOutputStream(fileStream);
			outputStream.writeObject(obj);
			outputStream.close();
			//statusBar.setStatusMessage(Messages.getInstance().getString("T_WRITING_COMPLETED")); //$NON-NLS-1$
			return true;
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (SecurityException ex) {
			System.err.println(ex.getMessage());
			JOptionPane.showMessageDialog(null,Messages.getInstance().getString("T_PERMISSION_DENIED"),Messages.getInstance().getString("T_PERMISSION_DENIED"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return false;
	}
}
