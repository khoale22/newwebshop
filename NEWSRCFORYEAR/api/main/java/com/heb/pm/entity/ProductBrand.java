package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by m594201 on 8/11/2017.
 */
@Entity
@Table(name = "prod_brnd")
public class ProductBrand implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s [%d]";

	@Id
	@Column(name = "prod_brnd_id")
	private Long productBrandId;

	@Column(name = "prod_brnd_des")
	private String productBrandDescription;

	@Column(name = "prod_brnd_tier_id")
	private Long productBrandTierId;

	@Column(name = "prod_brnd_abb")
	private String productBrandAbbreviation;

	@Column(name = "show_on_web_sw")
	private Boolean showOnWeb;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_brnd_tier_id", insertable = false, updatable = false)
	private ProductBrandTier productBrandTier;

	@JsonIgnoreProperties("productBrand")
	@OneToMany(mappedBy = "productBrand",fetch = FetchType.LAZY)
	private List<ProductBrandCostOwner> productBrandCostOwners;

	public Long getProductBrandId() {
		return productBrandId;
	}

	public void setProductBrandId(Long productBrandId) {
		this.productBrandId = productBrandId;
	}

	public String getProductBrandDescription() {
		return productBrandDescription;
	}

	public void setProductBrandDescription(String productBrandDescription) {
		this.productBrandDescription = productBrandDescription;
	}

	public ProductBrandTier getProductBrandTier() {
		return productBrandTier;
	}

	public void setProductBrandTier(ProductBrandTier productBrandTier) {
		this.productBrandTier = productBrandTier;
	}

	public Long getProductBrandTierId() {
		return productBrandTierId;
	}

	public void setProductBrandTierId(Long productBrandTierId) {
		this.productBrandTierId = productBrandTierId;
	}

	/**
	 * Returns the product brand abb.
	 *
	 * @return the product brand abb.
	 */
	public String getProductBrandAbbreviation() {
		return productBrandAbbreviation;
	}

	/**
	 * Set the product brand abb.
	 *
	 * @param productBrandAbbreviation the product brand abb.
	 */
	public void setProductBrandAbbreviation(String productBrandAbbreviation) {
		this.productBrandAbbreviation = productBrandAbbreviation;
	}
	/**
	 * Returns the show on web sw.
	 *
	 * @return the show on web sw.
	 */
	public Boolean getShowOnWeb() {
		return showOnWeb;
	}
	/**
	 * Set the show on web sw.
	 *
	 * @param showOnWeb the show on web sw.
	 */
	public void setShowOnWeb(Boolean showOnWeb) {
		this.showOnWeb = showOnWeb;
	}

	/**
	 * Gets the list of product Brand Cost Owners.
	 *
	 * @return the list of product Brand Cost Owners.
	 */
	public List<ProductBrandCostOwner> getProductBrandCostOwners() {
		return productBrandCostOwners;
	}

	/**
	 * Sets the list of product Brand Cost Owners.
	 *
	 * @param productBrandCostOwners the list of product Brand Cost Owners.
	 */
	public void setProductBrandCostOwners(List<ProductBrandCostOwner> productBrandCostOwners) {
		this.productBrandCostOwners = productBrandCostOwners;
	}

	/**
	 * Returns a display name for a ProductBrand to display on the GUI.
	 *
	 * @return A display name.
	 */
	public String getDisplayName() {
		return this.productBrandDescription == null ? this.productBrandId.toString() :
				String.format(ProductBrand.DISPLAY_NAME_FORMAT, this.productBrandDescription.trim(), this.productBrandId);
	}

	/**
	 * Compares another object to this one. The key is the only thing used to determine equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductBrand productBrand = (ProductBrand) o;

		return productBrandId != null ? productBrandId.equals(productBrand.productBrandId) : productBrand.productBrandId == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return productBrandId != null ? productBrandId.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductBrand{" +
				"productBrandId=" + productBrandId +
				", productBrandDescription='" + productBrandDescription + '\'' +
				'}';
	}
}
