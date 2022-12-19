package com.digital.v3.sql.vo;

import lombok.Data;

@Data
public class InventoryVO {

	private long productId;
	private long quantity;
	
	public long getProductId() {
		long productId = this.productId;
		return productId;
	}
	
	public long getQuantity() {
		long quantity = this.quantity;
		return quantity;
	}
	
}
