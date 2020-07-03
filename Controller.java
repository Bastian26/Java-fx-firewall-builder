package application;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class Controller implements Initializable{

	@FXML ListView<String> richtung;
	@FXML ListView<String> protokoll;
	
	@FXML private TextField quell;
	@FXML private TextField ziel;
	@FXML private TextField port_s;
	@FXML private TextField mac;
	@FXML private TextField comment;
	
	@FXML private TextArea resultLabel;
	
	@FXML private Button btnAkt;
	@FXML private Button btnSave;
	
	public void skripAktualisieren (ActionEvent event) {
		
		String quellString = quell.getText();
		String destinationString = ziel.getText();
		String port_sString = port_s.getText();
		String macString = mac.getText();
		String commentString = comment.getText().toUpperCase();
		String richtungString = richtung.getSelectionModel().getSelectedItem();
		String protokollString = protokoll.getSelectionModel().getSelectedItem();
		
		Model m = new Model();
		
		String skriptOutput = m.calculateIP(richtungString, protokollString, quellString, destinationString, port_sString, macString, commentString);
		
		//Ausgabe in TextArea
		resultLabel.setText(skriptOutput);
	}
	
	public void speichern (ActionEvent event) {
		
		String quellString = quell.getText();
		String destinationString = ziel.getText();
		String port_sString = port_s.getText();
		String macString = mac.getText();
		String commentString = comment.getText().toUpperCase();
		String richtungString = richtung.getSelectionModel().getSelectedItem();
		String protokollString = protokoll.getSelectionModel().getSelectedItem();
		
		Model m = new Model();
		
		String ausgabe = m.calculateIP(richtungString, protokollString, quellString, destinationString, port_sString, macString, commentString);
		// .sh-Datei wird mit createFile Methode erstellt und mit dem String befüllt, damit ist das Skript fertig und 
		// im temporären Verzeichnis abrufbar (C:\\Users\YourUserName\AppData\Local\Temp\FirewallBuilder_IpTablesSkcript.sh)
		createFile(ausgabe);
		
	}
	
	//Creates a new File when you push the save Button
	public void createFile(String stringForFileInput) {
		  try {
			  File datei = new File(System.getProperty("java.io.tmpdir") + "//FirewallBuilder_IpTablesSkcript.sh");
			  datei.createNewFile();
			  
			  PrintWriter pw = new PrintWriter(new FileWriter(datei));
			  pw.print(stringForFileInput);
			  pw.flush(); 
			  
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		  
	}
	// Tooltips nutzen um Infos über die Elemente anzuzeigen
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> directionList = FXCollections.observableArrayList (
                "FORWARD", "INPUT", "OUTPUT");
        richtung.setItems(directionList);
        
        ObservableList<String> protocolList = FXCollections.observableArrayList (
                "TCP", "UDP", "ICMP");
       protokoll.setItems(protocolList);
       
       final Tooltip tooltipDirection = new Tooltip();
       tooltipDirection.setText("Geben Sie die Firewall-Richtung an");
       richtung.setTooltip(tooltipDirection);
       
       final Tooltip tooltipProt = new Tooltip();
       tooltipProt.setText("Geben Sie die das zu verwendende Protokoll an");
       protokoll.setTooltip(tooltipProt);
       
       final Tooltip tooltipQuell = new Tooltip();
       tooltipQuell.setText("Geben Sie die Quell-IP-Adresse an");
       quell.setTooltip(tooltipQuell);
       
       final Tooltip tooltipZiel = new Tooltip();
       tooltipZiel.setText("Geben Sie die Ziel-IP-Adresse an");
       ziel.setTooltip(tooltipZiel);
       
       final Tooltip tooltipPorts = new Tooltip();
       tooltipPorts.setText("Geben Sie einen oder mehrere Ports an(Multiports müssen durch Komma getrennt werden wie z.B.: 80,8080)");
       port_s.setTooltip(tooltipPorts);
       
       final Tooltip tooltipMac = new Tooltip();
       tooltipMac.setText("Geben Sie die MAC-Adresse nach folgendem Format ein (XX-XX-XX-XX-XX-XX");
       mac.setTooltip(tooltipMac);
       
       final Tooltip tooltipComment = new Tooltip();
       tooltipComment.setText("Geben Sie Zusatzinformationen wie z.B.: DNS/PROXY/DHCP/PING/SSH/NTP an");
       comment.setTooltip(tooltipComment);
       
       final Tooltip tooltipTextArea = new Tooltip();
       tooltipTextArea.setText("Hier wird das fertige Linux-ShellSkript angezeigt");
       resultLabel.setTooltip(tooltipTextArea);
       
       final Tooltip tooltipBtnAkt = new Tooltip();
       tooltipBtnAkt.setText("Das Skript aktualisieren und somit mehr Regeln den IP-Tables hinzufügen");
       btnAkt.setTooltip(tooltipBtnAkt);
       
       final Tooltip tooltipBtnSave = new Tooltip();
       tooltipBtnSave.setText("Ihr Skript wird in eine Shell-Skript-Datei (.sh) in ihrem Temporären Verzeichnis gespeichert (C:\\Users\\YourUserName\\AppData\\Local\\Temp\\FirewallBuilder_IpTablesSkcript.sh");
       btnSave.setTooltip(tooltipBtnSave);
	}
	
}