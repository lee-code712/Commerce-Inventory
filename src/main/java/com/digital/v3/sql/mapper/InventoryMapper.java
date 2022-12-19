package com.digital.v3.sql.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.digital.v3.sql.vo.InventoryVO;

@Mapper
public interface InventoryMapper {

	// inventory 레코드 생성
	public void createInventory(InventoryVO inventoryVO);
	
	// productId로 inventory 조회
	public InventoryVO getInventoryById(long productId);
	
	// productName으로 inventory 조회
	public InventoryVO getInventoryByName(String productName); 
	
	// productId로 quantity, updateDate 변경
	public void updateInventoryQuantity(InventoryVO inventoryVO);
	
}
