package com.example.demo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginDTOSent;
import com.example.demo.dto.ModifyHeyUserSettingsDTOExpected;
import com.example.demo.dto.NewLocationDTO;
import com.example.demo.dto.RegisteringDTOExpected;
import com.example.demo.model.HeyUser;
import com.example.demo.service.HeyUService;

@RestController
public class HeyUController {
	
	@Autowired
	HeyUService hUServ;

	@GetMapping("/getTest")
	public String getTest() {
		return "abv";
	}
	
    @PostMapping("/updateLocation")
    public ArrayList<HeyUser> updateLocation(@RequestBody NewLocationDTO newLocationDto) {
    	HeyUser searchedUser = hUServ.searchUserInArrayList( newLocationDto.getHeyUserName(),newLocationDto.getHeyUserPassword(), hUServ.getListUsers());
    	if(searchedUser != null) {
    		hUServ.updateLocation(searchedUser.getHeyUserLongitude(), searchedUser.getHeyUserLatitude(), searchedUser);
    		return hUServ.findNearUser(searchedUser, newLocationDto.getHeyUserSearchRadius(), hUServ.getListUsers());
    	}
		return null;
    }
	
    
    
    @PostMapping("/login")
    public LoginDTOSent login(@RequestBody RegisteringDTOExpected loginDTO) {
        HeyUser searchedUser = hUServ.searchUserInArrayList(loginDTO.getHeyUserName(),loginDTO.getHeyUserPassword(), hUServ.getListUsers());
    	LoginDTOSent thisUserLoginDto = new LoginDTOSent();
        if(searchedUser != null) {
           	thisUserLoginDto.setUserconnected(searchedUser);
        	thisUserLoginDto.setConnected(true);
        	thisUserLoginDto.setMessageSent("You've been successfully logged in");
        	return thisUserLoginDto;
    	} else {
    		thisUserLoginDto.setConnected(false);
    		thisUserLoginDto.setMessageSent("We haven't been able to connect you, please check your informations... ");
    		return thisUserLoginDto;
    	}
    	
    	
    	
    }
    
    @PostMapping("/registering")
    public LoginDTOSent register(@RequestBody RegisteringDTOExpected registeringDTO) {
    	System.out.println("work");
    	HeyUser searchedUser = hUServ.searchUserInArrayListByName(registeringDTO.getHeyUserName(), hUServ.getListUsers());
    	LoginDTOSent thisUserLoginDto = new LoginDTOSent();
        if(searchedUser != null) {
    		thisUserLoginDto.setConnected(false); // A CONFIRMER (si un utilisateur entre en dur "true" dans le false...
    		thisUserLoginDto.setMessageSent("There's already a HeyUser with that name.");
    		return thisUserLoginDto;
    	} else if(registeringDTO.getHeyUserPassword().equals(registeringDTO.getHeyUserConfirmPassword())){
    		thisUserLoginDto.getUserconnected().setHeyUserName(registeringDTO.getHeyUserName());
        	thisUserLoginDto.getUserconnected().setHeyUserPassword(registeringDTO.getHeyUserPassword());
        	thisUserLoginDto.setConnected(true);
        	thisUserLoginDto.setMessageSent("Welcome "+thisUserLoginDto.getUserconnected().getHeyUserName() + " ! Your account has been successfully created !" );
        	hUServ.save(thisUserLoginDto.getUserconnected());
        	hUServ.getListUsers().add(thisUserLoginDto.getUserconnected());
        	return thisUserLoginDto;	
    	} else {    		
    		thisUserLoginDto.setConnected(false); // A CONFIRMER (si un utilisateur entre en dur "true" dans le false...
    		thisUserLoginDto.setMessageSent("Your ConfirmPasword is invalid. Please check your informations.");
    		return thisUserLoginDto;
    		
    	
    	}
    	
    	
    }
    

    
    @PostMapping("/ModifyHeyUserSettings")
    public LoginDTOSent modifySettings(@RequestBody ModifyHeyUserSettingsDTOExpected settingsDTO) {
    HeyUser searchedUser = hUServ.searchUserInArrayList( settingsDTO.getHeyUserName(),settingsDTO.getHeyUserPassword(), hUServ.getListUsers());
	LoginDTOSent thisUserLoginDto = new LoginDTOSent();
    if(searchedUser != null) {
    	thisUserLoginDto.setUserconnected(searchedUser);
    	thisUserLoginDto.setConnected(true);// inutile si le boolean se conserve apres envoi du settingsDTO
    	thisUserLoginDto.setMessageSent("Your modifications has been successfully registered !");
    	hUServ.save(thisUserLoginDto.getUserconnected());
    	hUServ.getListUsers().add(thisUserLoginDto.getUserconnected().getHeyUserId().intValue(), thisUserLoginDto.getUserconnected());
    	return thisUserLoginDto;
	} else {
		thisUserLoginDto.setConnected(false); // A CONFIRMER (si un utilisateur entre en dur "true" dans le false...
		thisUserLoginDto.setMessageSent("You don't have the permission to modify this content... ");
		return thisUserLoginDto;
	}
    	
    }

    
    
	
}


