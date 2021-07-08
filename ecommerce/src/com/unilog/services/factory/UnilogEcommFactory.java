package com.unilog.services.factory;

import com.unilog.services.UnilogFactoryInterface;

import com.unilog.services.impl.OrgillCustomServices;
import com.unilog.services.impl.PipesValvesCustomServices;
import com.unilog.services.impl.PneumaticsCustomServices;
import com.unilog.services.impl.TurtleAndHughesCustomServices;
import com.unilog.services.impl.WallaceHardwareCustomServices;
import com.unilog.services.impl.WarshauerCustomServices;
import com.unilog.services.impl.WernerRedesignCustomServices;
import com.unilog.services.impl.WinkleElectricCustomServices;
import com.unilog.services.impl.AdaptPharmaCustomServies;
import com.unilog.services.impl.AaronAndCompanyCustomServices;
import com.unilog.services.impl.AprCustomServices;
import com.unilog.services.impl.BlackmanPlumbingSupplyCustomServices;
import com.unilog.services.impl.BraasCoCustomServices;
import com.unilog.services.impl.CraneCustomServices;
import com.unilog.services.impl.DaveCarterCustomServices;
import com.unilog.services.impl.DefaultCustomServices;
import com.unilog.services.impl.EasternIndustrialAutomationCustomServices;
import com.unilog.services.impl.EddyCustomServices;
import com.unilog.services.impl.ElectrozadCustomServices;
import com.unilog.services.impl.EtnaSupplyCustomServices;
import com.unilog.services.impl.FrommElectricCustomServices;
import com.unilog.services.impl.GearyPacificCustomServices;
import com.unilog.services.impl.HabeggerCorporationCustomServices;
import com.unilog.services.impl.HillAndMarksCustomServices;
import com.unilog.services.impl.HirschCustomServices;
import com.unilog.services.impl.HubbardSupplyCustomServices;
import com.unilog.services.impl.KennyPipeAndSupplyCustomServices;
import com.unilog.services.impl.MacombGroupCustomServices;
import com.unilog.services.impl.MalloryCompany;
import com.unilog.services.impl.MarksSupplyCustomServices;
import com.unilog.services.impl.PurvisCustomServices;
import com.unilog.services.impl.SafewareCustomServices;
import com.unilog.services.impl.ShearerSupplyCustomServices;
import com.unilog.services.impl.SupplyForceUpgradeCustomService;
import com.unilog.services.impl.TexasCustomServices;
import com.unilog.services.impl.BillowsElectricCustomServices;
import com.unilog.utility.CommonUtility;
import com.unilog.services.impl.ScientificEquipmentCompanyCustomServices;

public class UnilogEcommFactory {
	
	private static UnilogEcommFactory unilogEcommFactory;

	private UnilogEcommFactory() {
	}

	public static UnilogEcommFactory getInstance() {
		if(unilogEcommFactory == null) {
			unilogEcommFactory = new UnilogEcommFactory();
		}
		return unilogEcommFactory;
	}
	
	public UnilogFactoryInterface getData(String className){
		try {
			System.out.println("UnilogFactoryInterface @ getData : className = "+CommonUtility.validateString(className));
			if(CommonUtility.validateString(className).length()>0) {
				switch(CommonUtility.validateString(className)) {

					case "OrgillCustomServices": return  OrgillCustomServices.getInstance(); // Orgill
					case "PurvisCustomServices": return  PurvisCustomServices.getInstance(); // Purvis Industries
					case "AprCustomServices": return  AprCustomServices.getInstance(); // APR Supply CO.
					case "AdaptPharmaCustomServies": return  AdaptPharmaCustomServies.getInstance();// Adapt Pharma
					case "TurtleAndHughesCustomServices": return  TurtleAndHughesCustomServices.getInstance();//Turtle&Hughes
					case "ElectrozadCustomServices": return ElectrozadCustomServices.getInstance();// Electrozad Supply
					case "GearyPacificCustomServices": return  GearyPacificCustomServices.getInstance(); // Geary Pacific
					case "BraasCoCustomServices": return BraasCoCustomServices.getInstance(); // BraasCo services
					case "MalloryCompanyCustomServices": return  MalloryCompany.getInstance(); // Mallory Company
					case "AaronAndCompanyCustomServices": return  AaronAndCompanyCustomServices.getInstance(); // Aaron and Company
					case "BlackmanPlumbingSupplyCustomServices": return  BlackmanPlumbingSupplyCustomServices.getInstance(); // Blackman Custom services
					case "HabeggerCorporationCustomServices": return  HabeggerCorporationCustomServices.getInstance(); // HabeggerCorporation Custom services
					case "EtnaSupplyCustomServices": return  EtnaSupplyCustomServices.getInstance();// Etna Supply
					case "WernerRedesignCustomServices": return  WernerRedesignCustomServices.getInstance(); // WernerRedesign
					case "EasternIndustrialAutomationCustomServices": return EasternIndustrialAutomationCustomServices.getInstance(); //EIA
					case "SupplyForceUpgradeCustomService" : return SupplyForceUpgradeCustomService.getInstance();
					case "DaveCarterCustomServices" : return DaveCarterCustomServices.getInstance();
					case "WallaceHardwareCustomServices" : return WallaceHardwareCustomServices.getInstance(); //Wallace Hardware
					case "WarshauerCustomServices" : return WarshauerCustomServices.getInstance(); //Warshauer Electrical
					case "CraneCustomServices" : return CraneCustomServices.getInstance(); //Crane Engineering
					case "MacombGroupCustomServices" : return MacombGroupCustomServices.getInstance();
					case "TexasCustomServices" : return TexasCustomServices.getInstance();
					case "ShearerSupplyCustomServices" : return ShearerSupplyCustomServices.getInstance();
					case "FrommElectricCustomServices" : return FrommElectricCustomServices.getInstance(); // Fromm Electric
					case "HubbardSupplyCustomServices" : return HubbardSupplyCustomServices.getInstance();
					case "EddyCustomServices" : return EddyCustomServices.getInstance();
					case "HirschCustomServices" : return HirschCustomServices.getInstance();
					case "WinkleElectricCustomServices" : return WinkleElectricCustomServices.getInstance(); // Winkle Electrics
					case "BillowsElectricCustomServices" : return BillowsElectricCustomServices.getInstance();
					case "ScientificEquipmentCompanyCustomServices" : return ScientificEquipmentCompanyCustomServices.getInstance();//SECO services
					case "PneumaticsCustomServices" : return PneumaticsCustomServices.getInstance(); //Pneumatics
					case "KennyPipeAndSupplyCustomServices": return KennyPipeAndSupplyCustomServices.getInstance(); //KennyPipeAndSupply
					case "MarksSupplyCustomServices" : return MarksSupplyCustomServices.getInstance(); //MarksSupply
					case "PipesValvesCustomServices" : return PipesValvesCustomServices.getInstance(); //PipesValves
					case "SafewareCustomServices" : return SafewareCustomServices.getInstance(); //Safeware
					case "HillAndMarksCustomServices" : return HillAndMarksCustomServices.getInstance(); //Safeware
					
					default: return  DefaultCustomServices.getInstance(); // Default return DefaultCustom Service for service object
				}
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}