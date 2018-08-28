package com.entity;
// Generated Aug 15, 2018 2:16:27 AM by Hibernate Tools 5.1.8.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * Product generated by hbm2java
 */
@Entity
@Table(name = "product", catalog = "webshop2")
@Proxy(lazy = false)
public class Product implements java.io.Serializable {

	private String productId;
	private Category category;
	private String productName;
	private String productImage;
	private double productPrice;
	private int quantity;
	private String screen;
	private String version;
	private String ram;

	@Column(name = "quantity", nullable = false)
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	private Set<Billdetail> billdetails = new HashSet<Billdetail>(0);

	public Product() {
	}

	public Product(String productId, Category category, String productName, String productImage, double productPrice) {
		this.productId = productId;
		this.category = category;
		this.productName = productName;
		this.productImage = productImage;
		this.productPrice = productPrice;
	}

	public Product(String productId, Category category, String productName, String productImage, double productPrice,
			Set<Billdetail> billdetails) {
		this.productId = productId;
		this.category = category;
		this.productName = productName;
		this.productImage = productImage;
		this.productPrice = productPrice;
		this.billdetails = billdetails;
	}

	@Id

	@Column(name = "product_id", unique = true, nullable = false, length = 45)
	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Column(name = "product_name", nullable = false, length = 45)
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "product_image", nullable = false, length = 45)
	public String getProductImage() {
		return this.productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	@Column(name = "product_price", nullable = false, precision = 22, scale = 0)
	public double getProductPrice() {
		return this.productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
	public Set<Billdetail> getBilldetails() {
		return this.billdetails;
	}

	public void setBilldetails(Set<Billdetail> billdetails) {
		this.billdetails = billdetails;
	}

	@Column(name = "screen", nullable = true, length = 45)
	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	@Column(name = "version", nullable = true, length = 45)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "ram", nullable = true, length = 45)
	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

}
