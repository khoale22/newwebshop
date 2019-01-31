package com.heb.scaleMaintenance.service;

import com.heb.pm.entity.PriceDetail;
import com.heb.pm.productDetails.product.ProductInformationService;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.PriceServiceClient;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetailKey;
import com.heb.util.ws.SoapException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds all business logic related to authorizationAndRetail.
 *
 * @author m314029
 * @since 2.17.8
 */
@Service
public class AuthorizationAndRetailService {

	private static final List<Integer> labStores = Arrays.asList(838,891);

	// errors
	private final static String RETAIL_ERROR = "Problems acquiring retail for: %d ," +
			"store: %d.";

	@Autowired
	private PriceServiceClient priceServiceClient;

	@Autowired
	private ProductInformationService productInformationService;

	public List<ScaleMaintenanceAuthorizeRetail> getAuthorizedAndRetailForUpcsByStore(
			Long transactionId, Integer store,
			List<Long> upcs) {
		List<ScaleMaintenanceAuthorizeRetail> toReturn = new ArrayList<>();
		ScaleMaintenanceAuthorizeRetail currentAuthorizationRetail;
		ScaleMaintenanceAuthorizeRetailKey key;
		Map<Long, Boolean> upcAuthorization = getAuthorizationsByStoreAndUpcs(store, upcs);
		PriceDetail currentPriceDetail;
		for (Long upc : upcs) {
			key = new ScaleMaintenanceAuthorizeRetailKey()
					.setStore(store)
					.setTransactionId(transactionId)
					.setUpc(upc);
			currentAuthorizationRetail = new ScaleMaintenanceAuthorizeRetail()
					.setKey(key)
					.setAuthorized(upcAuthorization.get(upc))
					.setCreateTime(LocalDateTime.now());
			// if upc is authorized, get retail
			if(upcAuthorization.get(upc)){
				try {
					currentPriceDetail = this.getRetailsByStoreAndUpc(store, upc);
					currentAuthorizationRetail
							.setRetail(currentPriceDetail.getRetailPrice())
							.setByCountQuantity(currentPriceDetail.getxFor())
							.setWeighed(currentPriceDetail.getWeight());
				} catch (SoapException e){
					currentAuthorizationRetail.setMessage(e.getMessage());
				}
			}
			toReturn.add(currentAuthorizationRetail);
		}
		return toReturn;
	}

	/**
	 * This method gets retails by store and upc. If there was an error retrieving the retail, set an error message
	 * on the authorize retail for that store/upc combination.
	 *
	 * @param store Store to retrieve retail for.
	 * @param upc UPC to retrieve retail for.
	 * @return Gets price details for a store and upc.
	 */
	private PriceDetail getRetailsByStoreAndUpc(Integer store, Long upc) throws SoapException{

		if(labStores.contains(store)){
			return this.productInformationService.getPriceInformation(upc);
		} else {
			throw new SoapException(String.format(RETAIL_ERROR, upc, store));
		}
	}

	/**
	 * This method finds out if a given list of upcs is authorized for the given store
	 *
	 * @param store Store to look up authorizationAndRetail for.
	 * @param upcs List of upcs to check for authorizationAndRetail.
	 * @return Map containing upc and whether or not the upc is authorized at the given store.
	 */
	private Map<Long, Boolean> getAuthorizationsByStoreAndUpcs(Integer store, List<Long> upcs) {
		Map<Long, Boolean> toReturn = new HashMap<>();
		if(labStores.contains(store)) {
			for(Long upc : upcs){
				toReturn.put(upc, true);
			}
		} else {

			// for now, the user can only select lab stores; eventually when the user is able to select real stores,
			// authorizationAndRetail will have to be looked up
			for(Long upc : upcs){
				toReturn.put(upc, false);
			}
		}
		return toReturn;
	}
}
