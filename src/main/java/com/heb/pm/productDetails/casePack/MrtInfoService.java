package com.heb.pm.productDetails.casePack;

import com.heb.pm.audit.AuditRecordWithUpc;
import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.Shipper;
import com.heb.pm.repository.MrtRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.ws.SoapException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds business logic related to MrtInfo. *
 *
 * @author m594201
 * @since 2.6.0
 */
@Service
class MrtInfoService {

	@Autowired
	private MrtRepository mrtRepository;

	@Autowired
	private AuditService auditService;

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	/**
	 * Update mrt quantity item master.
	 *
	 * @param itemMaster the item master
	 * @return the item master
	 */
	List<Shipper> updateMrtQuantity(ItemMaster itemMaster){

		List<Shipper> shipperList = itemMaster.getPrimaryUpc().getShipper();

		try {
			for (Shipper shipper : shipperList) {
				this.productManagementServiceClient.updateMrtQuantity(shipper);
			}
		} catch (Exception e){
			throw  new SoapException(e.getMessage());
		}
		//All shippers share a parent mrt Upc so i'm grabbing the first one that is the same across all
		return mrtRepository.findByKeyUpc(shipperList.get(0).getKey().getUpc());
	}

	/**
	 * Save new to mrt list.
	 *
	 * @param mrtShipper the mrt shipper
	 * @return the list
	 */
	List<Shipper> saveNewToMrt(List<Shipper> mrtShipper) {

		try {
			for (Shipper shipper : mrtShipper) {
				this.productManagementServiceClient.saveToMrt(shipper);
			}
		} catch (Exception e){
			throw  new SoapException(e.getMessage());
		}

		//All shippers share a parent mrt Upc so i'm grabbing the first one that is the same across all
		return mrtRepository.findByKeyUpc(mrtShipper.get(0).getKey().getUpc());
	}

	/**
	 * Delete from mrt list.
	 *
	 * @param deleteFromMrtData the delete from mrt data
	 * @return the list
	 */
	List<Shipper> deleteFromMrt(List<Shipper> deleteFromMrtData) {
		try {
			for (Shipper shipper : deleteFromMrtData) {
				this.productManagementServiceClient.deleteFromMrt(shipper);
			}
		} catch (Exception e){
			throw  new SoapException(e.getMessage());
		}

		//All shippers share a parent mrt Upc so i'm grabbing the first one that is the same across all
		return mrtRepository.findByKeyUpc(deleteFromMrtData.get(0).getKey().getUpc());
	}

	/**
	 * This returns a list of shipper audits based on the upc.
	 * @param upc
	 * @return
	 */
	List<AuditRecordWithUpc> getMrtAuditInformation(Long upc) {
		return this.auditService.getMrtAuditInformation(upc);
	}
}
