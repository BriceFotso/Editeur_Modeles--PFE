

package ca.iut.cs.IHMMODELE;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import ca.iut.cs.IHMMODELE.diagrams.FormDiagramGraph;
import ca.iut.cs.IHMMODELE.gui.EditorFrame;

/**
 * A program for editing UML diagrams.
 */
public final class FormEditor
{
	private static final int JAVA_MAJOR_VERSION = 7;
	private static final int JAVA_MINOR_VERSION = 0;
	
	private FormEditor() {}
	
	/**
	 * @param pArgs Each argument is a file to open upon launch.
	 * Can be empty.
	 */
	public static void main(String[] pArgs)
	{
		// checkVersion(); 
		try
		{
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}
		catch (SecurityException ex)
		{
			// well, we tried...
		}
		final String[] arguments = pArgs;
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				setLookAndFeel();
				EditorFrame frame = new EditorFrame(FormEditor.class);
				frame.addGraphType("Form_diagram", FormDiagramGraph.class);
				frame.setVisible(true);
				frame.readArgs(arguments);
				frame.addWelcomeTab();
				frame.setIcon();
			}
		});
   }
	
	private static void setLookAndFeel()
	{
		try
		{
			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) 
			{
				if("Nimbus".equals(info.getName())) 
				{
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} 
		catch(UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e) 
		{
		    // Nothing: We revert to the default LAF
		}
	}
	
	/**
	 *  Checks if the current VM has at least the given
	 *  version, and exits the program with an error dialog if not.
	 */
	@SuppressWarnings("unused")
	private static void checkVersion()
	{
		String version = obtainJavaVersion();
		if( version == null || !isOKJVMVersion(version))
		{
			ResourceBundle resources = ResourceBundle.getBundle("ca.iut.cs.IHMMODELE.gui.EditorStrings");
			String minor = "";
			int minorVersion = JAVA_MINOR_VERSION;
			if( minorVersion > 0 )
			{
				minor = "." + JAVA_MINOR_VERSION;
			}
			JOptionPane.showMessageDialog(null, resources.getString("error.version") + 
					"1." + JAVA_MAJOR_VERSION + minor);
			System.exit(1);
		}
	}
	
	static String obtainJavaVersion()
	{
		String version = System.getProperty("java.version");
		if( version == null )
		{
			version = System.getProperty("java.runtime.version");
		}
		return version;
	}
	
	/**
	 * @return True is the JVM version is higher than the 
	 * versions specified as constants.
	 */
	static boolean isOKJVMVersion(String pVersion)
	{
		assert pVersion != null;
		String[] components = pVersion.split("\\.|_");
		boolean lReturn = true;
		
		try
		{
			int systemMajor = Integer.parseInt(String.valueOf(components[1]));
			int systemMinor = Integer.parseInt(String.valueOf(components[2]));
			if( systemMajor > JAVA_MAJOR_VERSION )
			{
				lReturn = true;
			}
			else if( systemMajor < JAVA_MAJOR_VERSION )
			{
				lReturn = false;
			}
			else // major Equals
			{
				if( systemMinor > JAVA_MINOR_VERSION )
				{
					lReturn = true;
				}
				else if( systemMinor < JAVA_MINOR_VERSION )
				{
					lReturn = false;
				}
				else // minor equals
				{
					lReturn = true;
				}
			}
        }
		catch( NumberFormatException e)
		{
			lReturn = false;
		}
		return lReturn;
    }
}