package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.Shipper;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Created by m594201 on 5/22/2017.
 */
public class ShipperResolver implements LazyObjectResolver<Shipper> {
	@Override
	public void fetch(Shipper d) {
		d.getRealUpc().getAssociateUpcs().get(0).getSellingUnit().getTagSize();
	}
}
