package application;

public class Model {
	public static int zaehlerModel = 0;
	public static String statischeGesamtAusgabe = "";
	//Hier folgen die Berechnungen
	public String calculateIP(String richtung, String protokoll,String quelle,String ziel,String port_s,String mac,String kommentar) {
		
		// direction prüfen und in einen Zahlenwert umwandeln
		int value1 = 0;
		switch (richtung) {
		case "FORWARD":	value1 = 1;
				  		break;
		case "INPUT":	value1 = 2;
	  					break;
		case "OUTPUT":	value1 = 3;
	  					break;	
		}
		
		// prtotocoll prüfen und in einen Zahlenwert umwandeln
		int value2 = 0;
		switch (protokoll) {
		case "TCP":	value2 = 1;
				  	break;
		case "UDP":	value2 = 2;
	  				break;
		case "ICMP":value2 = 3;
	  				break;	
		}
		//Auswertung
				String valueErgebnis = value1 + "" + value2;
				String value3 = "";
				switch (valueErgebnis) {
				case "12": 	value3 = "$FU";//fU;
							break;
				case "11": 	value3 = "$FT";//fT;
							break;
				case "13":	value3 = "$FI";//fI;
							break;	
				//-------------------------------
				case "22": 	value3 = "$IU";//iU;
		  					break;
				case "21": 	value3 = "$IT";//iT;
							break;
				case "23":	value3 = "$II";//iI;
							break;
				//-------------------------------
				case "32": 	value3 = "$OU";//oU;
							break;
				case "31": 	value3 = "$OT";//oT;
							break;
				case "33":	value3 = "$OI";//oI;
							break;	
				}
		//-------------------------------
				
		// Port prüfen
		String srcIP = quelle += " "; 
		String destinationIP = ziel += " "; 
		String port  = port_s;
		
		String portInput = port_s;
		boolean isMultiPort = isItAMultiPort(portInput);
		
		if (isMultiPort) {
			portInput = "--dports " + portInput + " ";
		}
		else if (!isMultiPort) {
			portInput = "--dports " + portInput + " ";
		}
		// MAC prüfen
		boolean macEingegeben = false;
		String macInput = "";
		if (mac.length() > 0) {
		macInput = mac += " ";
			macEingegeben = true;
		}
		
		// Kommentar prüfen
		String comment = kommentar;
		
		//Rückgabe bilden (String konkatinieren)
		
		String rueckgabe = "echo \"("+ zaehlerModel +")\" "+ comment;
		rueckgabe += "\n"+value3 + " ";
		rueckgabe += "";
		if (isMultiPort) {
			rueckgabe += "$MP ";
		}
		if (macEingegeben) {
			rueckgabe += "$MAC " + macInput;
		}
		rueckgabe += "-s " + srcIP;
		rueckgabe += "-d " + destinationIP;
		rueckgabe += portInput;
		rueckgabe += "$R";
		
		//System.out.println(ausgabe);
		String endRueckgabe = "";
		//rueckgabe = getBigText1()+rueckgabe+getBigText2();
		
		if (zaehlerModel == 0) {
			statischeGesamtAusgabe += rueckgabe;
			endRueckgabe = getBigText1() + rueckgabe + getBigText2();
		}
		else {
			statischeGesamtAusgabe +="\n" + rueckgabe;
			endRueckgabe = getBigText1() + statischeGesamtAusgabe +getBigText2();
		}
		// Inkremention
		zaehlerModel++;
		return endRueckgabe; 
	
	} // END OF MAIN-METHOD

	//############################################################################################# 
	//Normale-Methoden
	
	// Prüft, ob es sich um einen Multiport (true) handelt oder nicht (false)
	public static boolean isItAMultiPort(String zuPruefenderPort) {
	boolean kommaVorhanden = false;
	for (int i = 0; i < zuPruefenderPort.length(); i++) {
	    if (zuPruefenderPort.charAt(i) == ',') {
	    	kommaVorhanden = true;
	    	return kommaVorhanden;
	    }
	}
	return kommaVorhanden;
	}
		
	// Hier sind die großen texte für das spätere Skript
	public static String getBigText1() {
		String x1 = "\n#HIER IST DAS AUSFÜHRBARE SKRIPT FÜR IP_TABLES:\n#-----------------------------------------------------------\n#!/bin/sh\r\n" + 
				"\r\n" + 
				"IPT=\"/sbin/iptables\"\r\n" + 
				"\r\n" + 
				"IU=\"/sbin/iptables -A INPUT -p udp -m udp\"\r\n" + 
				"IT=\"/sbin/iptables -A INPUT -p tcp -m tcp\"\r\n" + 
				"II=\"/sbin/iptables -A INPUT -p icmp -m icmp --icmp-type 8/0\"\r\n" + 
				"\r\n" + 
				"FU=\"/sbin/iptables -A FORWARD -p udp -m udp\"\r\n" + 
				"FT=\"/sbin/iptables -A FORWARD -p tcp -m tcp\"\r\n" + 
				"FI=\"/sbin/iptables -A FORWARD -p icmp -m icmp --icmp-type 8/0\"\r\n" + 
				"\r\n" + 
				"OU=\"/sbin/iptables -A OUTPUT -p udp -m udp\"\r\n" + 
				"OT=\"/sbin/iptables -A OUTPUT -p tcp -m tcp\"\r\n" + 
				"OI=\"/sbin/iptables -A OUTPUT -p icmp -m icmp --icmp-type 8/0\"\r\n" + 
				"\r\n" + 
				"MAC=\"-m mac --mac-source \"\r\n" + 
				"\r\n" + 
				"MP=\"-m multiport\"\r\n" + 
				"\r\n" + 
				"R=\"-m conntrack --ctstate NEW -j ACCEPT\"\r\n" + 
				"\r\n" + 
				"modprobe ip_conntrack \r\n" + 
				"modprobe ip_conntrack_ftp\r\n" + 
				"\r\n" + 
				"$IPT -F\r\n" + 
				"$IPT -X\r\n" + 
				"$IPT -t nat -F\r\n" + 
				"$IPT -t nat -X\r\n" + 
				"$IPT -t mangle -F\r\n" + 
				"$IPT -t mangle -X\r\n" + 
				"\r\n" + 
				"$IPT -P INPUT DROP\r\n" + 
				"$IPT -P OUTPUT DROP\r\n" + 
				"$IPT -P FORWARD DROP\r\n" + 
				"\r\n" + 
				"$IPT -A INPUT -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT \r\n" + 
				"$IPT -A OUTPUT -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT \r\n" + 
				"$IPT -A FORWARD -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT\r\n" + 
				"\r\n" + 
				"\r\n" + 
				" echo \"alles auf localhost\"\r\n" + 
				"$IPT -A INPUT -i lo $R\r\n" + 
				"$IPT -A OUTPUT -o lo $R\r\n" + 
				"\r\n" + 
				"# Anfang eigene Regeln\r\n" + 
				"\r\n" + 
				"";
					return x1;
		}
		public static String getBigText2() {
			String x2 ="\n\r\n" + 
				"# Ende eigene Regeln\r\n" + 
				"\r\n" + 
				" echo \"catch all\"\r\n" + 
				"$IPT -A INPUT -j DROP\r\n" + 
				"$IPT -A OUTPUT -j DROP\r\n" + 
				"$IPT -A FORWARD -j DROP\r\n" + 
				"\r\n" + 
				"/sbin/sysctl -w net.ipv4.ip_forward=1";
			return x2;
		}

}