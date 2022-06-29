

package ca.iut.cs.IHMMODELE.gui;

import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ca.iut.cs.IHMMODELE.graph.GraphElement;
import ca.iut.cs.IHMMODELE.graph.Property;
import ca.iut.cs.IHMMODELE.graph.nodes.NoteNode;

/**
 *  A GUI component that can present the properties of a GraphElement
 *  and allow editing them.
 *  
 *  
 */
@SuppressWarnings("serial")
public class PropertySheet extends JPanel
{
	/**
	 * A handler for whenever a property is being detected
	 * as being edited. This allows a more responsive UI,
	 * where properties can be shown as they are typed, as
	 * opposed to only when the value is entered.
	 */
	interface PropertyChangeListener
	{
		void propertyChanged();
	}
	
	private static final int TEXT_FIELD_WIDTH = 10;
	private static Set<AWTKeyStroke> tab = new HashSet<>(1);
	private static Set<AWTKeyStroke> shiftTab = new HashSet<>(1);
	private static ResourceBundle aPropertyNames = ResourceBundle.getBundle("ca.iut.cs.IHMMODELE.graph.GraphElementProperties");

	private final PropertyChangeListener aListener;
	
	static
	{  
		tab.add(KeyStroke.getKeyStroke("TAB" ));
		shiftTab.add(KeyStroke.getKeyStroke( "shift TAB" ));
	}
	
	/**
	 * Constructs a PropertySheet to show and support editing all the properties 
	 * for pElement.
	 * 
	 * @param pElement The element whose properties we wish to edit.
	 * @param pListener An object that responds to property change events.
	 * @pre pElement != null
	 */
	public PropertySheet(GraphElement pElement, PropertyChangeListener pListener)
	{
		assert pElement != null;
		aListener = pListener;
		setLayout(new FormLayout());
		for( Property property : pElement.properties() )
		{
			Component editor = getEditorComponent(pElement, property);
			if(property.isVisible() && editor != null )
			{
				add(new JLabel(getPropertyName(pElement.getClass(), property.getName())));
				add(editor);
			}
		}
	}
	
	/**
	 * @return aEmpty whether this PropertySheet has fields to edit or not.
	 */
	public boolean isEmpty()
	{
		return getComponentCount() == 0;
	}
	
	private Component getEditorComponent(GraphElement pElement, Property pProperty)   
	{      
		if( pProperty.get() instanceof String )
		{
			if( extended(pElement, pProperty.getName()))
			{
				return createExtendedStringEditor(pProperty);
			}
			else
			{
				return createStringEditor(pProperty);
			}
		}
		else if( pProperty.get() instanceof Enum )
		{
			return createEnumEditor(pProperty);
		}
		else if( pProperty.get() instanceof Boolean)
		{
			return createBooleanEditor(pProperty);
		}
		return null;
	}
	
	/*
	 * Not the greatest but avoids over-engineering the rest of the properties API. CSOFF:
	 */
  private static boolean extended(GraphElement pElement, String pProperty)
	{
		 
		return true;
	} // CSON:
	
	private Component createExtendedStringEditor(Property pProperty)
	{
		final int rows = 5;
		final int columns = 30;
		final JTextArea textArea = new JTextArea(rows, columns);

		textArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, tab);
		textArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, shiftTab);

		textArea.setText((String) pProperty.get());
		textArea.getDocument().addDocumentListener(new DocumentListener()
		{
			public void insertUpdate(DocumentEvent pEvent) 
			{
				pProperty.set(textArea.getText());
				aListener.propertyChanged();
			}
			public void removeUpdate(DocumentEvent pEvent) 
			{
				pProperty.set(textArea.getText());
				aListener.propertyChanged();
			}
			public void changedUpdate(DocumentEvent pEvent) 
			{}
		});
		return new JScrollPane(textArea);
	}
	
	private Component createStringEditor(Property pProperty)
	{
		JTextField textField = new JTextField((String) pProperty.get(), TEXT_FIELD_WIDTH);
		textField.getDocument().addDocumentListener(new DocumentListener()
        	{
				public void insertUpdate(DocumentEvent pEvent) 
				{
					pProperty.set(textField.getText());
					aListener.propertyChanged();
				}
				public void removeUpdate(DocumentEvent pEvent) 
				{
					pProperty.set(textField.getText());
					aListener.propertyChanged();
				}
				public void changedUpdate(DocumentEvent pEvent) 
				{}
        	});
		return textField;
	}
	
	private Component createEnumEditor(Property pProperty)
	{
		Enum<?> value = (Enum<?>)pProperty.get();
		try 
		{
			final JComboBox<Enum<?>> comboBox = new JComboBox<Enum<?>>((Enum<?>[])value.getClass().getMethod("values").invoke(null));
			comboBox.setSelectedItem(value);
			comboBox.addItemListener(new ItemListener()
			{
					@Override
					public void itemStateChanged(ItemEvent pEvent)
					{
						if(pEvent.getStateChange() == ItemEvent.SELECTED)
						{
							pProperty.set(comboBox.getSelectedItem().toString());
							aListener.propertyChanged();
						}
					}
	        	});
			return comboBox;
		}
		catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e) 
		{ 
			return null; 
		}
	}
	
	private Component createBooleanEditor(Property pProperty)
	{
		JCheckBox checkBox = new JCheckBox();
		checkBox.setSelected((boolean)pProperty.get());
		checkBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pEvent)
			{
				pProperty.set(checkBox.isSelected());
				aListener.propertyChanged();
			}
		});
		return checkBox;
	}

	/*
	 * Obtains the externalized name of a property and takes account
	 * of property inheritance: if a property is not found on a class,
	 * looks for the property name is superclasses. We do not use the actual
	 * property names to decouple visual representation (which can eventually
	 * be translated) from names in the design space.
	 */
	private static String getPropertyName(Class<?> pClass, String pProperty)
	{
		assert pProperty != null;
		if( pClass == null )
		{
			return pProperty;
		}
		String key = pClass.getSimpleName() + "." + pProperty;
		if( !aPropertyNames.containsKey(key) )
		{
			return getPropertyName(pClass.getSuperclass(), pProperty);
		}
		else
		{
			return aPropertyNames.getString(key);
		}
	}
}

