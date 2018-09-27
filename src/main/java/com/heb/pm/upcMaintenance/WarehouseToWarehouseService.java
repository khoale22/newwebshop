/*
 * MoveWarehouseUpcSwapService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.upcMaintenance;

import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to warehouse move upc swaps .
 *
 * @author m314029
 * @since 2.1.0
 */
@Service
public class WarehouseToWarehouseService {

	@Autowired
	private UpcSwapUtils upcSwapUtils;

	private static final String STRING_SUCCESS = "Success";


	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	/**
	 * Returns a list of all UPC swap information from a list of UPCs and matching item codes.
	 *
	 * @param upcList      the upc list
	 * @param itemCodeList the item code list
	 * @return A list of all UPC swaps.
	 */
	public List<UpcSwap> findAll(List<Long> upcList, List<Long> itemCodeList) {
		return upcSwapUtils.findAll(upcList,itemCodeList, UpcSwapUtils.WHS_TO_WHS);
	}

	/**
	 * Submits a list of warehouse move UPC swaps.
	 *
	 * @param upcSwapList List of UPC swaps to save.
	 * @return The updated list of UPC swaps.
	 */
	public List<UpcSwap> update(List<UpcSwap> upcSwapList) {

		upcSwapList = upcSwapUtils.rearrangeSwapListByPrecedence(this.createAdditionalSwaps(upcSwapList));
		String statusMessage;
		List<UpcSwap> returnList = new ArrayList<>();

		for(UpcSwap upcSwap : upcSwapList) {
			statusMessage = StringUtils.EMPTY;
			if (upcSwap.getSource().getErrorMessage() == null && upcSwap.getDestination().getErrorMessage() == null) {
				try {
					this.productManagementServiceClient.submitUpcSwap(upcSwap);
					upcSwap.setStatusMessage(STRING_SUCCESS);
				} catch(CheckedSoapException e){
					upcSwap.setErrorFound(true);
					upcSwap.getDestination().setErrorMessage(e.getMessage());
					upcSwap.setStatusMessage(e.getMessage());
				}
			} else {
				if (upcSwap.getSource().getErrorMessage() != null) {
					statusMessage = statusMessage.concat(upcSwap.getSource().getErrorMessage());
					upcSwap.setErrorFound(true);
				}
				if (upcSwap.getDestination().getErrorMessage() != null) {
					statusMessage = statusMessage.concat(upcSwap.getDestination().getErrorMessage());
					upcSwap.setErrorFound(true);
				}
				upcSwap.setStatusMessage(statusMessage);
			}
		}
		returnList.addAll(upcSwapUtils.getUpdatedAssociatedUpcList(upcSwapList));
		return returnList;
	}

	/**
	 * This method creates additional upc swaps from associates that the user declared as additional upc's to send,
	 * when the user entered a primary upc as the source upc.
	 *
	 * @param upcSwapList original upc swap list
	 * @return updated upc swap list
	 */
	private List<UpcSwap> createAdditionalSwaps(List<UpcSwap> upcSwapList) {
		List<UpcSwap> updatedList = new ArrayList<>();
		for(UpcSwap swap : upcSwapList){
			if(!swap.isSourcePrimaryUpc()){
				if(swap.getDestinationPrimaryUpcSelected() != null &&
						swap.getDestinationPrimaryUpcSelected().equals(swap.getSourceUpc())){
					swap.setMakeDestinationPrimaryUpc(true);
				}
				updatedList.add(swap);
			} else {
				switch (swap.getPrimarySelectOption()){
					case JUST_PRIMARY: {
						if(swap.getDestinationPrimaryUpcSelected() != null &&
								swap.getDestinationPrimaryUpcSelected().equals(swap.getSourceUpc())){
							swap.setMakeDestinationPrimaryUpc(true);
						} else {
							swap.setMakeDestinationPrimaryUpc(false);
						}
						updatedList.add(swap);
						break;
					}
					case PRIMARY_AND_ASSOCIATES: {
						this.createAssociateSwaps(swap, updatedList);
						if(swap.getDestinationPrimaryUpcSelected() != null &&
								swap.getDestinationPrimaryUpcSelected().equals(swap.getSourceUpc())){
							swap.setMakeDestinationPrimaryUpc(true);
						} else {
							swap.setMakeDestinationPrimaryUpc(false);
						}
						updatedList.add(swap);
						break;
					}
					case JUST_ASSOCIATES: {
						this.createAssociateSwaps(swap, updatedList);
						break;
					}
				}
			}
		}
		return updatedList;
	}

	/**
	 * This method clones the primary upc swap, then sets necessary attributes.
	 *
	 * @param primarySwap primary upc swap
	 * @param updatedList list of upc swaps
	 */
	private void createAssociateSwaps(UpcSwap primarySwap, List<UpcSwap> updatedList){
		UpcSwap newSwap;
		for(Long associateUpc : primarySwap.getAdditionalUpcList()) {
			newSwap = (UpcSwap) SerializationUtils.clone(primarySwap);
			newSwap.setSourceUpc(associateUpc);
			newSwap.setSourcePrimaryUpc(false);
			newSwap.setSelectSourcePrimaryUpc(primarySwap.getSourceUpc());
			if(primarySwap.getDestinationPrimaryUpcSelected() != null &&
					primarySwap.getDestinationPrimaryUpcSelected().equals(associateUpc)){
				newSwap.setMakeDestinationPrimaryUpc(true);
			} else {
				newSwap.setMakeDestinationPrimaryUpc(false);
			}
			updatedList.add(newSwap);
		}
	}

	/**
	 * Find all warehouse swap list.
	 *
	 * @param upcSwapList the list of upc swaps
	 * @return the list
	 */
	public List<UpcSwap> findAllWarehouseSwap(List<UpcSwap> upcSwapList) {
		return upcSwapUtils.findAllWarehouseSwaps(upcSwapList);
	}

	/**
	 * Update whs to whs swap list.
	 *
	 * @param whsToWhsSwapList the whs to whs swap list
	 * @return the list
	 */
	public List<UpcSwap> submitWarehouseToWarehouseSwap(List<UpcSwap> whsToWhsSwapList) {

		String statusMessage;
		List<UpcSwap> returnList = new ArrayList<>();

		for (UpcSwap upcSwap : whsToWhsSwapList) {
			statusMessage = StringUtils.EMPTY;
			if (upcSwap.getSource().getErrorMessage() == null && upcSwap.getDestination().getErrorMessage() == null) {
				try {
					this.productManagementServiceClient.submitWhsToWhsSwap(upcSwap);
				} catch (CheckedSoapException e) {
					upcSwap.setErrorFound(true);
					upcSwap.setStatusMessage(e.getMessage());
					upcSwap.getDestination().setErrorMessage(e.getMessage());
				}
			} else {
				if (upcSwap.getSource().getErrorMessage() != null) {
					statusMessage = statusMessage.concat(upcSwap.getSource().getErrorMessage());
					upcSwap.setErrorFound(true);
				}
				if (upcSwap.getDestination().getErrorMessage() != null) {
					statusMessage = statusMessage.concat(upcSwap.getDestination().getErrorMessage());
					upcSwap.setErrorFound(true);
				}
				upcSwap.setStatusMessage(statusMessage);
			}
		}

		returnList.addAll(upcSwapUtils.getUpdatedAssociatedUpcList(whsToWhsSwapList));

		return returnList;
	}

	/*
	 * These functions are to be used for support in testing.
	 */

	/**
	 * Sets the UpcSwapUtils.
	 *
	 * @param utils the UpcSwapUtils.
	 */
	public void setUpcSwapUtils(UpcSwapUtils utils){
		this.upcSwapUtils = utils;
	}

	/**
	 * Sets the ProductManagementServiceClient.
	 *
	 * @param productManagementServiceClient the ProductManagementServiceClient.
	 */
	public void setProductManagementServiceClient(ProductManagementServiceClient productManagementServiceClient){
		this.productManagementServiceClient = productManagementServiceClient;
	}
}
