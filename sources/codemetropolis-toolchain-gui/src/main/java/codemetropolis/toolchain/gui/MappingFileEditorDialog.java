package codemetropolis.toolchain.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import codemetropolis.toolchain.gui.beans.BadConfigFileFomatException;
import codemetropolis.toolchain.gui.components.CMButton;
import codemetropolis.toolchain.gui.components.CMCheckBox;
import codemetropolis.toolchain.gui.components.CMLabel;
import codemetropolis.toolchain.gui.components.CMScrollPane;
import codemetropolis.toolchain.gui.components.CMTextField;
import codemetropolis.toolchain.gui.utils.BuildableSettings;
import codemetropolis.toolchain.gui.utils.Property;
import codemetropolis.toolchain.gui.utils.PropertyCollector;
import codemetropolis.toolchain.gui.utils.Translations;

public class MappingFileEditorDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private Map<String, String[]> displayedBuildableAttributes;
	private Map<String, List<Property>> sourceCodeElementProperties;
	
	private JTabbedPane buildableTabbedPane;	
	private JPanel cellarPanel;
	private JPanel floorPanel;
	private JPanel gardenPanel;
	private JPanel groundPanel;
	private JTable cellarTable;
	private JTable floorTable;
	private JTable gardenTable;
	
	//ListModel and JList for the buildables: cellar, floor, garden
	private ListModel<String> cellarListmodel;
	private JList<String> cellarList;
	private ListModel<String> floorListmodel;
	private JList<String> floorList;
	private ListModel<String> gardenListmodel;
	private JList<String> gardenList;
	
	//ListModel and JList for the resources
	private ListModel<String> resourcesListmodel;
	private JList<String> resourcesList;
	
	private CMCheckBox useMappingCheckBox;
	private CMTextField pathField;
	
	private void loadDisplayedInfo(String cdfFilePath) {
		try {
			displayedBuildableAttributes = BuildableSettings.readSettings();
			BuildableSettings.displaySettings();
			
			PropertyCollector pc = new PropertyCollector();
			sourceCodeElementProperties = pc.getFromCdf(cdfFilePath);
			pc.displayProperties();
		}
		catch(BadConfigFileFomatException e) {
			JOptionPane.showMessageDialog(
					null,
					Translations.t("gui_err_invaild_config_file_format"),
					Translations.t("gui_err_title"),
					JOptionPane.ERROR_MESSAGE);
			
			displayedBuildableAttributes = BuildableSettings.DEFAULT_SETTINGS;
		}
		catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(
					null,
					Translations.t("gui_err_invaild_config_file_format"),
					Translations.t("gui_err_title"),
					JOptionPane.ERROR_MESSAGE);
			
			displayedBuildableAttributes = BuildableSettings.DEFAULT_SETTINGS;
		}
	}
	
	public MappingFileEditorDialog(String cdfFilePath, CodeMetropolisGUI cmGui) {
		super(cmGui, Translations.t("gui_mapping_editor_title") ,true);
		loadDisplayedInfo(cdfFilePath);
		
		JPanel panel = createBasePanel();
		addResourceOptions(panel);
		addSaveOptions(panel);
		addBuildableTabs(panel);
		addConversionOptions(panel);
		
		this.setResizable(false);
	    this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	    this.setContentPane(panel);
	    this.pack();
	    this.setLocationRelativeTo(cmGui);
	}
	
	private JPanel createBasePanel() {
		JPanel panel = new JPanel();
	    panel.setLayout(null);
	    panel.setBounds(0, 0, 800, 500);

	    Dimension size = new Dimension(800, 500);
	    panel.setMinimumSize(size);
	    panel.setPreferredSize(size);
	    panel.setMaximumSize(size);

	    return panel;
	}
	
	private void addResourceOptions(JPanel panel) {
		CMLabel resourcesLabel = new CMLabel(Translations.t("gui_l_resources"), 10, 0, 120, 30);
		
		resourcesListmodel = new DefaultListModel<String>();
	    resourcesList = new JList<String>(resourcesListmodel);
	    resourcesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    resourcesList.setLayoutOrientation(JList.VERTICAL);
	    resourcesList.setVisibleRowCount(-1);
		CMScrollPane resourcesScrollPane = new CMScrollPane(resourcesList, 10, 35, 240, 120);
		
		CMButton resourcesAddButton = new CMButton(Translations.t("gui_b_add"), 265, 35, 120, 30);
		CMButton resourcesRemoveButton = new CMButton(Translations.t("gui_b_remove"), 265, 80, 120, 30);
		
		panel.add(resourcesLabel);
		panel.add(resourcesScrollPane);
		panel.add(resourcesAddButton);
		panel.add(resourcesRemoveButton);
	}
	
	
	private void addSaveOptions(JPanel panel) {
		CMLabel saveSettingsLabel = new CMLabel(Translations.t("gui_l_save_settings"), 415, 0, 120, 30);
		CMLabel pathLabel = new CMLabel(Translations.t("gui_l_path"), 415, 35, 60, 30);
		pathField = new CMTextField(475, 35, 270, 30);
		CMButton specifyPathButton = new CMButton(Translations.t("gui_b_specify_path"), 415, 80, 120, 30);
		useMappingCheckBox = new CMCheckBox(550, 80, 30, 30);
		CMLabel useMappingLabel = new CMLabel(Translations.t("gui_l_use_mapping_file"),575, 80, 180, 30);
		CMButton saveMappingFileButton = new CMButton(Translations.t("gui_b_save_mapping_file"), 415, 120, 165, 30);
		
		panel.add(saveSettingsLabel);
		panel.add(pathLabel);
		panel.add(pathField);
		panel.add(specifyPathButton);
		panel.add(useMappingCheckBox);
		panel.add(useMappingLabel);
		panel.add(saveMappingFileButton);
	}
	
	private void addBuildableTabs(JPanel panel) {
		buildableTabbedPane = new JTabbedPane();
		
		createCellarTab();
		createFloorTab();
		createGardenTab();
		createGroundTab();
		
		buildableTabbedPane.add(Translations.t("gui_tab_cellar"), cellarPanel);
		buildableTabbedPane.add(Translations.t("gui_tab_floor"), floorPanel);
		buildableTabbedPane.add(Translations.t("gui_tab_garden"), gardenPanel);
		buildableTabbedPane.add(Translations.t("gui_tab_ground"), groundPanel);
		
		buildableTabbedPane.setFont(new Font("Source Sans Pro", Font.PLAIN, 16));
		buildableTabbedPane.setBounds(10, 175, 780, 270);
		
		panel.add(buildableTabbedPane);
		
	}
	
	private void createCellarTab() {
		cellarPanel = new JPanel();
		cellarPanel.setLayout(null);
	    cellarPanel.setBounds(0, 0, 780, 255);

	    Dimension size = new Dimension(780, 255);
	    cellarPanel.setMinimumSize(size);
	    cellarPanel.setPreferredSize(size);
	    cellarPanel.setMaximumSize(size);
	    
	    CMLabel assignedLabel = new CMLabel(Translations.t("gui_l_assigned_to"), 15, 15, 270, 30);
	    CMLabel attributeLabel = new CMLabel(Translations.t("gui_l_attribute"), 270, 15, 60, 30);
	    CMLabel propertiesLabel = new CMLabel(Translations.t("gui_l_properties"), 525, 15, 120, 30);
	    
	    cellarTable = setUpBuildableTable("CELLAR");
	    
	    cellarListmodel = initializeListModel("attribute");
	    cellarList = new JList<String>(cellarListmodel);
	    cellarList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    cellarList.setLayoutOrientation(JList.VERTICAL);
	    cellarList.setVisibleRowCount(-1);

	    CMScrollPane cellarScrollPane = new CMScrollPane(cellarList, 525, 50, 240, 180);
	    
	    cellarPanel.add(assignedLabel);
	    cellarPanel.add(attributeLabel);
	    cellarPanel.add(propertiesLabel);
	    cellarPanel.add(cellarTable);
	    cellarPanel.add(cellarScrollPane);	    
	}
	
	private void createFloorTab() {
		floorPanel = new JPanel();
		floorPanel.setLayout(null);
	    floorPanel.setBounds(0, 0, 780, 255);

	    Dimension size = new Dimension(780, 255);
	    floorPanel.setMinimumSize(size);
	    floorPanel.setPreferredSize(size);
	    floorPanel.setMaximumSize(size);
	    
	    CMLabel assignedLabel = new CMLabel(Translations.t("gui_l_assigned_to"), 15, 15, 270, 30);
	    CMLabel methodLabel = new CMLabel(Translations.t("gui_l_method"), 270, 15, 60, 30);
	    CMLabel propertiesLabel = new CMLabel(Translations.t("gui_l_properties"), 525, 15, 120, 30);		
	    
	    floorTable = setUpBuildableTable("FLOOR");
	    
	    floorListmodel = initializeListModel("method");
	    floorList = new JList<String>(floorListmodel);
	    floorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    floorList.setLayoutOrientation(JList.VERTICAL);
	    floorList.setVisibleRowCount(-1);
	    
	    CMScrollPane floorScrollPane = new CMScrollPane(floorList, 525, 50, 240, 180);
	    
	    floorPanel.add(assignedLabel);
	    floorPanel.add(methodLabel);
	    floorPanel.add(propertiesLabel);
	    floorPanel.add(floorTable);
	    floorPanel.add(floorScrollPane);
	}
	
	private void createGardenTab() {
		gardenPanel = new JPanel();
		gardenPanel.setLayout(null);
	    gardenPanel.setBounds(0, 0, 780, 255);

	    Dimension size = new Dimension(780, 255);
	    gardenPanel.setMinimumSize(size);
	    gardenPanel.setPreferredSize(size);
	    gardenPanel.setMaximumSize(size);
	    
	    CMLabel assignedLabel = new CMLabel(Translations.t("gui_l_assigned_to"), 15, 15, 270, 30);
	    CMLabel classLabel = new CMLabel(Translations.t("gui_l_class"), 270, 15, 60, 30);
	    CMLabel propertiesLabel = new CMLabel(Translations.t("gui_l_properties"), 525, 15, 120, 30);	
	    
	    gardenTable = setUpBuildableTable("GARDEN");
	    
	    gardenListmodel = initializeListModel("class");
	    gardenList = new JList<String>(gardenListmodel);
	    gardenList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    gardenList.setLayoutOrientation(JList.VERTICAL);
	    gardenList.setVisibleRowCount(-1);
	    
	    CMScrollPane gardenScrollPane = new CMScrollPane(gardenList, 525, 50, 240, 180);
	    
	    gardenPanel.add(assignedLabel);
	    gardenPanel.add(classLabel);
	    gardenPanel.add(propertiesLabel);
	    gardenPanel.add(gardenTable);
	    gardenPanel.add(gardenScrollPane);
	}
	
	private void createGroundTab() {
		groundPanel = new JPanel();
		groundPanel.setLayout(null);
	    groundPanel.setBounds(0, 0, 780, 255);

	    Dimension size = new Dimension(780, 255);
	    groundPanel.setMinimumSize(size);
	    groundPanel.setPreferredSize(size);
	    groundPanel.setMaximumSize(size);
	    
	    CMLabel assignedLabel = new CMLabel(Translations.t("gui_l_assigned_to"), 15, 15, 270, 30);
	    CMLabel packageLabel = new CMLabel(Translations.t("gui_l_package"), 270, 15, 60, 30);
	    CMLabel noAttrsLabel = new CMLabel(Translations.t("gui_l_no_attributes"), 15, 60, 300, 30);
	    
	    groundPanel.add(assignedLabel);
	    groundPanel.add(packageLabel);
	    groundPanel.add(noAttrsLabel);
	}
	
	private JTable setUpBuildableTable(String buildableType) {
		String[] displayedProperties = displayedBuildableAttributes.get(buildableType);
	    
	    Object[] columnNames = new String[] {Translations.t("gui_t_attribute"), Translations.t("gui_t_assigned_property")};
	    Object[][] initData = new Object[displayedProperties.length][2];
	    
	    for(int i = 0; i < displayedProperties.length; i++) {
	    	initData[i][0] = displayedProperties[i];
	    	initData[i][1] = null;
	    }
	    
	    JTable table = new JTable(initData, columnNames);
	    table.setFont(new Font("Source Sans Pro", Font.PLAIN, 14));
	    table.setRowHeight(30);
	    table.setBounds(15, 50, 480, displayedProperties.length * 30);
	    return table;
	}
	
	private ListModel<String> initializeListModel(String sourceCodeElementType) {
		List<Property> propertyList = sourceCodeElementProperties.get(sourceCodeElementType);
		
		DefaultListModel<String> model = new DefaultListModel<String>();
		
		for(Property p : propertyList) {
			model.addElement(p.name + ": " + p.type);
		}
		
		return model;
	}
	
	private void addConversionOptions(JPanel panel) {
		CMButton conversionButton = new CMButton(Translations.t("gui_b_conversions"), 10, 460, 150, 30);
		panel.add(conversionButton);
	}
}
