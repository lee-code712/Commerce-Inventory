package com.digital.v3.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digital.v3.schema.ErrorMsg;
import com.digital.v3.schema.Inventory;
import com.digital.v3.schema.SuccessMsg;
import com.digital.v3.service.InventoryService;
import com.digital.v3.utils.ExceptionUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "재고", description = "Inventory Related API")
@RequestMapping(value = "/rest/inventory")
public class InventoryController {

	@Resource
	private InventoryService inventorySvc;
	
	@RequestMapping(value = "/write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "재고 등록", notes = "특정 상품의 재고를 등록하기 위한 API. *입력 필드: productId, quantity")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Inventory.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> inventoryWrite (@ApiParam(value = "재고 정보", required = true) @RequestBody Inventory inventory) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		Inventory resInventory = new Inventory();
		try {
			if (inventorySvc.inventoryWrite(inventory)) {
				resInventory = inventorySvc.inventorySearchById(inventory.getProductId());
			}
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		return new ResponseEntity<Inventory>(resInventory, header, HttpStatus.valueOf(200));
	}
	
	@RequestMapping(value = "/inquiry/{productName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "재고 검색", notes = "상품명으로 상품 재고를 검색하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Inventory.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> inventorySearch (@ApiParam(value = "상품명", required = true) @PathVariable String productName) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();

		try {
			Inventory inventory = inventorySvc.inventorySearch(productName);
			return new ResponseEntity<Inventory>(inventory, header, HttpStatus.valueOf(200));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/inquiry/byId/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "재고 검색", notes = "상품 ID로 상품 재고를 검색하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Inventory.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> inventorySearchById (@ApiParam(value = "상품 ID", required = true, example = "0") @PathVariable long productId) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			Inventory inventory = inventorySvc.inventorySearchById(productId);
			return new ResponseEntity<Inventory>(inventory, header, HttpStatus.valueOf(200));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/quantityCheck", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "재고 수량 검증", notes = "재고 수량이 유효한지 검증하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Inventory.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> quantityCheck (@ApiParam(value = "상품 ID", required = true, example = "0") @RequestParam long productId, 
			@ApiParam(value = "상품 수량", required = true, example = "0") @RequestParam long quantity) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		System.out.println(productId + " " + quantity);
		Inventory resInventory = new Inventory();
		try {
			if (inventorySvc.inventoryQuantityCheck(productId, quantity)) {
				resInventory.setValidQuantity(true);
			}
			else {
				resInventory.setValidQuantity(false);
			}
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		return new ResponseEntity<Inventory>(resInventory, header, HttpStatus.valueOf(200));
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "재고 변경", notes = "특정 상품의 재고 수량을 변경하는 API. *입력필드: producId, quantity")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = SuccessMsg.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> inventoryUpdate (@ApiParam(value = "재고 정보", required = true) @RequestBody Inventory inventory) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		SuccessMsg success = new SuccessMsg();
		
		try {
			if (inventorySvc.inventoryUpdate(inventory)) {
				success.setSuccessCode(200);
				success.setSuccessMsg("재고를 변경했습니다.");
			}
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		return new ResponseEntity<SuccessMsg>(success, header, HttpStatus.valueOf(200));
	}
}
