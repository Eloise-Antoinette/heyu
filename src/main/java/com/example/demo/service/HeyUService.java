package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.HeyUser;
import com.example.demo.repository.HeyUserRepository;

@Service
public class HeyUService {

	@Autowired
	HeyUserRepository huRep;

	ArrayList<HeyUser> listUsers = new ArrayList<HeyUser>();

	public void updateLocation() {

	}




	public void findNearUser(HeyUser thisUser, ArrayList<HeyUser> listToCheck ) {

		// listToCheck can be this.listUsers or a reduced list according to parameters(online, )
		for(HeyUser heyUser : listToCheck) {

			int distance = calculateDistance(thisUser.getHeyUserLatitude(), thisUser.getHeyUserLongitude(),heyUser.getHeyUserLatitude(), heyUser.getHeyUserLongitude());

			if(!thisUser.equals(heyUser)) {

				// Add or Remove matchs for thisUser

				if(!thisUser.getHeyUserNearU().contains(heyUser)) {
					if(distance <= thisUser.getHeyUserSearchRadius()) {
						thisUser.addHeyUserNearU(heyUser);
					}

				}

				else if(distance > thisUser.getHeyUserSearchRadius()) {
					thisUser.removeHeyUserNearU(heyUser);
				}


				// Add or Remove matchs for heyUser

				if(!heyUser.getHeyUserNearU().contains(thisUser)) {
					if(distance <= heyUser.getHeyUserSearchRadius()) {
						heyUser.addHeyUserNearU(thisUser);
					}

				}

				else if(distance > heyUser.getHeyUserSearchRadius()) {
					heyUser.removeHeyUserNearU(thisUser);
				}
			}
		}
	}





	// Transform °DEC en Rad
	public double convertToRad(double angle) {
		return Math.PI*angle/180;  

	}



	public int calculateDistance(double latA, double longA, double latB, double longB) {

		// En WGS84  https://fr.wikipedia.org/wiki/WGS_84
		//https://geodesie.ign.fr/contenu/fichiers/Distance_longitude_latitude.pdf
		// cours-fad-public.ensg.eu/pluginfile.php/1507/mod_resource/content/1/geoell.pdf   ---- Page 24 à 27 -----


		// Transform °DEC to Rad
		double latARad = convertToRad(latA);
		double longARad =convertToRad(longA);
		double latBRad = convertToRad(latB);
		double longBRad =convertToRad(longB);

		//Average latitude 
		double latMoyRad=(latARad+latBRad)/2;

		//delta longitude
		double dlambda=longARad-longBRad;

		//Angular distance  in radians
		double SAB=Math.acos(Math.sin(latARad)*Math.sin(latBRad)+Math.cos(latARad)*Math.cos(latBRad)*Math.cos(dlambda));


		//Semi-major axis (WGS84)
		double a = 6378137.0;

		//first eccentricity (WGS84)
		double e =0.081819190842622;

		//main radii of curvature
		double p = a*(1-Math.pow(e, 2))/Math.pow((1-(Math.pow(e, 2)*Math.pow(Math.sin(latMoyRad), 2))), 1.5);
		double N =a/Math.sqrt(1-(Math.pow(e, 2)*Math.pow(Math.sin(latMoyRad), 2)));
		double radius= Math.sqrt(p*N);	


		double distance = SAB*radius;


		System.out.println("Distance between the two \"heyUsers\" is :  "+(int) distance+ " meters");

		return (int) distance;


	}





}
