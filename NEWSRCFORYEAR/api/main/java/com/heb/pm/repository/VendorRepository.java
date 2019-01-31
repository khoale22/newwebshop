/*
 * VendorReposiotry
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.Vendor;
import com.heb.pm.vendor.VendorServiceClient;
import com.heb.util.ws.ObjectConverter;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.Fault;
import com.heb.xmlns.ei.activeapvendorlist.ActiveApVendorList;
import com.heb.xmlns.ei.getactiveapvendorlist_reply.GetActiveApVendorListReply;
import com.heb.xmlns.ei.getactiveapvendorlist_request.GetActiveApVendorListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository to retrieve information about vendors.
 *
 * @author d116773
 * @since 2.0.2
 */
@Repository
public class VendorRepository {

	@Autowired
	private VendorServiceClient vendorServiceClient;

	private ObjectConverter<ActiveApVendorList, Vendor>  objectConverter = new ObjectConverter<>(Vendor::new);

	/**
	 * Calls the vendor service to retrieve a list of all active vendors.
	 *
	 * @return A list of all active vendors.
	 */
	public List<Vendor> findAll() {

		GetActiveApVendorListRequest request = new GetActiveApVendorListRequest();
		request.setAuthentication(this.vendorServiceClient.getAuthentication());

		try {
			GetActiveApVendorListReply reply = this.vendorServiceClient.getPort().getActiveApVendorList(request);

			List<Vendor> vendors = new ArrayList<>();

			if (reply != null && reply.getActiveApVendorList() != null) {
				reply.getActiveApVendorList().forEach((v) -> vendors.add(this.objectConverter.convert(v)));
			}

			return vendors;
		} catch (Fault fault) {
			throw new SoapException(fault.getMessage(), fault.getCause());
		}
	}

	/**
	 * Sets the object that makes the actual calls to the vendor service. This method is primarily used for testing.
	 *
	 * @param vendorServiceClient The object that makes the actual calls to the vendor service.
	 */
	public void setVendorServiceClient(VendorServiceClient vendorServiceClient) {
		this.vendorServiceClient = vendorServiceClient;
	}
}
