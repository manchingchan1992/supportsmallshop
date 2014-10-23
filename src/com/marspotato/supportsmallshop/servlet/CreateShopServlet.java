package com.marspotato.supportsmallshop.servlet;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.marspotato.supportsmallshop.BO.AuthCode;
import com.marspotato.supportsmallshop.BO.Helper;
import com.marspotato.supportsmallshop.BO.Submission;
import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.InputUtil;
import com.marspotato.supportsmallshop.util.OutputUtil;


@WebServlet("/CreateShop")
public class CreateShopServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CreateShopServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		String code = null;
		Submission s = new Submission();
		try 
		{
			code = InputUtil.getEncryptedString(request, "code");
			
			s.id = UUID.randomUUID().toString();
			//mandatory fields
			s.name = InputUtil.getNonEmptyString(request, "name");
			s.shopType = InputUtil.getStringInRange(request, "shopType", Config.shopTypes, false);
			s.shortDescription = InputUtil.getNonEmptyString(request, "shortDescription");
			s.fullDescription = InputUtil.getNonEmptyString(request, "fullDescription");
			s.district = InputUtil.getIntegerWithRange(request, "district", Config.WHOLE_HK, Config.NEW_TERRITORIES);
			s.address = InputUtil.getNonEmptyString(request, "address");
					
			//optional fields
			s.phone = InputUtil.getString(request, "phone", "");
			s.openHours = InputUtil.getString(request, "openHours", "");
			s.searchTags  = InputUtil.getString(request, "searchTags", "");
			s.latitude1000000 = InputUtil.getIntegerWithDefaultValue(request, "latitude1000000", 0);
			s.longitude1000000 = InputUtil.getIntegerWithDefaultValue(request, "longitude1000000", 0);
			//TODO: implement the photo and remove this hardcode
			s.photoUrl = "";
		}
		catch (Exception ex)
		{
			OutputUtil.response(response, HttpServletResponse.SC_BAD_REQUEST, "{Error : \""+ex.getMessage()+"\"}");
			return;
		}
		
		AuthCode ac = AuthCode.getAuthCode(code);
		if (ac == null)
		{
			OutputUtil.response(response, HttpServletResponse.SC_UNAUTHORIZED, "");
			return;
		}
		if (ac.registerUsage("CreateShop", s.id) == false)
		{
			//used action, try to get back the value
			String id = ac.getUsageValue("CreateShop");
			if (id != null)
			{
				s = Submission.getSubmission(id);
				OutputUtil.response(response, HttpServletResponse.SC_OK, Config.defaultGSON.toJson(s));
				return;
			}
			else
				OutputUtil.response(response, HttpServletResponse.SC_CONFLICT, "");
		}
		
		//save the record into database
		Helper h = Helper.getHelper(ac.deviceType, ac.regId);
		s.helperId = h.id;
		s.saveCreateShopRecord();
		
		OutputUtil.response(response, HttpServletResponse.SC_OK, Config.defaultGSON.toJson(s));
	}
}
