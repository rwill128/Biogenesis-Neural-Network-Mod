package auxiliar;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import geneticcodes.GeneticCode;
import world.World;

public class BioFileChooser extends JFileChooser {
	private static final long serialVersionUID = 1L;
	public static final String WORLD_EXTENSION = "bgw"; //$NON-NLS-1$
	public static final String GENETIC_CODE_EXTENSION = "bgg"; //$NON-NLS-1$
	public static final String IMAGE_EXTENSION = "png";
	
	private File lastFile;
	
	private static BioFileChooser fileChooser = null;
	private static FileNameExtensionFilter geneticCodeFilter =
		new FileNameExtensionFilter(Messages.getInstance().getString("T_BIOGENESIS_GENETIC_CODE_FILES"),  //$NON-NLS-1$);
				GENETIC_CODE_EXTENSION);
	private static FileNameExtensionFilter worldFilter =
		new FileNameExtensionFilter(Messages.getInstance().getString("T_BIOGENESIS_WORLD_FILES"),  //$NON-NLS-1$);
				WORLD_EXTENSION);
	private static FileNameExtensionFilter imageFilter =
		new FileNameExtensionFilter(Messages.getInstance().getString("T_PNG_IMAGE_FILES"),  //$NON-NLS-1$);
				IMAGE_EXTENSION);
	
	public File getLastFile() {
		return lastFile;
	}
	
	public void setLastFile(File file) {
		lastFile = file;
	}
	
	public static BioFileChooser getFileChooser(Object obj) throws IllegalArgumentException {
		if (obj instanceof GeneticCode)
			return getGeneticCodeChooser();
		if (obj instanceof World)
			return getWorldChooser();
		throw new IllegalArgumentException("getFileChooser: object type not supported.");
	}
	
	public static BioFileChooser getGeneticCodeChooser() {
		if (fileChooser == null)
			fileChooser = new BioFileChooser();
		fileChooser.setFileFilter(geneticCodeFilter);
		return fileChooser;
	}
	
	public static BioFileChooser getWorldChooser() {
		if (fileChooser == null)
			fileChooser = new BioFileChooser();
		fileChooser.setFileFilter(worldFilter);
		return fileChooser;
	}

	public static BioFileChooser getImageChooser() {
		if (fileChooser == null)
			fileChooser = new BioFileChooser();
		fileChooser.setFileFilter(imageFilter);
		return fileChooser;
	}
	
	public String getExtension() {
		return ((FileNameExtensionFilter)fileChooser.getFileFilter()).getExtensions()[0];
	}
	
	protected BioFileChooser() {
	}

	protected BioFileChooser(String arg0) {
		super(arg0);
	}

	protected BioFileChooser(File arg0) {
		super(arg0);
	}

	protected BioFileChooser(FileSystemView arg0) {
		super(arg0);
	}

	protected BioFileChooser(File arg0, FileSystemView arg1) {
		super(arg0, arg1);
	}

	protected BioFileChooser(String arg0, FileSystemView arg1) {
		super(arg0, arg1);
	}

}
