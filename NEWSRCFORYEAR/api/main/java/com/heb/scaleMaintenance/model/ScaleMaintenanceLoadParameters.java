package com.heb.scaleMaintenance.model;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Java object to hold scale maintenance load Parameters
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceLoadParameters implements TopLevelModel<ScaleMaintenanceLoadParameters>, Serializable {

	private static final long serialVersionUID = -2806056497053826346L;

	public static final String CURRENT_VERSION = "1.0.0";

	public ScaleMaintenanceLoadParameters(){
		super();
	}

	public ScaleMaintenanceLoadParameters(String upcs, String stores){
		this.upcs = upcs;
		this.stores = stores;
	}

	private String upcs;
	private String stores;

	/**
	 * Returns Upcs.
	 *
	 * @return The Upcs.
	 **/
	public String getUpcs() {
		return upcs;
	}

	/**
	 * Sets the Upcs.
	 *
	 * @param upcs The Upcs.
	 **/
	public ScaleMaintenanceLoadParameters setUpcs(String upcs) {
		this.upcs = upcs;
		return this;
	}

	/**
	 * Returns Stores.
	 *
	 * @return The Stores.
	 **/
	public String getStores() {
		return stores;
	}

	/**
	 * Sets the Stores.
	 *
	 * @param stores The Stores.
	 **/
	public ScaleMaintenanceLoadParameters setStores(String stores) {
		this.stores = stores;
		return this;
	}

	@Override
	public <R> R map(Function<? super ScaleMaintenanceLoadParameters, ? extends R> mapper) {
		return mapper.apply(this);
	}

	@Override
	public ScaleMaintenanceLoadParameters validate(Function<? super ScaleMaintenanceLoadParameters, ? extends ScaleMaintenanceLoadParameters> validator) {
		return validator.apply(this);
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ScaleMaintenanceLoadParameters{" +
				"upcs='" + upcs + '\'' +
				", stores='" + stores + '\'' +
				'}';
	}
}
